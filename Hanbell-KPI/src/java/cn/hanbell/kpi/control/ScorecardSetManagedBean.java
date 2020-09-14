/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.ejb.DepartmentBean;
import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.Department;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.ejb.ScorecardBean;
import cn.hanbell.kpi.ejb.ScorecardDetailBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.entity.ScorecardDetail;
import cn.hanbell.kpi.lazy.ScorecardModel;
import cn.hanbell.kpi.web.SuperMultiBean;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "scorecardSetManagedBean")
@javax.faces.bean.SessionScoped
public class ScorecardSetManagedBean extends SuperMultiBean<Scorecard, ScorecardDetail> {

    @EJB
    private ScorecardBean scorecardBean;
    @EJB
    private ScorecardDetailBean scorecardDetailBean;

    @EJB
    private DepartmentBean departmentBean;

    @EJB
    private SystemUserBean systemUserBean;

    protected Calendar c;
    private boolean freezed;

    protected String queryDeptno;
    protected String queryDeptname;
    protected String queryUserid;
    protected String queryUsername;
    protected int queryYear;

    protected List<String> paramDeptno = null;
    private List<String> deptList = null;

    public ScorecardSetManagedBean() {
        super(Scorecard.class, ScorecardDetail.class);
        c = Calendar.getInstance();
        openParams = new HashMap();
        openOptions = new HashMap();
    }

