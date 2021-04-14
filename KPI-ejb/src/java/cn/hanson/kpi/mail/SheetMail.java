/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.mail;

import cn.hanbell.kpi.comm.BscSheetMail;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanson.kpi.evaluation.SalesOrder;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author C0160
 */
public abstract class SheetMail extends BscSheetMail {

    protected BigDecimal totalActualValue;
    protected SalesOrder salesOrder;

    public SheetMail() {

    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        getData().put("sum2", BigDecimal.ZERO);
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th>产品</th><th>项目</th>");
            sb.append("<th>1月</th><th>2月</th><th>3月</th><th>4月</th><th>5月</th><th>6月</th><th>7月</th><th>8月</th><th>9月</th><th>10月</th><th>11月</th><th>12月</th><th>全年</th>");
            sb.append("<th>年度比重</th>");
            sb.append("</tr>");
            if (needsum) {
                totalActualValue = BigDecimal.ZERO;
                sumIndicator = indicatorBean.getSumValue(indicators);
                if (sumIndicator != null) {
                    totalActualValue = sumIndicator.getActualIndicator().getNfy();
                    indicatorBean.updatePerformance(sumIndicator);
                }
            }
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d));
            }
            if (needsum && sumIndicator != null) {
                sb.append(getHtmlTableRow(sumIndicator, y, m, d));
            }
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
            sb.append("<tr><th>产品</th><th>项目</th>");
            sb.append("<th>1月</th><th>2月</th><th>3月</th><th>4月</th><th>5月</th><th>6月</th><th>7月</th><th>8月</th><th>9月</th><th>10月</th><th>11月</th><th>12月</th><th>全年</th>");
            sb.append("<th>年度比重</th>");
            sb.append("</tr>");
            if (needsum) {
                totalActualValue = BigDecimal.ZERO;
                sumIndicator = indicatorBean.getSumValue(indicators);
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
            // 年度比重
            if (e.getId() != -1 && totalActualValue.compareTo(BigDecimal.ZERO) != 0) {
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

    @Override
    protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Hanson</title>");
        sb.append(css);
        sb.append("</head><body><div style=\"margin: auto;text-align: center;\">");
        sb.append("<div style=\"width:100%\" class=\"title\">");
        sb.append("<div style=\"text-align:center;width:100%\">浙江汉声精密机械有限公司</div>");
        sb.append("<div style=\"text-align:center;width:100%\">").append(mailSubject).append("</div>");
        sb.append("<div style=\"text-align:center;width:100%; color:Red;\">日期:")
                .append(BaseLib.formatDate("yyyy-MM-dd", d)).append("</div>");
        sb.append("</div>");
        return sb.toString();
    }

    @Override
    public void setD(Date d) {
        this.d = d;
        this.c.setTime(d);
        this.y = c.get(Calendar.YEAR);
        this.m = c.get(Calendar.MONTH) + 1;
    }

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

}
