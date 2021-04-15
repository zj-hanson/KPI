/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.mail;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanson.kpi.evaluation.SalesOrderTon;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class SalesSheetMailBean extends SheetMail {

    protected BigDecimal totalSum1;
    protected BigDecimal totalSum2;

    protected Indicator otherIndicator;

    public SalesSheetMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        salesOrder = new SalesOrderTon();
        super.init();
    }

    @Override
    protected String getMailBody() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：吨</div>");
        sb.append(getSalesOrderTonTable());
        sb.append("<div class=\"tableTitle\" style='text-align:center'>主要客户订单统计</div>");
        sb.append("<div class=\"tableTitle\">单位：吨</div>");
        sb.append(getCustomerOrderTonTable());
        sb.append("<div class=\"tableTitle\">本月实际: 本月累计订单量</div>");
        sb.append("<div class=\"tableTitle\">本月目标: 年度方针设定的月完成目标</div>");
        sb.append("<div class=\"tableTitle\">本月达成: (本月实际/本月目标) ×100% </div>");
        sb.append("<div class=\"tableTitle\">年累计实际: 累计至报表查询日的订单量</div>");
        sb.append("<div class=\"tableTitle\">年累计目标: 累计至报表查询日的目标值</div>");
        sb.append("<div class=\"tableTitle\">年累计达成: (年累计实际/年累计目标) ×100% </div>");
        sb.append("<div class=\"tableTitle\"><span style=\"color:red\">注：报表数据已做合并抵消，扣除汉扬销售汉声部分</span></div>");
        return sb.toString();
    }

    private String getSalesOrderTonTable() {
        StringBuilder sb = new StringBuilder();

        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("HSHY订单重量", y);
        indicatorBean.getEntityManager().clear();
        //指标排序
        indicators.sort((Indicator o1, Indicator o2) -> {
            if (o1.getSortid() > o2.getSortid()) {
                return 1;
            } else {
                return -1;
            }
        });
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                //按换算率计算结果
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(indicators, y, m, d, true, "HS/HY合计"));
        } else {
            sb.append("");
        }
        totalSum1 = getData().get("sum1");
        totalSum2 = getData().get("sum2");

        return sb.toString();
    }

    private String getCustomerOrderTonTable() {
        StringBuilder sb = new StringBuilder();

        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("主要客户订单重量", y);
        indicatorBean.getEntityManager().clear();
        //指标排序
        indicators.sort((Indicator o1, Indicator o2) -> {
            if (o1.getSortid() > o2.getSortid()) {
                return 1;
            } else {
                return -1;
            }
        });
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                //按换算率计算结果
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(indicators, y, m, d, false));
        } else {
            sb.append("");
        }

        return sb.toString();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        getData().put("sum2", BigDecimal.ZERO);
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th>类别</th><th>项目</th>");
            sb.append("<th>1月</th><th>2月</th><th>3月</th><th>4月</th><th>5月</th><th>6月</th><th>7月</th><th>8月</th><th>9月</th><th>10月</th><th>11月</th><th>12月</th><th>全年</th>");
            sb.append("<th>订单未交</th>");
            sb.append("<th>年度比重</th>");
            sb.append("</tr>");
            if (!indicatorList.isEmpty()) {
                // 主要客户合计
                indicator = indicatorBean.getSumValue(indicatorList);
                // 计算其他客户
                if (sumIndicator != null) {
                    otherIndicator = indicatorBean.getSubtractValue(sumIndicator, indicator);
                    otherIndicator.setFormid("其他客户订单重量");
                    otherIndicator.setName("其他客户");
                    indicatorBean.updatePerformance(otherIndicator);
                }
            }
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d));
            }
            // 其他客户
            if (totalSum1.compareTo(BigDecimal.ZERO) != 0) {
                getData().put("sum1", totalSum1.subtract(getData().get("sum1")));
            }
            if (totalSum2.compareTo(BigDecimal.ZERO) != 0) {
                getData().put("sum2", totalSum2.subtract(getData().get("sum2")));
            }
            sb.append(getHtmlTableRow(otherIndicator, y, m, d));
            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum, String sumTitle) {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        getData().put("sum2", BigDecimal.ZERO);
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th>类别</th><th>项目</th>");
            sb.append("<th>1月</th><th>2月</th><th>3月</th><th>4月</th><th>5月</th><th>6月</th><th>7月</th><th>8月</th><th>9月</th><th>10月</th><th>11月</th><th>12月</th><th>全年</th>");
            sb.append("<th>订单未交</th>");
            sb.append("<th>年度比重</th>");
            sb.append("</tr>");
            if (needsum) {
                totalActualValue = BigDecimal.ZERO;
                sumIndicator = indicatorBean.getSumValue(indicatorList);
                if (sumIndicator != null) {
                    if (sumTitle != null) {
                        sumIndicator.setName(sumTitle);
                    }
                    totalActualValue = sumIndicator.getActualIndicator().getNfy();
                    indicatorBean.updatePerformance(sumIndicator);
                }
            }
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d, null));
            }
            if (needsum && sumIndicator != null) {
                sb.append(getHtmlTableRow(sumIndicator, y, m, d, null));
            }
            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    @Override
    protected String getHtmlTableRow(Indicator e, int y, int m, Date d) throws Exception {
        String col, mon;
        StringBuilder sb = new StringBuilder();
        IndicatorDetail a = e.getActualIndicator();
        IndicatorDetail b = e.getBenchmarkIndicator();
        IndicatorDetail p = e.getPerformanceIndicator();
        IndicatorDetail t = e.getTargetIndicator();
        Field f;
        mon = indicatorBean.getIndicatorColumn("N", getM());
        BigDecimal v;
        Method setMethod;
        BigDecimal num1, num2;
        try {
            actualAccumulated = new IndicatorDetail();
            actualAccumulated.setParent(e);
            actualAccumulated.setType("A");

            benchmarkAccumulated = new IndicatorDetail();
            benchmarkAccumulated.setParent(e);
            benchmarkAccumulated.setType("B");

            targetAccumulated = new IndicatorDetail();
            targetAccumulated.setParent(e);
            targetAccumulated.setType("T");

            AP = new IndicatorDetail();
            AP.setParent(e);
            AP.setType("P");

            BG = new IndicatorDetail();
            BG.setParent(e);
            BG.setType("P");

            AG = new IndicatorDetail();
            AG.setParent(e);
            AG.setType("P");

            for (int i = getM(); i > 0; i--) {
                //顺序计算的话会导致累计值重复累加
                //实际值累计
                v = indicatorBean.getAccumulatedValue(e.getActualIndicator(), i);
                setMethod = actualAccumulated.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(actualAccumulated, v);
                //同期值累计
                v = indicatorBean.getAccumulatedValue(e.getBenchmarkIndicator(), i);
                setMethod = benchmarkAccumulated.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(benchmarkAccumulated, v);
                //目标值累计
                v = indicatorBean.getAccumulatedValue(e.getTargetIndicator(), i);
                setMethod = targetAccumulated.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(targetAccumulated, v);
                //累计达成
                v = indicatorBean.getAccumulatedPerformance(e.getActualIndicator(), e.getTargetIndicator(), i);
                setMethod = AP.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(AP, v);
                //同比成长率
                v = indicatorBean.getGrowth(e.getActualIndicator(), e.getBenchmarkIndicator(), i);
                setMethod = BG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(BG, v);
                //累计成长率
                v = indicatorBean.getGrowth(actualAccumulated, benchmarkAccumulated, i);
                setMethod = AG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(AG, v);
            }
            //按当前月份累计值重设全年累计
            f = actualAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            actualAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(actualAccumulated).toString())));

            f = benchmarkAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            benchmarkAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(benchmarkAccumulated).toString())));

            f = targetAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            targetAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(targetAccumulated).toString())));

            if (e.getActualInterface() != null && e.getActualEJB() != null && e.getId() != -1) {
                // 本日订单
                Actual actualInterface = (Actual) Class.forName(e.getActualInterface()).newInstance();
                actualInterface.setEJB(e.getActualEJB());
                num1 = actualInterface.getValue(y, m, d, Calendar.DATE, actualInterface.getQueryParams())
                        .divide(e.getRate(), 2, RoundingMode.HALF_UP);
                // 未交订单
                if (salesOrder != null) {
                    salesOrder.setEJB(e.getActualEJB());
                    num2 = salesOrder.getNotDelivery(d, actualInterface.getQueryParams()).divide(e.getRate(), 2,
                            RoundingMode.HALF_UP);
                } else {
                    num2 = BigDecimal.ZERO;
                }
            } else {
                num1 = BigDecimal.ZERO;
                num2 = BigDecimal.ZERO;
            }
            if (e.getId() != -1) {
                sumAdditionalData("sum1", num1);
                sumAdditionalData("sum2", num2);
            }

            sb.append("<tr><td  rowspan=\"6\" colspan=\"1\">").append(e.getName()).append("</td>");
            sb.append("<td>接单</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(a))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(a))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(a.getNfy())).append("</td>");
            // 订单未交
            sb.append("<td rowspan=\"6\">").append(decimalFormat.format(e.getId() != -1 ? num2 : getData().get("sum2"))).append("</td>");

            // 年度比重
            if (totalActualValue.compareTo(BigDecimal.ZERO) != 0) {
                sb.append("<td rowspan=\"6\">")
                        .append(percentFormat(a.getNfy()
                                .divide(totalActualValue, 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(BigDecimal.valueOf(100))))
                        .append("</td>");
            } else {
                sb.append("<td rowspan=\"6\"></td>");
            }
            sb.append("</tr>");
            sb.append("<tr><td>接单累计</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = actualAccumulated.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(actualAccumulated))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(actualAccumulated))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(actualAccumulated.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr><td>去年同期</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = b.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(b))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(b))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(b.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr><td>去年累计</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = benchmarkAccumulated.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(benchmarkAccumulated))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(benchmarkAccumulated))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(benchmarkAccumulated.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr><td>同比成长</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = BG.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(BG))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(BG))).append("</td>");
                }
            }
            sb.append("<td>").append(percentFormat(BG.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr><td>累计成长</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = AG.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(AG))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(AG))).append("</td>");
                }
            }
            sb.append("<td>").append(percentFormat(AG.getNfy())).append("</td>");
            sb.append("</tr>");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

    protected String getHtmlTableRow(Indicator e, int y, int m, Date d, String sumStyle) throws Exception {
        String col, mon;
        StringBuilder sb = new StringBuilder();
        IndicatorDetail a = e.getActualIndicator();
        IndicatorDetail b = e.getBenchmarkIndicator();
        IndicatorDetail p = e.getPerformanceIndicator();
        IndicatorDetail t = e.getTargetIndicator();
        Field f;
        mon = indicatorBean.getIndicatorColumn("N", getM());
        BigDecimal v;
        Method setMethod;
        BigDecimal num1, num2;
        try {
            actualAccumulated = new IndicatorDetail();
            actualAccumulated.setParent(e);
            actualAccumulated.setType("A");

            benchmarkAccumulated = new IndicatorDetail();
            benchmarkAccumulated.setParent(e);
            benchmarkAccumulated.setType("B");

            targetAccumulated = new IndicatorDetail();
            targetAccumulated.setParent(e);
            targetAccumulated.setType("T");

            AP = new IndicatorDetail();
            AP.setParent(e);
            AP.setType("P");

            BG = new IndicatorDetail();
            BG.setParent(e);
            BG.setType("P");

            AG = new IndicatorDetail();
            AG.setParent(e);
            AG.setType("P");

            for (int i = getM(); i > 0; i--) {
                //顺序计算的话会导致累计值重复累加
                //实际值累计
                v = indicatorBean.getAccumulatedValue(e.getActualIndicator(), i);
                setMethod = actualAccumulated.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(actualAccumulated, v);
                //同期值累计
                v = indicatorBean.getAccumulatedValue(e.getBenchmarkIndicator(), i);
                setMethod = benchmarkAccumulated.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(benchmarkAccumulated, v);
                //目标值累计
                v = indicatorBean.getAccumulatedValue(e.getTargetIndicator(), i);
                setMethod = targetAccumulated.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(targetAccumulated, v);
                //累计达成
                v = indicatorBean.getAccumulatedPerformance(e.getActualIndicator(), e.getTargetIndicator(), i);
                setMethod = AP.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(AP, v);
                //同比成长率
                v = indicatorBean.getGrowth(e.getActualIndicator(), e.getBenchmarkIndicator(), i);
                setMethod = BG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(BG, v);
                //累计成长率
                v = indicatorBean.getGrowth(actualAccumulated, benchmarkAccumulated, i);
                setMethod = AG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(AG, v);
            }
            //按当前月份累计值重设全年累计
            f = actualAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            actualAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(actualAccumulated).toString())));

            f = benchmarkAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            benchmarkAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(benchmarkAccumulated).toString())));

            f = targetAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            targetAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(targetAccumulated).toString())));

            if (e.getActualInterface() != null && e.getActualEJB() != null && e.getId() != -1) {
                // 本日订单
                Actual actualInterface = (Actual) Class.forName(e.getActualInterface()).newInstance();
                actualInterface.setEJB(e.getActualEJB());
                num1 = actualInterface.getValue(y, m, d, Calendar.DATE, actualInterface.getQueryParams())
                        .divide(e.getRate(), 2, RoundingMode.HALF_UP);
                // 未交订单
                if (salesOrder != null) {
                    salesOrder.setEJB(e.getActualEJB());
                    num2 = salesOrder.getNotDelivery(d, actualInterface.getQueryParams()).divide(e.getRate(), 2,
                            RoundingMode.HALF_UP);
                } else {
                    num2 = BigDecimal.ZERO;
                }
            } else {
                num1 = BigDecimal.ZERO;
                num2 = BigDecimal.ZERO;
            }
            if (e.getId() != -1) {
                sumAdditionalData("sum1", num1);
                sumAdditionalData("sum2", num2);
            }

            sb.append("<tr><td  rowspan=\"10\" colspan=\"1\">").append(e.getName()).append("</td>");
            sb.append("<td>目标</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = t.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(t))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(t))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(t.getNfy())).append("</td>");
            // 订单未交
            sb.append("<td rowspan=\"10\">").append(decimalFormat.format(e.getId() != -1 ? num2 : getData().get("sum2"))).append("</td>");
            // 年度比重
            if (e.getId() != -1 && totalActualValue.compareTo(BigDecimal.ZERO) != 0) {
                sb.append("<td rowspan=\"10\">")
                        .append(percentFormat(a.getNfy()
                                .divide(totalActualValue, 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(BigDecimal.valueOf(100))))
                        .append("</td>");
            } else {
                sb.append("<td rowspan=\"10\"></td>");
            }
            sb.append("</tr>");
            sb.append("<tr><td>实际</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(a))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(a))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(a.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr><td>本月达成</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = p.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(p))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(p))).append("</td>");
                }
            }
            sb.append("<td>").append(percentFormat(p.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr><td>目标累计</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = targetAccumulated.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(targetAccumulated))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(targetAccumulated))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(targetAccumulated.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr><td>实际累计</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = actualAccumulated.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(actualAccumulated))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(actualAccumulated))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(actualAccumulated.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr><td>累计达成</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = AP.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(AP))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(AP))).append("</td>");
                }
            }
            sb.append("<td>").append(percentFormat(AP.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr><td>去年同期</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = b.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(b))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(b))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(b.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr><td>去年累计</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = benchmarkAccumulated.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(benchmarkAccumulated))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(benchmarkAccumulated))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(benchmarkAccumulated.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr><td>同比成长</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = BG.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(BG))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(BG))).append("</td>");
                }
            }
            sb.append("<td>").append(percentFormat(BG.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr><td>累计成长</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = AG.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(AG))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(AG))).append("</td>");
                }
            }
            sb.append("<td>").append(percentFormat(AG.getNfy())).append("</td>");
            sb.append("</tr>");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

}