    /**
     * @description 只能查看自己部门的考核内容
     */
    public List<String> findByDeptListForUserid(String userid) {
        SystemUser systemUser = systemUserBean.findByUserId(userid);
        List<Department> departmentList = departmentBean.findByDeptList(systemUser.getDeptno().substring(0, 2) + "%");
        if (departmentList != null && !departmentList.isEmpty()) {
            List<String> executors = new ArrayList<>();
            departmentList.stream().forEach((e) -> {
                executors.add(e.getDeptno());
            });
            try {
                return executors;
            } catch (Exception ex) {
                return null;
            }
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void create() {
        super.create();
        this.newEntity.setCompany(userManagedBean.getCompany());
        this.newEntity.setSeq(queryYear);
    }

    public void calcScore() {
        if (currentEntity != null && currentEntity.getFreezeDate() != null && currentEntity.getFreezeDate().after(userManagedBean.getBaseDate())) {
            showErrorMsg("Error", "资料已冻结,不可更新");
            return;
        }
        String col;
        BigDecimal value;
        col = scorecardBean.getColumn("sq", userManagedBean.getQ());
        List<ScorecardDetail> data = scorecardDetailBean.findByPId(currentEntity.getId());
        try {
            value = scorecardBean.getDetailScores(data, col);
            switch (userManagedBean.getQ()) {
                case 1:
                    currentEntity.setSq1(value);
                    break;
                case 2:
                    currentEntity.setSq2(value);
                    value = scorecardBean.getDetailScores(data, "sh1");
                    currentEntity.setSh1(value);
                    break;
                case 3:
                    currentEntity.setSq3(value);
                    break;
                case 4:
                    currentEntity.setSq4(value);
                    value = scorecardBean.getDetailScores(data, "sh2");
                    currentEntity.setSh2(value);
                    value = scorecardBean.getDetailScores(data, "sfy");
                    currentEntity.setSfy(value);
                    break;
            }
            scorecardBean.update(currentEntity);
            showInfoMsg("Info", "更新成功");
        } catch (Exception ex) {
            showErrorMsg("Error", ex.getMessage());
        }
    }

    public void calcItemScore() {
        if (currentDetail != null) {
            if (!currentDetail.getType().equals("N")) {
                showWarnMsg("Warn", "数值型才能更新");
                return;
            }
            if (currentDetail.getFreezeDate() != null && currentDetail.getFreezeDate().after(userManagedBean.getBaseDate())) {
                showErrorMsg("Error", "资料已冻结,不可更新");
                return;
            }
            String col = scorecardBean.getColumn("q", userManagedBean.getQ());
            try {
                if (currentDetail.getScoreJexl() != null && !"".equals(currentDetail.getScoreJexl())) {
                    //计算得分
                    scorecardBean.setDetailScore(currentDetail, col);
                }
                switch (userManagedBean.getQ()) {
                    case 1:
                        if (currentDetail.getGeneralScore().getSq1().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSq1(currentDetail.getGeneralScore().getSq1());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSq1(currentDetail.getGeneralScore().getSq1());
                        }
                        break;
                    case 2:
                        if (currentDetail.getGeneralScore().getSq2().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSq2(currentDetail.getGeneralScore().getSq2());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSq2(currentDetail.getGeneralScore().getSq2());
                        }
                        if (currentDetail.getGeneralScore().getSh1().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSh1(currentDetail.getGeneralScore().getSh1());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSh1(currentDetail.getGeneralScore().getSh1());
                        }
                        break;
                    case 3:
                        if (currentDetail.getGeneralScore().getSq3().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSq3(currentDetail.getGeneralScore().getSq3());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSq3(currentDetail.getGeneralScore().getSq3());
                        }
                        break;
                    case 4:
                        if (currentDetail.getGeneralScore().getSq4().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSq4(currentDetail.getGeneralScore().getSq4());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSq4(currentDetail.getGeneralScore().getSq4());
                        }
                        if (currentDetail.getGeneralScore().getSh2().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSh2(currentDetail.getGeneralScore().getSh2());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSh2(currentDetail.getGeneralScore().getSh2());
                        }
                        if (currentDetail.getGeneralScore().getSfy().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSfy(currentDetail.getGeneralScore().getSfy());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSfy(currentDetail.getGeneralScore().getSfy());
                        }
                        break;
                }
                showInfoMsg("Info", "更新部门分数成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        }
    }

    @Override
    protected boolean doBeforeVerify() throws Exception {
        if (super.doBeforeVerify()) {
            //super.doBeforeVerify()会重设detailList
            if (!currentEntity.getTemplate()) {
                //不是模板需要检查权重是否100
                BigDecimal weight = BigDecimal.ZERO;
                for (ScorecardDetail d : detailList) {
                    weight = weight.add(d.getWeight());
                }
                if (weight.compareTo(BigDecimal.valueOf(100)) != 0) {
                    showErrorMsg("Error", "权重合计需要等于100");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void doConfirmDetail() {
        if (currentDetail.getFreezeDate() != null && currentDetail.getFreezeDate().after(userManagedBean.getBaseDate())) {
            showErrorMsg("Error", "资料已冻结,不可更新");
            return;
        }
        try {
            switch (userManagedBean.getQ()) {
                case 1:
                    if (currentDetail.getGeneralScore().getSq1().compareTo(BigDecimal.ZERO) != 0) {
                        currentDetail.setSq1(currentDetail.getGeneralScore().getSq1());
                    } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentDetail.setSq1(currentDetail.getGeneralScore().getSq1());
                    }
                    break;
                case 2:
                    if (currentDetail.getGeneralScore().getSq2().compareTo(BigDecimal.ZERO) != 0) {
                        currentDetail.setSq2(currentDetail.getGeneralScore().getSq2());
                    } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentDetail.setSq2(currentDetail.getGeneralScore().getSq2());
                    }
                    if (currentDetail.getGeneralScore().getSh1().compareTo(BigDecimal.ZERO) != 0) {
                        currentDetail.setSh1(currentDetail.getGeneralScore().getSh1());
                    } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentDetail.setSh1(currentDetail.getGeneralScore().getSh1());
                    }
                    break;
                case 3:
                    if (currentDetail.getGeneralScore().getSq3().compareTo(BigDecimal.ZERO) != 0) {
                        currentDetail.setSq3(currentDetail.getGeneralScore().getSq3());
                    } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentDetail.setSq3(currentDetail.getGeneralScore().getSq3());
                    }
                    break;
                case 4:
                    if (currentDetail.getGeneralScore().getSq4().compareTo(BigDecimal.ZERO) != 0) {
                        currentDetail.setSq4(currentDetail.getGeneralScore().getSq4());
                    } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentDetail.setSq4(currentDetail.getGeneralScore().getSq4());
                    }
                    if (currentDetail.getGeneralScore().getSh2().compareTo(BigDecimal.ZERO) != 0) {
                        currentDetail.setSh2(currentDetail.getGeneralScore().getSh2());
                    } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentDetail.setSh2(currentDetail.getGeneralScore().getSh2());
                    }
                    if (currentDetail.getGeneralScore().getSfy().compareTo(BigDecimal.ZERO) != 0) {
                        currentDetail.setSfy(currentDetail.getGeneralScore().getSfy());
                    } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentDetail.setSfy(currentDetail.getGeneralScore().getSfy());
                    }
                    break;
            }
        } catch (Exception ex) {
            showErrorMsg("Error", "更新异常");
        }
        super.doConfirmDetail();
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

    public void handleDialogReturnTemplateWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Object o = event.getObject();
            Scorecard sc = (Scorecard) o;
            currentEntity.setTemplateId(sc.getName());
            currentEntity.setApi(sc.getApi());
            List<ScorecardDetail> templateDetail = scorecardDetailBean.findByPId(sc.getId());
            if (templateDetail != null && !templateDetail.isEmpty()) {
                for (ScorecardDetail d : templateDetail) {
                    this.createDetail();
                    this.currentDetail.setPid(currentEntity.getId());
                    this.currentDetail.setContent(d.getContent());
                    this.currentDetail.setWeight(d.getWeight());
                    this.currentDetail.setType(d.getType());
                    this.currentDetail.setKind(d.getKind());
                    this.currentDetail.setValueMode(d.getValueMode());
                    this.currentDetail.setUnit(d.getUnit());
                    this.currentDetail.setSymbol(d.getSymbol());
                    this.currentDetail.setMinNum(d.getMinNum());
                    this.currentDetail.setMaxNum(d.getMaxNum());
                    this.currentDetail.setPerformanceJexl(d.getPerformanceJexl());
                    this.currentDetail.setPerformanceInterface(d.getPerformanceInterface());
                    this.currentDetail.setScoreJexl(d.getScoreJexl());
                    this.currentDetail.setScoreInterface(d.getScoreInterface());
                    this.currentDetail.setBfy(d.getBfy());
                    this.currentDetail.setBh2(d.getBh2());
                    this.currentDetail.setBh1(d.getBh1());
                    this.currentDetail.setBq1(d.getBq1());
                    this.currentDetail.setBq2(d.getBq2());
                    this.currentDetail.setBq3(d.getBq3());
                    this.currentDetail.setBq4(d.getBq4());
                    this.currentDetail.setTfy(d.getTfy());
                    this.currentDetail.setTh2(d.getTh2());
                    this.currentDetail.setTh1(d.getTh1());
                    this.currentDetail.setTq1(d.getTq1());
                    this.currentDetail.setTq2(d.getTq2());
                    this.currentDetail.setTq3(d.getTq3());
                    this.currentDetail.setTq4(d.getTq4());
                    this.doConfirmDetail();
                }
            }
        }
    }

    public void handleDialogReturnUserWhenNew(SelectEvent event) {
        if (event.getObject() != null && newEntity != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            newEntity.setUserid(user.getUserid());
            newEntity.setUsername(user.getUsername());
        }
    }

    public void handleDialogReturnUserWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            currentEntity.setUserid(user.getUserid());
            currentEntity.setUsername(user.getUsername());
        }
    }

    @Override
    public void handleDialogReturnWhenDetailNew(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Indicator e = (Indicator) event.getObject();
            currentDetail.setIndicator(e.getFormid());
            currentDetail.setDeptno(e.getDeptno());
            currentDetail.setDeptname(e.getDeptname());
            currentDetail.setUserid(e.getUserid());
            currentDetail.setUsername(e.getUsername());
            //从指标代入基准
            currentDetail.setBq1(e.getBenchmarkIndicator().getNq1().toString());
            currentDetail.setBq2(e.getBenchmarkIndicator().getNq2().toString());
            currentDetail.setBh1(e.getBenchmarkIndicator().getNh1().toString());
            currentDetail.setBq3(e.getBenchmarkIndicator().getNq3().toString());
            currentDetail.setBq4(e.getBenchmarkIndicator().getNq4().toString());
            currentDetail.setBh2(e.getBenchmarkIndicator().getNh2().toString());
            currentDetail.setBfy(e.getBenchmarkIndicator().getNfy().toString());
            //从指标代入目标
            currentDetail.setTq1(e.getTargetIndicator().getNq1().toString());
            currentDetail.setTq2(e.getTargetIndicator().getNq2().toString());
            currentDetail.setTh1(e.getTargetIndicator().getNh1().toString());
            currentDetail.setTq3(e.getTargetIndicator().getNq3().toString());
            currentDetail.setTq4(e.getTargetIndicator().getNq4().toString());
            currentDetail.setTh2(e.getTargetIndicator().getNh2().toString());
            currentDetail.setTfy(e.getTargetIndicator().getNfy().toString());
        }
    }

    @Override
    public void handleDialogReturnWhenDetailEdit(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Indicator e = (Indicator) event.getObject();
            currentDetail.setIndicator(e.getFormid());
            currentDetail.setDeptno(e.getDeptno());
            currentDetail.setDeptname(e.getDeptname());
            currentDetail.setUserid(e.getUserid());
            currentDetail.setUsername(e.getUsername());
            //从指标代入基准
            currentDetail.setBq1(e.getBenchmarkIndicator().getNq1().toString());
            currentDetail.setBq2(e.getBenchmarkIndicator().getNq2().toString());
            currentDetail.setBh1(e.getBenchmarkIndicator().getNh1().toString());
            currentDetail.setBq3(e.getBenchmarkIndicator().getNq3().toString());
            currentDetail.setBq4(e.getBenchmarkIndicator().getNq4().toString());
            currentDetail.setBh2(e.getBenchmarkIndicator().getNh2().toString());
            currentDetail.setBfy(e.getBenchmarkIndicator().getNfy().toString());
            //从指标代入目标
            currentDetail.setTq1(e.getTargetIndicator().getNq1().toString());
            currentDetail.setTq2(e.getTargetIndicator().getNq2().toString());
            currentDetail.setTh1(e.getTargetIndicator().getNh1().toString());
            currentDetail.setTq3(e.getTargetIndicator().getNq3().toString());
            currentDetail.setTq4(e.getTargetIndicator().getNq4().toString());
            currentDetail.setTh2(e.getTargetIndicator().getNh2().toString());
            currentDetail.setTfy(e.getTargetIndicator().getNfy().toString());
        }
    }

    public void handleDialogReturnDeptWhenDetailNew(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Department e = (Department) event.getObject();
            currentDetail.setDeptno(e.getDeptno());
            currentDetail.setDeptname(e.getDept());
        }
    }

    public void handleDialogReturnDeptWhenDetailEdit(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Object o = event.getObject();
            Department e = (Department) event.getObject();
            currentDetail.setDeptno(e.getDeptno());
            currentDetail.setDeptname(e.getDept());
        }
    }

    public void handleDialogReturnUserWhenDetailNew(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            currentDetail.setUserid(user.getUserid());
            currentDetail.setUsername(user.getUsername());
        }
    }

    public void handleDialogReturnUserWhenDetailEdit(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            currentDetail.setUserid(user.getUserid());
            currentDetail.setUsername(user.getUsername());
        }
    }

    @Override
    public void init() {
        superEJB = scorecardBean;
        detailEJB = scorecardDetailBean;
        model = new ScorecardModel(scorecardBean, this.userManagedBean);
        deptList = findByDeptListForUserid(userManagedBean.getUserid());
        model.getFilterFields().put("deptno IN ", deptList);
        model.getSortFields().put("seq", "DESC");
        model.getSortFields().put("sortid", "ASC");
        model.getSortFields().put("deptno", "ASC");
        c.setTime(userManagedBean.getBaseDate());
        queryYear = c.get(Calendar.YEAR);
        super.init();
    }

    @Override
    public void query() {
        super.query();
        if (model != null) {
            model.getFilterFields().clear();
            if (queryYear != 0) {
                model.getFilterFields().put("seq", queryYear);
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
            if (queryState != null && !"ALL".equals(queryState)) {
                model.getFilterFields().put("status", queryState);
            }
        }
    }

    public void moveDown() {
        if (currentDetail != null) {
            int i, n, m;
            n = currentDetail.getSeq();
            i = detailList.indexOf(currentDetail);
            if (i < detailList.size() - 1) {
                ScorecardDetail scd = detailList.get(i + 1);
                m = scd.getSeq();
                scd.setSeq(-1);
                currentDetail.setSeq(m);
                this.doConfirmDetail();
                scd.setSeq(n);
                currentDetail = scd;
                this.doConfirmDetail();
                //重新排序
                detailList.sort((ScorecardDetail o1, ScorecardDetail o2) -> {
                    if (o1.getSeq() > o2.getSeq()) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
                showInfoMsg("Info", "MoveDown");
            } else {
                showWarnMsg("Warn", "已是最后");
            }
        }
    }

    public void moveUp() {
        if (currentDetail != null) {
            int i, n, m;
            n = currentDetail.getSeq();
            i = detailList.indexOf(currentDetail);
            if (i > 0) {
                ScorecardDetail scd = detailList.get(i - 1);
                m = scd.getSeq();
                scd.setSeq(-1);
                currentDetail.setSeq(m);
                this.doConfirmDetail();
                scd.setSeq(n);
                currentDetail = scd;
                this.doConfirmDetail();
                //重新排序
                detailList.sort((ScorecardDetail o1, ScorecardDetail o2) -> {
                    if (o1.getSeq() > o2.getSeq()) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
                showInfoMsg("Info", "MoveUp");
            } else {
                showWarnMsg("Warn", "已是最前");
            }
        }
    }

    @Override
    public void openDialog(String view) {
        switch (view) {
            case "indicatorSelect":
                openParams.clear();
                if (paramDeptno == null) {
                    paramDeptno = new ArrayList<>();
                } else {
                    paramDeptno.clear();
                }
                paramDeptno.add(currentEntity.getDeptno());
                openParams.put("deptno", paramDeptno);
                openOptions.clear();
                openOptions.put("modal", true);
                openOptions.put("contentWidth", "900");
                super.openDialog(view, openOptions, openParams);
                break;
            default:
                super.openDialog(view);
        }
    }

    public void updateScorecardExplanationScore() {
        if (currentDetail != null) {
            if (currentDetail.getFreezeDate() != null && currentDetail.getFreezeDate().after(userManagedBean.getBaseDate())) {
                showErrorMsg("Error", "资料已冻结,不可更新");
                return;
            }
            ScorecardDetail sd = scorecardDetailBean.findById(currentDetail.getId());
            try {
                if (sd != null) {
                    switch (userManagedBean.getQ()) {
                        case 1:
                            sd.getCauseScore1().setQ1(currentDetail.getCauseScore2().getQ1());
                            sd.getCauseScore2().setQ1(currentDetail.getCauseScore2().getQ1());
                            sd.getSummaryScore1().setQ1(currentDetail.getSummaryScore1().getQ1());
                            sd.getSummaryScore2().setQ1(currentDetail.getSummaryScore2().getQ1());
                            break;
                        case 2:
                            sd.getCauseScore1().setQ2(currentDetail.getSummaryScore1().getQ1());
                            sd.getCauseScore2().setQ2(currentDetail.getCauseScore2().getQ2());
                            sd.getSummaryScore1().setQ2(currentDetail.getSummaryScore1().getQ2());
                            sd.getSummaryScore2().setQ2(currentDetail.getSummaryScore2().getQ2());
                            break;
                        case 3:
                            sd.getCauseScore1().setQ3(currentDetail.getCauseScore1().getQ3());
                            sd.getCauseScore2().setQ3(currentDetail.getCauseScore2().getQ3());
                            sd.getSummaryScore1().setQ3(currentDetail.getSummaryScore1().getQ3());
                            sd.getSummaryScore2().setQ3(currentDetail.getSummaryScore2().getQ3());
                            break;
                        case 4:
                            sd.getCauseScore1().setQ4(currentDetail.getCauseScore1().getQ4());
                            sd.getCauseScore2().setQ4(currentDetail.getCauseScore2().getQ4());
                            sd.getSummaryScore1().setQ4(currentDetail.getSummaryScore1().getQ4());
                            sd.getSummaryScore2().setQ4(currentDetail.getSummaryScore2().getQ4());
                            break;
                    }
                    scorecardDetailBean.update(sd);
                }
                showInfoMsg("Info", "更新成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        }

    }

    @Override
    public void print() throws Exception {
        if (currentPrgGrant != null && currentPrgGrant.getDoprt()) {
            HashMap<String, Object> reportParams = new HashMap<>();
            reportParams.put("company", userManagedBean.getCurrentCompany().getName());
            reportParams.put("companyFullName", userManagedBean.getCurrentCompany().getFullname());
            reportParams.put("JNDIName", this.currentPrgGrant.getSysprg().getRptjndi());
            reportParams.put("seq", currentEntity.getSeq());
            reportParams.put("deptname", currentEntity.getDeptname());
            reportParams.put("id", currentEntity.getId());
            reportParams.put("season", userManagedBean.getQ());
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
            this.fileName = this.currentPrgGrant.getSysprg().getApi() + BaseLib.formatDate("yyyyMMddHHss", this.getDate()) + "." + "xls";
            String reportName = reportPath + this.currentPrgGrant.getSysprg().getRptdesign();
            String outputName = reportOutputPath + this.fileName;
            this.reportViewPath = reportViewContext + this.fileName;
            try {
                if (this.currentPrgGrant.getSysprg().getRptclazz() != null) {
                    reportClassLoader = Class.forName(this.currentPrgGrant.getSysprg().getRptclazz()).getClassLoader();
                }
                //初始配置
                this.reportInitAndConfig();
                //生成报表
                this.reportRunAndOutput(reportName, reportParams, outputName, "xls", null);
                //预览报表
                this.preview();
            } catch (Exception ex) {
                throw ex;
            }
        }
    }

    @Override
    public void setCurrentEntity(Scorecard currentEntity) {
        super.setCurrentEntity(currentEntity);
        if (currentEntity != null) {
            this.freezed = currentEntity.getFreezeDate() != null && currentEntity.getFreezeDate().after(userManagedBean.getBaseDate());
        }
    }

    //月结功能
    public void setFreezeDate() {
        if (currentEntity != null) {
            currentEntity.setFreezeDate(userManagedBean.getBaseDate());
            scorecardBean.update(currentEntity);
            if (detailList != null) {
                for (ScorecardDetail s : detailList) {
                    s.setFreezeDate(userManagedBean.getBaseDate());
                    scorecardDetailBean.update(s);
                }
                showInfoMsg("Info", "冻结时间成功");
            }
        } else {
            showErrorMsg("Error", "请选中一个指标");
        }
    }

    /**
     * @return the freezed
     */
    public boolean isFreezed() {
        return freezed;
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

    /**
     * @return the queryUsername
     */
    public String getQueryUsername() {
        return queryUsername;
    }

    /**
     * @param queryUsername the queryUsername to set
     */
    public void setQueryUsername(String queryUsername) {
        this.queryUsername = queryUsername;
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

    public void setDeptList(List<String> deptList) {
        this.deptList = deptList;
    }

}
