/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanson.kpi.ejb.ppm.QualityBean;
import cn.hanson.kpi.ejb.ppm.SafetyIncidentBean;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author FredJie
 */
public class MinorInjury implements Actual {

    SafetyIncidentBean safetyIncidentBean = lookupSafetyIncidentBean();

    protected LinkedHashMap<String, Object> queryParams;

    protected final org.apache.logging.log4j.Logger log4j = LogManager.getLogger("cn.hanbell.kpi");

    public MinorInjury() {
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

    private SafetyIncidentBean lookupSafetyIncidentBean() {
        try {
            Context c = new InitialContext();
            return (SafetyIncidentBean) c
                      .lookup("java:global/KPI/KPI-ejb/SafetyIncidentBean!cn.hanson.kpi.ejb.ppm.SafetyIncidentBean");
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

        try {
            BigDecimal num =safetyIncidentBean.getNum(y, m, d, type, map);
            return  num;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

}
