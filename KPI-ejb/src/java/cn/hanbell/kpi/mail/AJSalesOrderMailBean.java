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
public class AJSalesOrderMailBean extends SalesOrderMail {

    public AJSalesOrderMailBean() {
    }

    protected List<Indicator> indicatorList = new ArrayList<>();

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    public String getMailBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getQuantityTableDomesticSale());
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getQuantityTable_SAM());
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getQuantityTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAmountTableDomesticSale());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAmountTable_SAM());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAmountTable());
        return sb.toString();
    }

    protected String getQuantityTableDomesticSale() {
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("A机体订单台数-内销", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体订单台数-内销设定错误";
        }
    }

    //无油涡旋
    protected String getQuantityTable_SAM() {
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("A机体每日订单台数-涡旋内销", y);
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
        this.indicatorList.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("A机体订单台数", y);
        indicatorList = indicatorBean.findByCategoryAndYear("A机体每日订单台数-涡旋", y);
        if (!indicatorList.isEmpty()) {
            indicators.addAll(indicatorList);
        }
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体订单台数设定错误";
        }
    }

    protected String getAmountTableDomesticSale() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体订单金额-内销", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体订单金额-内销设定错误";
        }
    }

    //无油涡旋
    protected String getAmountTable_SAM() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体每日订单金额-涡旋内销", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体每日出货金额设定错误";
        }
    }

    protected String getAmountTable() {
        this.indicators.clear();
        this.indicatorList.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体订单金额", y);
        indicatorList = indicatorBean.findByCategoryAndYear("A机体每日订单金额-涡旋", y);
        if (!indicatorList.isEmpty()) {
            indicators.addAll(indicatorList);
        }
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体订单金额设定错误";
        }
    }

}
