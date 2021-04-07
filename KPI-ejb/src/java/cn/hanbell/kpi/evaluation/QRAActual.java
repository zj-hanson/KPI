/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author C1749
 */
public class QRAActual extends QRA {

    IndicatorBean indicatorBean = lookupIndicatorBean();

    public QRAActual() {

    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal value;
        Indicator i = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y, map.get("deptno").toString());
        try {
            value = getActualValue(i, y, m);
            return value;
        } catch (Exception ex) {
            log4j.error("QRAActual类的getValue()方法异常：", ex);
        }
        return BigDecimal.ZERO;
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

}
