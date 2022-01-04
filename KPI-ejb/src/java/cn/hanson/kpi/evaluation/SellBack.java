/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
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
public class SellBack implements Actual {

    IndicatorBean indicatorBean = lookupIndicatorBean();

    SuperEJBForERP superEJBForERP = lookupSuperEJBForERP();

    protected LinkedHashMap<String, Object> queryParams;

    protected final org.apache.logging.log4j.Logger log4j = LogManager.getLogger("cn.hanbell.kpi");

    public SellBack() {
        queryParams = new LinkedHashMap<>();
        queryParams.put("facno", "H");
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
        BigDecimal saleAamount = BigDecimal.ZERO;
        BigDecimal backAamount = BigDecimal.ZERO;

        int id = Integer.parseInt(map.get("id").toString());
        Indicator indicator = indicatorBean.findById(id);
        if (indicator == null) {
            return BigDecimal.ZERO;
        }
        try {
            Method setMethod = IndicatorDetail.class
                    .getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", m).toUpperCase(), BigDecimal.class);
            // o1退货金额（包含扣款）
            IndicatorDetail o1 = indicator.getOther1Indicator();
            if (o1 != null) {
                backAamount = getBackAamount(y, m, d, type, map);
                backAamount= backAamount.add(getBuckleAamount(y, m, d, type, map));
                setMethod.invoke(o1, backAamount);
                indicatorBean.updateIndicatorDetail(o1);
            }
            // o2生产总量
            IndicatorDetail o2 = indicator.getOther2Indicator();
            if (o2 != null) {
                saleAamount = getSaleAamount(y, m, d, type, map);
                setMethod.invoke(o2, saleAamount);
                indicatorBean.updateIndicatorDetail(o2);
            }
            if (saleAamount.compareTo(BigDecimal.ZERO) != 0) {
                return backAamount.divide(saleAamount, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d));
            }
            if (backAamount.compareTo(BigDecimal.ZERO) != 0) {
                return BigDecimal.valueOf(100d);
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 退货金额
     * @param y
     * @param m
     * @param d
     * @param type
     * @param map
     * @return 
     */
    protected BigDecimal getBackAamount(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        
        BigDecimal backAamount = BigDecimal.ZERO;
        
        StringBuilder sb = new StringBuilder();
        sb.append("   SELECT round(ISNull(SUM(CASE cdrbhad.tax WHEN '4' THEN cdrbdta.bakamts /(1 + cdrbhad.taxrate) ELSE cdrbdta.bakamts END),0),2) ");
        sb.append(" FROM cdrbhad, cdrbdta ");
        sb.append(" WHERE (cdrbdta.facno = cdrbhad.facno) AND   (cdrbdta.bakno = cdrbhad.bakno) ");
        sb.append(" AND  cdrbhad.baksta = 'Y'  AND cdrbhad.facno = '${f}' ");
        sb.append(" AND YEAR(cdrbhad.bakdate)=${y} AND MONTH(cdrbhad.bakdate)=${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,cdrbhad.bakdate) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,cdrbhad.bakdate) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,cdrbhad.bakdate) <= ${d} ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}",
                BaseLib.formatDate("dd", d)).replace("${f}", facno);

        superEJBForERP.setCompany(facno);
        Query query = superEJBForERP.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            backAamount = BigDecimal.valueOf(Double.valueOf(o1.toString()));
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return backAamount;
    }
   
    /**
     * 扣款金额
     * @param y
     * @param m
     * @param d
     * @param type
     * @param map
     * @return 
     */
    protected BigDecimal getBuckleAamount(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";

        BigDecimal buckleAamount = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append("   SELECT ROUND(ISNULL(SUM(pmamt),0),2) FROM armpmm   ");
        sb.append(" WHERE YEAR(trdat)=${y} AND MONTH(trdat)=${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,trdat) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,trdat) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,trdat) <= ${d} ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}",
                BaseLib.formatDate("dd", d));
        superEJBForERP.setCompany(facno);
        Query query = superEJBForERP.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            buckleAamount = BigDecimal.valueOf(Double.valueOf(o1.toString()));
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return buckleAamount;
    }

    /**
     * 销售总金额
     * @param y
     * @param m
     * @param d
     * @param type
     * @param map
     * @return 
     */
    protected BigDecimal getSaleAamount(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        BigDecimal saleAamount = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT round(ISNull(SUM(CASE cdrhad.tax WHEN '4' THEN cdrdta.shpamts /(1 + cdrhad.taxrate) ELSE cdrdta.shpamts END),0),2) ");
        sb.append("  FROM cdrdta, cdrhad   ");
        sb.append(" WHERE (cdrhad.facno = cdrdta.facno) AND   (cdrhad.shpno = cdrdta.shpno) ");
        sb.append(" AND  cdrhad.facno = '${f}'  AND cdrhad.houtsta = 'Y'   ");
        sb.append(" AND year(cdrhad.shpdate) = ${y} AND month(cdrhad.shpdate)= ${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,cdrhad.shpdate) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,cdrhad.shpdate) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,cdrhad.shpdate) = ${d} ");
        }

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}",
                BaseLib.formatDate("dd", d)).replace("${f}", facno);
        superEJBForERP.setCompany(facno);
        Query query = superEJBForERP.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            saleAamount = BigDecimal.valueOf(Double.valueOf(o1.toString()));
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return saleAamount;
    }

}
