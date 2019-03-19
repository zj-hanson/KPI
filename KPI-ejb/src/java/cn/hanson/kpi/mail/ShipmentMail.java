/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.evaluation.SalesOrder;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author C1749
 */
public abstract class ShipmentMail extends MailNotification {

    protected Indicator sumIndicator;

    protected SalesOrder salesOrder;
    
    protected DecimalFormat DoublelFormat;

    public ShipmentMail() {
        this.DoublelFormat = new DecimalFormat("##.##");
    }

    @Override
    protected abstract String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception;

    @Override
    protected abstract String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum);

    public Indicator getSumIndicator() {
        return sumIndicator;
    }

    public void setSumIndicator(Indicator sumIndicator) {
        this.sumIndicator = sumIndicator;
    }

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

    @Override
    public void setDecimalFormat(String format) {
        this.DoublelFormat.applyPattern(format);
    }

    public DecimalFormat getDoublelFormat() {
        return DoublelFormat;
    }

    public void setDoublelFormat(DecimalFormat DoublelFormat) {
        this.DoublelFormat = DoublelFormat;
    }

}
