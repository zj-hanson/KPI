/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.erp;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.entity.erp.BscGroupShipment;
import com.lightshell.comm.BaseLib;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class BscGroupSHServiceBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    protected LinkedHashMap<String, Object> queryParams = new LinkedHashMap<>();

    public BscGroupSHServiceBean() {
    }

    public LinkedHashMap<String, Object> getQueryParams() {
        return this.queryParams;
    }

    public void updataServerActualValue(int y, int m, Date d) {
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("ogdkid", " ='RL01' ");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DC", " <>'RT' ");
        List<BscGroupShipment> resultData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        List<BscGroupShipment> tempData;
        queryParams.clear();
        queryParams.put("facno", "G");
        queryParams.put("ogdkid", " ='RL01' ");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DC", " <>'RT' ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "J");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DC", " <>'RT' ");
        queryParams.put("ogdkid", " IN ('RL01') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "N");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DC", " <>'RT' ");
        queryParams.put("ogdkid", " IN ('RL01') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C4");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DC", " <>'RT' ");
        queryParams.put("ogdkid", " IN ('RL01') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("ogdkid", " IN ('RL01','RL03') ");
        queryParams.put("n_code_DA", "='AA' ");
        queryParams.put("n_code_DC", " LIKE 'AA%' ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C4");//目前A机组有重庆的数据，暂定这一个分公司。后续有其他分公司再添加。
        queryParams.put("ogdkid", " IN('RL01','RL03') ");
        queryParams.put("n_code_DA", "='AA' ");
        queryParams.put("n_code_DC", " LIKE 'AA%' ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "G");
        queryParams.put("ogdkid", " IN('RL01','RL03') ");
        queryParams.put("n_code_DA", "='AA' ");
        queryParams.put("n_code_DC", " LIKE 'AA%' ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "N");
        queryParams.put("ogdkid", " IN('RL01','RL03') ");
        queryParams.put("n_code_DA", "='AA' ");
        queryParams.put("n_code_DC", " LIKE 'AA%' ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "J");
        queryParams.put("ogdkid", " IN('RL01','RL03') ");
        queryParams.put("n_code_DA", "='AA' ");
        queryParams.put("n_code_DC", " LIKE 'AA%' ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='P' ");
        queryParams.put("ogdkid", " IN('RL01','RL03') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='S' ");
        queryParams.put("ogdkid", " IN('RL01','RL03') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AH' ");
        queryParams.put("ogdkid", " IN('RL01','RL03') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "K");
        queryParams.put("n_code_DA", " ='OH' ");
        queryParams.put("ogdkid", " IN('RL01','RL03') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "K");
        queryParams.put("n_code_DA", " ='RT' ");
        queryParams.put("ogdkid", " IN('RL01','RL03') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        if (resultData != null) {
            erpEJB.setCompany("C");
            erpEJB.getEntityManager().createNativeQuery("delete from bsc_groupshipment where protypeno = 'Z' and facno = 'S' and year(soday)=" + y + " and month(soday) = " + m + " and type = 'ServiceAmount' ").executeUpdate();
            for (BscGroupShipment e : resultData) {
                erpEJB.getEntityManager().persist(e);
            }
        }
    }

    //服务金额
    protected List<BscGroupShipment> getServiceValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";
        String ogdkid = map.get("ogdkid") != null ? map.get("ogdkid").toString() : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        StringBuilder sb = new StringBuilder();//1
        List<BscGroupShipment> data = new ArrayList<>();
        BigDecimal sqty = BigDecimal.ZERO;
        sb.append(" select cdrdate,isnull(cast(sum(amount) as decimal(10,2)),0) as num from( ");
        sb.append(" SELECT facno,'ServiceAmount' AS type,itnbrcus,a.cusno,s.cusna,cdrdate,depno,quantity,amount ");
        sb.append(" ,n_code_DA,n_code_CD,n_code_DC,n_code_DD,mancode,e.username AS manname,hmark1,hmark2 FROM (");
        sb.append(" select h.facno,itnbrcus,h.cusno,h.shpdate AS cdrdate,depno,0 as quantity, ");
        sb.append(" isnull(convert(decimal(16,4),sum((d.shpamts * h.ratio)/(h.taxrate + 1))),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
        sb.append(" from cdrhad h,cdrdta d where h.facno=d.facno and h.shpno=d.shpno and h.houtsta<>'W' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        sb.append("  and d.issevdta='Y' and h.facno='${facno}' ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
        sb.append(" group by  h.facno,itnbrcus,h.cusno,h.shpdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
        sb.append(" union all ");
        sb.append(" select h.facno,itnbrcus,h.cusno,h.bakdate AS cdrdate,depno,0 as quantity, ");
        sb.append(" isnull(convert(decimal(16,4),-sum((d.bakamts * h.ratio)/(h.taxrate + 1))),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ,mancode,hmark1,hmark2 ");
        sb.append(" from cdrbhad h,cdrbdta d where h.facno=d.facno and h.bakno=d.bakno and h.baksta<>'W' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='Y' and h.facno='${facno}' ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} ");
        sb.append(" group by  h.facno,itnbrcus,h.cusno,h.bakdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
        //getARM232Value 加扣款单独列出来 只算金额不算台数
        sb.append(" union all ");
        sb.append(" SELECT h.facno,itnbrcus,h.cusno,h.trdat AS cdrdate ,d.depno,0 AS quantity, ");
        sb.append(" ISNULL(SUM(CASE h.amtco WHEN 'P' THEN d.psamt WHEN 'M' THEN d.psamt *(-1) ELSE 0 END),0) AS amount ");
        sb.append(" ,s.n_code_DA,s.n_code_CD,s.n_code_DC,s.n_code_DD,mancode,'ARM232' as hmark1,'ARM232' as hmark2 ");
        sb.append(" FROM armpmm h,armacq d,cdrdta s,cdrhad c WHERE h.facno=d.facno AND h.trno = d.trno AND d.facno = s.facno ");
        sb.append(" AND d.shpno=s.shpno AND d.shpseq = s.trseq AND s.shpno=c.shpno and s.issevdta='Y' and h.facno='${facno}' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.trdat) = ${y}  and month(h.trdat) = ${m} ");
        sb.append(" group by  h.facno,itnbrcus,h.cusno,h.trdat,d.depno,s.n_code_DA,s.n_code_CD,s.n_code_DC,s.n_code_DD,mancode ");
        //getARM423Value 折让
        sb.append(" union all ");
        sb.append(" SELECT h.facno,'ARM423' as itnbrcus,d.ivocus AS 'cusno',h.recdate AS cdrdate,h.depno,0 AS quantity, ");
        sb.append(" ISNULL(sum(d.recamt),0) AS amount ,h.n_code_DA,h.n_code_CD,h.n_code_DC,h.n_code_DD,mancode,hmark1,hmark2 ");
        sb.append(" FROM armrec d,armrech h where d.facno=h.facno AND d.recno=h.recno ");
        sb.append(" AND h.prgno='ARM423' AND h.recstat='1' AND d.raccno IN ('6001','6002') ");
        sb.append(" AND h.ogdkid ").append(ogdkid);
        if (!"".equals(n_code_DA)) {
            sb.append(" and h.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and h.n_code_DC ").append(n_code_DC);
        }
        sb.append("  AND h.n_code_DD  ='01' ");
        sb.append(" and year(h.recdate) = ${y}   and month(h.recdate) =${m} ");
        sb.append(" GROUP BY h.facno,d.ivocus,h.recdate,h.depno,h.n_code_DA,h.n_code_CD,h.n_code_DC,h.n_code_DD,mancode,hmark1,hmark2 ");
        sb.append(" ) a,cdrcus s ,secuser e WHERE a.cusno=s.cusno AND a.mancode=e.userno ");
        sb.append("  ) as a GROUP BY cdrdate ");
        String sql = sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${ogdkid}", ogdkid);
        erpEJB.setCompany(facno);
        Query query1 = erpEJB.getEntityManager().createNativeQuery(sql);
        try {
            List shpResult = query1.getResultList();
            Date shpdate;
            String protype, protypeno, shptype;
            protype = "服务";
            protypeno = "Z";
            shptype = "21";
            for (int i = 0; i < shpResult.size(); i++) {
                Object o[] = (Object[]) shpResult.get(i);
                shpdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
                sqty = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
                BscGroupShipment e = new BscGroupShipment("S", shpdate, "ServiceAmount", protype, protypeno, shptype);
                e.setQuantity(BigDecimal.ZERO);
                e.setAmount(sqty);
                data.add(e);
            }
        } catch (Exception ex) {
            Logger.getLogger(BscGroupSHServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

}
