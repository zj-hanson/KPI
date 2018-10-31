/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.ejb.IndicatorBean;
import java.lang.reflect.Field;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedHashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author C1749 PPM*1000000空压机组的
 */
public class FeedstockPPMAA extends Complaints {

    IndicatorBean indicatorBean = lookupIndicatorBeanBean();

    public FeedstockPPMAA() {
        super();
        queryParams.put("formid", "JL-空压机组进料");
        queryParams.put("deptno", "1M000");
    }

    //Other1(不良数) Other2 （总进料数）
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mon;
        Field f;
        double t = 0;
        BigDecimal sum = BigDecimal.ZERO;
        Double a1, a2,a3;
        Indicator i = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y, map.get("deptno").toString());
        IndicatorDetail a = i.getActualIndicator();
        IndicatorDetail o1 = i.getOther1Indicator();
        IndicatorDetail o2 = i.getOther2Indicator();
        IndicatorDetail o3 = i.getOther3Indicator();
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
            if(a3 != 0){
                BigDecimal v1 = new BigDecimal(a1);
                BigDecimal v2 = new BigDecimal(a2);
                sum = v1.add(v2);//QZ+QJQC
                sum = sum.divide(BigDecimal.valueOf(a3), 6,BigDecimal.ROUND_HALF_UP);//除以总的进料数
                sum = sum.multiply(BigDecimal.valueOf(1000000));//乘以1000000等于PPM值
                System.out.println(sum);
                return  sum;
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
