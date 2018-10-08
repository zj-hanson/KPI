/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.RoleGrantModule;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "productManagedBean")
@ViewScoped
public class ProductManagedBean extends IndicatorSetManagedBean {

    private IndicatorDetail otherIndicator;
    private String otherLabel;
    private boolean deny = true;

    /**
     * Creates a new instance of IndicatorManagedBean
     */
    public ProductManagedBean() {
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
        summaryList = indicatorSummaryBean.findByPId(id);
        analysisList = indicatorAnalysisBean.findByPId(id);
        this.doEdit = true;
    }

    public void setOtherIndicator(String label, IndicatorDetail otherIndicator) {
        this.otherLabel = label;
        this.otherIndicator = otherIndicator;
    }

    public void updateOtherIndicator() {
        if (otherIndicator != null) {
            try {
                indicatorDetailBean.update(otherIndicator);
                showInfoMsg("Info", "更新其他数据成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.toString());
            }
        }
    }

    /**
     * @return the otherIndicator
     */
    public IndicatorDetail getOtherIndicator() {
        return otherIndicator;
    }

    /**
     * @param otherIndicator the otherIndicator to set
     */
    public void setOtherIndicator(IndicatorDetail otherIndicator) {
        this.otherIndicator = otherIndicator;
    }

    /**
     * @return the otherLabel
     */
    public String getOtherLabel() {
        return otherLabel;
    }

    /**
     * @param otherLabel the otherLabel to set
     */
    public void setOtherLabel(String otherLabel) {
        this.otherLabel = otherLabel;
    }

    /**
     * @return the deny
     */
    public boolean isDeny() {
        return deny;
    }

}
