/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.ShipmentMail;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.evaluation.SalesOrder;
import cn.hanbell.kpi.evaluation.SalesOrderAmount;
import cn.hanbell.kpi.evaluation.SalesOrderAmountAA;
import cn.hanbell.kpi.evaluation.SalesOrderQuantity;
import cn.hanbell.kpi.evaluation.SalesOrderQuantityAA;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class AAShipmentSpecialMailBean extends ShipmentMail {

    List<Indicator> indicatorList = new ArrayList<>();
    protected SalesOrder oldSalesOrder;

    public AAShipmentSpecialMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    public String getMailBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getQuantityTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAmountTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getServiceTable());
        return sb.toString();
    }

    protected String getQuantityTable() {
        this.indicatorList.clear();
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("A机组每日出货台数", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            indicatorList.addAll(indicators);
            List<Indicator> data = indicatorBean.findByCategoryAndYear("A机组每日出货台数-按机型分", y);
            if (data != null && !data.isEmpty()) {
                indicatorList.addAll(1, data);
            }
            salesOrder = new SalesOrderQuantity();
            oldSalesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicatorList, y, m, d, true);
        } else {
            return "A机组每日出货台数设定错误";
        }
    }

    protected String getAmountTable() {
        this.indicatorList.clear();
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机组每日出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            indicatorList.addAll(indicators);
            List<Indicator> data = indicatorBean.findByCategoryAndYear("A机组每日出货金额-按机型分", y);
            if (data != null && !data.isEmpty()) {
                indicatorList.addAll(1, data);
            }
            for (Indicator i : indicatorList) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            oldSalesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicatorList, y, m, d, true);
        } else {
            return "A机组每日出货金额设定错误";
        }
    }

    protected String getServiceTable() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机组收费服务金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = null;
            return getHtmlTable(this.indicators, y, m, d, false);
        } else {
            return "A机组收费服务金额设定错误";
        }
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        getData().put("sum2", BigDecimal.ZERO);
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
                sumIndicator = indicatorBean.getSumValue(indicators);
                if (sumIndicator != null) {
                    indicatorBean.updatePerformance(sumIndicator);
                    sb.append(getHtmlTableRow(sumIndicator, y, m, d));
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
            BigDecimal num1, num2;
            if (indicator.getActualInterface() != null && indicator.getActualEJB() != null && indicator.getId() != -1) {
                //本日出货
                Actual actualInterface = (Actual) Class.forName(indicator.getActualInterface()).newInstance();
                actualInterface.setEJB(indicator.getActualEJB());
                num1 = actualInterface.getValue(y, m, d, Calendar.DATE, actualInterface.getQueryParams()).divide(indicator.getRate(), 2, RoundingMode.HALF_UP);
                //未交订单
                switch (indicator.getCategory()) {
                    case "A机组每日出货台数-按机型分":
                    case "A机组每日订单台数-按机型分":
                        salesOrder = new SalesOrderQuantityAA();
                        break;
                    case "A机组每日出货金额-按机型分":
                    case "A机组每日订单金额-按机型分":
                        salesOrder = new SalesOrderAmountAA();
                        break;
                    default:
                        salesOrder = oldSalesOrder;
                        break;
                }
                if (salesOrder != null) {
                    salesOrder.setEJB(indicator.getActualEJB());
                    num2 = salesOrder.getNotDelivery(d, actualInterface.getQueryParams()).divide(indicator.getRate(), 2, RoundingMode.HALF_UP);
                } else {
                    num2 = BigDecimal.ZERO;
                }
            } else {
                num1 = BigDecimal.ZERO;
                num2 = BigDecimal.ZERO;
            }
            if (indicator.getId() != -1 && !indicator.getCategory().contains("-按机型分")) {
                sumAdditionalData("sum1", num1);
                sumAdditionalData("sum2", num2);
            }
            String color = indicator.getCategory().contains("-按机型分") ? "style=\"background:#CCFFCC;\"" : "";
            sb.append("<tr>");
            sb.append("<td ").append(color).append(">").append(indicator.getName()).append("</td>");
            sb.append("<td ").append(color).append(">").append(decimalFormat.format(indicator.getId() != -1 ? num1 : getData().get("sum1"))).append("</td>");
            //当月
            //实际
            f = a.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td ").append(color).append(">").append(decimalFormat.format(f.get(a))).append("</td>");
            //目标
            f = t.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td ").append(color).append(">").append(decimalFormat.format(f.get(t))).append("</td>");
            //达成
            f = p.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td ").append(color).append(">").append(percentFormat(f.get(p))).append("</td>");
            //同期
            f = b.getClass().getDeclaredField(col);
            f.setAccessible(true);
            //sb.append("<td>").append(decimalFormat.format(f.get(b))).append("</td>");
            //改成按天折算
            sb.append("<td ").append(color).append(">").append(decimalFormat.format(indicatorBean.getValueOfDays(BigDecimal.valueOf(Double.valueOf(f.get(b).toString())), d, 0))).append("</td>");
            //成长
            //sb.append("<td>").append(percentFormat(indicatorBean.getGrowth(a, b, m))).append("</td>");
            //改成按天折算
            sb.append("<td ").append(color).append(">").append(percentFormat(indicatorBean.getGrowth(a, b, m, d, 0))).append("</td>");
            //累计
            //实际
            sb.append("<td ").append(color).append(">").append(decimalFormat.format(indicatorBean.getAccumulatedValue(a, m))).append("</td>");
            //目标
            //sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(t, m))).append("</td>");
            //改成按天折算
            sb.append("<td ").append(color).append(">").append(decimalFormat.format(indicatorBean.getAccumulatedValue(t, m, d))).append("</td>");
            //达成
            //sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedPerformance(a, t, m))).append("</td>");
            //改成按天折算
            sb.append("<td ").append(color).append(">").append(percentFormat(indicatorBean.getAccumulatedPerformance(a, false, t, true, m, d))).append("</td>");
            //同期
            //sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(b, m))).append("</td>");
            //改成按天折算
            sb.append("<td ").append(color).append(">").append(decimalFormat.format(indicatorBean.getAccumulatedValue(b, m, d))).append("</td>");
            //成长
            //sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m))).append("</td>");
            //改成按天折算
            sb.append("<td ").append(color).append(">").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m, d))).append("</td>");
            //年度目标
            f = t.getClass().getDeclaredField("nfy");
            f.setAccessible(true);
            sb.append("<td ").append(color).append(">").append(decimalFormat.format(f.get(t))).append("</td>");
            //年度达成
            f = p.getClass().getDeclaredField("nfy");
            f.setAccessible(true);
            sb.append("<td ").append(color).append(">").append(percentFormat(f.get(p))).append("</td>");
            sb.append("<td ").append(color).append(">").append(decimalFormat.format(indicator.getId() != -1 ? num2 : getData().get("sum2"))).append("</td>");
            sb.append("</tr>");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

}
