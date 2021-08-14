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
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class WXShipmentMailBean extends ShipmentMail {

    private List<Indicator> indicatorList;

    public WXShipmentMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    public String getMailBody() throws Exception {
        indicatorList = indicatorBean.findByDeptnoObjtypeAndYear("1T100", "P", y);
        indicatorBean.getEntityManager().clear();
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getQuantityTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAmountTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getServiceTable());
        return sb.toString();
    }

    protected String getQuantityTable() throws Exception {
        indicators.clear();
        for (Indicator i : indicatorList) {
            if (i.isAssigned()) {
                continue;
            }
            if (i.getFormid().startsWith("Q") && !i.getFormid().contains("订单")) {
                indicators.add(i);
            }
        }
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "外销每日出货台数设定错误";
        }
    }

    protected String getAmountTable() throws Exception {
        indicators.clear();
        for (Indicator i : indicatorList) {
            if (i.isAssigned()) {
                continue;
            }
            if (i.getFormid().startsWith("A") && !i.getFormid().contains("订单")) {
                indicators.add(i);
            }
        }
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "外销每日出货金额设定错误";
        }
    }

    protected String getServiceTable() throws Exception {
        indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("外销零件", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = null;
            return getHtmlTable(indicators, y, m, d, false);
        } else {
            return "外销零件金额设定错误";
        }
    }

}
