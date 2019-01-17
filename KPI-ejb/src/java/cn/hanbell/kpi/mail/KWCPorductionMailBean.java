/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.BscProductiontMail;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDaily;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class KWCPorductionMailBean extends BscProductiontMail {

    public KWCPorductionMailBean() {
        
    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getHtmlTableRow(Indicator e, int y, int m, Date d) throws Exception {
        //获取需要取值栏位
        String col, mon;
        StringBuilder sb = new StringBuilder();

        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(d);
        int day = aCalendar.get(Calendar.DAY_OF_MONTH);

        IndicatorDaily tDaily, o1Daily, o2Daily, o3Daily, o4Daily, daily;
        //生产计划数
        tDaily = findByPidDateAndType(e.getTargetIndicator(), m);
        //实际工令数
        o1Daily = findByPidDateAndType(e.getOther1Indicator(), m);
        //实际完工数
        o2Daily = findByPidDateAndType(e.getOther2Indicator(), m);
        //交货数
        o3Daily = findByPidDateAndType(e.getOther3Indicator(), m);
        //接单数
        o4Daily = findByPidDateAndType(e.getOther4Indicator(), m);
        Field f;
        mon = indicatorBean.getIndicatorColumn("N", getM());
        BigDecimal v;
        Method setMethod;
        try {
            targetAccumulated = new IndicatorDaily();
            Other1Accumulated = new IndicatorDaily();
            Other2Accumulated = new IndicatorDaily();
            Other3Accumulated = new IndicatorDaily();
            Other4Accumulated = new IndicatorDaily();
            for (int i = days(y, m); i > 0; i--) {
                //顺序计算的话会导致累计值重复累加
                //生产计划数累计
                v = indicatorBean.getAccumulatedValue(tDaily, i);
                setMethod = targetAccumulated.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("D", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(targetAccumulated, v);
                //实际工令数累计
                v = indicatorBean.getAccumulatedValue(o1Daily, i);
                setMethod = Other1Accumulated.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("D", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(Other1Accumulated, v);
                //实际完工数累计
                v = indicatorBean.getAccumulatedValue(o2Daily, i);
                setMethod = Other2Accumulated.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("D", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(Other2Accumulated, v);
                //交货数累计
                v = indicatorBean.getAccumulatedValue(o3Daily, i);
                setMethod = Other3Accumulated.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("D", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(Other3Accumulated, v);
                //接单数累计
                v = indicatorBean.getAccumulatedValue(o4Daily, i);
                setMethod = Other4Accumulated.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("D", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(Other4Accumulated, v);
            }

            sb.append("<tr><td>生产计划数</td>");
            for (int i = 1; i <= days(y, m); i++) {
                col = indicatorBean.getIndicatorColumn("D", i);
                f = tDaily.getClass().getDeclaredField(col);
                f.setAccessible(true);
                String value = decimalFormat.format(f.get(tDaily));
                if (i == day) {
                    sb.append("<td style=\"color:red\">").append(value.equals("0") ? "" : value).append("</td>");
                } else {
                    sb.append("<td>").append(value.equals("0") ? "" : value).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(tDaily.getTotal())).append("</td>");
            sb.append("<td>").append(avgBigDecimal(tDaily, days(y, tDaily.getMth())).toString()).append("</td>");
            sb.append("</tr>");

            sb.append("<tr><td>实际工令数</td>");
            for (int i = 1; i <= days(y, m); i++) {
                col = indicatorBean.getIndicatorColumn("D", i);
                f = o1Daily.getClass().getDeclaredField(col);
                f.setAccessible(true);
                String value = decimalFormat.format(f.get(o1Daily));
                if (i == day) {
                    sb.append("<td style=\"color:red\">").append(value.equals("0") ? "" : value).append("</td>");
                } else {
                    sb.append("<td>").append(value.equals("0") ? "" : value).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(o1Daily.getTotal())).append("</td>");
            sb.append("<td>").append(avgBigDecimal(o1Daily, day).toString()).append("</td>");
            sb.append("</tr>");

            sb.append("<tr><td>实际完工数</td>");
            for (int i = 1; i <= days(y, m); i++) {
                col = indicatorBean.getIndicatorColumn("D", i);
                f = o2Daily.getClass().getDeclaredField(col);
                f.setAccessible(true);
                String value = decimalFormat.format(f.get(o2Daily));
                if (i == day) {
                    sb.append("<td style=\"color:red\">").append(value.equals("0") ? "" : value).append("</td>");
                } else {
                    sb.append("<td>").append(value.equals("0") ? "" : value).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(o2Daily.getTotal())).append("</td>");
            sb.append("<td>").append(avgBigDecimal(o2Daily, day).toString()).append("</td>");
            sb.append("</tr>");

            sb.append("<tr><td>工令与完工差异</td>");
            daily = dailySubtract(o2Daily, o1Daily);
            for (int i = 1; i <= days(y, m); i++) {
                col = indicatorBean.getIndicatorColumn("D", i);
                f = daily.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i < day) {
                    sb.append("<td>").append(decimalFormat.format(f.get(daily))).append("</td>");
                } else if (i == day) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(daily))).append("</td>");
                } else {
                    sb.append("<td></td>");
                }
            }
            sb.append("<td></td><td></td></tr>");

            sb.append("<tr><td>生产计划数累计</td>");
            for (int i = 1; i <= days(y, m); i++) {
                col = indicatorBean.getIndicatorColumn("D", i);
                f = targetAccumulated.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == day) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(targetAccumulated))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(targetAccumulated))).append("</td>");
                }
            }
            sb.append("<td></td><td></td></tr>");

            sb.append("<tr><td>实际工令数累计</td>");
            for (int i = 1; i <= days(y, m); i++) {
                col = indicatorBean.getIndicatorColumn("D", i);
                f = Other1Accumulated.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i < day) {
                    sb.append("<td>").append(decimalFormat.format(f.get(Other1Accumulated))).append("</td>");
                } else if (i == day) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(Other1Accumulated))).append("</td>");
                } else {
                    sb.append("<td></td>");
                }
            }
            sb.append("<td></td><td></td></tr>");

            sb.append("<tr><td>计划与工令差异累计</td>");
            daily = dailySubtract(Other1Accumulated, targetAccumulated);
            for (int i = 1; i <= days(y, m); i++) {
                col = indicatorBean.getIndicatorColumn("D", i);
                f = daily.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i < day) {
                    sb.append("<td>").append(decimalFormat.format(f.get(daily))).append("</td>");
                } else if (i == day) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(daily))).append("</td>");
                } else {
                    sb.append("<td></td>");
                }
            }
            sb.append("<td></td><td></td></tr>");

            sb.append("<tr><td>交货数</td>");
            for (int i = 1; i <= days(y, m); i++) {
                col = indicatorBean.getIndicatorColumn("D", i);
                f = o3Daily.getClass().getDeclaredField(col);
                f.setAccessible(true);
                String value = decimalFormat.format(f.get(o3Daily));
                if (i == day) {
                    sb.append("<td style=\"color:red\">").append(value.equals("0") ? "" : value).append("</td>");
                } else {
                    sb.append("<td>").append(value.equals("0") ? "" : value).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(o3Daily.getTotal())).append("</td>");
            sb.append("<td>").append(avgBigDecimal(o3Daily, day).toString()).append("</td>");
            sb.append("</tr>");

            sb.append("<tr><td>交货数累计</td>");
            for (int i = 1; i <= days(y, m); i++) {
                col = indicatorBean.getIndicatorColumn("D", i);
                f = Other3Accumulated.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i < day) {
                    sb.append("<td>").append(decimalFormat.format(f.get(Other3Accumulated))).append("</td>");
                } else if (i == day) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(Other3Accumulated))).append("</td>");
                } else {
                    sb.append("<td></td>");
                }
            }
            sb.append("<td></td><td></td></tr>");
                        
            sb.append("<tr><td>接单数量</td>");
            for (int i = 1; i <= days(y, m); i++) {
                col = indicatorBean.getIndicatorColumn("D", i);
                f = o4Daily.getClass().getDeclaredField(col);
                f.setAccessible(true);
                String value = decimalFormat.format(f.get(o4Daily));
                if (i == day) {
                    sb.append("<td style=\"color:red\">").append(value.equals("0") ? "" : value).append("</td>");
                } else {
                    sb.append("<td>").append(value.equals("0") ? "" : value).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(o4Daily.getTotal())).append("</td>");
            sb.append("<td>").append(avgBigDecimal(o4Daily, day).toString()).append("</td>");
            sb.append("</tr>");

            sb.append("<tr><td>接单累计</td>");
            for (int i = 1; i <= days(y, m); i++) {
                col = indicatorBean.getIndicatorColumn("D", i);
                f = Other4Accumulated.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i < day) {
                    sb.append("<td>").append(decimalFormat.format(f.get(Other4Accumulated))).append("</td>");
                } else if (i == day) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(Other4Accumulated))).append("</td>");
                } else {
                    sb.append("<td></td>");
                }
            }
            sb.append("<td></td><td></td></tr>");

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }
    
    
    @Override
    protected String getMailBody() {
        indicator = indicatorBean.findByFormidYearAndDeptno("Q-螺杆机组每日生产", y, "1P000");
        if (indicator == null) {
            throw new UnsupportedOperationException("Not found 每日生产");
        }
        indicators.clear();
        indicators.add(indicator);
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：").append(indicator.getUnit()).append("</div>");
        sb.append(getHtmlTable(indicators, y, m, d, true));
        return sb.toString();
    }

    
}
