/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForCRM;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.comm.SuperEJBForMES;
import cn.hanbell.kpi.ejb.QualityLevelTargetBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.QualityLevelTarget;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1749
 */
public abstract class QRA implements Actual {

    protected SuperEJBForCRM superEJBForCRM = lookupSuperEJBForCRM();
    protected SuperEJBForERP superEJBForERP = lookupSuperEJBForERP();
    protected SuperEJBForKPI superEJBForKPI = lookupSuperEJBForKPI();
    protected SuperEJBForMES superEJBForMES = lookupSuperEJBForMES();

    QualityLevelTargetBean qualityLevelTargetBean = lookupQualityLevelTargetBean();
    ;
    protected LinkedHashMap<String, Object> queryParams;
    protected final Logger log4j = LogManager.getLogger();

    public QRA() {
        queryParams = new LinkedHashMap<>();
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {

    }

    protected SuperEJBForCRM lookupSuperEJBForCRM() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForCRM) c.lookup("java:global/KPI/KPI-ejb/SuperEJBForCRM!cn.hanbell.kpi.comm.SuperEJBForCRM");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    protected SuperEJBForERP lookupSuperEJBForERP() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForERP) c.lookup("java:global/KPI/KPI-ejb/SuperEJBForERP!cn.hanbell.kpi.comm.SuperEJBForERP");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    protected SuperEJBForKPI lookupSuperEJBForKPI() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForKPI) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    protected SuperEJBForMES lookupSuperEJBForMES() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForMES) c.lookup("java:global/KPI/KPI-ejb/SuperEJBForMES!cn.hanbell.kpi.comm.SuperEJBForMES");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }

    @Override
    public int getUpdateMonth(int y, int m) {
        int month;
        if (m == 1) {
            month = 12;
        } else {
            month = m - 1;
        }
        return month;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        int year;
        if (m == 1) {
            year = y - 1;
        } else {
            year = y;
        }
        return year;
    }

    public List removeDuplicate(List list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    public BigDecimal getActualValue(Indicator i, int y, int m) {
        IndicatorDetail other1 = i.getOther1Indicator();
        IndicatorDetail other2 = i.getOther2Indicator();
        IndicatorDetail other3 = i.getOther3Indicator();
        IndicatorDetail other4 = i.getOther4Indicator();
        IndicatorDetail other5 = i.getOther5Indicator();
        IndicatorDetail other6 = i.getOther6Indicator();
        List<QualityLevelTarget> data;

        BigDecimal value = BigDecimal.ZERO;
        BigDecimal otherValue;
        try {
            data = qualityLevelTargetBean.findByIndicatorIdAndSeq(i.getId(), y);
            if (!data.isEmpty()) {
                for (QualityLevelTarget q : data) {
                    if (Objects.equals(q.getOther(), other1.getId())) {
                        otherValue = getOtherMonValue(other1, m);
                        value = value.add(q.getTarget1().multiply(otherValue).setScale(2, BigDecimal.ROUND_HALF_UP));
                        System.out.println("other1"+otherValue);
                    }
                    if (Objects.equals(q.getOther(), other2.getId())) {
                        otherValue = getOtherMonValue(other2, m);
                        value = value.add(q.getTarget1().multiply(otherValue).setScale(2, BigDecimal.ROUND_HALF_UP));
                        System.out.println("other2"+otherValue);
                    }
                    if (Objects.equals(q.getOther(), other3.getId())) {
                        otherValue = getOtherMonValue(other3, m);
                        value = value.add(q.getTarget1().multiply(otherValue).setScale(2, BigDecimal.ROUND_HALF_UP));
                        System.out.println("other3"+otherValue);
                    }
                    if (Objects.equals(q.getOther(), other4.getId())) {
                        otherValue = getOtherMonValue(other4, m);
                        value = value.add(q.getTarget1().multiply(otherValue).setScale(2, BigDecimal.ROUND_HALF_UP));
                        System.out.println("other4"+otherValue);
                    }
                    if (Objects.equals(q.getOther(), other5.getId())) {
                        otherValue = getOtherMonValue(other5, m);
                        value = value.add(q.getTarget1().multiply(otherValue).setScale(2, BigDecimal.ROUND_HALF_UP));
                        System.out.println("other5"+otherValue);
                    }
                    if (Objects.equals(q.getOther(), other6.getId())) {
                        otherValue = getOtherMonValue(other6, m);
                        value = value.add(q.getTarget1().multiply(otherValue).setScale(2, BigDecimal.ROUND_HALF_UP));
                        System.out.println("other6"+otherValue);
                    }
                }
            }
            return value.divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP);
        } catch (Exception ex) {
            log4j.error("QRA类中getActualValue()方法异常：", ex);
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal getOtherMonValue(IndicatorDetail d, int m) {
        Field f;
        String mon;
        BigDecimal value;
        try {
            mon = this.getIndicatorColumn("N", m);
            //分数
            f = d.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            value = BigDecimal.valueOf(Double.valueOf(f.get(d).toString()));
            return value;
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex) {
            log4j.error("QRA类的getOtherMonValue()方法异常：", ex);
        }
        return BigDecimal.ZERO;

    }

    public String getIndicatorColumn(String formtype, int m) {
        if (formtype.equals("N")) {
            return "n" + String.format("%02d", m);
        } else if (formtype.equals("D")) {
            return "d" + String.format("%02d", m);
        } else {
            return "";
        }
    }

    private QualityLevelTargetBean lookupQualityLevelTargetBean() {
        try {
            Context c = new InitialContext();
            return (QualityLevelTargetBean) c.lookup("java:global/KPI/KPI-ejb/QualityLevelTargetBean!cn.hanbell.kpi.ejb.QualityLevelTargetBean");
        } catch (NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
