/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.erp;

/**
 *
 * @author C1749
 */
import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.entity.erp.SalesDetails;
import com.lightshell.comm.BaseLib;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
@LocalBean
public class SalesDetailsBean implements Serializable {

    //建立数据库连接
    @EJB
    private SuperEJBForERP erpEJB;

    protected LinkedHashMap<String, Object> queryParams = new LinkedHashMap<>();

    public SalesDetailsBean() {
    }

    //SQL逻辑
    private List getShipmentList(int y, int m, Date d, String facno) {

        List data;
        StringBuilder sb = new StringBuilder();
        //出货
        sb.append(" select h.facno,'shipment' AS type,h.shpno,h.decode,itnbrcus,h.cusno,h.shpdate AS cdrdate,h.depno,mancode, ");
        sb.append(" d.issevdta,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD	,'' as ogdkid,hmark1,hmark2, ");
        sb.append(" sum(CASE  when d.n_code_DA='AA' AND left(d.itnbr,1)='3' THEN shpqy1 ");
        sb.append(" when d.n_code_DA!='AA' THEN shpqy1 ELSE 0 END ) as quantity, ");
        sb.append(" isnull(convert(decimal(16,4),sum((d.shpamts * h.ratio)/(h.taxrate + 1))),0) as amount ");
        sb.append(" from cdrdta d left join cdrhad h on d.shpno=h.shpno ");
        sb.append(" where h.facno='${facno}' and h.houtsta <> 'W' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.n_code_DD <> 'ZZ'   ");
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
        sb.append(" GROUP BY h.facno,h.shpno,h.decode,itnbrcus,h.cusno,h.shpdate,h.depno, ");
        sb.append(" d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2,d.issevdta ");
        sb.append(" union all ");
        //销退
        sb.append(" select h.facno,'shipment' AS type,h.bakno AS shpno,h.decode,itnbrcus,h.cusno,h.bakdate AS cdrdate,depno,     ");
        sb.append(" mancode,d.issevdta,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,'' as ogdkid,hmark1,hmark2 , ");
        sb.append(" -sum(CASE  when d.n_code_DA='AA' AND left(d.itnbr,1)='3' THEN bshpqy1 ");
        sb.append(" when d.n_code_DA!='AA' THEN bshpqy1 ELSE 0 END ) as quantity, ");
        sb.append(" isnull(convert(decimal(16,4),-sum((d.bakamts * h.ratio)/(h.taxrate + 1))),0) as amount ");
        sb.append(" from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno ");
        sb.append(" where h.baksta <> 'W'  and  h.facno='${facno}' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        sb.append(" AND  year(h.bakdate)= ${y} AND month(h.bakdate)= ${m} ");
        sb.append(" group by  h.facno,h.bakno,h.decode,itnbrcus,h.cusno,h.bakdate,depno, ");
        sb.append(" d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2,d.issevdta ");
        sb.append(" union all ");
        //ARM232加扣款单
        sb.append(" SELECT h.facno,'ARM232' AS type,h.trno AS shpno,'' AS decode,itnbrcus,h.cusno,h.trdat AS cdrdate ,d.depno,mancode, ");
        sb.append(" '' AS issevdta,s.n_code_DA,s.n_code_CD,s.n_code_DC,s.n_code_DD,'' AS ogdkid,'' as hmark1,'' as hmark2, ");
        sb.append(" 0 AS quantity, ISNULL(SUM(CASE h.amtco WHEN 'P' THEN d.psamt WHEN 'M' THEN d.psamt *(-1) ELSE 0 END),0) AS amount  ");
        sb.append(" FROM armpmm h,armacq d,cdrdta s,cdrhad c WHERE h.facno=d.facno AND h.trno = d.trno AND d.facno = s.facno   ");
        sb.append(" AND d.shpno=s.shpno AND d.shpseq = s.trseq AND s.shpno=c.shpno ");
        sb.append(" and year(h.trdat) = ${y}  and month(h.trdat) = ${m}  ");
        sb.append(" group by  h.facno,h.trno,itnbrcus,h.cusno,h.trdat,d.depno,s.n_code_DA,s.n_code_CD,s.n_code_DC,s.n_code_DD,mancode ");
        sb.append(" union all ");
        //ARM423 折让
        sb.append(" SELECT h.facno,'ARM423' AS type,d.recno AS shpno,'' AS decode,'' as itnbrcus,d.ivocus AS 'cusno',h.recdate AS cdrdate,h.depno, ");
        sb.append("mancode,'' AS issevdta,h.n_code_DA,h.n_code_CD,h.n_code_DC,h.n_code_DD,h.ogdkid,hmark1,'' hmark2, ");
        sb.append(" 0 AS quantity,ISNULL(sum(d.recamt),0) AS amount  ");
        sb.append(" FROM armrec d,armrech h where d.facno=h.facno AND d.recno=h.recno ");
        sb.append(" AND h.prgno='ARM423' AND h.recstat='1' AND d.raccno IN ('6001','6002') ");
        sb.append(" and year(h.recdate) = ${y}   and month(h.recdate) = ${m} ");
        sb.append(" GROUP BY h.facno,d.recno,d.ivocus,h.recdate,h.depno,h.n_code_DA,h.n_code_CD,h.n_code_DC,h.n_code_DD,mancode,hmark1,hmark2,h.ogdkid  ");
        //ARM270 柯茂的卢南卢北需要单独算
        sb.append(" union all ");
        sb.append(" SELECT h.facno,'ARM270' AS type,h.bilno AS shpno,'' decode,'' as itnbrcus,h.cusno,h.bildat AS cdrdate ,h.depno, ");
        sb.append(" mancode,'' AS issevdta,h.depno AS n_code_DA,'' as  n_code_CD,h.depno AS n_code_DC, ");
        sb.append(" '00' as  n_code_DD,h.rkd AS ogdkid,'' as hmark1,'' as hmark2,0 AS quantity,ISNULL(sum(h.shpamt),0) AS amount ");
        sb.append(" FROM armbil h WHERE 1=1 AND h.facno = '${facno}' ");
        sb.append(" and h.rkd in ('RQ51','RQ11') ");
        sb.append(" and year(h.bildat) = ${y}   and month(h.bildat) = ${m} ");
        sb.append(" group by  h.facno,h.bilno,h.cusno,h.bildat,h.depno,h.mancode,h.rkd   ");
        String salesSql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        erpEJB.setCompany(facno);
        Query shpList = erpEJB.getEntityManager().createNativeQuery(salesSql);
        sb.setLength(0);
        try {
            data = shpList.getResultList();
        } catch (Exception e) {
            return null;
        }
        return data;
    }

    //订单
    private List getSalesOrderList(int y, int m, Date d, String facno) {
        List data;
        StringBuilder sb = new StringBuilder();
        sb.append(" select d.facno,'salesOrder' AS type,d.cdrno AS shpno,h.decode,itnbrcus,h.cusno,h.recdate AS cdrdate,depno, ");
        sb.append(" mancode,'' AS issevdta ,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,'' ogdkid,hmark1,hmark2, ");
        sb.append(" isnull(sum(d.cdrqy1),0) AS quantity,isnull(convert(decimal(16,4),sum((d.tramts*h.ratio)/(h.taxrate+1))),0) as amount  ");
        sb.append(" from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno where h.hrecsta <> 'W'  ");
        sb.append(" and h.cusno not in ('SSD00107','SGD00088','SJS00254','SCQ00146') and isnull(h.hmark2,'') <> 'FW' and  h.facno='${facno}' ");
        sb.append(" and d.drecsta not in ('98','99','10') and d.n_code_DD IN ('00','02')  ");
        sb.append(" AND  year(h.recdate)= ${y} AND month(h.recdate)= ${m}  ");
        sb.append(" GROUP BY d.facno,d.cdrno,h.decode,itnbrcus,h.cusno,h.recdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ,mancode,hmark1,hmark2  ");
        String salesSql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        erpEJB.setCompany(facno);
        Query shpList = erpEJB.getEntityManager().createNativeQuery(salesSql);
        sb.setLength(0);
        try {
            data = shpList.getResultList();
        } catch (Exception e) {
            return null;
        }
        return data;
    }

    //获得数据
    protected List getSalesDetailsList(int y, int m, Date d, String type, LinkedHashMap<String, Object> map) {
        String facnos = map.get("facno") != null ? map.get("facno").toString() : "";
        List tempData;
        List resultData = new ArrayList();
        String arr[] = facnos.split(",");

        try {
            switch (type) {
                //出货（出货和服务）
                case "S":
                    for (String facno : arr) {
                        tempData = getShipmentList(y, m, d, facno);
                        if (!tempData.isEmpty()) {
                            resultData.addAll(tempData);
                        }
                    }
                //订单
                case "D":
                    for (String facno : arr) {
                        tempData = getSalesOrderList(y, m, d, facno);
                        if (!tempData.isEmpty()) {
                            resultData.addAll(tempData);
                        }
                    }
                //所有
                default:
                    for (String facno : arr) {
                        tempData = getShipmentList(y, m, d, facno);
                        if (!tempData.isEmpty()) {
                            resultData.addAll(tempData);
                        }
                        tempData = getSalesOrderList(y, m, d, facno);
                        if (!tempData.isEmpty()) {
                            resultData.addAll(tempData);
                        }
                    }
            }
        } catch (Exception ex) {
            //log4j
            ex.toString();
        }
        return resultData;
    }

    //更新到ERP数据库
    public boolean peisiteSalesOrdersDetails(int y, int m, Date d, String type) {
        boolean flag = false;
        queryParams.put("facno", "C,C4,G,J,N,K");
        List data = getSalesDetailsList(y, m, d, type, queryParams);
        List<SalesDetails> tempData = new ArrayList();
        try {
            if (!data.isEmpty()) {
                for (Object e : data) {
                    Object[] row = (Object[]) e;
                    SalesDetails s = new SalesDetails(row[0].toString());
                    s.setType(row[1].toString());
                    s.setShpno(row[2].toString());
                    s.setDecode(row[3].toString() != null ? row[3].toString().charAt(0) : ' ');
                    s.setItnbrcus(row[4].toString());
                    s.setCusno(row[5].toString());
                    s.setCdrdate((Date) row[6]);
                    s.setDepno(row[7].toString());
                    s.setMancode(row[8].toString());
                    s.setIssevdta(row[9].toString());
                    s.setNcodeDA(row[10].toString());
                    s.setNcodeCD(row[11].toString());
                    s.setNcodeDC(row[12].toString());
                    s.setNcodeDD(row[13].toString());
                    s.setOgdkid(row[14].toString());
                    s.setHmark1(row[15] != null ? row[15].toString() : "");
                    s.setHmark2(row[15] != null ? row[16].toString() : "");
                    s.setQuantity((BigDecimal) row[17]);
                    s.setAmount((BigDecimal) row[18]);
                    tempData.add(s);
                }
                if (!tempData.isEmpty()) {
                    erpEJB.setCompany("C");
                    if (type != null) {
                        erpEJB.getEntityManager().createNativeQuery(" delete from salesdetails where " + " type = '" + type + "' and year(cdrdate)=  " + y + " and month(cdrdate) = " + m).executeUpdate();
                    } else {
                        erpEJB.getEntityManager().createNativeQuery(" delete from salesdetails where year(cdrdate)=  " + y + " and month(cdrdate) = " + m).executeUpdate();
                    }
                    for (SalesDetails s : tempData) {
                        erpEJB.getEntityManager().persist(s);
                    }
                }
            }
            flag = true;
        } catch (Exception ex) {
            ex.toString();
        }
        return flag;

    }
}
