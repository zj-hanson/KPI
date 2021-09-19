/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */

package cn.hanson.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorSet;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C0160
 */
public class MachiningEfficiencyCollection implements Actual {

    IndicatorBean indicatorBean = lookupIndicatorBeanBean();

    protected LinkedHashMap<String, Object> queryParams;

    protected final Logger log4j = LogManager.getLogger("cn.hanbell.kpi");

    public MachiningEfficiencyCollection() {
        this.queryParams = new LinkedHashMap<>();
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {

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
        Integer id = Integer.parseInt(map.get("id").toString());
        Indicator entity = indicatorBean.findById(id);
        if (entity == null) {
            return BigDecimal.ZERO;
        }
        List<Indicator> indicators = new ArrayList<>();
        List<IndicatorSet> setList = indicatorBean.findIndicatorSetByPId(id);
        for (IndicatorSet is : setList) {
            Indicator i = indicatorBean.findByFormidYearAndDeptno(is.getFormid(), entity.getSeq(), is.getDeptno());
            if (i != null) {
                indicators.add(i);
            }
        }
        if (!indicators.isEmpty()) {
            Indicator si = indicatorBean.getSumValue(indicators);
            String col = "";
            Field field;
            Method setMethod;
            try {
                col = indicatorBean.getIndicatorColumn(entity.getFormtype(), m);
                if (col != null && !"".equals(col)) {
                    // 实际产值
                    field = si.getForecastIndicator().getClass().getDeclaredField(col);
                    field.setAccessible(true);
                    setMethod = entity.getForecastIndicator().getClass().getDeclaredMethod(
                        "set" + col.substring(0, 1).toUpperCase() + col.substring(1), BigDecimal.class);
                    setMethod.invoke(entity.getForecastIndicator(),
                        BigDecimal.valueOf(Double.valueOf(field.get(si.getForecastIndicator()).toString())));
                    indicatorBean.updateIndicatorDetail(entity.getForecastIndicator());

                    // 标准工时
                    field = si.getBenchmarkIndicator().getClass().getDeclaredField(col);
                    field.setAccessible(true);
                    setMethod = entity.getBenchmarkIndicator().getClass().getDeclaredMethod(
                        "set" + col.substring(0, 1).toUpperCase() + col.substring(1), BigDecimal.class);
                    setMethod.invoke(entity.getBenchmarkIndicator(),
                        BigDecimal.valueOf(Double.valueOf(field.get(si.getBenchmarkIndicator()).toString())));
                    indicatorBean.updateIndicatorDetail(entity.getBenchmarkIndicator());

                    // 计划工时
                    field = si.getTargetIndicator().getClass().getDeclaredField(col);
                    field.setAccessible(true);
                    setMethod = entity.getTargetIndicator().getClass().getDeclaredMethod(
                        "set" + col.substring(0, 1).toUpperCase() + col.substring(1), BigDecimal.class);
                    setMethod.invoke(entity.getTargetIndicator(),
                        BigDecimal.valueOf(Double.valueOf(field.get(si.getTargetIndicator()).toString())));
                    indicatorBean.updateIndicatorDetail(entity.getTargetIndicator());

                    // 实际工时
                    field = si.getActualIndicator().getClass().getDeclaredField(col);
                    field.setAccessible(true);
                    return BigDecimal.valueOf(Double.valueOf(field.get(si.getActualIndicator()).toString()));
                }
            } catch (NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalArgumentException
                | IllegalAccessException | InvocationTargetException ex) {
                log4j.error(ex);
            }
        }
        return BigDecimal.ZERO;
    }

    private IndicatorBean lookupIndicatorBeanBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorBean)c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            log4j.error(ne);
            throw new RuntimeException(ne);
        }
    }

}
