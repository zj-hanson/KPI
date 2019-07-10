/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.ParentCompanyBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.ParentCompany;
import cn.hanbell.kpi.lazy.ParentCompanyModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "parentCompanyManagedBean")
@SessionScoped
public class ParentCompanyManagedBean extends SuperSingleBean<ParentCompany> {

    @EJB
    protected ParentCompanyBean parentCompanyBean;
    @EJB
    protected IndicatorBean indicatorBean;

    protected Indicator indicator;
    protected String querycusno;
    protected String querycusna;

    public ParentCompanyManagedBean() {
        super(ParentCompany.class);
    }

    @Override
    public void construct() {
        fc = FacesContext.getCurrentInstance();
        ec = fc.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        indicator = indicatorBean.findById(id);
        super.construct();
    }

    @Override
    public void create() {
        newEntity = new ParentCompany();
        super.create();
    }

    @Override
    protected boolean doBeforePersist() throws Exception {
        if (newEntity != null) {
            newEntity.setDeptno(indicator.getDeptno());
            if (parentCompanyBean.queryCusnoIsExist(newEntity.getCusno(), newEntity.getDeptno())) {
                showErrorMsg("Error", "添加失败已存在该客户变更资料");
                return false;
            }
        }
        return super.doBeforePersist();
    }

    @Override
    protected boolean doAfterPersist() throws Exception {
        super.doAfterPersist();
        create();
        return true;
    }

    @Override
    public void init() {
        querycusno = "";
        querycusna = "";
        this.superEJB = parentCompanyBean;
        this.model = new ParentCompanyModel(superEJB);
        this.model.getFilterFields().put("deptno", indicator.getDeptno());
        this.model.getSortFields().put("cusno", "ASC");
        super.init();
    }

    @Override
    public void query() {
        if (model != null) {
            model.getFilterFields().clear();
            if (querycusno != null && !"".equals(querycusno)) {
                model.getFilterFields().put("cusno", querycusno);
            }
            if (querycusna != null && !"".equals(querycusna)) {
                model.getFilterFields().put("cusna", querycusna);
            }
        }
    }

    @Override
    public void update() {
        if (currentEntity != null) {
            currentEntity.setCfmuser(getUserManagedBean().getCurrentUser().getUsername());
            currentEntity.setCfmdateToNow();
        }
        super.update();
    }

    @Override
    public void reset() {
        super.reset();
        querycusno = null;
        querycusna = null;
    }

    /**
     * @return the querycusno
     */
    public String getQuerycusno() {
        return querycusno;
    }

    /**
     * @param querycusno the querycusno to set
     */
    public void setQuerycusno(String querycusno) {
        this.querycusno = querycusno;
    }

    /**
     * @return the querycusna
     */
    public String getQuerycusna() {
        return querycusna;
    }

    /**
     * @param querycusna the querycusna to set
     */
    public void setQuerycusna(String querycusna) {
        this.querycusna = querycusna;
    }

    /**
     * @return the newEntity
     */
    public ParentCompany getParentCompanyEntity() {
        return newEntity;
    }

    /**
     * @param newEntity the newEntity to set
     */
    public void setParentCompanyEntity(ParentCompany newEntity) {
        this.newEntity = newEntity;
    }

}
