/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.entity.Department;
import cn.hanbell.kpi.ejb.IndicatorAnalysisBean;
import cn.hanbell.kpi.ejb.IndicatorAssignmentBean;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorDepartmentBean;
import cn.hanbell.kpi.ejb.IndicatorDetailBean;
import cn.hanbell.kpi.ejb.IndicatorSetBean;
import cn.hanbell.kpi.ejb.IndicatorSummaryBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorAnalysis;
import cn.hanbell.kpi.entity.IndicatorAssignment;
import cn.hanbell.kpi.entity.IndicatorDepartment;
import cn.hanbell.kpi.entity.IndicatorSet;
import cn.hanbell.kpi.entity.IndicatorSummary;
import cn.hanbell.kpi.lazy.IndicatorModel;
import cn.hanbell.kpi.web.SuperMulti3Bean;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "indicatorSetManagedBean")
@SessionScoped
public class IndicatorSetManagedBean extends SuperMulti3Bean<Indicator, IndicatorDepartment, IndicatorAssignment, IndicatorSet> {

    @EJB
    protected IndicatorBean indicatorBean;
    @EJB
    protected IndicatorDepartmentBean indicatorDepartmentBean;
    @EJB
    protected IndicatorAssignmentBean indicatorAssignmentBean;
    @EJB
    protected IndicatorDetailBean indicatorDetailBean;
    @EJB
    protected IndicatorSetBean indicatorSetBean;
    @EJB
    protected IndicatorAnalysisBean indicatorAnalysisBean;
    @EJB
    protected IndicatorSummaryBean indicatorSummaryBean;

    protected IndicatorAnalysis indicatorAnalysis;
    protected IndicatorSummary indicatorSummary;

    protected List<IndicatorAnalysis> analysisList;
    protected List<IndicatorSummary> summaryList;

    protected Calendar c;

    protected String queryDeptno;
    protected String queryDeptname;
    protected int queryYear;

    public IndicatorSetManagedBean() {
        super(Indicator.class, IndicatorDepartment.class, IndicatorAssignment.class, IndicatorSet.class);
        summaryList = new ArrayList<>();
        analysisList = new ArrayList<>();
        c = Calendar.getInstance();
    }

