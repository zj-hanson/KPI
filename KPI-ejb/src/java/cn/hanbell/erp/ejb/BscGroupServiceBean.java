/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.erp.ejb;

import cn.hanbell.erp.entity.BscGroupShipment;
import cn.hanbell.kpi.comm.SuperEJBForERP;
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
public class BscGroupServiceBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    protected LinkedHashMap<String, Object> queryParams = new LinkedHashMap<>();

    public BscGroupServiceBean() {
    }

    public LinkedHashMap<String, Object> getQueryParams() {
        return this.queryParams;
    }

    public void updataActualValue(int y, int m, Date d) {
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("decode", "1");
        queryParams.put("ogdkid", " IN ('RL01') ");
        List<BscGroupShipment> resultData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        List<BscGroupShipment> tempData;
        queryParams.clear();
        queryParams.put("facno", "G");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("decode", "1");
        queryParams.put("ogdkid", " IN ('RL01') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(BigDecimal.ZERO);
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(BigDecimal.ZERO);
                    a.setOrdamts(BigDecimal.ZERO);
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "J");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("decode", "1");
        queryParams.put("ogdkid", " IN ('RL01') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(BigDecimal.ZERO);
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(BigDecimal.ZERO);
                    a.setOrdamts(BigDecimal.ZERO);
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "N");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("decode", "1");
        queryParams.put("ogdkid", " IN ('RL01') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(BigDecimal.ZERO);
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(BigDecimal.ZERO);
                    a.setOrdamts(BigDecimal.ZERO);
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C4");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("decode", "1");
        queryParams.put("ogdkid", " IN ('RL01') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(BigDecimal.ZERO);
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(BigDecimal.ZERO);
                    a.setOrdamts(BigDecimal.ZERO);
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AA' ");
        queryParams.put("decode", "1");
        queryParams.put("ogdkid", " IN ('RL01') ");
        queryParams.put("n_code_DC", " LIKE 'AA%' ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(BigDecimal.ZERO);
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(BigDecimal.ZERO);
                    a.setOrdamts(BigDecimal.ZERO);
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='P' ");
        queryParams.put("ogdkid", " IN ('RL01') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(BigDecimal.ZERO);
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(BigDecimal.ZERO);
                    a.setOrdamts(BigDecimal.ZERO);
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='S' ");
        queryParams.put("ogdkid", " IN ('RL01') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(BigDecimal.ZERO);
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(BigDecimal.ZERO);
                    a.setOrdamts(BigDecimal.ZERO);
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AH' ");
        queryParams.put("ogdkid", " IN ('RL01') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(BigDecimal.ZERO);
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(BigDecimal.ZERO);
                    a.setOrdamts(BigDecimal.ZERO);
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "K");
        queryParams.put("n_code_DA", " ='OH' ");
        queryParams.put("ogdkid", " IN ('RL01') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(BigDecimal.ZERO);
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(BigDecimal.ZERO);
                    a.setOrdamts(BigDecimal.ZERO);
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "K");
        queryParams.put("n_code_DA", " ='RT' ");
        queryParams.put("ogdkid", " IN ('RL01') ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(BigDecimal.ZERO);
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(BigDecimal.ZERO);
                    a.setOrdamts(BigDecimal.ZERO);
                } else {
                    resultData.add(b);
                }
            }
        }
        if (resultData != null) {
            erpEJB.setCompany("C");
            erpEJB.getEntityManager().createNativeQuery("delete from bsc_groupshipment where protypeno = 'Z' and year(soday)=" + y + " and month(soday) = " + m).executeUpdate();
            for (BscGroupShipment e : resultData) {
                erpEJB.getEntityManager().persist(e);
            }
        }
    }

    //服务金额
    protected List<BscGroupShipment> getServiceValue(int y, int m, Date d, int type,LinkedHashMap<String, Object> map) {
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
        sb.append(" select soday,isnull(cast(sum(num) as decimal(10,2)),0) as num from( ");
        sb.append("SELECT h.trdat as soday,ISNULL(SUM(CASE h.amtco WHEN 'P' THEN d.psamt WHEN 'M' THEN d.psamt *(-1) ELSE 0 END),0) as num FROM armpmm h,armacq d,cdrdta s ");
        sb.append(" WHERE h.facno=d.facno AND h.trno = d.trno AND d.facno = s.facno AND d.shpno=s.shpno AND d.shpseq = s.trseq and s.issevdta='Y' AND h.facno='${facno}' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" AND s.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" AND s.n_code_CD ").append(n_code_CD);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" AND s.n_code_DC ").append(n_code_DC);
        }
        sb.append(" AND year(h.trdat) = ${y} AND month(h.trdat) = ${m} GROUP BY h.trdat ");
        sb.append(" UNION  all ");//2
        sb.append("SELECT h.recdate as soday,ISNULL(SUM(d.recamt),0) as num FROM armrec d,armrech h where d.facno=h.facno AND d.recno=h.recno AND h.prgno='ARM423' AND h.recstat='1' AND d.raccno='6001' ");
        sb.append(" AND h.facno='${facno}' AND h.ogdkid  ${ogdkid} ");
        if (!"".equals(n_code_DA)) {
            sb.append(" AND h.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" AND h.n_code_CD ").append(n_code_CD);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" AND h.n_code_DC ").append(n_code_DC);
        }
        sb.append(" AND h.n_code_DD='01' ");
        sb.append(" AND year(h.recdate) = ${y} and month(h.recdate)= ${m} GROUP BY h.recdate ");
        sb.append(" UNION  all ");//3
        sb.append("select h.shpdate as soday,isnull(sum((d.shpamts * h.ratio)/(h.taxrate + 1)),0) as num from cdrhad h,cdrdta d where h.facno=d.facno and h.shpno=d.shpno and h.houtsta<>'W' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        sb.append(" and d.issevdta='Y' and h.facno='${facno}' ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" and d.n_code_CD ").append(n_code_CD);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} GROUP BY h.shpdate ");
        sb.append(" UNION  all ");//4
        sb.append("select h.bakdate as soday,isnull(sum((d.bakamts * h.ratio)/(h.taxrate + 1)),0) as num from cdrbhad h,cdrbdta d where h.facno=d.facno and h.bakno=d.bakno and h.baksta<>'W' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        sb.append(" and d.issevdta='Y' and h.facno='${facno}' ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" and d.n_code_CD ").append(n_code_CD);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} GROUP BY h.bakdate ");
        sb.append(" ) as a GROUP BY soday ");
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
                BscGroupShipment e = new BscGroupShipment("S", shpdate, protype, protypeno, shptype);
                e.setShpnum(BigDecimal.ZERO);
                e.setShpamts(sqty);
                data.add(e);
            }
        } catch (Exception ex) {
            Logger.getLogger(BscGroupServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

}
