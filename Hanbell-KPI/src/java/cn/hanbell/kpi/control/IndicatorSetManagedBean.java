/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.entity.Department;
import cn.hanbell.kpi.ejb.IndicatorAssignmentBean;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorDepartmentBean;
import cn.hanbell.kpi.ejb.IndicatorDetailBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorAssignment;
import cn.hanbell.kpi.entity.IndicatorDepartment;
import cn.hanbell.kpi.lazy.IndicatorModel;
import cn.hanbell.kpi.web.SuperMulti2Bean;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "indicatorSetManagedBean")
@SessionScoped
public class IndicatorSetManagedBean extends SuperMulti2Bean<Indicator, IndicatorDepartment, IndicatorAssignment> {

    @EJB
    protected IndicatorBean indicatorBean;
    @EJB
    protected IndicatorDepartmentBean indicatorDepartmentBean;
    @EJB
    protected IndicatorAssignmentBean indicatorAssignmentBean;
    @EJB
    protected IndicatorDetailBean indicatorDetailBean;

    protected String queryDeptno;
    protected String queryDeptname;
    protected int queryYear;

    public IndicatorSetManagedBean() {
        super(Indicator.class, IndicatorDepartment.class, IndicatorAssignment.class);
    }

    @Override
    public void createDetail2() {
        super.createDetail2();
        newDetail2.setCompany(currentEntity.getCompany());
        newDetail2.setFormid(currentEntity.getFormid());
        newDetail2.setFormtype(currentEntity.getFormtype());
        newDetail2.setFormkind(currentEntity.getFormkind());
        newDetail2.setName(currentEntity.getName());
        newDetail2.setDescript(currentEntity.getDescript());
        newDetail2.setPid(currentEntity.getId());
        newDetail2.setSeq(currentEntity.getSeq());
        newDetail2.setObjtype("D");
        newDetail2.setSortid(currentEntity.getSortid());
        newDetail2.setLvl(currentEntity.getLvl() + 1);
        newDetail2.setValueMode(currentEntity.getValueMode());
        newDetail2.setPerfCalc(currentEntity.getPerfCalc());
        newDetail2.setSymbol(currentEntity.getSymbol());
        newDetail2.setUnit(currentEntity.getUnit());
        newDetail2.setRate(currentEntity.getRate());
        newDetail2.setMinNum(currentEntity.getMinNum());
        newDetail2.setMaxNum(currentEntity.getMaxNum());
        newDetail2.setLimited(currentEntity.isLimited());
        newDetail2.setApi(currentEntity.getApi());
        newDetail2.setStatus("N");
    }

    @Override
    protected boolean doBeforeUpdate() throws Exception {
        if (currentEntity != null && !detailList2.isEmpty()) {
            currentEntity.setAssigned(true);
        } else {
            currentEntity.setAssigned(false);
        }
        return super.doBeforeUpdate();
    }

