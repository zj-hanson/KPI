/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.entity.Department;
import cn.hanbell.kpi.ejb.ScorecardGrantBean;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.entity.ScorecardDetail;
import cn.hanbell.kpi.entity.ScorecardGrant;
import cn.hanbell.kpi.lazy.ScorecardGrantModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C1749
 */
@ManagedBean(name = "scorecardGrantManagedBean")
@SessionScoped
public class ScorecardGrantManagedBean extends SuperSingleBean<ScorecardGrant> {

    protected String scorecardId;
    protected String scorecardName;
    protected String contentId;
    protected String contentName;
    protected String queryDeptno;
    protected String queryDeptname;
    protected int queryYear;
    @EJB
    private ScorecardGrantBean scorecardGrantBean;
    private List<String> params = null;

    public ScorecardGrantManagedBean() {
        super(ScorecardGrant.class);
    }

    @Override
    public void create() {
        super.create(); //To change body of generated methods, choose Tools | Templates.
        this.newEntity.setCompany(userManagedBean.getCompany());
        this.newEntity.setSeq(queryYear);
    }

    @Override
    public void init() {
        openParams = new HashMap<>();
        superEJB = scorecardGrantBean;
        this.model = new ScorecardGrantModel(scorecardGrantBean, userManagedBean);
        super.init();
    }

    @Override
    public void persist() {
        if (scorecardGrantBean.findByCompanyAndScorecardidAndContentidAndSeq(newEntity.getCompany(), newEntity.getScorecardid(), newEntity.getContentid(), newEntity.getSeq()) == null) {
            super.persist();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "已存在该条权限数据，无法做出新增!"));
        }
    }

    @Override
    public void query() {
        if (model != null) {
            model.getFilterFields().clear();
            if (userManagedBean.getCompany() != null && !"".equals(userManagedBean.getCompany())) {
                model.getFilterFields().put("company", userManagedBean.getCompany());
            }
            if (scorecardName != null && !"".equals(scorecardName)) {
                model.getFilterFields().put("scorecardid", scorecardName);
            }
            if (contentName != null && !"".equals(contentName)) {
                model.getFilterFields().put("contentid", contentName);
            }
            if (queryDeptno != null && !"".equals(queryDeptno)) {
                model.getFilterFields().put("deptno", queryDeptno);
            }
            if (queryDeptname != null && !"".equals(queryDeptname)) {
                model.getFilterFields().put("deptname", queryDeptname);
            }
            if (queryYear > 0) {
                model.getFilterFields().put("seq", queryYear);
            }
            if (queryState != null && !"".equals(queryState)) {
                model.getFilterFields().put("status", queryState);
            }
        }
        super.query();
    }

    @Override
    public void openDialog(String view) {
        switch (view) {
            case "scorecardSelect":
                super.openDialog(view);
                break;
            case "departmentSelect":
                super.openDialog(view);
                break;
            case "scorecardDetailSelect":
                if (newEntity.getScorecardid() == null) {
                    showWarnMsg("Warn", "请先选择考核名称");
                    return;
                }
                openParams.clear();
                if (params == null) {
                    params = new ArrayList<>();
                } else {
                    params.clear();
                }
                params.add(newEntity.getScorecardid().toString());
                openParams.put("pid", params);
                super.openDialog("scorecardDetailSelect", openParams);
                break;
        }
    }

    public void openEditDialog(String view) {
        switch (view) {
            case "scorecardSelect":
                super.openDialog(view);
                break;
            case "departmentSelect":
                super.openDialog(view);
                break;
            case "scorecardDetailSelect":
                if (currentEntity.getScorecardid() == null) {
                    showWarnMsg("Warn", "请先选择考核名称");
                    return;
                }
                openParams.clear();
                if (params == null) {
                    params = new ArrayList<>();
                } else {
                    params.clear();
                }
                params.add(currentEntity.getScorecardid().toString());
                openParams.put("pid", params);
                super.openDialog("scorecardDetailSelect", openParams);
                break;
        }
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

    /**
     * 考核项目开窗
     *
     * @param event
     */
    public void handleDialogReturnScorecardSelectWhenNew(SelectEvent event) {
        if (event.getObject() != null && newEntity != null) {
            Scorecard s = (Scorecard) event.getObject();
            newEntity.setScorecardid(s.getId());
            newEntity.setScorecardname(s.getName());
        }
    }

    /**
     * 考核项目开窗
     *
     * @param event
     */
    public void handleDialogReturnScorecardSelectWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Scorecard s = (Scorecard) event.getObject();
            currentEntity.setScorecardid(s.getId());
            currentEntity.setScorecardname(s.getName());
        }
    }

    /**
     * 考核内容开窗
     *
     * @param event
     */
    public void handleDialogReturnContentNameWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            ScorecardDetail s = (ScorecardDetail) event.getObject();
            currentEntity.setContentid(s.getId());
            currentEntity.setContentname(s.getContent());
        }
    }

    /**
     * 考核内容开窗
     *
     * @param event
     */
    public void handleDialogReturnContentNameWhenNew(SelectEvent event) {
        if (event.getObject() != null && newEntity != null) {
            ScorecardDetail s = (ScorecardDetail) event.getObject();
            newEntity.setContentid(s.getId());
            newEntity.setContentname(s.getContent());
        }
    }

    public String getScorecardId() {
        return scorecardId;
    }

    public void setScorecardId(String scorecardId) {
        this.scorecardId = scorecardId;
    }

    public String getScorecardName() {
        return scorecardName;
    }

    public void setScorecardName(String scorecardName) {
        this.scorecardName = scorecardName;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getQueryDeptno() {
        return queryDeptno;
    }

    public void setQueryDeptno(String queryDeptno) {
        this.queryDeptno = queryDeptno;
    }

    public String getQueryDeptname() {
        return queryDeptname;
    }

    public void setQueryDeptname(String queryDeptname) {
        this.queryDeptname = queryDeptname;
    }

    public int getQueryYear() {
        return queryYear;
    }

    public void setQueryYear(int queryYear) {
        this.queryYear = queryYear;
    }

}