    public void calcActual() {
        if (currentEntity != null) {
            try {
                updateActual(currentEntity);
                showInfoMsg("Info", "更新实际值成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        } else {
            showErrorMsg("Error", "没有可更新指标");
        }
    }

    public void calcBenchmark() {
        if (currentEntity != null) {
            try {
                updateBenchmark(currentEntity);
                showInfoMsg("Info", "更新基准值成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        } else {
            showErrorMsg("Error", "没有可更新指标");
        }
    }

    public void calcTarget() {
        if (currentEntity != null) {
            try {
                updateTarget(currentEntity);
                showInfoMsg("Info", "更新目标值成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        } else {
            showErrorMsg("Error", "没有可更新指标");
        }
    }

    public void calcForecast() {
        if (currentEntity != null) {
            try {
                updateForecast(currentEntity);
                showInfoMsg("Info", "更新预测值成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        } else {
            showErrorMsg("Error", "没有可更新指标");
        }
    }

    public String copyEntity(String path) {
        if (this.currentEntity != null && this.currentPrgGrant != null) {
            try {
                Indicator entity = (Indicator) BeanUtils.cloneBean(currentEntity);
                entity.setId(null);
                entity.setAssigned(false);
                entity.setFreezeDate(null);
                entity.setPid(0);
                entity.setSeq(entity.getSeq() + 1);
                entity.setOther1Indicator(null);
                entity.setOther2Indicator(null);
                entity.setOther3Indicator(null);
                entity.setOther4Indicator(null);
                entity.setOther5Indicator(null);
                entity.setOther6Indicator(null);
                entity.setCreator(this.userManagedBean.getCurrentUser().getUserid() + "-" + this.userManagedBean.getCurrentUser().getUsername());
                entity.setCredate(getDate());
                entity.setStatus("N");
                setNewEntity(entity);
                return path;
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        }
        return "";
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
        newDetail2.setObjtype(currentEntity.getObjtype());
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

    public void createIndicatorAnalysis() {
        if (currentEntity == null) {
            showErrorMsg("Error", "没有选择任何对象");
            return;
        }
        setIndicatorAnalysis(new IndicatorAnalysis());
        getIndicatorAnalysis().setPid(currentEntity.getId());
        getIndicatorAnalysis().setM(c.get(Calendar.MONTH) + 1);
        getIndicatorAnalysis().setSeq(this.getMaxSeq(analysisList));
    }

    public void createIndicatorSummary() {
        if (currentEntity == null) {
            showErrorMsg("Error", "没有选择任何对象");
            return;
        }
        setIndicatorSummary(new IndicatorSummary());
        indicatorSummary.setPid(currentEntity.getId());
        indicatorSummary.setM(c.get(Calendar.MONTH) + 1);
        indicatorSummary.setSeq(this.getMaxSeq(summaryList));
    }

    public void deleteIndicatorAnalysis() {
        if (indicatorAnalysis != null) {
            analysisList.remove(indicatorAnalysis);
            try {
                indicatorAnalysisBean.delete(indicatorAnalysis);
                showInfoMsg("Info", "删除成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        }
    }

    public void deleteIndicatorSummary() {
        if (indicatorSummary != null) {
            summaryList.remove(indicatorSummary);
            try {
                indicatorSummaryBean.delete(indicatorSummary);
                showInfoMsg("Info", "删除成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        }
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
        detailEJB3 = indicatorSetBean;
        model = new IndicatorModel(indicatorBean);
        model.getSortFields().put("seq", "DESC");
        model.getSortFields().put("sortid", "ASC");
        model.getSortFields().put("deptno", "ASC");
        model.getFilterFields().put("objtype =", "D");
        c.setTime(userManagedBean.getBaseDate());
        queryYear = c.get(Calendar.YEAR);
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
            model.getFilterFields().put("objtype =", "D");
        }
    }

    @Override
    public void reset() {
        super.reset();
        model.getFilterFields().put("objtype =", "D");
        queryName = null;
        queryDeptno = null;
        queryDeptname = null;
    }

    public void updateIndicatorAnalysis() {
        if (indicatorAnalysis != null) {
            try {
                if (indicatorAnalysis.getId() == null) {
                    analysisList.add(indicatorAnalysis);
                    indicatorAnalysisBean.persist(indicatorAnalysis);
                } else {
                    indicatorAnalysisBean.update(indicatorAnalysis);
                }
                showInfoMsg("Info", "更新成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        }
    }

    public void updateIndicatorSummary() {
        if (indicatorSummary != null) {
            try {
                if (indicatorSummary.getId() == null) {
                    summaryList.add(indicatorSummary);
                    indicatorSummaryBean.persist(indicatorSummary);
                } else {
                    indicatorSummaryBean.update(indicatorSummary);
                }
                showInfoMsg("Info", "更新成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        }
    }

    public void updateActual() {
        if (currentEntity != null) {
            indicatorBean.updateActual(currentEntity);
            showInfoMsg("Info", "更新实际值成功,请保存");
        } else {
            showErrorMsg("Error", "没有可更新指标");
        }
    }

    public void updateActual(Indicator entity) {
        if (entity.isAssigned()) {
            List<Indicator> details = indicatorBean.findByPId(entity.getId());
            if (details != null && !details.isEmpty()) {
                for (Indicator d : details) {
                    updateActual(d);
                }
            }//先计算子项值
            indicatorBean.updateActual(entity);
            indicatorBean.updatePerformance(entity);
            indicatorBean.update(entity);
        } else {
            indicatorBean.updateActual(entity);
            indicatorBean.updatePerformance(entity);
            indicatorBean.update(entity);
        }
    }

    public void updateBenchmark() {
        if (currentEntity != null) {
            indicatorBean.updateBenchmark(currentEntity);
            showInfoMsg("Info", "更新基准值成功,请保存");
        } else {
            showErrorMsg("Error", "没有可更新指标");
        }
    }

    public void updateBenchmark(Indicator entity) {
        if (entity.isAssigned()) {
            List<Indicator> details = indicatorBean.findByPId(entity.getId());
            if (details != null && !details.isEmpty()) {
                for (Indicator d : details) {
                    updateBenchmark(d);
                }
            }//先计算子项值
            indicatorBean.updateBenchmark(entity);
            indicatorBean.update(entity);
        } else {
            indicatorBean.updateBenchmark(entity);
            indicatorBean.update(entity);
        }
    }

    public void updatePerformance() {
        if (currentEntity != null) {
            indicatorBean.updatePerformance(currentEntity);
            showInfoMsg("Info", "更新达成率成功,请保存");
        } else {
            showErrorMsg("Error", "没有可更新指标");
        }
    }

    public void updateTarget() {
        if (currentEntity != null) {
            indicatorBean.updateTarget(currentEntity);
            showInfoMsg("Info", "更新目标值成功,请保存");
        } else {
            showErrorMsg("Error", "没有可更新指标");
        }
    }

    public void updateTarget(Indicator entity) {
        if (entity.isAssigned()) {
            List<Indicator> details = indicatorBean.findByPId(entity.getId());
            if (details != null && !details.isEmpty()) {
                for (Indicator d : details) {
                    updateTarget(d);
                }
            }//先计算子项值
            indicatorBean.updateTarget(entity);
            indicatorBean.updatePerformance(entity);
            indicatorBean.update(entity);
        } else {
            indicatorBean.updateTarget(entity);
            indicatorBean.updatePerformance(entity);
            indicatorBean.update(entity);
        }
    }

    public void updateForecast() {
        if (currentEntity != null) {
            indicatorBean.updateForecast(currentEntity);
            showInfoMsg("Info", "更新实际值成功,请保存");
        } else {
            showErrorMsg("Error", "没有可更新指标");
        }
    }

    public void updateForecast(Indicator entity) {
        if (entity.isAssigned()) {
            List<Indicator> details = indicatorBean.findByPId(entity.getId());
            if (details != null && !details.isEmpty()) {
                for (Indicator d : details) {
                    updateForecast(d);
                }
            }//先计算子项值
            indicatorBean.updateForecast(entity);
            indicatorBean.updatePerformance(entity);
            indicatorBean.update(entity);
        } else {
            indicatorBean.updateForecast(entity);
            indicatorBean.updatePerformance(entity);
            indicatorBean.update(entity);
        }
    }

    @Override
    public void setCurrentEntity(Indicator currentEntity) {
        super.setCurrentEntity(currentEntity);
        if (currentEntity != null && currentEntity.getId() != null) {
            summaryList = indicatorSummaryBean.findByPId(currentEntity.getId());
            analysisList = indicatorAnalysisBean.findByPId(currentEntity.getId());
        } else {
            if (summaryList != null) {
                summaryList.clear();
            }
            if (analysisList != null) {
                analysisList.clear();
            }
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

    /**
     * @return the indicatorAnalysis
     */
    public IndicatorAnalysis getIndicatorAnalysis() {
        return indicatorAnalysis;
    }

    /**
     * @param indicatorAnalysis the indicatorAnalysis to set
     */
    public void setIndicatorAnalysis(IndicatorAnalysis indicatorAnalysis) {
        this.indicatorAnalysis = indicatorAnalysis;
    }

    /**
     * @return the indicatorSummary
     */
    public IndicatorSummary getIndicatorSummary() {
        return indicatorSummary;
    }

    /**
     * @param indicatorSummary the indicatorSummary to set
     */
    public void setIndicatorSummary(IndicatorSummary indicatorSummary) {
        this.indicatorSummary = indicatorSummary;
    }

    /**
     * @return the analysisList
     */
    public List<IndicatorAnalysis> getAnalysisList() {
        return analysisList;
    }

    /**
     * @param analysisList the analysisList to set
     */
    public void setAnalysisList(List<IndicatorAnalysis> analysisList) {
        this.analysisList = analysisList;
    }

    /**
     * @return the summaryList
     */
    public List<IndicatorSummary> getSummaryList() {
        return summaryList;
    }

    /**
     * @param summaryList the summaryList to set
     */
    public void setSummaryList(List<IndicatorSummary> summaryList) {
        this.summaryList = summaryList;
    }

}
