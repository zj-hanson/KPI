/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.entity.Department;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorDefineBean;
import cn.hanbell.kpi.entity.IndicatorDefine;
import cn.hanbell.kpi.lazy.IndicatorDefineModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "indicatorDefineManagedBean")
@SessionScoped
public class IndicatorDefineManagedBean extends SuperSingleBean<IndicatorDefine> {

    @EJB
    protected IndicatorBean indicatorBean;
    @EJB
    protected IndicatorDefineBean indicatorDefineBean;
    protected String queryDeptno;
    protected String queryDeptname;

    public IndicatorDefineManagedBean() {
        super(IndicatorDefine.class);
    }

    @Override
    public void create() {
        super.create();
        newEntity.setCompany(userManagedBean.getCompany());
    }

    @Override
    protected boolean doBeforePersist() throws Exception {
        if (newEntity != null) {
            if (newEntity.getDeptno() == null || "".equals(newEntity.getDeptno())) {
                showErrorMsg("Error", "请输入考核部门");
                return false;
            }
            if (this.getCurrentPrgGrant() != null && this.getCurrentPrgGrant().getSysprg().getNoauto()) {
                String formid = superEJB.getFormId(this.getDate(), this.getCurrentPrgGrant().getSysprg().getNolead(), this.getCurrentPrgGrant().getSysprg().getNoformat(), this.getCurrentPrgGrant().getSysprg().getNoseqlen());
                newEntity.setFormid(formid);
            }
        }
        return super.doBeforePersist();
    }

    @Override
    protected boolean doBeforeUpdate() throws Exception {
        if (currentEntity != null) {
            if (currentEntity.getDeptno() == null || "".equals(currentEntity.getDeptno())) {
                showErrorMsg("Error", "请输入考核部门");
                return false;
            }
        }
        return super.doBeforeUpdate();
    }

    @Override
    public void handleDialogReturnWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Department d = (Department) event.getObject();
            currentEntity.setDeptno(d.getDeptno());
            currentEntity.setDeptname(d.getDept());
        }
    }

    @Override
    public void handleDialogReturnWhenNew(SelectEvent event) {
        handleDialogReturnWhenEdit(event);
    }

    @Override
    public void init() {
        superEJB = indicatorDefineBean;
        model = new IndicatorDefineModel(indicatorDefineBean);
        super.init();
    }

    @Override
    public void query() {
        if (this.model != null) {
            this.model.getFilterFields().clear();
            if (this.queryName != null && !"".equals(this.queryName)) {
                this.model.getFilterFields().put("name", this.queryName);
            }
            if (this.queryDeptno != null && !"".equals(this.queryDeptno)) {
                this.model.getFilterFields().put("deptno", this.queryDeptno);
            }
            if (this.queryDeptname != null && !"".equals(this.queryDeptname)) {
                this.model.getFilterFields().put("deptname", this.queryDeptname);
            }
            if (queryState != null && !"ALL".equals(queryState)) {
                this.model.getFilterFields().put("status", queryState);
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.queryName = null;
        this.queryDeptno = null;
        this.queryDeptname = null;
    }

    /**
     * @return the queryDeptno
     */
    public String getQueryDeptno() {
        return queryDeptno;
    }

    /**
     * @param queryDeptno the queryDeptno to set
     */
    public void setQueryDeptno(String queryDeptno) {
        this.queryDeptno = queryDeptno;
    }

    /**
     * @return the queryDeptname
     */
    public String getQueryDeptname() {
        return queryDeptname;
    }

    /**
     * @param queryDeptname the queryDeptname to set
     */
    public void setQueryDeptname(String queryDeptname) {
        this.queryDeptname = queryDeptname;
    }

}
