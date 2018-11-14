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
 * @author C1749
 */
public class ComplaintsQualityBroadAH extends Complaints{
    IndicatorBean indicatorBean = lookupIndicatorBeanBean();

    public ComplaintsQualityBroadAH() {
        super();
        queryParams.put("formid", "KS-机体保外");
        queryParams.put("deptno", "1M000");
    }

    //Other1(客诉) Other2 （移动平均）
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mon;
        Field f;
        BigDecimal t = BigDecimal.ZERO;
        Double a1, a2;
        Indicator i = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y, map.get("deptno").toString());
        IndicatorDetail a = i.getActualIndicator();
        IndicatorDetail o1 = i.getOther2Indicator();
        IndicatorDetail o2 = i.getOther3Indicator();
        try {
            mon = indicatorBean.getIndicatorColumn("N", m);
            f = o1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a1 = Double.valueOf(f.get(o1).toString());

            f = o2.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a2 = Double.valueOf(f.get(o2).toString());  
            if(a2 != 0){
                BigDecimal v1 = new BigDecimal(a1);
                BigDecimal v2 = new BigDecimal(a2);
                t = v1.divide(v2);
                t = t.multiply(BigDecimal.valueOf(100));
                return  t;
            }else{
                System.out.println("移动平均数为0");
            }
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
