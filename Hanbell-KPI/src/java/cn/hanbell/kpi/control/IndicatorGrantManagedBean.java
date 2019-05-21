/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.ejb.IndicatorgrantBean;
import cn.hanbell.kpi.entity.Indicatorgrant;
import cn.hanbell.kpi.lazy.IndicatorGrantModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "indicatorGrantManagedBean")
@SessionScoped
public class IndicatorGrantManagedBean extends SuperSingleBean<Indicatorgrant> {

    @EJB
    private IndicatorgrantBean indicatorgrantBean;

    protected String queryUserid;

    public IndicatorGrantManagedBean() {
        super(Indicatorgrant.class);
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void persist() {
        if (indicatorgrantBean.findByUseridAndFormid(newEntity.getUserid(), newEntity.getFormid()) == null) {
            super.persist();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "已存在该条权限数据，无法做出新增!"));
        }

    }

    @Override
    public void update() {
        if (indicatorgrantBean.findByUseridAndFormidNotId(currentEntity.getUserid(), currentEntity.getFormid(), currentEntity.getId()) == null) {
            super.update();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "权限数据已重复，无法做出修改!"));
        }
    }

    public void handleDialogReturnUserWhenDetailNew(SelectEvent event) {
        if (event.getObject() != null && newEntity != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            newEntity.setUserid(user.getUserid());
            newEntity.setUsername(user.getUsername());
        }
    }

    public void handleDialogReturnUserWhenDetailEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            currentEntity.setUserid(user.getUserid());
            currentEntity.setUsername(user.getUsername());
        }
    }

    @Override
    public void init() {
        this.superEJB = indicatorgrantBean;
        this.model = new IndicatorGrantModel(indicatorgrantBean);
        super.init();
    }

    @Override
    public void query() {
        if (model != null) {
            model.getFilterFields().clear();
            if (queryUserid != null && !"".equals(queryUserid)) {
                model.getFilterFields().put("userid", queryUserid);
            }
            if (queryName != null && !"".equals(queryName)) {
                model.getFilterFields().put("username", queryName);
            }
        }
    }

    /**
     * @return the queryUserid
     */
    public String getQueryUserid() {
        return queryUserid;
    }

    /**
     * @param queryUserid the queryUserid to set
     */
    public void setQueryUserid(String queryUserid) {
        this.queryUserid = queryUserid;
    }

}
