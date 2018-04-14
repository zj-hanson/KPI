/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.entity.RoleGrantModule;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "indicatorManagedBean")
@ViewScoped
public class IndicatorManagedBean extends IndicatorSetManagedBean{

    private boolean deny = true;

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
        if (currentEntity != null) {
            for (RoleGrantModule m : userManagedBean.getRoleGrantDeptList()) {
                if (m.getDeptno().equals(currentEntity.getDeptno())) {
                    deny = false;
                }
            }
        }
        detailList = detailEJB.findByPId(id);
        this.doEdit = true;
    }

    /**
     * @return the deny
     */
    public boolean isDeny() {
        return deny;
    }


}
