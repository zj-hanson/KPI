/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package cn.hanson.kpi.rpt;

import cn.hanbell.kpi.web.BscChartManagedBean;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "scrapChartReportBean")
@ViewScoped
public class ScrapChartReportBean extends BscChartManagedBean {

    /**
     * Creates a new instance of ShipmentReportBean
     */
    public ScrapChartReportBean() {

    }

    @Override
    public void init() {
        super.init();
        decimalFormat.applyPattern("###,##0.00");
    }

}
