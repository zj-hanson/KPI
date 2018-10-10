/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.ShipmentMail;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.evaluation.SalesOrderAmount;
import cn.hanbell.kpi.evaluation.SalesOrderQuantity;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class SHBShipmentMailBean extends ShipmentMail {

    public SHBShipmentMailBean() {

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
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getVHBQuantityTable());
        sb.append("<div class=\"tableTitle\">单位：百万越南盾</div>");
        sb.append(getVHBAmountTable());
        return sb.toString();
    }

    protected String getQuantityTable() {
        StringBuilder sb = new StringBuilder();
        Indicator total;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th><th rowspan=\"2\" colspan=\"1\">订单未交</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");

            salesOrder = new SalesOrderQuantity();

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("R冷媒出货台数", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("R机体出货台数");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("A机体每日出货台数", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("A机体出货台数");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("A机组每日出货台数", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("A机组出货台数");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("SDS无油每日出货台数", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("SDS无油出货台数");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("P每日出货台数", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("P真空出货台数");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("柯茂每日出货台数", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("柯茂出货台数");
            sb.append(getHtmlTableRow(total, y, m, d));

            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getAmountTable() {
        StringBuilder sb = new StringBuilder();
        Indicator total;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th><th rowspan=\"2\" colspan=\"1\">订单未交</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");

            salesOrder = new SalesOrderAmount();

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("R冷媒出货金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("R机体出货金额");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("A机体每日出货金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("A机体出货金额");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("A机组每日出货金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("A机组出货金额");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("SDS无油每日出货金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("SDS无油出货金额");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("P每日出货金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("P真空出货金额");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("柯茂每日出货金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("柯茂出货金额");
            sb.append(getHtmlTableRow(total, y, m, d));

            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getServiceTable() {
        StringBuilder sb = new StringBuilder();
        Indicator total;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th><th rowspan=\"2\" colspan=\"1\">订单未交</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");

            salesOrder = null;

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("R收费服务金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("R机体收费服务");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("A机体收费服务", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("A机体收费服务");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("A机组收费服务金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("A机组收费服务");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("SDS无油收费服务金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("SDS无油收费服务");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("P收费服务金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("P真空收费服务");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("柯茂收费服务金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("柯茂收费服务金额");
            sb.append(getHtmlTableRow(total, y, m, d));

            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getVHBQuantityTable() {
        StringBuilder sb = new StringBuilder();
        Indicator total;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\">公司别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th><th rowspan=\"2\" colspan=\"1\">订单未交</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");

            salesOrder = new SalesOrderQuantity();

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("越南出货台数", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("越南出货台数");
            sb.append(getHtmlTableRow(total, y, m, d));

            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getVHBAmountTable() {
        StringBuilder sb = new StringBuilder();
        Indicator total;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\">公司别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th><th rowspan=\"2\" colspan=\"1\">订单未交</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");

            salesOrder = new SalesOrderAmount();

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("越南出货金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("越南出货金额");
            sb.append(getHtmlTableRow(total, y, m, d));

            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

}