    @Override
    protected boolean doBeforeUnverify() throws Exception {
        if (super.doBeforeUnverify()) {
            if (currentEntity.getParent() != null && currentEntity.getParent().getStatus().equals("V")) {
                showErrorMsg("Error", "主目标已审核");
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean doBeforeVerify() throws Exception {
        if (super.doBeforeVerify()) {
            for (IndicatorAssignment ia : detailList2) {
                if (!ia.getStatus().equals("V")) {
                    showErrorMsg("Error", "子目标未审核");
                    return false;
                }
            }
            if (currentEntity.getFormtype().equals("N") && currentEntity.isAssigned()) {
                BigDecimal tQ1 = BigDecimal.ZERO;
                BigDecimal tQ2 = BigDecimal.ZERO;
                BigDecimal tQ3 = BigDecimal.ZERO;
                BigDecimal tQ4 = BigDecimal.ZERO;
                BigDecimal bQ1 = BigDecimal.ZERO;
                BigDecimal bQ2 = BigDecimal.ZERO;
                BigDecimal bQ3 = BigDecimal.ZERO;
                BigDecimal bQ4 = BigDecimal.ZERO;
                for (IndicatorAssignment ia : detailList2) {
                    tQ1 = tQ1.add(ia.getTargetIndicator().getNq1());
                    tQ2 = tQ2.add(ia.getTargetIndicator().getNq2());
                    tQ3 = tQ3.add(ia.getTargetIndicator().getNq3());
                    tQ4 = tQ4.add(ia.getTargetIndicator().getNq4());
                    bQ1 = bQ1.add(ia.getBenchmarkIndicator().getNq1());
                    bQ2 = bQ2.add(ia.getBenchmarkIndicator().getNq2());
                    bQ3 = bQ3.add(ia.getBenchmarkIndicator().getNq3());
                    bQ4 = bQ4.add(ia.getBenchmarkIndicator().getNq4());
                }
                if (tQ1.compareTo(currentEntity.getTargetIndicator().getNq1()) != 0) {
                    showErrorMsg("Error", "Q1目标,子目标合计与总目标不同");
                    return false;
                }
                if (tQ2.compareTo(currentEntity.getTargetIndicator().getNq2()) != 0) {
                    showErrorMsg("Error", "Q2目标,子目标合计与总目标不同");
                    return false;
                }
                if (tQ3.compareTo(currentEntity.getTargetIndicator().getNq3()) != 0) {
                    showErrorMsg("Error", "Q3目标,子目标合计与总目标不同");
                    return false;
                }
                if (tQ4.compareTo(currentEntity.getTargetIndicator().getNq4()) != 0) {
                    showErrorMsg("Error", "Q4目标,子目标合计与总目标不同");
                    return false;
                }
                if (bQ1.compareTo(currentEntity.getBenchmarkIndicator().getNq1()) != 0) {
                    showErrorMsg("Error", "Q1基准,子目标合计与总目标不同");
                    return false;
                }
                if (bQ2.compareTo(currentEntity.getBenchmarkIndicator().getNq2()) != 0) {
                    showErrorMsg("Error", "Q2基准,子目标合计与总目标不同");
                    return false;
                }
                if (bQ3.compareTo(currentEntity.getBenchmarkIndicator().getNq3()) != 0) {
                    showErrorMsg("Error", "Q3基准,子目标合计与总目标不同");
                    return false;
                }
                if (bQ4.compareTo(currentEntity.getBenchmarkIndicator().getNq4()) != 0) {
                    showErrorMsg("Error", "Q4基准,子目标合计与总目标不同");
                    return false;
                }
            }
            return true;
        }
        return false;
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
    public void handleDialogReturnWhenNew(SelectEvent event) {
        if (event.getObject() != null && newEntity != null) {
            Department e = (Department) event.getObject();
            newEntity.setDeptno(e.getDeptno());
            newEntity.setDeptname(e.getDept());
        }
    }

    public void handleDialogReturnDeptForQuery(SelectEvent event) {
        if (event.getObject() != null) {
            Department d = (Department) event.getObject();
            queryDeptno = d.getDeptno();
            queryDeptname = d.getDept();
        }
    }

    @Override
    public void handleDialogReturnWhenDetailEdit(SelectEvent event) {
        if (currentDetail != null && event.getObject() != null) {
            Department dept = (Department) event.getObject();
            currentDetail.setPid(currentEntity.getId());
            currentDetail.setSeq(currentEntity.getSeq());
            currentDetail.setDeptno(dept.getDeptno());
            currentDetail.setDeptname(dept.getDept());
        }
    }

    @Override
    public void handleDialogReturnWhenDetail2Edit(SelectEvent event) {
        if (currentDetail2 != null && event.getObject() != null) {
            Department dept = (Department) event.getObject();
            currentDetail2.setPid(currentEntity.getId());
            currentDetail2.setSeq(currentEntity.getSeq());
            currentDetail2.setSortid(currentEntity.getSortid());
            currentDetail2.setDeptno(dept.getDeptno());
            currentDetail2.setDeptname(dept.getDept());
        }
    }

    @Override
    public void init() {
        superEJB = indicatorBean;
        detailEJB = indicatorDepartmentBean;
        detailEJB2 = indicatorAssignmentBean;
        model = new IndicatorModel(indicatorBean);
        model.getSortFields().put("seq", "DESC");
        model.getSortFields().put("sortid", "ASC");
        model.getSortFields().put("deptno", "ASC");
        super.init();
    }

    public void print(String rptdesign, String reportFormat) throws Exception {
        if (currentPrgGrant != null && currentPrgGrant.getDoprt()) {
            HashMap<String, Object> reportParams = new HashMap<>();
            reportParams.put("company", userManagedBean.getCurrentCompany().getName());
            reportParams.put("companyFullName", userManagedBean.getCurrentCompany().getFullname());
            reportParams.put("JNDIName", this.currentPrgGrant.getSysprg().getRptjndi());
            if (!this.model.getFilterFields().isEmpty()) {
                reportParams.put("filterFields", BaseLib.convertMapToStringWithClass(this.model.getFilterFields()));
            } else {
                reportParams.put("filterFields", "");
            }
            if (!this.model.getSortFields().isEmpty()) {
                reportParams.put("sortFields", BaseLib.convertMapToString(this.model.getSortFields()));
            } else {
                reportParams.put("sortFields", "");
            }
            this.fileName = this.currentPrgGrant.getSysprg().getApi() + BaseLib.formatDate("yyyyMMddHHss", this.getDate()) + "." + reportFormat;
            String reportName = reportPath + rptdesign;
            String outputName = reportOutputPath + this.fileName;
            this.reportViewPath = reportViewContext + this.fileName;
            try {
                if (this.currentPrgGrant.getSysprg().getRptclazz() != null) {
                    reportClassLoader = Class.forName(this.currentPrgGrant.getSysprg().getRptclazz()).getClassLoader();
                }
                //初始配置
                this.reportInitAndConfig();
                //生成报表
                this.reportRunAndOutput(reportName, reportParams, outputName, reportFormat, null);
                //预览报表
                this.preview();
            } catch (Exception ex) {
                throw ex;
            }
        }
    }

    @Override
    public void query() {
        if (model != null) {
            model.getFilterFields().clear();
            if (queryFormId != null && !"".equals(queryFormId)) {
                model.getFilterFields().put("formid", queryFormId);
            }
            if (queryName != null && !"".equals(queryName)) {
                model.getFilterFields().put("name", queryName);
            }
            if (queryDeptno != null && !"".equals(queryDeptno)) {
                model.getFilterFields().put("deptno", queryDeptno);
            }
            if (queryDeptname != null && !"".equals(queryDeptname)) {
                model.getFilterFields().put("deptname", queryDeptname);
            }
            if (queryYear > 2016) {
                model.getFilterFields().put("seq", queryYear);
            }
            if (queryState != null && !"ALL".equals(queryState)) {
                model.getFilterFields().put("status", queryState);
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        queryName = null;
        queryDeptno = null;
        queryDeptname = null;
    }

    public void updatePerformance() {
        if (currentEntity != null) {
            indicatorBean.updatePerformance(currentEntity);
        } else {
            showErrorMsg("Error", "没有可更新指标");
        }
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

    /**
     * @return the queryYear
     */
    public int getQueryYear() {
        return queryYear;
    }

    /**
     * @param queryYear the queryYear to set
     */
    public void setQueryYear(int queryYear) {
        this.queryYear = queryYear;
    }

}
