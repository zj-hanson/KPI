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
 * @author C1879 刀具总费用
 */
public class ConsumptionCutterYXo6 extends ConsumptionCutter {

    IndicatorBean indicatorBean = lookupIndicatorBeanBean();

    public ConsumptionCutterYXo6() {
        super();
        queryParams.put("formid", "A-圆型件刀具耗用");
        queryParams.put("deptno", "1P500");
    }

    //得到Other2值、Other3的值、Other4的值相加
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mon;
        Field f;
        BigDecimal v1;
        Double a2, a3, a4;
        Indicator i = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y, map.get("deptno").toString());
        IndicatorDetail o2 = i.getOther2Indicator();
        IndicatorDetail o3 = i.getOther3Indicator();
        IndicatorDetail o4 = i.getOther4Indicator();
        try {
            mon = indicatorBean.getIndicatorColumn("N", m);
            f = o2.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a2 = Double.valueOf(f.get(o2).toString());

            f = o3.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a3 = Double.valueOf(f.get(o3).toString());

            f = o4.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a4 = Double.valueOf(f.get(o4).toString());

            v1 = BigDecimal.valueOf(a3 + a2 + a4);

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
