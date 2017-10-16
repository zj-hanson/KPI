/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.ejb.RoleBean;
import cn.hanbell.kpi.ejb.RoleDetailBean;
import cn.hanbell.kpi.entity.Role;
import cn.hanbell.kpi.entity.RoleDetail;
import cn.hanbell.kpi.lazy.SystemRoleModel;
import cn.hanbell.kpi.web.SuperMultiBean;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author kevindong
 */
@ManagedBean(name = "roleManagedBean")
@SessionScoped
public class RoleManagedBean extends SuperMultiBean<Role, RoleDetail> {

    @EJB
    private RoleBean systemRoleBean;

    @EJB
    private RoleDetailBean systemRoleDetailBean;

    public RoleManagedBean() {
        super(Role.class, RoleDetail.class);
    }

    @Override
    public void handleDialogReturnWhenDetailEdit(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            SystemUser u = (SystemUser) event.getObject();
            currentDetail.setUserid(u.getUserid());
            currentDetail.setUsername(u.getUsername());
        }
    }

    @Override
    public void init() {
        this.superEJB = systemRoleBean;
        this.detailEJB = systemRoleDetailBean;
        setModel(new SystemRoleModel(systemRoleBean));
        super.init();
    }

    @Override
    protected void setToolBar() {
        if (currentEntity != null && currentEntity.getStatus() != null) {
            switch (currentEntity.getStatus()) {
                case "V":
                    this.doEdit = false;
                    this.doDel = false;
                    this.doCfm = false;
                    this.doUnCfm = true;
                    break;
                default:
                    this.doEdit = true;
                    this.doDel = true;
                    this.doCfm = true;
                    this.doUnCfm = false;
            }
        } else {
            this.doEdit = false;
            this.doDel = false;
            this.doCfm = false;
            this.doUnCfm = false;
        }
    }

}
