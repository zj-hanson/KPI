/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.entity.ClientTable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class ClientShipmentBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;
    protected LinkedHashMap<String, String> queryParams;

    public ClientShipmentBean() {
        queryParams = new LinkedHashMap<>();
    }

    public List<ClientTable> getClientListSum(int y, int m, String da) {
        queryParams.clear();
        List<ClientTable> clientList = new ArrayList<>();
        List<ClientTable> list;
        if ("".equals(da.trim()) || "R".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C,C4,N,G,J");
            queryParams.put("depno", " IN ('1B000','1C000','1D000','1E000','1V000') ");
            queryParams.put("n_code_DA", " ='R' ");
            queryParams.put("ogdkid", " IN ('RL01') ");
            list = getClientList(y, m, queryParams);
            if (list != null && !list.isEmpty()) {
                for (ClientTable clientTable : list) {
                    clientList.add(clientTable);
                }
            }
        }
        if ("".equals(da.trim()) || "AA".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("depno", " IN ('1Q000','1Q100') ");
            queryParams.put("n_code_DA", " ='AA' ");
            queryParams.put("n_code_DD", " IN ('02') ");
            queryParams.put("ogdkid", " IN ('RL01','RL03') ");
            list = getClientList(y, m, queryParams);
            if (list != null && !list.isEmpty()) {
                for (ClientTable clientTable : list) {
                    clientList.add(clientTable);
                }
            }
        }
        if ("".equals(da.trim()) || "AH".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("depno", " IN('1G110','1G500') ");
            queryParams.put("n_code_DA", " ='AH' ");
            queryParams.put("ogdkid", " IN('RL01','RL03') ");
            list = getClientList(y, m, queryParams);
            if (list != null && !list.isEmpty()) {
                for (ClientTable clientTable : list) {
                    clientList.add(clientTable);
                }
            }
        }
        if ("".equals(da.trim()) || "P".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("depno", " IN ('1H000','1H100') ");
            queryParams.put("n_code_DA", "= 'P'");
            queryParams.put("ogdkid", " IN ('RL01','RL03') ");
            list = getClientList(y, m, queryParams);
            if (list != null && !list.isEmpty()) {
                for (ClientTable clientTable : list) {
                    clientList.add(clientTable);
                }
            }
        }
        if ("".equals(da.trim()) || "S".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("depno", " IN ('1U000') ");
            queryParams.put("n_code_DA", " ='S'");
            queryParams.put("ogdkid", " IN ('RL01','RL03') ");
            list = getClientList(y, m, queryParams);
            if (list != null && !list.isEmpty()) {
                for (ClientTable clientTable : list) {
                    clientList.add(clientTable);
                }
            }
        }
        if ("".equals(da.trim()) || "RT".equals(da.trim()) || "OH".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "K");
            queryParams.put("depno", " IN('5A000','5A100','5B000') ");
            queryParams.put("n_code_DA", " IN('RT','OH') ");
            queryParams.put("ogdkid", " IN('RL01','RL03') ");
            list = getClientList(y, m, queryParams);
            if (list != null && !list.isEmpty()) {
                for (ClientTable clientTable : list) {
                    clientList.add(clientTable);
                }
            }
        }
        return clientList;
    }

    private List<ClientTable> getClientList(int y, int m, LinkedHashMap<String, String> map) {
        String facno = map.get("facno") != null ? map.get("facno") : "";
        String[] arr = facno.split(",");
        List<ClientTable> returnlist = new ArrayList<>();
        ClientTable ct;
        try {
            for (String arr1 : arr) {
                List list = getClient(y, m, arr1, map);
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        ct = new ClientTable();
                        Object[] row = (Object[]) list.get(i);
                        ct.setFacno(row[0].toString());
                        ct.setCusno(row[1].toString());
                        ct.setCusna(row[2].toString());
                        ct.setShipmentsyear(Integer.parseInt(row[3].toString()));
                        ct.setShipmentmonth(Integer.parseInt(row[4].toString()));
                        ct.setDeptno(row[5].toString());
                        ct.setNcodeDA(row[6].toString());
                        ct.setNcodeDC(row[7].toString());
                        ct.setNcodeCD(row[8].toString());
                        ct.setQuantity(BigDecimal.valueOf(Double.parseDouble(row[9].toString())).intValue());
                        ct.setAmount(BigDecimal.valueOf(Double.parseDouble(row[10].toString())));
                        ct.setStatus("N");
                        returnlist.add(ct);
                    }
                }
            }
            return returnlist;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ClientShipmentBean.getClientListSum()-----" + e.toString());
            return null;
        }

    }

    private List<ClientTable> getClient(int y, int m, String arrString, LinkedHashMap<String, String> map) {
        String depno = map.get("depno") != null ? map.get("depno") : "";
        String ogdkid = map.get("ogdkid") != null ? map.get("ogdkid") : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD") : "";
        StringBuilder sb = new StringBuilder();
        //第一部分为整机出货
        sb.append(" select  z.facno,z.cusno as 'cusno' ,c.cusna as 'cusna' ,z.year,z.month,z.depno,z.n_code_DA,z.n_code_DC,z.n_code_CD,z.num  as 'shpqy1',z.shpamts  from ");
        sb.append(" (  select x.facno,x.cusno,x.year,x.month,x.depno,x.n_code_DA,x.n_code_DC,x.n_code_CD,sum(num) as num,sum(shpamts) AS shpamts from ");
        sb.append(" (  select h.facno,h.cusno,h.depno,year(h.shpdate) as 'year' ,month(h.shpdate) as 'month',d.n_code_DA,d.n_code_DC,d.n_code_CD, ");
        sb.append(" sum(CASE  when d.n_code_DA='AA' AND left(d.itnbr,1)='3' THEN shpqy1 when d.n_code_DA!='AA' AND left(d.itnbr,1)='3' THEN shpqy1 ELSE 0 END ) as num, ");
        sb.append(" isnull(convert(decimal(16,2),sum((d.shpamts * h.ratio)/(h.taxrate + 1))),0) as shpamts ");
        sb.append("  from cdrdta d left join cdrhad h on d.shpno=h.shpno   where h.facno='${facno}' and h.houtsta <> 'W' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N'   and d.n_code_DD ='00'");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        sb.append(" AND  year(h.shpdate)=${y} AND month(h.shpdate)=${m} ");
        sb.append(" group by  h.facno,h.cusno,h.depno,year(h.shpdate),month(h.shpdate),d.n_code_DA,d.n_code_DC,d.n_code_CD ");
        sb.append(" union all ");
        sb.append(" select  h.facno,h.cusno,h.depno,year(h.bakdate) as 'year',month(h.bakdate) as 'month',d.n_code_DA,d.n_code_DC,d.n_code_CD, ");
        sb.append(" -sum(CASE  when d.n_code_DA='AA' AND left(d.itnbr,1)='3' THEN shpqy1 when d.n_code_DA!='AA' AND left(d.itnbr,1)='3' THEN shpqy1 ELSE 0 END ) as num, ");
        sb.append("  isnull(convert(decimal(16,2),-sum((d.bakamts * h.ratio)/(h.taxrate + 1))),0) as shpamts ");
        sb.append(" from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno  where h.baksta <> 'W'  and  h.facno='${facno}' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' and d.n_code_DD ='00' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        sb.append(" AND  year(h.bakdate)=${y} AND month(h.bakdate)=${m} ");
        sb.append(" group by  h.facno,h.cusno,h.depno,d.n_code_DA,d.n_code_DC,d.n_code_CD,year(h.bakdate),month(h.bakdate) ");
        sb.append(" )  x  group by x.facno,x.cusno,x.year,x.month,x.depno,x.n_code_DA,x.n_code_DC,x.n_code_CD ");
        sb.append(" )  z,cdrcus c where z.cusno=c.cusno ");

        //如果算入后处理金额则单独处理，只算金额不算台数
        if (!"".equals(n_code_DD)) {
            sb.append(" union all ");
            sb.append(" select  z.facno,z.cusno as 'cusno' ,c.cusna as 'cusna' ,z.year,z.month,z.depno,z.n_code_DA,z.n_code_DC,z.n_code_CD,0 as 'shpqy1', z.num  as 'shpamts' from ");
            sb.append(" (  select x.facno,x.cusno,x.year,x.month,x.depno,x.n_code_DA,x.n_code_DC,x.n_code_CD,sum(num) as num  from ");
            sb.append(" (  select h.facno,h.cusno,h.depno,year(h.shpdate) as 'year' ,month(h.shpdate) as 'month',d.n_code_DA,d.n_code_DC,d.n_code_CD,");
            sb.append(" isnull(convert(decimal(16,2),sum((d.shpamts * h.ratio)/(h.taxrate + 1))),0) as num ");
            sb.append(" from cdrdta d left join cdrhad h on d.shpno=h.shpno   where h.facno='${facno}'  and h.houtsta <> 'W' ");
            sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            sb.append(" and d.n_code_DD ").append(n_code_DD);
            sb.append(" AND  year(h.shpdate)=${y} AND month(h.shpdate)=${m} ");
            sb.append(" group by  h.facno,h.cusno,h.depno,year(h.shpdate),month(h.shpdate),d.n_code_DA,d.n_code_DC,d.n_code_CD ");
            sb.append(" union all ");
            sb.append(" select  h.facno,h.cusno,h.depno,year(h.bakdate) as 'year',month(h.bakdate) as 'month',d.n_code_DA,d.n_code_DC,d.n_code_CD, ");
            sb.append(" isnull(convert(decimal(16,2),-sum((d.bakamts * h.ratio)/(h.taxrate + 1))),0) as num ");
            sb.append(" from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno  where h.baksta <> 'W'  and  h.facno='${facno}' ");
            sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            sb.append(" and d.n_code_DD ").append(n_code_DD);
            sb.append(" AND  year(h.bakdate)=${y} AND month(h.bakdate)=${m} ");
            sb.append(" group by  h.facno,h.cusno,h.depno,d.n_code_DA,d.n_code_DC,d.n_code_CD,year(h.bakdate),month(h.bakdate) ");
            sb.append(" )  x  group by x.facno,x.cusno,x.year,x.month,x.depno,x.n_code_DA,x.n_code_DC,x.n_code_CD ");
            sb.append(" )  z,cdrcus c where z.cusno=c.cusno ");
        }
        //getARM232Value 加扣款单独列出来 只算金额不算台数
        sb.append(" union all ");
        sb.append(" SELECT a.facno,a.cusno as 'cusno',b.cusna as 'cusna',a.year,a.month,a.depno,a.n_code_DA,a.n_code_DC,a.n_code_CD,a.shpqy1 as 'shpqy1',a.shpamts as 'shpamts' FROM ");
        sb.append(" (SELECT h.facno,h.cusno AS 'cusno',d.depno,year(h.trdat) as 'year',month(h.trdat) as 'month',s.n_code_DA,s.n_code_DC,s.n_code_CD,0 AS shpqy1, ");
        sb.append(" ISNULL(SUM(CASE h.amtco WHEN 'P' THEN d.psamt WHEN 'M' THEN d.psamt *(-1) ELSE 0 END),0) AS shpamts ");
        sb.append(" FROM armpmm h,armacq d,cdrdta s  WHERE h.facno=d.facno AND h.trno = d.trno AND d.facno = s.facno AND d.shpno=s.shpno AND d.shpseq = s.trseq  ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and s.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" AND (s.n_code_DD  ='00'  or s.n_code_DD ").append(n_code_DD).append(" ) ");
        } else {
            sb.append("  AND s.n_code_DD  ='00' ");
        }
        sb.append(" and year(h.trdat) = ${y}  and month(h.trdat) = ${m} ");
        sb.append(" group by  h.facno,h.cusno,d.depno,year(h.trdat),month(h.trdat),s.n_code_DA,s.n_code_DC,s.n_code_CD ");
        sb.append(" )a,cdrcus b where b.cusno  =a.cusno ");
        //getARM423Value 折让
        sb.append(" union all ");
        sb.append(" SELECT a.facno,a.cusno as 'cusno',b.cusna as 'cusna',a.year,a.month,a.depno,a.n_code_DA,a.n_code_DC,a.n_code_CD,a.shpqy1 as 'shpqy1',a.shpamts as 'shpamts' FROM ");
        sb.append(" (SELECT h.facno,d.ivocus AS 'cusno',h.depno,year(h.recdate) as 'year',month(h.recdate) as 'month',h.n_code_DA,h.n_code_DC,h.n_code_CD,0 AS shpqy1, ");
        sb.append(" ISNULL(sum(d.recamt),0) AS shpamts FROM armrec d,armrech h where d.facno=h.facno AND d.recno=h.recno ");
        sb.append(" AND h.prgno='ARM423' AND h.recstat='1' AND d.raccno='6001' ");
        sb.append(" AND h.ogdkid  ").append(ogdkid);
        if (!"".equals(n_code_DA)) {
            sb.append(" and h.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" AND (h.n_code_DD  ='00'  or h.n_code_DD ").append(n_code_DD).append(" ) ");
        } else {
            sb.append("  AND h.n_code_DD  ='00' ");
        }
        sb.append(" and year(h.recdate) = ${y}   and month(h.recdate) =${m} ");
        sb.append(" GROUP BY h.facno,d.ivocus,h.depno,year(h.recdate),month(h.recdate),h.n_code_DA,h.n_code_DC,h.n_code_CD ");
        sb.append("  )a,cdrcus b where b.cusno  =a.cusno ");
        //getARM270Value 其它项金额 关联部门
        if (!"".equals(depno)) {
            sb.append(" union all ");
            sb.append(" SELECT a.facno,a.cusno as 'cusno',b.cusna as 'cusna',a.year,a.month,a.depno,a.n_code_DA,a.n_code_DC,a.n_code_CD,a.shpqy1 as 'shpqy1',a.shpamts as 'shpamts' FROM  ( ");
            sb.append(" SELECT h.facno,h.cusno AS 'cusno',h.depno,year(h.bildat) as 'year',month(h.bildat) as 'month', ");
            sb.append(getDA(depno)).append(" as n_code_DA, ");
            sb.append(getDC(depno)).append(" as n_code_DC, ");
            sb.append(" 'ARM270' as n_code_CD,0 AS shpqy1,ISNULL(sum(h.shpamt),0) AS shpamts FROM armbil h WHERE h.rkd='RQ11' ");
            sb.append(" AND h.depno ").append(depno);
            sb.append(" and year(h.bildat) = ${y}   and month(h.bildat) =${m} ");
            sb.append(" group by  h.facno,h.cusno,h.depno,year(h.bildat),month(h.bildat) ");
            sb.append("   )a,cdrcus b where b.cusno  =a.cusno ");
        }
        String sql = sb.toString().replace("${facno}", arrString).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));

        try {
            erpEJB.setCompany(arrString);
            Query query = erpEJB.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ClientShipmentBean.getClientSQL()----异常" + e.toString());
            return null;
        }
    }

    private String getDA(String depno) {
        String aa = "'ARM270'";
        if (depno.contains("1B")) {
            aa = "'R'";
        }
        if (depno.contains("1C")) {
            aa = "'R'";
        }
        if (depno.contains("1D")) {
            aa = "'R'";
        }
        if (depno.contains("1E")) {
            aa = "'R'";
        }
        if (depno.contains("1V")) {
            aa = "'R'";
        }
        if (depno.contains("1Q")) {
            aa = "'AA'";
        }
        if (depno.contains("1G")) {
            aa = "'AH'";
        }
        if (depno.contains("1H")) {
            aa = "'P'";
        }
        if (depno.contains("1U")) {
            aa = "'S'";
        }
        if (depno.contains("5B")) {
            aa = "'OH'";
        }
        if (depno.contains("5A")) {
            aa = "'RT'";
        }
        return aa;
    }

    private String getDC(String depno) {
        String aa = "'ARM270'";
        if (depno.contains("1G1")) {
            aa = "'AJ'";
        }
        if (depno.contains("1G5")) {
            aa = "'SDS'";
        }
        return aa;
    }

}
