/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class InventoryTurnoverBean implements Serializable {

    @EJB
    protected IndicatorBean indicatorBean;

    protected List<Indicator> indicators;

    protected Calendar c;
    protected int y;
    protected int m;
    protected Date d;
    protected final DecimalFormat doubleFormat;
    protected Logger log4j = LogManager.getLogger();
    String[] strArr;

    public InventoryTurnoverBean() {
        this.c = Calendar.getInstance();
        this.doubleFormat = new DecimalFormat("###,###.##");
        indicators = new ArrayList<>();
    }

    public void init() {
        this.y = c.get(Calendar.YEAR);
        this.m = c.get(Calendar.MONTH) + 1;
        this.d = c.getTime();
        if (this.indicators != null) {
            this.indicators.clear();
        }
    }

    public void addIndicators(Indicator i) {
        this.indicators.add(i);
    }

    /**
     * @param indicatorList
     * @param y
     * @param m
     * @return List
     * @throws java.lang.Exception
     * @description 获取总表
     */
    public List<String[]> getHtmlTable(List<Indicator> indicatorList, int y, int m) throws Exception {
        List<String[]> arrList = new ArrayList<>();
        try {
            /**
             * 添加指标
             */
            for (Indicator i : indicatorList) {
                arrList.addAll(getHtmlTableRow(i, y, m));
            }
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return arrList;
    }

    /**
     * @param indicator
     * @param y
     * @param m
     * @return List
     * @throws java.lang.Exception
     * @description 获取各目标表的行
     */
    protected List<String[]> getHtmlTableRow(Indicator indicator, int y, int m) throws Exception {
        List<String[]> arrList;
        IndicatorDetail a = indicator.getActualIndicator();// 实际
        IndicatorDetail b = indicator.getBenchmarkIndicator();// 去年同期
        IndicatorDetail t = indicator.getTargetIndicator();// 目标
        IndicatorDetail other1 = indicator.getOther1Indicator();//本年累计
        IndicatorDetail other2 = indicator.getOther2Indicator();//去年累计
        IndicatorDetail fc = indicator.getForecastIndicator();//预测值 放去年累计值
        String mon;
        Field f;
        try {
            arrList = new ArrayList<>();
            strArr = new String[9];
            /**
             * 目标类型*
             */
            strArr[0] = indicator.getCategory();

            /**
             * 责任单位*
             */
            strArr[1] = indicator.getDeptname();
            /**
             * 分类*
             */
            strArr[2] = indicator.getDescript();

            /**
             * 负责人*
             */
            strArr[3] = indicator.getUsername();

            /**
             * 当季目标*
             */
            mon = this.getIndicatorColumn("N", m);
            f = t.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            String targetAmt = f.get(t).toString();
            strArr[4] = targetAmt;

            /**
             * 当月库存*
             */
            mon = this.getIndicatorColumn("N", m);
            f = a.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            String actualAmt = f.get(a).toString();
            strArr[5] = actualAmt;

            /**
             * 去年同期*
             */
            mon = this.getIndicatorColumn("N", m);
            f = b.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            String benchmarkAmt = f.get(b).toString();
            strArr[6] = benchmarkAmt;

            /**
             * 本年累计*
             */
            mon = this.getIndicatorColumn("N", m);
            f = other1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            String other1Amt = f.get(other1).toString();
            strArr[7] = other1Amt;

            /**
             * 去年累计*
             */
            mon = this.getIndicatorColumn("N", m);
            f = fc.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            String fcAmt = f.get(fc).toString();
            strArr[8] = fcAmt;
            arrList.add(strArr);
            return arrList;
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex) {
            log4j.error("InventoryProductBean.getDisplayInvamountProductList()异常！！！", ex.toString());
        }
        return null;
    }

    public List<String[]> getInventoryTurnoverResultList(int y, int m) {
        List<String[]> arrList = new ArrayList<>();
        try {
            /**
             * 生产目标
             */
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear("生产周转天数", y);
            indicatorBean.getEntityManager().clear();
            /**
             * 指标排序
             */
            indicators.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() > o2.getSortid()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    this.divideByRateOther(i, 2);
                }
                arrList.addAll(getHtmlTable(this.indicators, y, m));
            }
            /**
             * 营业目标
             */
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear("营业周转天数", y);
            indicatorBean.getEntityManager().clear();
            /**
             * 指标排序
             */
            indicators.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() > o2.getSortid()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    this.divideByRateOther(i, 2);
                }
                arrList.addAll(getHtmlTable(this.indicators, y, m));
            }

            /**
             * 服务目标
             */
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear("服务周转天数", y);
            indicatorBean.getEntityManager().clear();
            // 指标排序
            indicators.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() > o2.getSortid()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    this.divideByRateOther(i, 2);
                }
                arrList.addAll(getHtmlTable(this.indicators, y, m));
            }
            /**
             * 其他
             */
//            if (this.indicators.size() > 0) {
//                this.indicators.clear();
//            }
//            indicators = indicatorBean.findByCategoryAndYear("其他目标", y);
//            indicatorBean.getEntityManager().clear();
//            // 指标排序
//            indicators.sort((Indicator o1, Indicator o2) -> {
//                if (o1.getSortid() > o2.getSortid()) {
//                    return 1;
//                } else {
//                    return -1;
//                }
//            });
//            if (indicators != null && !indicators.isEmpty()) {
//                for (Indicator i : indicators) {
//                    this.divideByRateOther(i, 2);
//                }
//                arrList.addAll(getHtmlTable(this.indicators, y, m));
//            }

            /**
             * 最后的总合计
             */
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear("周转天数总计", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    this.divideByRateOther(i, 2);
                }
                arrList.addAll(getHtmlTable(this.indicators, y, m));
            }
            return arrList;
        } catch (Exception ex) {
            ex.toString();
        }
        return null;
    }

    public String getIndicatorColumn(String formtype, int m) {
        if (formtype.equals("N")) {
            return "n" + String.format("%02d", m);
        } else if (formtype.equals("D")) {
            return "d" + String.format("%02d", m);
        } else if (formtype.equals("NQ")) {
            return "nq" + String.valueOf(m);
        } else {
            return "";
        }
    }

    /**
     * @param i
     * @param scale
     * @description 换算率
     */
    public void divideByRateOther(Indicator i, int scale) {
        indicatorBean.divideByRate(i.getActualIndicator(), i.getRate(), scale);
        indicatorBean.divideByRate(i.getBenchmarkIndicator(), i.getRate(), scale);
        indicatorBean.divideByRate(i.getTargetIndicator(), i.getRate(), scale);
    }

    public List<Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<Indicator> indicators) {
        this.indicators = indicators;
    }
}
