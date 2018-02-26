/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author C0160
 */
public abstract class ShipmentMail extends MailNotification {

    public ShipmentMail() {

    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th><th rowspan=\"2\" colspan=\"1\">订单未交</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d));
            }
            if (needsum) {
                Indicator sum = indicatorBean.getSumValue(indicators);
                if (sum != null) {
                    indicatorBean.updatePerformance(sum);
                    sb.append(getHtmlTableRow(sum, y, m, d));
                }
            }
            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        //获取需要取值栏位
        String col = indicatorBean.getIndicatorColumn(indicator.getFormtype(), m);
        StringBuilder sb = new StringBuilder();
        IndicatorDetail a = indicator.getActualIndicator();
        IndicatorDetail b = indicator.getBenchmarkIndicator();
        IndicatorDetail p = indicator.getPerformanceIndicator();
        IndicatorDetail t = indicator.getTargetIndicator();
        Field f;
        try {
            Actual actualInterface = (Actual) Class.forName(indicator.getActualInterface()).newInstance();
            actualInterface.setEJB(indicator.getActualEJB());
            BigDecimal num1 = actualInterface.getValue(y, m, d, Calendar.DATE, actualInterface.getQueryParams()).divide(indicator.getRate(), 2, RoundingMode.HALF_UP);
            if (indicator.getId() != -1) {
                sumAdditionalData("sum1", num1);
            }
            sb.append("<tr>");
            sb.append("<td>").append(indicator.getName()).append("</td>");
            sb.append("<td>").append(decimalFormat.format(indicator.getId() != -1 ? num1 : getData().get("sum1"))).append("</td>");
            //当月
            //实际
            f = a.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td>").append(decimalFormat.format(f.get(a))).append("</td>");
            //目标
            f = t.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td>").append(decimalFormat.format(f.get(t))).append("</td>");
            //达成
            f = p.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td>").append(percentFormat(f.get(p))).append("</td>");
            //同期
            f = b.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td>").append(decimalFormat.format(f.get(b))).append("</td>");
            //成长
            sb.append("<td>").append(percentFormat(indicatorBean.getGrowth(a, b, m))).append("</td>");
            //累计
            //实际
            sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(a, m))).append("</td>");
            //目标
            sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(t, m))).append("</td>");
            //达成
            sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedPerformance(a, t, m))).append("</td>");
            //同期
            sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(b, m))).append("</td>");
            //成长
            sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m))).append("</td>");
            //年度目标
            f = t.getClass().getDeclaredField("nfy");
            f.setAccessible(true);
            sb.append("<td>").append(decimalFormat.format(f.get(t))).append("</td>");
            //年度达成
            f = p.getClass().getDeclaredField("nfy");
            f.setAccessible(true);
            sb.append("<td>").append(percentFormat(f.get(p))).append("</td>");
            sb.append("<td>订单未交</td>");
            sb.append("</tr>");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

}
