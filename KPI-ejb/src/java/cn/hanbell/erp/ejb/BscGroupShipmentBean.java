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
public class BscGroupShipmentBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    protected LinkedHashMap<String, Object> queryParams = new LinkedHashMap<>();

    public BscGroupShipmentBean() {
    }

    public LinkedHashMap<String, Object> getQueryParams() {
        return this.queryParams;
    }

    public void updataActualValue(int y, int m, Date d) {
        queryParams.clear();
        
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("ogdkid", " IN ('RL01','RL03') ");
        List<BscGroupShipment> resultData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        List<BscGroupShipment> tempData;
        queryParams.clear();
        queryParams.put("facno", "G");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("ogdkid", " IN ('RL01','RL03') ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "J");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("ogdkid", " IN ('RL01','RL03') ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "N");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("ogdkid", " IN ('RL01','RL03') ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C4");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("ogdkid", " IN ('RL01','RL03') ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AA' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("ogdkid", " IN ('RL01','RL03') ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='P' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("ogdkid", " IN ('RL01','RL03') ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='S' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("ogdkid", " IN ('RL01','RL03') ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AH' ");
        queryParams.put("n_code_DC", " LIKE 'AJ%' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("ogdkid", " IN ('RL01','RL03') ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AH' ");
        queryParams.put("n_code_DC", " ='SDS' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("ogdkid", " IN ('RL01','RL03') ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "K");
        queryParams.put("n_code_DA", " ='OH' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("ogdkid", " IN ('RL01','RL03') ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "K");
        queryParams.put("n_code_DA", " ='RT' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("ogdkid", " IN ('RL01','RL03') ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        if (resultData != null) {
            erpEJB.setCompany("C");
            erpEJB.getEntityManager().createNativeQuery("delete from bsc_groupshipment where protypeno <> 'Z' and year(soday)=" + y + " and month(soday) = " + m).executeUpdate();
            for (BscGroupShipment e : resultData) {
                erpEJB.getEntityManager().persist(e);
            }
        }
    }

    //出货
    protected List<BscGroupShipment> getShipment(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String depno = map.get("depno") != null ? map.get("depno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";

        List<BscGroupShipment> data = new ArrayList<>();
        BigDecimal qty = BigDecimal.ZERO;
        BigDecimal amts = BigDecimal.ZERO;
        Double orderNum = 0.0;
        Double orderamts = 0.0;
        StringBuilder shpSql = new StringBuilder();
        shpSql.append("select h.shpdate,isnull(sum(d.shpqy1),0) from cdrhad h,cdrdta d where h.facno=d.facno and h.shpno=d.shpno  and h.houtsta<>'W' ");
        shpSql.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        shpSql.append(" and d.issevdta='N' and h.facno='${facno}' ");
        if (!"".equals(n_code_DA)) {
            shpSql.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            shpSql.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            shpSql.append(" and d.n_code_DD ").append(n_code_DD);
        }
        shpSql.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} group by h.shpdate");
        String cdrdta = shpSql.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        StringBuilder bakSql = new StringBuilder();
        bakSql.append("select h.bakdate,isnull(sum(0 - d.bshpqy1),0) from cdrbhad h,cdrbdta d where h.facno=d.facno and h.bakno=d.bakno and h.baksta<>'W' ");
        bakSql.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        bakSql.append(" and d.issevdta='N' and h.facno='${facno}' ");
        if (!"".equals(n_code_DA)) {
            bakSql.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            bakSql.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            bakSql.append(" and d.n_code_DD ").append(n_code_DD);
        }
        bakSql.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} group by h.bakdate");
        String cdrbdta = bakSql.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);
        erpEJB.setCompany(facno);
        Query shpQuery = erpEJB.getEntityManager().createNativeQuery(cdrdta);
        Query bakQuery = erpEJB.getEntityManager().createNativeQuery(cdrbdta);
        try {         
            List shpResult = shpQuery.getResultList();
            List bakResult = bakQuery.getResultList();
            Date shpdate;
            String protype, protypeno, shptype;
            if (n_code_DA.contains("R")) {
                protype = "R机体";
                protypeno = "R";
                shptype = "1";
            } else if (n_code_DA.contains("AA")) {
                protype = "A机组";
                protypeno = "A";
                shptype = "2";
            } else if (n_code_DA.contains("AH")) {
                if (n_code_DC.contains("SDS")) {
                    protype = "日立A机组";
                    protypeno = "A";
                    shptype = "3";
                } else {
                    protype = "A机体";
                    protypeno = "A";
                    shptype = "1";
                }
            } else if (n_code_DA.contains("P")) {
                protype = "真空泵";
                protypeno = "P";
                shptype = "1";
            } else if (n_code_DA.contains("S")) {
                protype = "无油机组";
                protypeno = "S";
                shptype = "1";
            } else if (n_code_DA.contains("OH")) {
                protype = "OH";
                protypeno = "低环温热泵";
                shptype = "2";
            } else if (n_code_DA.contains("RT")) {
                protype = "RT";
                protypeno = "离心机体";
                shptype = "2";
            } else {
                protype = "";
                protypeno = "";
                shptype = "";
            }
            for (int i = 0; i < shpResult.size(); i++) {
                Object o[] = (Object[]) shpResult.get(i);
                shpdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
                qty = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
                BscGroupShipment e = new BscGroupShipment("S", shpdate, protype, protypeno, shptype);
                e.setShpnum(qty);
                e.setShpamts(BigDecimal.ZERO);
                data.add(e);
            }
            for (int i = 0; i < bakResult.size(); i++) {
                Object o[] = (Object[]) bakResult.get(i);
                shpdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
                qty = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
                BscGroupShipment e = new BscGroupShipment("S", shpdate, protype, protypeno, shptype);
                e.setShpnum(qty);
                e.setShpamts(BigDecimal.ZERO);
                if (data.contains(e)) {
                    BscGroupShipment entity = data.get(data.indexOf(e));
                    entity.setShpnum(entity.getShpnum().add(qty));
                } else {
                    data.add(e);
                }
            }
            
            //订单
            if(!data.isEmpty()){
                for (BscGroupShipment e : data) {
                  List  orderList = getOrder(y, m, e.getBscGroupShipmentPK().getSoday(), Calendar.DATE, getQueryParams());
                    for (int i = 0; i < orderList.size(); i++) {
                        Object [] row = (Object[])orderList.get(i);
                        orderNum = Double.valueOf(row[0].toString());
                        orderamts = Double.valueOf(row[1].toString());
                    }
                    if (!orderList.isEmpty()) {
                        e.setOrdnum(BigDecimal.valueOf(orderNum));
                        e.setOrdamts(BigDecimal.valueOf(orderamts));
                    }
                }
            }
            
            if (!data.isEmpty()) {
                if (map.get("n_code_DD") != null) {
                    map.remove("n_code_DD");
                    map.put("n_code_DD", " IN ('00','02') ");
                }
                for (BscGroupShipment e : data) {
                    amts = getShipmentAmount(y, m, e.getBscGroupShipmentPK().getSoday(), Calendar.DATE, getQueryParams());
                    if (amts != null) {
                        e.setShpamts(amts);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(BscGroupShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    //出货金额
    public BigDecimal getShipmentAmount(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";

        BigDecimal arm232 = BigDecimal.ZERO;
        BigDecimal arm235 = BigDecimal.ZERO;
        BigDecimal arm270 = BigDecimal.ZERO;
        BigDecimal arm423 = BigDecimal.ZERO;
        BigDecimal shp1 = BigDecimal.ZERO;
        BigDecimal bshp1 = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append("select isnull(sum((d.shpamts * h.ratio)/(h.taxrate + 1)),0) from cdrhad h,cdrdta d where h.facno=d.facno and h.shpno=d.shpno and h.houtsta<>'W' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        sb.append(" and d.issevdta='N' and h.facno='${facno}' ");
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
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and h.shpdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and h.shpdate= '${d}' ");
                break;
            default:
                sb.append(" and h.shpdate<= '${d}' ");
        }
        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        sb.setLength(0);
        sb.append("select isnull(sum((d.bakamts * h.ratio)/(h.taxrate + 1)),0) from cdrbhad h,cdrbdta d where h.facno=d.facno and h.bakno=d.bakno and h.baksta<>'W' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        sb.append(" and d.issevdta='N' and h.facno='${facno}' ");
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
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and h.bakdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and h.bakdate= '${d}' ");
                break;
            default:
                sb.append(" and h.bakdate<= '${d}' ");
        }
        String cdrbdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);
        erpEJB.setCompany(facno);
        Query query1 = erpEJB.getEntityManager().createNativeQuery(cdrdta);
        Query query2 = erpEJB.getEntityManager().createNativeQuery(cdrbdta);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            shp1 = (BigDecimal) o1;
            bshp1 = (BigDecimal) o2;
        } catch (Exception ex) {
            Logger.getLogger(BscGroupShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (queryParams != null && !queryParams.isEmpty()) {
            //计算其他金额
            arm232 = this.getARM232Value(y, m, d, type, getQueryParams());
            //ARM235不算事业部
            //arm235 = this.getARM235Value(y, m, d, type, getQueryParams());
            //arm270 = this.getARM270Value(y, m, d, type, getQueryParams());
            arm423 = this.getARM423Value(y, m, d, type, getQueryParams());
        }
        return shp1.subtract(bshp1).add(arm232).add(arm235).add(arm270).add(arm423);
    }

    //加扣款,关联4个分类
    public BigDecimal getARM232Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ISNULL(SUM(CASE h.amtco WHEN 'P' THEN d.psamt WHEN 'M' THEN d.psamt *(-1) ELSE 0 END),0) FROM armpmm h,armacq d,cdrdta s ");
        sb.append(" WHERE h.facno=d.facno AND h.trno = d.trno AND d.facno = s.facno AND d.shpno=s.shpno AND d.shpseq = s.trseq AND h.facno='${facno}' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" AND s.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" AND s.n_code_CD ").append(n_code_CD);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" AND s.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" AND s.n_code_DD ").append(n_code_DD);
        }
        sb.append(" AND year(h.trdat) = ${y} AND month(h.trdat) = ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" AND h.trdat<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" AND h.trdat= '${d}' ");
                break;
            default:
                sb.append(" AND h.trdat<= '${d}' ");
        }
        String sqlstr = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${facno}", facno);
        erpEJB.setCompany(facno);
        Query query = erpEJB.getEntityManager().createNativeQuery(sqlstr);
        try {
            Object o = query.getSingleResult();
            return (BigDecimal) o;
        } catch (Exception ex) {
            Logger.getLogger(BscGroupShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    //代收其他款项,关联4个分类
    public BigDecimal getARM235Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ISNULL(SUM(apmamt),0) FROM armicdh h WHERE h.shpno IN ");
        sb.append(" (SELECT DISTINCT b.shpno FROM cdrhad a,cdrdta b WHERE a.facno=b.facno AND a.shpno=b.shpno AND a.houtsta<>'W' AND a.facno='${facno}' ");
        if (!"".equals(decode)) {
            sb.append(" AND h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" AND b.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" AND b.n_code_CD ").append(n_code_CD);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" AND b.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" AND b.n_code_DD ").append(n_code_DD);
        }
        sb.append(" AND year(a.shpdate) = ${y}  AND month(a.shpdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" AND a.shpdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" AND a.shpdate= '${d}' ");
                break;
            default:
                sb.append(" AND a.shpdate<= '${d}' ");
        }
        sb.append(" ) ");
        sb.append(" AND year(h.shpdate) = ${y} AND month(h.shpdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" AND h.shpdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" AND h.shpdate= '${d}' ");
                break;
            default:
                sb.append(" AND h.shpdate<= '${d}' ");
        }
        String sqlstr = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${facno}", facno);
        erpEJB.setCompany(facno);
        Query query = erpEJB.getEntityManager().createNativeQuery(sqlstr);
        try {
            Object o = query.getSingleResult();
            return (BigDecimal) o;

        } catch (Exception ex) {
            Logger.getLogger(BscGroupShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    //它项金额,关联部门
    public BigDecimal getARM270Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ISNULL(SUM(h.shpamt),0) FROM armbil h WHERE h.rkd='RQ11' AND h.facno='${facno}' ");
        sb.append(" AND year(h.bildat) = ${y} AND month(h.bildat)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" AND h.bildat<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" AND h.bildat= '${d}' ");
                break;
            default:
                sb.append(" AND h.bildat<= '${d}' ");
        }
        String sqlstr = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${facno}", facno);
        erpEJB.setCompany(facno);
        Query query = erpEJB.getEntityManager().createNativeQuery(sqlstr);
        try {
            Object o = query.getSingleResult();
            return (BigDecimal) o;

        } catch (Exception ex) {
            Logger.getLogger(BscGroupShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    //折让,关联4个分类
    public BigDecimal getARM423Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";
        String ogdkid = map.get("ogdkid") != null ? map.get("ogdkid").toString() : "";
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ISNULL(SUM(d.recamt),0) FROM armrec d,armrech h where d.facno=h.facno AND d.recno=h.recno AND h.prgno='ARM423' AND h.recstat='1' AND d.raccno='6001' ");
        sb.append(" AND h.facno='${facno}' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" AND h.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" AND h.n_code_CD ").append(n_code_CD);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" AND h.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" AND h.n_code_DD ").append(n_code_DD);
        }
        sb.append(" AND year(h.recdate) = ${y} and month(h.recdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" AND h.recdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" AND h.recdate= '${d}' ");
                break;
            default:
                sb.append(" AND h.recdate<= '${d}' ");
        }
        String sqlstr = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${facno}", facno);
        erpEJB.setCompany(facno);
        Query query = erpEJB.getEntityManager().createNativeQuery(sqlstr);
        try {
            Object o = query.getSingleResult();
            return (BigDecimal) o;
        } catch (Exception ex) {
            Logger.getLogger(BscGroupShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }
    
   //订单
    protected List getOrder(int y, int m, Date d, int type,LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";
        StringBuilder sb = new StringBuilder();
        List result = new ArrayList();
        String num = "";
        String tramt = "";
        sb.append(" select isnull(sum(d.cdrqy1),0) as num,isnull(convert(decimal(16,2),sum((d.tramts*h.ratio)/(h.taxrate+1))),0) as tramt  ");
        sb.append(" from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno where h.hrecsta <> 'W' ");
        sb.append(" and h.cusno not in ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        sb.append(" and isnull(h.hmark2,'') <> 'FW' and  h.facno='${facno}' ");
        if ("='R'".equals(n_code_DA.trim())) {
            sb.append(" and d.drecsta not in ('98','99','10') ");
        } else {
            sb.append(" and d.drecsta not in ('98','99') ");
        }
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
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.recdate) = ${y} and month(h.recdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and h.recdate= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and h.recdate= '${d}' ");
                break;
            default:
                sb.append(" and h.recdate= '${d}' ");
        }
        String sql = sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        erpEJB.setCompany(facno);
        Query query1 = erpEJB.getEntityManager().createNativeQuery(sql);
        try {
            result = query1.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    Object[] row = (Object[]) result.get(i);
                    num = row[0].toString();
                    tramt = row[1].toString();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(BscGroupShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;

    }
    
}
