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
public class RTSalesOrderMailBean extends SalesOrderMail {

    private List<Indicator> rtlist;

    public RTSalesOrderMailBean() {
    }

    @Override
    public void init() {
        rtlist = new ArrayList<>();
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
        return sb.toString();
    }

    protected String getQuantityTable() {
        this.rtlist.clear();
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("R冷媒订单台数", y);
        indicatorBean.getEntityManager().clear();
        for (Indicator indicator : indicators) {
            if (indicator.getName().contains("离心")) {
                rtlist.add(indicator);
            }
        }
        if (rtlist != null && !rtlist.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.rtlist, y, m, d, true);
        } else {
            return "离心机体订单台数设定错误";
        }
    }

    protected String getAmountTable() {
        this.rtlist.clear();
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("R冷媒订单金额", y);
        indicatorBean.getEntityManager().clear();
        for (Indicator indicator : indicators) {
            if (indicator.getName().contains("离心")) {
                rtlist.add(indicator);
            }
        }
        if (rtlist != null && !rtlist.isEmpty()) {
            for (Indicator i : rtlist) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.rtlist, y, m, d, true);
        } else {
            return "离心机体订单金额设定错误";
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
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d));
            }
            if (needsum) {
                sumIndicator = indicatorBean.getSumValue(rtlist);
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
