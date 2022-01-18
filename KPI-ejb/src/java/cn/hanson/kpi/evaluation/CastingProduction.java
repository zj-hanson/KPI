/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */

package cn.hanson.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForMES;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDaily;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author C0160
 */
public abstract class CastingProduction implements Actual {

    protected IndicatorBean indicatorBean = lookupIndicatorBean();
    protected SuperEJBForMES superEJBForMES = lookupSuperEJBForMESBean();

    protected LinkedHashMap<String, Object> queryParams;

    protected final org.apache.logging.log4j.Logger log4j = LogManager.getLogger("cn.hanbell.kpi");

    public CastingProduction() {
        queryParams = new LinkedHashMap<>();
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        // 指定连接此处不处理
    }

    private IndicatorBean lookupIndicatorBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorBean)c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            log4j.error(ne);
            throw new RuntimeException(ne);
        }
    }

    private SuperEJBForMES lookupSuperEJBForMESBean() {
        try {
            Context context = new InitialContext();
            return (SuperEJBForMES)context
                .lookup("java:global/KPI/KPI-ejb/SuperEJBForMES!cn.hanbell.kpi.comm.SuperEJBForMES");
        } catch (NamingException ne) {
            log4j.error(ne);
            throw new RuntimeException(ne);
        }
    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
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
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int id = Integer.parseInt(map.get("id").toString());
        Indicator indicator = indicatorBean.findById(id);
        if (indicator == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal planBox = BigDecimal.ZERO;
        BigDecimal planQty = BigDecimal.ZERO;
        BigDecimal planWeight = BigDecimal.ZERO;
        BigDecimal actualBox = BigDecimal.ZERO;
        BigDecimal actualQty = BigDecimal.ZERO;
        BigDecimal actualWeight = BigDecimal.ZERO;
        try {
            Method setMethod = IndicatorDetail.class
                .getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", m).toUpperCase(), BigDecimal.class);
            // 计划箱数o1
            IndicatorDetail o1 = indicator.getOther1Indicator();
            if (o1 != null) {
                planBox = getPlanBox(o1, y, m, day, type, map);
                setMethod.invoke(o1, planBox);
                indicatorBean.updateIndicatorDetail(o1);
            }
            // 计划件数o2
            IndicatorDetail o2 = indicator.getOther2Indicator();
            if (o2 != null) {
                planQty = getPlanQuantity(o2, y, m, day, type, map);
                setMethod.invoke(o2, planQty);
                indicatorBean.updateIndicatorDetail(o2);
            }
            // 计划重量o3
            IndicatorDetail o3 = indicator.getOther3Indicator();
            if (o3 != null) {
                planWeight = getPlanWeight(o3, y, m, day, type, map);
                setMethod.invoke(o3, planWeight);
                indicatorBean.updateIndicatorDetail(o3);
            }
            // 实际箱数o4
            IndicatorDetail o4 = indicator.getOther4Indicator();
            if (o4 != null) {
                actualBox = getBox(o4, y, m, day, type, map);
                setMethod.invoke(o4, actualBox);
                indicatorBean.updateIndicatorDetail(o4);
            }
            // 实际件数o5
            IndicatorDetail o5 = indicator.getOther5Indicator();
            if (o5 != null) {
                actualQty = getQuantity(o5, y, m, day, type, map);
                setMethod.invoke(o5, actualQty);
                indicatorBean.updateIndicatorDetail(o5);
            }
            // 实际重量
            actualWeight = getWeight(y, m, d, type, map);
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return actualWeight;
    }

    protected BigDecimal getPlanBox(IndicatorDetail entity, int y, int m, int d, int type,
        LinkedHashMap<String, Object> map) {
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        // 产线
        String line = map.get("line") != null ? map.get("line").toString() : "";
        // 工序
        String step = "";

        BigDecimal boxes = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ISNULL(SUM(CAST(A.PLANBOXES AS FLOAT)),0) FROM CAST_PROCESS A  WHERE 1=1 ");
        if (!"".equals(line)) {
            sb.append(" AND A.PROCESSLINE ").append(line);
        }
        if (!"".equals(step)) {
            sb.append(" AND A.STEPID ").append(step);
        }
        sb.append(" AND year(A.PRODUCTTIME) = ${y} AND month(A.PRODUCTTIME)= ${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,A.PRODUCTTIME) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,A.PRODUCTTIME) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,A.PRODUCTTIME) = ${d} ");
        }

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}",
            String.valueOf(d));

        superEJBForMES.setCompany(facno);
        Query query = superEJBForMES.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            boxes = BigDecimal.valueOf(Double.valueOf(o1.toString()));
            IndicatorDaily daily =
                indicatorBean.findIndicatorDailyByPIdDateAndType(entity.getId(), entity.getSeq(), m, entity.getType());
            if (daily != null) {
                Method setMethod = daily.getClass().getDeclaredMethod(
                    "set" + indicatorBean.getIndicatorColumn("D", d).toUpperCase(), BigDecimal.class);
                setMethod.invoke(daily, boxes);
                indicatorBean.updateIndicatorDaily(daily);
                return daily.getTotal();
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
        // 计划箱数
        return boxes;
    }

    protected BigDecimal getPlanQuantity(IndicatorDetail entity, int y, int m, int d, int type,
        LinkedHashMap<String, Object> map) {
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        // 产线
        String line = map.get("line") != null ? map.get("line").toString() : "";
        // 工序
        String step = "";

        BigDecimal qty = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ISNULL(SUM(CAST(A.CREATQTY AS FLOAT)),0) FROM CAST_PROCESS A  WHERE 1=1 ");
        if (!"".equals(line)) {
            sb.append(" AND A.PROCESSLINE ").append(line);
        }
        if (!"".equals(step)) {
            sb.append(" AND A.STEPID ").append(step);
        }
        sb.append(" AND year(A.PRODUCTTIME) = ${y} AND month(A.PRODUCTTIME)= ${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,A.PRODUCTTIME) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,A.PRODUCTTIME) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,A.PRODUCTTIME) = ${d} ");
        }

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}",
            String.valueOf(d));

        superEJBForMES.setCompany(facno);
        Query query = superEJBForMES.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            qty = BigDecimal.valueOf(Double.valueOf(o1.toString()));
            IndicatorDaily daily =
                indicatorBean.findIndicatorDailyByPIdDateAndType(entity.getId(), entity.getSeq(), m, entity.getType());
            if (daily != null) {
                Method setMethod = daily.getClass().getDeclaredMethod(
                    "set" + indicatorBean.getIndicatorColumn("D", d).toUpperCase(), BigDecimal.class);
                setMethod.invoke(daily, qty);
                indicatorBean.updateIndicatorDaily(daily);
                return daily.getTotal();
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
        // 计划件数
        return qty;
    }

    protected BigDecimal getPlanWeight(IndicatorDetail entity, int y, int m, int d, int type,
        LinkedHashMap<String, Object> map) {
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        // 产线
        String line = map.get("line") != null ? map.get("line").toString() : "";
        // 工序
        String step = "";

        BigDecimal ton = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ISNULL(SUM(CAST(A.CREATQTY AS FLOAT)*CAST(A.CASTINGWEIGHT AS FLOAT)),0) ");
        sb.append(" FROM CAST_PROCESS A  WHERE 1=1 ");
        if (!"".equals(line)) {
            sb.append(" AND A.PROCESSLINE ").append(line);
        }
        if (!"".equals(step)) {
            sb.append(" AND A.STEPID ").append(step);
        }
        sb.append(" AND year(A.PRODUCTTIME) = ${y} AND month(A.PRODUCTTIME)= ${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,A.PRODUCTTIME) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,A.PRODUCTTIME) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,A.PRODUCTTIME) = ${d} ");
        }

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}",
            String.valueOf(d));

        superEJBForMES.setCompany(facno);
        Query query = superEJBForMES.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            ton = BigDecimal.valueOf(Double.valueOf(o1.toString()));
            IndicatorDaily daily =
                indicatorBean.findIndicatorDailyByPIdDateAndType(entity.getId(), entity.getSeq(), m, entity.getType());
            if (daily != null) {
                Method setMethod = daily.getClass().getDeclaredMethod(
                    "set" + indicatorBean.getIndicatorColumn("D", d).toUpperCase(), BigDecimal.class);
                setMethod.invoke(daily, ton);
                indicatorBean.updateIndicatorDaily(daily);
                return daily.getTotal();
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
        // 计划重量
        return ton;
    }

    protected BigDecimal getBox(IndicatorDetail entity, int y, int m, int d, int type,
        LinkedHashMap<String, Object> map) {
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        // 产线
        String line = map.get("line") != null ? map.get("line").toString() : "";
        // 工序
        String step = map.get("step") != null ? map.get("step").toString() : "";

        BigDecimal boxes = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ISNULL(SUM(CAST(A.TRACKOUTBOXQTY AS FLOAT)),0) ");
        sb.append(" FROM CAST_PROCESS_STEP_P A  WHERE 1=1 ");
        if (!"".equals(line)) {
            sb.append(" AND A.PROCESSLINE ").append(line);
        }
        if (!"".equals(step)) {
            sb.append(" AND A.STEPID ").append(step);
        }
        sb.append(" AND year(A.TRACKOUTTIME) = ${y} AND month(A.TRACKOUTTIME)= ${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,A.TRACKOUTTIME) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,A.TRACKOUTTIME) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,A.TRACKOUTTIME) = ${d} ");
        }

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}",
            String.valueOf(d));

        superEJBForMES.setCompany(facno);
        Query query = superEJBForMES.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            boxes = BigDecimal.valueOf(Double.valueOf(o1.toString()));
            IndicatorDaily daily =
                indicatorBean.findIndicatorDailyByPIdDateAndType(entity.getId(), entity.getSeq(), m, entity.getType());
            if (daily != null) {
                Method setMethod = daily.getClass().getDeclaredMethod(
                    "set" + indicatorBean.getIndicatorColumn("D", d).toUpperCase(), BigDecimal.class);
                setMethod.invoke(daily, boxes);
                indicatorBean.updateIndicatorDaily(daily);
                return daily.getTotal();
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
        // 报工箱数
        return boxes;
    }

    protected BigDecimal getQuantity(IndicatorDetail entity, int y, int m, int d, int type,
        LinkedHashMap<String, Object> map) {
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        // 产线
        String line = map.get("line") != null ? map.get("line").toString() : "";
        // 工序
        String step = map.get("step") != null ? map.get("step").toString() : "";

        BigDecimal qty = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ISNULL(SUM(CAST(A.TRACKOUTQTY AS FLOAT)),0) ");
        sb.append(" FROM CAST_PROCESS_STEP_P A  WHERE 1=1 ");
        if (!"".equals(line)) {
            sb.append(" AND A.PROCESSLINE ").append(line);
        }
        if (!"".equals(step)) {
            sb.append(" AND A.STEPID ").append(step);
        }
        sb.append(" AND year(A.TRACKOUTTIME) = ${y} AND month(A.TRACKOUTTIME)= ${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,A.TRACKOUTTIME) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,A.TRACKOUTTIME) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,A.TRACKOUTTIME) = ${d} ");
        }

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}",
            String.valueOf(d));

        superEJBForMES.setCompany(facno);
        Query query = superEJBForMES.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            qty = BigDecimal.valueOf(Double.valueOf(o1.toString()));
            IndicatorDaily daily =
                indicatorBean.findIndicatorDailyByPIdDateAndType(entity.getId(), entity.getSeq(), m, entity.getType());
            if (daily != null) {
                Method setMethod = daily.getClass().getDeclaredMethod(
                    "set" + indicatorBean.getIndicatorColumn("D", d).toUpperCase(), BigDecimal.class);
                setMethod.invoke(daily, qty);
                indicatorBean.updateIndicatorDaily(daily);
                return daily.getTotal();
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
        // 报工件数
        return qty;
    }

    protected BigDecimal getWeight(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        // 产线
        String line = map.get("line") != null ? map.get("line").toString() : "";
        // 工序
        String step = map.get("step") != null ? map.get("step").toString() : "";

        BigDecimal ton = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ISNULL(SUM(CAST(A.TRACKOUTQTY AS FLOAT)*CAST(A.CASTINGWEIGHT AS FLOAT)),0) ");
        sb.append(" FROM CAST_PROCESS_STEP_P A  WHERE 1=1 ");
        if (!"".equals(line)) {
            sb.append(" AND A.PROCESSLINE ").append(line);
        }
        if (!"".equals(step)) {
            sb.append(" AND A.STEPID ").append(step);
        }
        sb.append(" AND year(A.TRACKOUTTIME) = ${y} AND month(A.TRACKOUTTIME)= ${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,A.TRACKOUTTIME) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,A.TRACKOUTTIME) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,A.TRACKOUTTIME) = ${d} ");
        }

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}",
            String.valueOf(day));

        superEJBForMES.setCompany(facno);
        Query query = superEJBForMES.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            ton = BigDecimal.valueOf(Double.valueOf(o1.toString()));
        } catch (Exception ex) {
            log4j.error(ex);
        }
        // 报工重量
        return ton;
    }

}
