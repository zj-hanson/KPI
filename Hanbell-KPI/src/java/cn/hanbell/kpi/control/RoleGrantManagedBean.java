/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.entity.Department;
import cn.hanbell.kpi.ejb.RoleGrantModuleBean;
import cn.hanbell.kpi.ejb.RoleBean;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.entity.Role;
import cn.hanbell.kpi.lazy.SystemRoleModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import com.lightshell.comm.SuperDetailEntity;
import com.lightshell.comm.SuperEJB;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author kevindong
 */
@ManagedBean(name = "roleGrantManagedBean")
@SessionScoped
public class RoleGrantManagedBean extends SuperSingleBean<Role> {

    @EJB
    private RoleGrantModuleBean detailEJB;

    @EJB
    private RoleBean systemRoleBean;

    private RoleGrantModule newDetail;
    private RoleGrantModule currentDetail;

    private List<RoleGrantModule> detailList;
    private List<RoleGrantModule> addedDetailList;
    private List<RoleGrantModule> editedDetailList;
    private List<RoleGrantModule> deletedDetailList;

    private HashMap<SuperEJB, List<?>> detailAdded;
    private HashMap<SuperEJB, List<?>> detailEdited;
    private HashMap<SuperEJB, List<?>> detailDeleted;

    public RoleGrantManagedBean() {
        super(Role.class);
    }

    @Override
    public void construct() {
        this.addedDetailList = new ArrayList<>();
        this.editedDetailList = new ArrayList<>();
        this.deletedDetailList = new ArrayList<>();
        this.detailAdded = new HashMap<>();
        this.detailEdited = new HashMap<>();
        this.detailDeleted = new HashMap<>();
        super.construct();
    }

    @Override
    public void destory() {
        if (this.addedDetailList != null) {
            this.addedDetailList.clear();
        }
        if (this.editedDetailList != null) {
            this.editedDetailList.clear();
        }
        if (this.deletedDetailList != null) {
            this.deletedDetailList.clear();
        }
        super.destory();
    }

    public void createDetail() {
        if (getNewDetail() == null) {
            try {
                setNewDetail(RoleGrantModule.class.newInstance());
                newDetail.setKind("R");
                newDetail.setSystemRole(currentEntity);
                newDetail.setStatus("N");
                newDetail.setCreator(this.userManagedBean.getCurrentUser().getUsername());
                newDetail.setCredateToNow();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(RoleGrantModule.class.getName()).log(Level.SEVERE, null, ex);
                showErrorMsg("Error", ex.getMessage());
            }
        }
        setCurrentDetail(getNewDetail());
    }

    public void deleteDetail() {
        if (currentDetail != null) {
            try {
                deleteDetail(currentDetail);
                setCurrentDetail(null);
            } catch (Exception e) {
                showErrorMsg("Error", e.getMessage());
            }
        } else {
            showWarnMsg("Warn", "没有可删除数据");
        }
    }

    public void deleteDetail(RoleGrantModule entity) {
        if (entity != null) {
            try {
                if (this.addedDetailList.contains(entity)) {
                    this.addedDetailList.remove(entity);
                } else {
                    if (this.editedDetailList.contains(entity)) {
                        this.editedDetailList.remove(entity);
                    }
                    if (!this.deletedDetailList.contains(entity)) {
                        this.deletedDetailList.add(entity);
                    }
                }
                if (this.detailList.contains(entity)) {
                    getDetailList().remove(entity);
                }
                showInfoMsg("Info", "删除成功");
            } catch (Exception e) {
                showErrorMsg("Error", e.getMessage());
            }
        } else {
            showWarnMsg("Warn", "没有可删除数据");
        }
    }

    @Override
    protected boolean doAfterUpdate() throws Exception {
        addedDetailList.clear();
        editedDetailList.clear();
        deletedDetailList.clear();
        setNewDetail(null);
        setCurrentDetail(null);
        return super.doAfterUpdate();
    }

