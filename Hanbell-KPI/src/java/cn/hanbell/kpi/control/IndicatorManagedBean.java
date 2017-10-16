/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "indicatorManagedBean")
@ViewScoped
public class IndicatorManagedBean extends IndicatorSetManagedBean {

    /**
     * Creates a new instance of IndicatorManagedBean
     */
    public IndicatorManagedBean() {
    }

    @Override
    public void init() {
        super.init();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        currentEntity = indicatorBean.findById(id);
        detailList = detailEJB.findByPId(id);
        this.doEdit = true;
    }

}
