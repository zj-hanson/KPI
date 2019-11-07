/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @version V1.0
 * @author C1749
 * @data:2019-10-28
 * @description:
 */
public abstract class InventoryTurnover implements Actual {

    protected SuperEJBForERP superEJB;

    IndicatorBean indicatorBean = lookupIndicatorBeanBean();

    protected LinkedHashMap<String, Object> queryParams;

    protected final Logger log4j = LogManager.getLogger();

    public InventoryTurnover() {
        queryParams = new LinkedHashMap<>();
    }

    public SuperEJBForERP getEJB() {
        return superEJB;
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup(JNDIName);
        superEJB = (SuperEJBForERP) objRef;
    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }

    private IndicatorBean lookupIndicatorBeanBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
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

    /**
     * @param y
     * @param m
     * @param d
     * @param type
     * @param map
     * @param sell
     * @return BigDecimal
     * @description 本月周转天数 = 30 / (本月销售成本/((本月库存金额+上月库存金额)/2))
     * @throws:
     */
    public BigDecimal getMonthValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map, BigDecimal sell) {
        String mon, upmon;
        Field f;
        BigDecimal v1, v2, v3;
        BigDecimal result = BigDecimal.ZERO;
        Double a1, a2;
        Double r1, r2;
        Indicator upi;
        IndicatorDetail a;
        Indicator i = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y, map.get("deptno").toString());
        /**
         * 判断当前Indicator是否是空值
         */
        if (i != null) {
            a = i.getActualIndicator();
        } else {
            return BigDecimal.ZERO;
        }
        try {
            if (i.getFormid().equals("冷媒库存金额")) {
                Indicator rateIndicator = indicatorBean.findByFormidYearAndDeptno("制冷库存分摊比率", y, "1F000");
                IndicatorDetail rate = rateIndicator.getOther1Indicator();
                /**
                 * 当月冷煤分摊比率
                 */
                mon = indicatorBean.getIndicatorColumn("N", m);
                f = rate.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                r1 = Double.valueOf(f.get(rate).toString());
                /**
                 * 当月库存金额
                 */
                f = a.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                a1 = Double.valueOf(f.get(a).toString());
                v1 = BigDecimal.valueOf(a1).divide(BigDecimal.valueOf(r1), 2, BigDecimal.ROUND_HALF_UP);
                /**
                 * 上月库存金额 考虑跨年的情况
                 */
                if (m == 1) {
                    upi = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y - 1, map.get("deptno").toString());
                    IndicatorDetail upa = upi.getActualIndicator();
                    Indicator upRateIndicator = indicatorBean.findByFormidYearAndDeptno("制冷库存分摊比率", y - 1, "1F000");
                    IndicatorDetail upRate = upRateIndicator.getOther1Indicator();
                    /**
                     * 分摊比率
                     */
                    upmon = indicatorBean.getIndicatorColumn("N", 12);
                    f = upRate.getClass().getDeclaredField(mon);
                    f.setAccessible(true);
                    r2 = Double.valueOf(f.get(upRate).toString());

                    f = upa.getClass().getDeclaredField(upmon);
                    f.setAccessible(true);
                    a2 = Double.valueOf(f.get(upa).toString());
                    v2 = BigDecimal.valueOf(a2).divide(BigDecimal.valueOf(r2), 2, BigDecimal.ROUND_HALF_UP);
                } else {
                    upmon = indicatorBean.getIndicatorColumn("N", m - 1);
                    /**
                     * 分摊比率
                     */
                    f = rate.getClass().getDeclaredField(upmon);
                    f.setAccessible(true);
                    r2 = Double.valueOf(f.get(rate).toString());

                    /**
                     * 库存金额
                     */
                    f = a.getClass().getDeclaredField(upmon);
                    f.setAccessible(true);
                    a2 = Double.valueOf(f.get(a).toString());
                    v2 = BigDecimal.valueOf(a2).divide(BigDecimal.valueOf(r2), 2, BigDecimal.ROUND_HALF_UP);
                }
            } else {
                /**
                 * 当月库存金额
                 */
                mon = indicatorBean.getIndicatorColumn("N", m);
                f = a.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                a1 = Double.valueOf(f.get(a).toString());
                v1 = BigDecimal.valueOf(a1);
                /**
                 * 上月库存金额 考虑跨年的情况
                 */
                if (m == 1) {
                    upi = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y - 1, map.get("deptno").toString());
                    IndicatorDetail upa = upi.getActualIndicator();
                    upmon = indicatorBean.getIndicatorColumn("N", 12);
                    f = upa.getClass().getDeclaredField(upmon);
                    f.setAccessible(true);
                    a2 = Double.valueOf(f.get(upa).toString());
                    v2 = BigDecimal.valueOf(a2);
                } else {
                    upmon = indicatorBean.getIndicatorColumn("N", m - 1);
                    f = a.getClass().getDeclaredField(upmon);
                    f.setAccessible(true);
                    a2 = Double.valueOf(f.get(a).toString());
                    v2 = BigDecimal.valueOf(a2);
                }
            }

            /**
             * 销售成本
             */
            v3 = sell;
            /**
             * 本月周转天数 = 30 / (本月销售成本/((本月库存金额+上月库存金额)/2))
             */
            if (v1.add(v2).compareTo(BigDecimal.ZERO) != 0) {
                /**
                 * 周转率 = (本月销售成本/((本月库存金额+上月库存金额)/2))
                 */
                BigDecimal rate = v3.divide((v1.add(v2)).divide(BigDecimal.valueOf(2), 2, BigDecimal.ROUND_HALF_UP), 2, BigDecimal.ROUND_HALF_UP);
                /**
                 * 周转天数
                 */
                if (rate.compareTo(BigDecimal.ZERO) != 0) {
                    result = BigDecimal.valueOf(30).divide(rate, 0, BigDecimal.ROUND_HALF_UP);
                }
            }
            return result;
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    /**
     * @param y
     * @param m
     * @param d
     * @param type
     * @param map
     * @param sell
     * @return BigDecimal
     * @description 累计周转天数 = 今年到本月底的天数 / (本月销售成本/((本月库存金额+上月库存金额)/2))
     * @throws:
     */
    public BigDecimal getYearValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map, BigDecimal sell) {
        String mon, upmon;
        Field f;
        BigDecimal v1, v2, v3;
        BigDecimal result = BigDecimal.ZERO;
        Double a1, a2;
        Double r1, r2;
        Indicator upi;
        IndicatorDetail a;
        Indicator i = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y, map.get("deptno").toString());
        /**
         * 判断当前Indicator是否是空值
         */
        if (i != null) {
            a = i.getActualIndicator();
        } else {
            return BigDecimal.ZERO;
        }
        try {
            if (i.getFormid().equals("冷媒库存金额")) {
                Indicator rateIndicator = indicatorBean.findByFormidYearAndDeptno("制冷库存分摊比率", y, "1F000");
                IndicatorDetail rate = rateIndicator.getOther1Indicator();

                /**
                 * 当月冷煤分摊比率
                 */
                mon = indicatorBean.getIndicatorColumn("N", m);
                f = rate.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                r1 = Double.valueOf(f.get(rate).toString());

                /**
                 * 当月库存金额
                 */
                f = a.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                a1 = Double.valueOf(f.get(a).toString());
                v1 = BigDecimal.valueOf(a1).divide(BigDecimal.valueOf(r1), 2, BigDecimal.ROUND_HALF_UP);
                /**
                 * 上月库存金额 考虑跨年的情况
                 */
                if (m == 1) {
                    upi = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y - 1, map.get("deptno").toString());
                    IndicatorDetail upa = upi.getActualIndicator();
                    Indicator upRateIndicator = indicatorBean.findByFormidYearAndDeptno("制冷库存分摊比率", y - 1, "1F000");
                    IndicatorDetail upRate = upRateIndicator.getOther1Indicator();
                    /**
                     * 分摊比率
                     */
                    upmon = indicatorBean.getIndicatorColumn("N", 12);
                    f = upRate.getClass().getDeclaredField(mon);
                    f.setAccessible(true);
                    r2 = Double.valueOf(f.get(upRate).toString());

                    f = upa.getClass().getDeclaredField(upmon);
                    f.setAccessible(true);
                    a2 = Double.valueOf(f.get(upa).toString());
                    v2 = BigDecimal.valueOf(a2).divide(BigDecimal.valueOf(r2), 2, BigDecimal.ROUND_HALF_UP);
                } else {
                    upmon = indicatorBean.getIndicatorColumn("N", m - 1);
                    /**
                     * 分摊比率
                     */
                    f = rate.getClass().getDeclaredField(upmon);
                    f.setAccessible(true);
                    r2 = Double.valueOf(f.get(rate).toString());

                    /**
                     * 库存金额
                     */
                    f = a.getClass().getDeclaredField(upmon);
                    f.setAccessible(true);
                    a2 = Double.valueOf(f.get(a).toString());
                    v2 = BigDecimal.valueOf(a2).divide(BigDecimal.valueOf(r2), 2, BigDecimal.ROUND_HALF_UP);
                }
            } else {

                /**
                 * 当月库存金额
                 */
                mon = indicatorBean.getIndicatorColumn("N", m);
                f = a.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                a1 = Double.valueOf(f.get(a).toString());
                v1 = BigDecimal.valueOf(a1);
                /**
                 * 上月库存金额 考虑跨年的情况
                 */
                if (m == 1) {
                    upi = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y - 1, map.get("deptno").toString());
                    IndicatorDetail upa = upi.getActualIndicator();
                    upmon = indicatorBean.getIndicatorColumn("N", 12);
                    f = upa.getClass().getDeclaredField(upmon);
                    f.setAccessible(true);
                    a2 = Double.valueOf(f.get(upa).toString());
                    v2 = BigDecimal.valueOf(a2);
                } else {
                    upmon = indicatorBean.getIndicatorColumn("N", m - 1);
                    f = a.getClass().getDeclaredField(upmon);
                    f.setAccessible(true);
                    a2 = Double.valueOf(f.get(a).toString());
                    v2 = BigDecimal.valueOf(a2);
                }
            }
            /**
             * 销售成本
             */
            v3 = sell;
            /**
             * 本月周转天数 = 今年截止到本月底的天数 / (本年销售成本/((去年年底库存金额+本月库存金额)/2))
             */
            if (v1.add(v2).compareTo(BigDecimal.ZERO) != 0) {
                /**
                 * 周转率 = (本年销售成本/((去年年底库存金额+上月库存金额)/2))
                 */
                BigDecimal rate = v3.divide((v1.add(v2)).divide(BigDecimal.valueOf(2), 2, BigDecimal.ROUND_HALF_UP), 2, BigDecimal.ROUND_HALF_UP);
                /**
                 * 周转天数
                 */
                if (rate.compareTo(BigDecimal.ZERO) != 0) {
                    result = BigDecimal.valueOf(days(y, m)).divide(rate, 0, BigDecimal.ROUND_HALF_UP);
                }
            }
            return result;
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    /**
     * @param year
     * @param month
     * @return int
     * @description 获取本年度到本月底的天数
     */
    public int days(int year, int month) {
        int sumDays = 0;
        int days = 0;
        for (int i = 1; i <= month; i++) {
            switch (i) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    days = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    days = 30;
                    break;
                case 2:
                    // 闰年
                    if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                        days = 29;
                    } else {
                        days = 28;
                    }
                    break;
            }
            sumDays += days;
        }
        return sumDays;
    }

}