    public void doConfirmDetail() {
        if (this.getNewDetail() != null && this.getNewDetail().equals(this.currentDetail)) {
            if (!this.addedDetailList.contains(this.newDetail)) {
                boolean flag = true;
                //检查是否存在相同来源信息
                for (RoleGrantModule detail : this.getDetailList()) {
                    if (detail.equals(this.getNewDetail())) {
                        flag = false;
                    }
                }
                if (flag) {
                    this.addedDetailList.add(this.getNewDetail());
                    this.getDetailList().add(this.getNewDetail());
                    setNewDetail(null);
                    setCurrentDetail(null);
                }
            }
        } else if (this.currentDetail != null && !this.addedDetailList.contains(this.currentDetail)) {
            this.editedDetailList.add(this.currentDetail);
            setCurrentDetail(null);
        }
    }

    protected int getMaxSeq(List<? extends SuperDetailEntity> list) {
        if (list == null || list.isEmpty()) {
            return 1;
        }
        int seq = 0;
        for (SuperDetailEntity entity : list) {
            if (entity.getSeq() > seq) {
                seq = entity.getSeq();
            }
        }
        boolean b = true;
        boolean ret;
        for (int i = 1; i <= seq; i++) {
            ret = true;
            for (SuperDetailEntity entity : list) {
                if (entity.getSeq() == i) {
                    ret = ret && false;
                    break;
                }
            }
            if (ret) {
                return i;
            }
        }
        if (b) {
            return seq + 1;
        } else {
            return 0;
        }
    }

    @Override
    public void handleDialogReturnWhenEdit(SelectEvent event) {
        if (currentDetail != null && event.getObject() != null) {
            Department d = (Department) event.getObject();
            currentDetail.setDeptno(d.getDeptno());
            currentDetail.setDept(d.getDept());
        }
    }

    @Override
    public void init() {
        detailAdded.put(detailEJB, this.addedDetailList);
        detailEdited.put(detailEJB, this.editedDetailList);
        detailDeleted.put(detailEJB, this.deletedDetailList);
        superEJB = systemRoleBean;
        setModel(new SystemRoleModel(systemRoleBean));
        model.getSortFields().put("roleno", "ASC");
        super.init();
    }

    @Override
    public void query() {
        if (model != null) {
            model.getFilterFields().clear();
            if (queryFormId != null && !"".equals(queryFormId)) {
                model.getFilterFields().put("roleno", queryFormId);
            }
            if (queryName != null && !"".equals(queryName)) {
                model.getFilterFields().put("rolename", queryName);
            }
        }
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

    @Override
    public void update() {
        if (currentEntity != null) {
            try {
                if (doBeforeUpdate()) {
                    superEJB.update(currentEntity, detailAdded, detailEdited, detailDeleted);
                    doAfterUpdate();
                    showInfoMsg("Info", "更新成功");
                } else {
                    showErrorMsg("Error", "更新前检核失败");
                }
            } catch (Exception e) {
                showErrorMsg("Error", e.toString());
            }
        } else {
            showWarnMsg("Warn", "没有可更新数据");
        }
    }

    /**
     * @return the currentDetail
     */
    public RoleGrantModule getCurrentDetail() {
        return currentDetail;
    }

    /**
     * @param currentDetail the currentDetail to set
     */
    public void setCurrentDetail(RoleGrantModule currentDetail) {
        this.currentDetail = currentDetail;
    }

    /**
     * @return the detailList
     */
    public List<RoleGrantModule> getDetailList() {
        return detailList;
    }

    /**
     * @param detailList the detailList to set
     */
    public void setDetailList(List<RoleGrantModule> detailList) {
        this.detailList = detailList;
    }

    /**
     * @return the newDetail
     */
    public RoleGrantModule getNewDetail() {
        return newDetail;
    }

    /**
     * @param newDetail the newDetail to set
     */
    public void setNewDetail(RoleGrantModule newDetail) {
        this.newDetail = newDetail;
    }

    @Override
    public void setCurrentEntity(Role currentEntity) {
        super.setCurrentEntity(currentEntity);
        if (detailList != null) {
            detailList.clear();
        }
        if (currentEntity != null && currentEntity.getId() != null) {
            setDetailList(detailEJB.findByRoleId(currentEntity.getId()));
        }
        if (this.detailList == null) {
            this.detailList = new ArrayList<>();
        }
        addedDetailList.clear();
        editedDetailList.clear();
        deletedDetailList.clear();
    }

}
