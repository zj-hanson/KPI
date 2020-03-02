/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.SalesOrderMail;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.evaluation.SalesOrderAmount;
import cn.hanbell.kpi.evaluation.SalesOrderQuantity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class ComerSalesOrderMailBean extends SalesOrderMail {

    protected List<Indicator> sumList;
    protected BigDecimal sum1 = BigDecimal.ZERO;
    protected BigDecimal sum2 = BigDecimal.ZERO;

    public ComerSalesOrderMailBean() {
    }

    @Override
    public void init() {
        sumList = new ArrayList<>();
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    public String getMailBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getQuantityTable());
        sb.append("<div class=\"tableTitle\"  style=\"margin-top: 30px\" >单位：万元</div>");
        sb.append(getAmountTable());
        return sb.toString();
    }

    protected String getQuantityTable() {
        StringBuilder sb = new StringBuilder();
        String deptno = "5C000";
        Indicator indicator;
        Indicator total;
        try {
            //涡轮产品部分
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\" width=\"15%\" >产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");

            sum1 = BigDecimal.ZERO;
            sum2 = BigDecimal.ZERO;
            sumList.clear();
            salesOrder = new SalesOrderQuantity();

            indicators.clear();
            indicator = indicatorBean.findByFormidYearAndDeptno("Q-柯茂离心机订单", y, deptno);
            indicators.add(indicator);
            indicator = indicatorBean.findByFormidYearAndDeptno("Q-磁悬浮机体订单", y, deptno);
            indicators.add(indicator);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("离心机体内销订单台数");
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));

            indicators.clear();
            indicator = indicatorBean.findByFormidYearAndDeptno("Q-离心机组内销订单", y, deptno);
            indicators.add(indicator);
            indicator = indicatorBean.findByFormidYearAndDeptno("Q-磁悬浮机组内销订单", y, deptno);
            indicators.add(indicator);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("离心机组内销订单台数");
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));

            indicators.clear();
            indicator = indicatorBean.findByFormidYearAndDeptno("Q-离心机组外销订单", y, deptno);
            indicators.add(indicator);
            indicator = indicatorBean.findByFormidYearAndDeptno("Q-磁悬浮机组外销订单", y, deptno);
            indicators.add(indicator);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("离心机组外销订单台数");
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));

            indicators.clear();
            indicator = indicatorBean.findByFormidYearAndDeptno("Q-螺杆机组外销订单", y, deptno);
            indicatorBean.getEntityManager().clear();
            total = indicator;
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));

            total = indicatorBean.getSumValue(sumList);
            if (total != null) {
                indicatorBean.updatePerformance(total);
                total.setName("涡轮合计");
                getData().put("sum1", sum1);
                getData().put("sum2", sum2);
                sb.append(getHtmlTableRow(total, y, m, d));
            }
            sb.append("</table></div>");

            //再生能源部分
            this.indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("再生订单台数", y);
            indicatorBean.getEntityManager().clear();
            sb.append(getHtmlTable(indicators, y, m, d, true, "再生能源合计"));

        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();

    }

    protected String getAmountTable() {
        StringBuilder sb = new StringBuilder();
        String deptno = "5C000";
        Indicator indicator;
        Indicator total;
        try {
            //涡轮产品部分
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\" width=\"15%\" >产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");

            sum1 = BigDecimal.ZERO;
            sum2 = BigDecimal.ZERO;
            sumList.clear();
            salesOrder = new SalesOrderAmount();

            indicators.clear();
            indicator = indicatorBean.findByFormidYearAndDeptno("A-柯茂离心机订单", y, deptno);
            indicators.add(indicator);
            indicator = indicatorBean.findByFormidYearAndDeptno("A-磁悬浮机体订单", y, deptno);
            indicators.add(indicator);
            indicatorBean.getEntityManager().clear();
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("离心机体内销订单金额");
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));

            indicators.clear();
            indicator = indicatorBean.findByFormidYearAndDeptno("A-离心机组内销订单", y, deptno);
            indicators.add(indicator);
            indicator = indicatorBean.findByFormidYearAndDeptno("A-磁悬浮机组内销订单", y, deptno);
            indicators.add(indicator);
            indicatorBean.getEntityManager().clear();
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("离心机组内销订单金额");
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));

            indicators.clear();
            indicator = indicatorBean.findByFormidYearAndDeptno("A-离心机组外销订单", y, deptno);
            indicators.add(indicator);
            indicator = indicatorBean.findByFormidYearAndDeptno("A-磁悬浮机组外销订单", y, deptno);
            indicators.add(indicator);
            indicatorBean.getEntityManager().clear();
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("离心机组外销订单金额");
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));

            indicators.clear();
            indicator = indicatorBean.findByFormidYearAndDeptno("A-螺杆机组外销订单", y, deptno);
            indicatorBean.getEntityManager().clear();
            indicatorBean.divideByRate(indicator, 2);
            total = indicator;
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));

            total = indicatorBean.getSumValue(sumList);
            if (total != null) {
                indicatorBean.updatePerformance(total);
                total.setName("涡轮合计");
                getData().put("sum1", sum1);
                getData().put("sum2", sum2);
                sb.append(getHtmlTableRow(total, y, m, d));
            }
            
            sb.append("</table></div>");

            //再生能源部分
            this.indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("再生订单金额", y);
            indicatorBean.getEntityManager().clear();
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true, "再生能源合计"));

        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum, String name) {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        getData().put("sum2", BigDecimal.ZERO);
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<div class=\"tbl\" ><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\" width=\"15%\" >产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d));
            }
            if (needsum) {
                sumIndicator = indicatorBean.getSumValue(indicators);
                sumIndicator.setName(name);
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

}
