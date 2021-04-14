/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.mail;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanson.kpi.evaluation.SalesOrderAmount;
import cn.hanson.kpi.evaluation.SalesOrderTon;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class ShipmentWithHSMailBean extends DailyMail {

    public List<Indicator> sumIndicatorList;
    public Indicator total;
    protected BigDecimal sum1 = BigDecimal.ZERO;
    protected BigDecimal sum2 = BigDecimal.ZERO;

    public ShipmentWithHSMailBean() {
        this.sumIndicatorList = new ArrayList();
    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        if (sumIndicatorList != null) {
            this.sumIndicatorList.clear();
        }
        super.init();
    }

    @Override
    public String getMailBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：吨</div>");
        sb.append(getShipementTonTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getShipementAmountTable());
        sb.append("<div class=\"tableTitle\">本月实际: 本月累计出货 - 本月累计退货</div>");
        sb.append("<div class=\"tableTitle\">本月目标: 年度方针设定的月完成目标</div>");
        sb.append("<div class=\"tableTitle\">本月达成: (本月实际/本月目标) ×100% </div>");
        sb.append("<div class=\"tableTitle\">年累计实际: 累计至报表查询日的出货 - 累计至报表查询日的退货</div>");
        sb.append("<div class=\"tableTitle\">年累计目标: 之前月份的累计目标 + 本月目标/本月天数 ×当前天数</div>");
        sb.append("<div class=\"tableTitle\">年累计达成: (年累计实际/年累计目标) ×100% </div>");
        sb.append("<div class=\"tableTitle\"><span style=\"color:red\">注：报表目标实际累计数据，包含汉扬销售汉声部分</span></div>");
        return sb.toString();
    }

    protected String getShipementTonTable() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append(
                    "<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th><th rowspan=\"2\" colspan=\"1\">订单未交</th></tr>");
            sb.append(
                    "<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append(
                    "<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");

            sum1 = BigDecimal.ZERO;
            sum2 = BigDecimal.ZERO;
            sumIndicatorList.clear();

            salesOrder = new SalesOrderTon();

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("汉声依材质别出货重量", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    indicatorBean.divideByRate(i, 2);
                }
                sb.append(getHtmlTable(indicators, y, m, d, true));
                total = getSumIndicator();
                total.setName("汉声出货吨数");
                sumIndicatorList.add(total);
                sum1 = sum1.add(getData().get("sum1"));
                sum2 = sum2.add(getData().get("sum2"));
            }

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HY依材质别出货重量", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    indicatorBean.divideByRate(i, 2);
                }
                sb.append(getHtmlTable(indicators, y, m, d, true));
                total = getSumIndicator();
                total.setName("汉扬出货吨数");
                sumIndicatorList.add(total);
                sum1 = sum1.add(getData().get("sum1"));
                sum2 = sum2.add(getData().get("sum2"));
            }

            total = indicatorBean.getSumValue(sumIndicatorList);
            if (total != null) {
                indicatorBean.updatePerformance(total);
                total.setName("HS/HY总计");
                getData().put("sum1", sum1);
                getData().put("sum2", sum2);
                sb.append(getHtmlTableRow(total, y, m, d, "'background-color:#ff8e67';"));
            }

            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getShipementAmountTable() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append(
                    "<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th><th rowspan=\"2\" colspan=\"1\">订单未交</th></tr>");
            sb.append(
                    "<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append(
                    "<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");

            sum1 = BigDecimal.ZERO;
            sum2 = BigDecimal.ZERO;
            sumIndicatorList.clear();

            salesOrder = new SalesOrderAmount();

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("汉声依材质别出货金额", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    indicatorBean.divideByRate(i, 2);
                }
                sb.append(getHtmlTable(indicators, y, m, d, true));
                total = getSumIndicator();
                total.setName("汉声出货吨数");
                sumIndicatorList.add(total);
                sum1 = sum1.add(getData().get("sum1"));
                sum2 = sum2.add(getData().get("sum2"));
            }

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HY依材质别出货金额", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    indicatorBean.divideByRate(i, 2);
                }
                sb.append(getHtmlTable(indicators, y, m, d, true));
                total = getSumIndicator();
                total.setName("汉扬出货吨数");
                sumIndicatorList.add(total);
                sum1 = sum1.add(getData().get("sum1"));
                sum2 = sum2.add(getData().get("sum2"));
            }

            total = indicatorBean.getSumValue(sumIndicatorList);
            if (total != null) {
                indicatorBean.updatePerformance(total);
                total.setName("HS/HY总计");
                getData().put("sum1", sum1);
                getData().put("sum2", sum2);
                sb.append(getHtmlTableRow(total, y, m, d, "'background-color:#ff8e67';"));
            }

            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
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
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d));
            }
            if (needsum) {
                sumIndicator = indicatorBean.getSumValue(indicators);
                if (sumIndicator != null) {
                    indicatorBean.updatePerformance(sumIndicator);
                    sb.append(getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';"));
                }
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

}
