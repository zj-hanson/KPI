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
public class AJShipmentMailBean extends ShipmentMail {

    public AJShipmentMailBean() {

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
        sb.append(getQuantityTable_NX());
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getQuantityTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAmountTable_NX());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAmountTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getServiceTable());
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getTransferTable());
        return sb.toString();
    }

    protected String getQuantityTable_NX() {
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("A机体每日出货台数", y);
        this.indicators.removeIf((Indicator indicator)->{
            return "Q-A机体外销".equals(indicator.getFormid());
        });
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体每日出货台数设定错误";
        }
    }

    protected String getQuantityTable() {
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("A机体每日出货台数", y);
        indicators.removeIf((Indicator indicator) -> {
            return !"Q-A机体外销".equals(indicator.getFormid());
        }
        );
        this.indicators.addAll(indicatorBean.findByCategoryAndYear("A机体每日出货台数-内销", y));
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体每日出货台数-外销设定错误";
        }
    }

    protected String getAmountTable_NX() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体每日出货金额", y);
        this.indicators.removeIf((Indicator indicator)->{
            return "A-A机体外销".equals(indicator.getFormid());
        });
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体每日出货金额-内销设定错误";
        }
    }

    protected String getAmountTable() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体每日出货金额", y);
        indicators.removeIf((Indicator indicator) -> {
            return !"A-A机体外销".equals(indicator.getFormid());
        });
        indicators.addAll(indicatorBean.findByCategoryAndYear("A机体每日出货金额-内销", y));
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体每日出货金额-外销设定错误";
        }
    }

    protected String getServiceTable() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体收费服务", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = null;
            setDecimalFormat("#,###.00");
            String t = getHtmlTable(this.indicators, y, m, d, false);
            setDecimalFormat("#,###");
            return t;
        } else {
            return "A机体每日出货金额设定错误";
        }
    }

    protected String getTransferTable() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体厂内调拨", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            return getHtmlTable(this.indicators, y, m, d, false);
        } else {
            return "A机体每日出货厂内调拨设定错误";
        }
    }

}
