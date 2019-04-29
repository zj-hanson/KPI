/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorDailyBean;
import cn.hanbell.kpi.entity.IndicatorDaily;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author C1879
 */
public abstract class Production implements Actual {

    protected SuperEJBForERP superEJB;

    protected LinkedHashMap<String, Object> queryParams;

    public IndicatorDailyBean indicatorDailyBean = lookupIndicatorDailyBeanBean();

    public IndicatorBean indicatorBean = lookupIndicatorBeanBean();

    public Production() {
        queryParams = new LinkedHashMap<>();
    }

    public SuperEJBForERP getEJB() {
        return superEJB;
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup(JNDIName);
        superEJB = (SuperEJBForERP) objRef;
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

    public void updateValue(int day, BigDecimal na, IndicatorDaily daily) {
        String col = "setD" + String.format("%02d", day);
        try {
            Method setMethod = daily.getClass().getDeclaredMethod(col, BigDecimal.class);
            setMethod.invoke(daily, na);
        } catch (Exception ex) {
            Logger.getLogger(ProductionPlanOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private IndicatorBean lookupIndicatorBeanBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private IndicatorDailyBean lookupIndicatorDailyBeanBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorDailyBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorDailyBean!cn.hanbell.kpi.ejb.IndicatorDailyBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
