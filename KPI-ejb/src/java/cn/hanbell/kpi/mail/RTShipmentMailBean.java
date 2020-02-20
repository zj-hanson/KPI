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
public class RTShipmentMailBean extends ShipmentMail {

    public RTShipmentMailBean() {

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
        return sb.toString();
    }

    @Override
    protected String getMailFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("</div>");
        sb.append("<div class=\"divFoot\">此报表由系统自动发送,请不要直接回复</div>");
        sb.append("<div class=\"divFoot\">报表管理员</div>");
        sb.append("</div></body></html>");
        return sb.toString();
    }

    protected String getQuantityTable() {
        this.indicators.clear();
        Indicator indic = indicatorBean.findByFormidYearAndDeptno("Q-离心机体出货(总)", y, "5C0000");
        this.indicators =  indicatorBean.findByPId(indic.getId());
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "离心机体出货台数设定错误";
        }
    }

    protected String getAmountTable() {
        this.indicators.clear();
        Indicator indic = indicatorBean.findByFormidYearAndDeptno("A-离心机体出货(总)", y, "5C0000");
        this.indicators =  indicatorBean.findByPId(indic.getId());
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "离心机体出货金额设定错误";
        }
    }


}
