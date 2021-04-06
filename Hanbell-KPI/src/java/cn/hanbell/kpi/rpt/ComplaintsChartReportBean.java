/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.web.BscChartManagedBean;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "complaintsChartReportBean")
@ViewScoped
public class ComplaintsChartReportBean extends BscChartManagedBean {

    List<Indicator> indicatorList;
    
    public ComplaintsChartReportBean() {
    }

    @Override
    public void init() {
        super.init();
        indicatorList = indicatorBean.findByCategoryAndYear("客诉", y);
    }
    public List<Indicator> getIndicatorList() {
        return indicatorList;
    }

    public void setIndicatorList(List<Indicator> indicatorList) {
        this.indicatorList = indicatorList;
    }
}
