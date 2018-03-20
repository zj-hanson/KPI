/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.entity.Department;
import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.entity.IndicatorChart;
import cn.hanbell.kpi.lazy.IndicatorChartModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "indicatorChartManagedBean")
@SessionScoped
public class IndicatorChartManagedBean extends SuperSingleBean<IndicatorChart> {

    @EJB
    private IndicatorChartBean indicatorChartBean;

    public IndicatorChartManagedBean() {
        super(IndicatorChart.class);
    }

    @Override
    public void create() {
        super.create();
        newEntity.setCompany(userManagedBean.getCompany());
    }

    @Override
    public void handleDialogReturnWhenNew(SelectEvent event) {
        if (event.getObject() != null && newEntity != null) {
            Department e = (Department) event.getObject();
            newEntity.setDeptno(e.getDeptno());
            newEntity.setDeptname(e.getDept());
        }
    }

    @Override
    public void handleDialogReturnWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Department e = (Department) event.getObject();
            currentEntity.setDeptno(e.getDeptno());
            currentEntity.setDeptname(e.getDept());
        }
    }

    @Override
    public void init() {
        this.superEJB = indicatorChartBean;
        this.model = new IndicatorChartModel(indicatorChartBean, userManagedBean);
        super.init();
    }

}
