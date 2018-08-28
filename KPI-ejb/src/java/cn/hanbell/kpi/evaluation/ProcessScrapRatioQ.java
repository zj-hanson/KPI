/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Field;
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
 * @author C1879
 */
public class ProcessScrapRatioQ extends ProcessQuantity {

    IndicatorBean indicatorBean = lookupIndicatorBeanBean();

    //件数报废率
    public ProcessScrapRatioQ() {
        super();
        queryParams.put("formid", "R-加工报废率");
        queryParams.put("deptno", "1P000");
    }

    //得到Other2（报废数）与Other1的值（加工数总数）
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mon;
        Field f;
        BigDecimal v1;
        Double a1, a2;
        Indicator i = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y, map.get("deptno").toString());
        IndicatorDetail o1 = i.getOther1Indicator();
        IndicatorDetail o2 = i.getOther2Indicator();
        try {
            mon = indicatorBean.getIndicatorColumn("N", m);
            f = o1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a1 = Double.valueOf(f.get(o1).toString());

            f = o2.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a2 = Double.valueOf(f.get(o2).toString());

            if ("".equals(f.get(o1).toString()) || f.get(o1) == null || "0".equals(f.get(o1).toString())) {
                v1 = BigDecimal.valueOf(0);
            } else {
                v1 = BigDecimal.valueOf((a2 / a1) * 100);
            }
            return v1;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ProcessQuantityHFX.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
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
}
