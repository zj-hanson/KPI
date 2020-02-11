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
public class ComerShipmentMailBean extends ShipmentMail {

    public ComerShipmentMailBean() {

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
        //sb.append("<div class=\"tableTitle\">单位：万元</div>");
        //sb.append(getServiceTable());
        return sb.toString();
    }

    protected String getQuantityTable() {
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("涡轮每日出货台数", y);
        this.indicators.addAll(indicatorBean.findByCategoryAndYear("再生每日出货台数", y));
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "柯茂每日出货台数设定错误";
        }
    }

    protected String getAmountTable() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("涡轮每日出货金额", y);
        this.indicators.addAll(indicatorBean.findByCategoryAndYear("再生每日出货金额", y));
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "柯茂每日出货金额设定错误";
        }
    }

    protected String getServiceTable() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("柯茂收费服务金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = null;
            return getHtmlTable(this.indicators, y, m, d, false);
        } else {
            return "柯茂收费服务金额设定错误";
        }
    }

}
