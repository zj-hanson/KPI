/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.comm.SuperEJBForMES;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;

/**
 * 
 * @author FredJie
 */
public class CustomerComplaintsIn implements Actual {

    IndicatorBean indicatorBean = lookupIndicatorBean();

    SuperEJBForMES superEJBForMES = lookupSuperEJBForMES();
    
    SuperEJBForERP superEJBForErp =lookupSuperEJBForERP(); 

    protected LinkedHashMap<String, Object> queryParams;

    protected final org.apache.logging.log4j.Logger log4j = LogManager.getLogger("cn.hanbell.kpi");

    public CustomerComplaintsIn() {
        queryParams = new LinkedHashMap<>();        
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {

    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }

    private IndicatorBean lookupIndicatorBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private SuperEJBForMES lookupSuperEJBForMES() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForMES) c
                    .lookup("java:global/KPI/KPI-ejb/SuperEJBForMES!cn.hanbell.kpi.comm.SuperEJBForMES");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
    private SuperEJBForERP lookupSuperEJBForERP() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForERP) c
                    .lookup("java:global/KPI/KPI-ejb/SuperEJBForERP!cn.hanbell.kpi.comm.SuperEJBForERP");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
    @Override
    public int getUpdateMonth(int y, int m) {
        return m;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        return y;
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal complaintNum = BigDecimal.ZERO;
        BigDecimal shipNum = BigDecimal.ZERO;

        int id = Integer.parseInt(map.get("id").toString());
        Indicator indicator = indicatorBean.findById(id);
        if (indicator == null) {
            return BigDecimal.ZERO;
        }
        try {
            Method setMethod = IndicatorDetail.class
                    .getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", m).toUpperCase(), BigDecimal.class);
            // o1投诉总数		
            IndicatorDetail o1 = indicator.getOther1Indicator();
            if (o1 != null) {
                map.put("facno", "C");
                map.put("sSupplyId", "SZJ00065");
                complaintNum = getComplaintNum(y, m, d, type, map);
                map.put("facno", "H");
                map.put("sSupplyId", "HAH00001");
                complaintNum.add(getComplaintNum(y, m, d, type, map));
                setMethod.invoke(o1, complaintNum);
                indicatorBean.updateIndicatorDetail(o1);
            }
            // o2总发货件数		
            IndicatorDetail o2 = indicator.getOther2Indicator();
            if (o2 != null) {
                map.put("facno", "H");
                map.put("con", " and h.cusno !='HAH00001'");
                shipNum = getShipNum(y, m, d, type, map);
                map.put("facno", "Y");
                map.put("con", " and h.cusno !='YZJ00001'");
                shipNum=shipNum.add(getShipNum(y, m, d, type, map));
                setMethod.invoke(o2, shipNum);
                indicatorBean.updateIndicatorDetail(o2);
            }
            if (shipNum.compareTo(BigDecimal.ZERO) != 0) {
                return complaintNum.divide(shipNum, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d));
            }
            if (complaintNum.compareTo(BigDecimal.ZERO) != 0) {
                return BigDecimal.valueOf(100d);
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    protected BigDecimal getComplaintNum(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        //供应商
        String sSupplyId = map.get("sSupplyId") != null ? map.get("sSupplyId").toString() : "";

        BigDecimal complaintNum = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ISNULL(SUM(F.SUMNUM),0) FROM (SELECT PROJECTID  ");
        sb.append(" FROM FLOW_FORM_UQF_S_NOW WHERE RESPONSIBILITYTYPE = '厂外责任' ");
        sb.append(" AND (RESPONSIBILITYCOMPANYID = '${s}' OR RELATEDCOMPANYID = '${s}') ");
        sb.append(" AND YEAR(PROJECTCREATETIME)=${y} AND MONTH(PROJECTCREATETIME)=${m}  ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,PROJECTCREATETIME) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,PROJECTCREATETIME) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,PROJECTCREATETIME) <= ${d} ");
        }
        sb.append(" ) G INNER JOIN (SELECT PROJECTID, SUM(DEFECTNUM) AS SUMNUM FROM ANALYSISRESULT_QCD ");
        sb.append(" WHERE isnull(PROJECTID,'') <> '' AND   isnull(PRODUCTID,'') <> '' GROUP BY PROJECTID) F ON G.PROJECTID = F.PROJECTID ");
        sb.append(" INNER  JOIN (SELECT PROJECTID FROM FLOW_PROJECT_NOW WHERE FLOWPROCESSID = 'UQF' AND   PROCESSNODEID > 'UQFN0004') CC ON G.PROJECTID = CC.PROJECTID ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}",
                BaseLib.formatDate("dd", d)).replace("${s}", sSupplyId);

        superEJBForMES.setCompany(facno);
        Query query = superEJBForMES.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            complaintNum = BigDecimal.valueOf(Double.valueOf(o1.toString()));
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return complaintNum;
    }

    protected BigDecimal getShipNum(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        // 获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String con = map.get("con") != null ? map.get("con").toString() : "";
        BigDecimal ton = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ISNULL(SUM(d.shpqy1 ),0) FROM cdrhad h, cdrdta d ");
        sb.append(" WHERE h.facno = d.facno AND   h.shpno = d.shpno ");
        sb.append(" and h.houtsta not in ('N','W') and h.trtype in ('L1A','S1A') ");
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" and h.shpdate<= '${d}' ");
                break;
            case 5:
                // 日
                sb.append(" and h.shpdate= '${d}' ");
                break;
            default:
                sb.append(" and h.shpdate<= '${d}' ");
        }
        if(con!=null){
            sb.append(con);
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m))
                .replace("${d}", BaseLib.formatDate("yyyy-MM-dd", d));

        superEJBForErp.setCompany(facno);
        Query query = superEJBForErp.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            ton = (BigDecimal) o1;
        } catch (Exception ex) {
            log4j.error("ShipmentPredictTonHY.getValue()异常", ex);
        }
        return ton;
    }

}
