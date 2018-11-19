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
public class FreeServiceOuterSum1G extends FreeServiceERP{

    IndicatorBean indicatorBean = lookupIndicatorBean();

    public FreeServiceOuterSum1G() {
        super();
        queryParams.put("formid", "A-空压机体服务成本");
        queryParams.put("deptno", "1G000");
    }

    //差旅费+运费+服务领退料
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mon;
        Field f;
        BigDecimal v1;
        Double a1, a2, a3;
        Indicator i = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y, map.get("deptno").toString());
        IndicatorDetail o1 = i.getOther1Indicator();
        IndicatorDetail o2 = i.getOther2Indicator();
        IndicatorDetail o3 = i.getOther2Indicator();
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

            v1 = BigDecimal.valueOf(a1 + a2 + a3);
            
            return v1;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(FreeServiceOuterSum1G.class.getName()).log(Level.SEVERE, null, ex);
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
