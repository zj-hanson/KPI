/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForCRM;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorDetailBean;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1879
 */
public abstract class Service implements Actual {

    protected SuperEJBForCRM superEJB;

    protected LinkedHashMap<String, Object> queryParams;

    public IndicatorBean indicatorBean = lookupIndicatorBean();

    public IndicatorDetailBean indicatorDetailBean = lookupIndicatorDetailBean();

    protected final Logger log4j = LogManager.getLogger();

    public Service() {
        queryParams = new LinkedHashMap<>();
    }

    public SuperEJBForCRM getEJB() {
        return superEJB;
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup(JNDIName);
        superEJB = (SuperEJBForCRM) objRef;
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

    public void updateValue(int m, BigDecimal na, IndicatorDetail detail) {
        String col = "setN" + String.format("%02d", m);
        try {
            Method setMethod = detail.getClass().getDeclaredMethod(col, BigDecimal.class);
            setMethod.invoke(detail, na);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ProductionPlanOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private IndicatorBean lookupIndicatorBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private IndicatorDetailBean lookupIndicatorDetailBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorDetailBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorDetailBean!cn.hanbell.kpi.ejb.IndicatorDetailBean");
        } catch (NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
