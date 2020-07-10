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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class RSalesOrderMailBean extends SalesOrderMail {

    public RSalesOrderMailBean() {
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
        sb.append(getQuantityTable_DC());
        sb.append("<br/>");
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAmountTable());
        sb.append(getAmountTable_DC());
        return sb.toString();
    }

    protected String getQuantityTable() {
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("R冷媒订单台数", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "R冷媒订单台数设定错误";
        }
    }

    protected String getQuantityTable_DC() {
        List<Indicator> indicatorsR = new ArrayList<>();
        List<Indicator> indicatorsL = new ArrayList<>();
        List<Indicator> indicatorsH = new ArrayList<>();
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("R冷媒订单台数", y);
        for (Indicator i : indicators) {
            switch (i.getProduct().trim()) {
                case "空调":
                    indicatorsR.add(i);
                    break;
                case "热泵":
                    indicatorsH.add(i);
                    break;
                case "冷冻":
                    indicatorsL.add(i);
                    break;
                default:
            }
        }
        indicators.clear();
        sumIndicator = indicatorBean.getSumValue(indicatorsR);
        indicatorBean.updatePerformance(sumIndicator);
        sumIndicator.setName("空调订单台数合计");
        indicators.add(sumIndicator);
        sumIndicator = indicatorBean.getSumValue(indicatorsH);
        indicatorBean.updatePerformance(sumIndicator);
        sumIndicator.setName("热泵订单台数合计");
        indicators.add(sumIndicator);
        sumIndicator = indicatorBean.getSumValue(indicatorsL);
        indicatorBean.updatePerformance(sumIndicator);
        sumIndicator.setName("冷冻订单台数合计");
        indicators.add(sumIndicator);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "R冷媒订单台数设定错误";
        }
    }

    protected String getAmountTable() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("R冷媒订单金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "R冷媒订单金额设定错误";
        }
    }

    protected String getAmountTable_DC() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("R冷媒订单金额", y);
        List<Indicator> indicatorsR = new ArrayList<>();
        List<Indicator> indicatorsL = new ArrayList<>();
        List<Indicator> indicatorsH = new ArrayList<>();
        for (Indicator i : indicators) {
            switch (i.getProduct().trim()) {
                case "空调":
                    indicatorsR.add(i);
                    break;
                case "热泵":
                    indicatorsH.add(i);
                    break;
                case "冷冻":
                    indicatorsL.add(i);
                    break;
                default:
            }
        }
        indicators.clear();
        sumIndicator = indicatorBean.getSumValue(indicatorsR);
        indicatorBean.updatePerformance(sumIndicator);
        sumIndicator.setName("空调订单金额合计");
        indicators.add(sumIndicator);
        sumIndicator = indicatorBean.getSumValue(indicatorsH);
        indicatorBean.updatePerformance(sumIndicator);
        sumIndicator.setName("热泵订单金额合计");
        indicators.add(sumIndicator);
        sumIndicator = indicatorBean.getSumValue(indicatorsL);
        indicatorBean.updatePerformance(sumIndicator);
        sumIndicator.setName("冷冻订单金额合计");
        indicators.add(sumIndicator);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "R冷媒订单金额设定错误";
        }
    }
}
