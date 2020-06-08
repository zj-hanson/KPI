/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import cn.hanbell.kpi.ejb.IndicatorDailyBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDaily;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author C0160
 */
public abstract class BscProductiontMail extends MailNotification {

    protected Indicator indicator;
    protected IndicatorDaily targetAccumulated;
    protected IndicatorDaily Other1Accumulated;
    protected IndicatorDaily Other2Accumulated;
    protected IndicatorDaily Other3Accumulated;
    protected IndicatorDaily Other4Accumulated;

    @EJB
    protected IndicatorDailyBean indicatorDailyBean;

    public BscProductiontMail() {

    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {

        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th>计划数/日期</th>");
            for (int i = 1; i <= days(y, m); i++) {
                sb.append("<th>").append(i).append("</th>");
            }
            sb.append("<th>Total</th><th>日平均量</th></tr>");
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d));
            }
            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected IndicatorDaily findByPidDateAndType(IndicatorDetail entit, int m) {
        return indicatorDailyBean.findByPIdDateAndType(entit.getId(), entit.getSeq(), m, entit.getType());
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

            sb.append("<tr><td>完工与工令差异</td>");
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

            sb.append("<tr><td>实际完工数累计</td>");
            for (int i = 1; i <= days(y, m); i++) {
                col = indicatorBean.getIndicatorColumn("D", i);
                f = Other2Accumulated.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i < day) {
                    sb.append("<td>").append(decimalFormat.format(f.get(Other2Accumulated))).append("</td>");
                } else if (i == day) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(Other2Accumulated))).append("</td>");
                } else {
                    sb.append("<td></td>");
                }
            }
            sb.append("<td></td><td></td></tr>");;

            sb.append("<tr><td>工令与计划差异累计</td>");
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

            sb.append("<tr><td>完工与工令差异累计</td>");
            daily = dailySubtract(Other2Accumulated, Other1Accumulated);
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

            sb.append("<tr><td>交货与计划差异累计</td>");
            daily = dailySubtract(Other3Accumulated, targetAccumulated);
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

            sb.append("<tr><td>交货与工令差异累计</td>");
            daily = dailySubtract(Other3Accumulated, Other1Accumulated);
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

            sb.append("<tr><td>接单数</td>");
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

    public int days(int year, int month) {
        int days = 0;
        if (month != 2) {
            switch (month) {
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
            }
        } else {
            // 闰年
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                days = 29;
            } else {
                days = 28;
            }
        }
        return days;
    }

    //按有值天数均分
    public BigDecimal avgBigDecimal(IndicatorDaily entity, int day) {
        String mon;
        BigDecimal total = BigDecimal.ZERO;
        Field f;
        int aa = 0;
        for (int i = 1; i <= day; i++) {
            try {
                mon = indicatorBean.getIndicatorColumn("D", i);
                f = entity.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                total = total.add(BigDecimal.valueOf(Double.valueOf(f.get(entity).toString())));
                if (Double.valueOf(f.get(entity).toString()) != 0.0) {
                    aa += 1;
                }
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                log4j.error(ex);
                total = BigDecimal.ZERO;
            }
        }
        if (aa == 0) {
            return total;
        } else {
            return total.divide(BigDecimal.valueOf(aa), 1, RoundingMode.HALF_UP);
        }
    }

    //IndicatorDaily两两相减
    protected IndicatorDaily dailySubtract(IndicatorDaily aDetail, IndicatorDaily bDetail) {
        IndicatorDaily enDaily = new IndicatorDaily();
        String col;
        Field f1, f2;
        BigDecimal decimal;
        Method setMethod;
        try {
            for (int i = 1; i < 31; i++) {
                col = indicatorBean.getIndicatorColumn("D", i);
                f1 = aDetail.getClass().getDeclaredField(col);
                f1.setAccessible(true);

                f2 = bDetail.getClass().getDeclaredField(col);
                f2.setAccessible(true);

                decimal = BigDecimal.valueOf((Double.valueOf(f1.get(aDetail).toString()) - Double.valueOf(f1.get(bDetail).toString())));
                setMethod = enDaily.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("D", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(enDaily, decimal);
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.comm.BscProductiontMail.dailySubtract()" + e.toString());
        }
        return enDaily;
    }

}
