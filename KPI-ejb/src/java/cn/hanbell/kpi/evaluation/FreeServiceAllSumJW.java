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
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author C1879
 */
public class FreeServiceAllSumJW extends FreeServiceERP {

    IndicatorBean indicatorBean = lookupIndicatorBeanBean();

    public FreeServiceAllSumJW() {
        super();
        queryParams.put("formid", "A-境外综合成本");
        queryParams.put("deptno", "1T100");
    }

    //6产品合计
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mon;
        Field f;
        BigDecimal v1;
        Double a1, a2, a3, a4, a5, a6;
        Indicator i = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y, map.get("deptno").toString());
        IndicatorDetail o1 = i.getOther1Indicator();
        IndicatorDetail o2 = i.getOther2Indicator();
        IndicatorDetail o3 = i.getOther3Indicator();
        IndicatorDetail o4 = i.getOther4Indicator();
        IndicatorDetail o5 = i.getOther5Indicator();
        IndicatorDetail o6 = i.getOther6Indicator();
        try {
            mon = indicatorBean.getIndicatorColumn("N", m);
            f = o1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a1 = Double.valueOf(f.get(o1).toString());

            f = o2.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a2 = Double.valueOf(f.get(o2).toString());

            f = o3.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a3 = Double.valueOf(f.get(o3).toString());

            f = o4.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a4 = Double.valueOf(f.get(o4).toString());

            f = o5.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a5 = Double.valueOf(f.get(o5).toString());

            f = o6.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a6 = Double.valueOf(f.get(o6).toString());

            v1 = BigDecimal.valueOf(a1 + a2 + a3 + a4 + a5 + a6);

            return v1;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            indicatorBean.getLog4j().error(ex);
        }
        return BigDecimal.ZERO;
    }

    private IndicatorBean lookupIndicatorBeanBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            indicatorBean.getLog4j().error(ne);
            throw new RuntimeException(ne);
        }
    }
}
