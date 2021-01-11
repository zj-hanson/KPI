/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.ScorecardAuditorBean;
import cn.hanbell.kpi.ejb.ScorecardBean;
import cn.hanbell.kpi.ejb.ScorecardContentBean;
import cn.hanbell.kpi.ejb.ScorecardGrantBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.entity.ScorecardAuditor;
import cn.hanbell.kpi.entity.ScorecardContent;
import cn.hanbell.kpi.entity.ScorecardGrant;
import cn.hanbell.kpi.lazy.ScorecardContentModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "scorecardManagedBean")
@ViewScoped
public class ScorecardManagedBean extends SuperSingleBean<ScorecardContent> {

    @EJB
    private IndicatorBean indicatorBean;
    @EJB
    private ScorecardBean scorecardBean;
    @EJB
    private ScorecardContentBean scorecardContentBean;
    @EJB
    private ScorecardGrantBean scorecardGrantBean;
    @EJB
    private ScorecardAuditorBean scorecardAuditorBean;
    @EJB
    SystemUserBean systemUserBean;

    protected ScorecardGrant scorecardGrant;

    protected Calendar c;
    private Scorecard scorecard;
    private boolean freezed;
    protected LinkedHashMap<String, String> auditorMap;
    protected List<ScorecardAuditor> scorecardAuditorList;

    public ScorecardManagedBean() {
        super(ScorecardContent.class);
        c = Calendar.getInstance();
    }

    public void calcScore() {
        if (scorecard != null) {
            if (scorecard.getFreezeDate() != null && scorecard.getFreezeDate().after(userManagedBean.getBaseDate())) {
                showErrorMsg("Error", "资料已冻结,不可更新");
                return;
            }
            String col;
            BigDecimal value;
            col = scorecardBean.getColumn("sq", userManagedBean.getQ());
            List<ScorecardContent> data = scorecardContentBean.findByPId(scorecard.getId());
            try {
                value = scorecardBean.getContentScores(data, col);
                switch (userManagedBean.getQ()) {
                    case 1:
                        scorecard.setSq1(value);
                        break;
                    case 2:
                        scorecard.setSq2(value);
                        value = scorecardBean.getContentScores(data, "sh1");
                        scorecard.setSh1(value);
                        break;
                    case 3:
                        scorecard.setSq3(value);
                        break;
                    case 4:
                        scorecard.setSq4(value);
                        value = scorecardBean.getContentScores(data, "sh2");
                        scorecard.setSh2(value);
                        value = scorecardBean.getContentScores(data, "sfy");
                        scorecard.setSfy(value);
                        break;
                }
                scorecardBean.update(scorecard);
                showInfoMsg("Info", "更新成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        }
    }

    public void calcItemScore() {
        if (currentEntity != null) {
            if (!currentEntity.getType().equals("N")) {
                showWarnMsg("Warn", "数值型才能按计算公式更新");
                return;
            }
            if (currentEntity.getFreezeDate() != null && currentEntity.getFreezeDate().after(userManagedBean.getBaseDate())) {
                showErrorMsg("Error", "资料已冻结,不可更新");
                return;
            }
            String col = scorecardBean.getColumn("q", userManagedBean.getQ());
            if (currentEntity.getIndicator() != null && !"".equals(currentEntity.getIndicator())) {
                Indicator i = indicatorBean.findByFormidYearAndDeptno(currentEntity.getIndicator(), currentEntity.getParent().getSeq(), currentEntity.getDeptno());
                if (i != null) {
                    switch (userManagedBean.getQ()) {
                        case 1:
                            currentEntity.setAq1(i.getActualIndicator().getNq1().toString());
                            currentEntity.setPq1(i.getPerformanceIndicator().getNq1());
                            break;
                        case 2:
                            currentEntity.setAq2(i.getActualIndicator().getNq2().toString());
                            currentEntity.setPq2(i.getPerformanceIndicator().getNq2());
                            currentEntity.setAh1(i.getActualIndicator().getNh1().toString());
                            currentEntity.setPh1(i.getPerformanceIndicator().getNh1());
                            break;
                        case 3:
                            currentEntity.setAq3(i.getActualIndicator().getNq3().toString());
                            currentEntity.setPq3(i.getPerformanceIndicator().getNq3());
                            break;
                        case 4:
                            currentEntity.setAq4(i.getActualIndicator().getNq4().toString());
                            currentEntity.setPq4(i.getPerformanceIndicator().getNq4());
                            currentEntity.setAh2(i.getActualIndicator().getNh2().toString());
                            currentEntity.setPh2(i.getPerformanceIndicator().getNh2());
                            currentEntity.setAfy(i.getActualIndicator().getNfy().toString());
                            currentEntity.setPfy(i.getPerformanceIndicator().getNfy());
                            break;
                    }
                    showInfoMsg("Info", "更新实际值成功");
                } else {
                    showErrorMsg("Error", "找不到相关指标,更新失败");
                }
            }
            try {
                if (currentEntity.getPerformanceJexl() != null && !"".equals(currentEntity.getPerformanceJexl())) {
                    //计算达成
                    scorecardBean.setPerf(currentEntity, col);
                    if (userManagedBean.getQ() == 2) {
                        col = scorecardBean.getColumn("h", 1);
                        scorecardBean.setPerf(currentEntity, col);
                    } else if (userManagedBean.getQ() == 4) {
                        //下半年隐藏 不需要计算
                        //col = scorecardBean.getColumn("h", 2);
                        //scorecardBean.setPerf(currentEntity, col);
                        //全年
                        scorecardBean.setPerf(currentEntity, "fy");
                    }
                    showInfoMsg("Info", "更新达成率成功");
                }
                if (currentEntity.getScoreJexl() != null && !"".equals(currentEntity.getScoreJexl())) {
                    //计算得分
                    col = scorecardBean.getColumn("q", userManagedBean.getQ());
                    scorecardBean.setContentScore(currentEntity, col);
                    //上半年
                    if (userManagedBean.getQ() == 2) {
                        col = scorecardBean.getColumn("h", 1);
                        scorecardBean.setContentScore(currentEntity, col);

                    } else if (userManagedBean.getQ() == 4) {
                        //下半年隐藏 不需要计算
                        //col = scorecardBean.getColumn("h", 2);
                        //scorecardBean.setContentScore(currentEntity, col);
                        //全年
                        scorecardBean.setContentScore(currentEntity, "fy");
                    }
                    showInfoMsg("Info", "更新部门分数成功");
                }
            } catch (Exception ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        }
    }

    @Override
    protected boolean doBeforeUpdate() throws Exception {
        if (currentEntity != null) {
            if (currentEntity.getFreezeDate() != null && currentEntity.getFreezeDate().after(userManagedBean.getBaseDate())) {
                showErrorMsg("Error", "资料已冻结,不可更新");
                return false;
            }
            if (currentEntity.getType().equals("N")) {
                if (currentEntity.getAq1() != null && !currentEntity.getAq1().contains(".")) {
                    currentEntity.setAq1(currentEntity.getAq1().concat(".00"));
                }
                if (currentEntity.getAq2() != null && !currentEntity.getAq2().contains(".")) {
                    currentEntity.setAq2(currentEntity.getAq2().concat(".00"));
                }
                if (currentEntity.getAh1() != null && !currentEntity.getAh1().contains(".")) {
                    currentEntity.setAh1(currentEntity.getAh1().concat(".00"));
                }
                if (currentEntity.getAq3() != null && !currentEntity.getAq3().contains(".")) {
                    currentEntity.setAq3(currentEntity.getAq3().concat(".00"));
                }
                if (currentEntity.getAq4() != null && !currentEntity.getAq4().contains(".")) {
                    currentEntity.setAq4(currentEntity.getAq4().concat(".00"));
                }
                if (currentEntity.getAh2() != null && !currentEntity.getAh2().contains(".")) {
                    currentEntity.setAh2(currentEntity.getAh2().concat(".00"));
                }
                if (currentEntity.getAfy() != null && !currentEntity.getAfy().contains(".")) {
                    currentEntity.setAfy(currentEntity.getAfy().concat(".00"));
                }
            }
            switch (userManagedBean.getQ()) {
                case 1:
                    currentEntity.setSq1(currentEntity.getDeptScore().getSq1());
                    if (currentEntity.getGeneralScore().getSq1().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq1(currentEntity.getGeneralScore().getSq1());
                    } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentEntity.setSq1(currentEntity.getGeneralScore().getSq1());
                    }
                    break;
                case 2:
                    currentEntity.setSq2(currentEntity.getDeptScore().getSq2());
                    if (currentEntity.getGeneralScore().getSq2().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq2(currentEntity.getGeneralScore().getSq2());
                    } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentEntity.setSq2(currentEntity.getGeneralScore().getSq2());
                    }
                    currentEntity.setSh1(currentEntity.getDeptScore().getSh1());
                    if (currentEntity.getGeneralScore().getSh1().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSh1(currentEntity.getGeneralScore().getSh1());
                    } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentEntity.setSh1(currentEntity.getGeneralScore().getSh1());
                    }
                    break;
                case 3:
                    currentEntity.setSq3(currentEntity.getDeptScore().getSq3());
                    if (currentEntity.getGeneralScore().getSq3().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq3(currentEntity.getGeneralScore().getSq3());
                    } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentEntity.setSq3(currentEntity.getGeneralScore().getSq3());
                    }
                    break;
                case 4:
                    currentEntity.setSq4(currentEntity.getDeptScore().getSq4());
                    if (currentEntity.getGeneralScore().getSq4().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq4(currentEntity.getGeneralScore().getSq4());
                    } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentEntity.setSq4(currentEntity.getGeneralScore().getSq4());
                    }
                    currentEntity.setSh2(currentEntity.getDeptScore().getSh2());
                    if (currentEntity.getGeneralScore().getSh2().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSh2(currentEntity.getGeneralScore().getSh2());
                    } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentEntity.setSh2(currentEntity.getGeneralScore().getSh2());
                    }
                    currentEntity.setSfy(currentEntity.getDeptScore().getSfy());
                    if (currentEntity.getGeneralScore().getSfy().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSfy(currentEntity.getGeneralScore().getSfy());
                    } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentEntity.setSfy(currentEntity.getGeneralScore().getSfy());
                    }
                    break;
            }

        }
        return super.doBeforeUpdate();
    }

    @Override
    public void init() {
        c.setTime(userManagedBean.getBaseDate());
        superEJB = scorecardContentBean;
        model = new ScorecardContentModel(scorecardContentBean, this.userManagedBean);
        model.getSortFields().put("seq", "ASC");
        model.getSortFields().put("deptno", "ASC");
        model.getFilterFields().put("parent.seq", c.get(Calendar.YEAR));
        if (this.getScorecard() != null && this.getScorecard().getId() != null) {
            model.getFilterFields().put("pid", getScorecard().getId());
        }
        scorecardAuditorList = new ArrayList<>();
        scorecardAuditorList = scorecardAuditorBean.findByPidAndQuarter(scorecard.getId(), userManagedBean.getY(), userManagedBean.getQ());
        auditorMap = new LinkedHashMap<>();
        if (!scorecardAuditorList.isEmpty()) {
            for (ScorecardAuditor sa : scorecardAuditorList) {
                auditorMap.put(sa.getAuditorId(), sa.getAuditorName() + "Q" + userManagedBean.getQ() + "已审核");
            }
        } else {
            auditorMap.put("no", "没有人审核哟");
        }
        super.init();
    }

    @Override
    public void construct() {
        if (fc == null) {
            fc = FacesContext.getCurrentInstance();
        }
        if (ec == null) {
            ec = fc.getExternalContext();
        }
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String id = request.getParameter("id");
        if (id == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        scorecard = scorecardBean.findById(Integer.valueOf(id));
        if (scorecard == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        } else {
            this.freezed = scorecard.getFreezeDate() != null && scorecard.getFreezeDate().after(userManagedBean.getBaseDate());
        }
        init();
        super.construct();
    }

    @Override
    public void verify() {
        try {
            if (scorecard != null) {
                //如果审核过了 就不能再审了
//            List<ScorecardAuditor> data = scorecardAuditorBean.findByPidAndAuditorId(scorecard.getId(), userManagedBean.getUserid());
//            if (!data.isEmpty()) {
//                return;
//            }
                ScorecardAuditor sa = new ScorecardAuditor();
                sa.setPid(scorecard.getId());
                sa.setCredateToNow();
                sa.setCreator(userManagedBean.getUserid());
                SystemUser user = systemUserBean.findByUserId(userManagedBean.getUserid());
                sa.setAuditorId(userManagedBean.getUserid());
                sa.setAuditorName(user.getUsername());
                sa.setSeq(userManagedBean.getY());
                sa.setQuarter(userManagedBean.getQ());
                scorecardAuditorBean.persist(sa);
            }
            showInfoMsg("Info", "审核成功");
        } catch (Exception ex) {
            showErrorMsg("Error", ex.getMessage());
        }
        init();
    }

    public void handleDialogReturnWhenSelect(SelectEvent event) {
        //考核表修改权限设定
        if (scorecard.getId() != null) {
            scorecardGrant = scorecardGrantBean.findByCompanyAndScorecardidAndContentidAndSeq(userManagedBean.getCompany(), scorecard.getId(), currentEntity.getId(), c.get(Calendar.YEAR));
            if (scorecardGrant == null) {
                scorecardGrant = new ScorecardGrant();
                scorecardGrant.setBenchmark(false);
                scorecardGrant.setTarget(false);
                scorecardGrant.setActual(false);
                scorecardGrant.setPerformance(false);
                scorecardGrant.setDeptscore(false);
                scorecardGrant.setGeneralscore(false);
            }
        }
    }

    @Override
    public void print() throws Exception {
        if (scorecard == null) {
            showMsg(FacesMessage.SEVERITY_WARN, "Warn", "没有可打印数据");
            return;
        }
        HashMap<String, Object> reportParams = new HashMap<>();
        reportParams.put("company", userManagedBean.getCurrentCompany().getName());
        reportParams.put("companyFullName", userManagedBean.getCurrentCompany().getFullname());
        reportParams.put("JNDIName", "java:global/KPI/KPI-ejb/ScorecardBean!cn.hanbell.kpi.ejb.ScorecardBean");
        reportParams.put("seq", scorecard.getSeq());
        reportParams.put("deptname", scorecard.getDeptname());
        reportParams.put("id", scorecard.getId());
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
        this.fileName = "scorecard" + BaseLib.formatDate("yyyyMMddHHss", this.getDate()) + "." + "xls";
        String reportName = reportPath + "scorecarddetail.rptdesign";
        String outputName = reportOutputPath + this.fileName;
        this.reportViewPath = reportViewContext + this.fileName;
        try {
            reportClassLoader = Class.forName("cn.hanbell.kpi.rpt.ScorecardReport").getClassLoader();
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

    /**
     * @return the scorecard
     */
    public Scorecard getScorecard() {
        return scorecard;
    }

    /**
     * @return the freezed
     */
    public boolean isFreezed() {
        return freezed;
    }

    public ScorecardGrant getScorecardGrant() {
        return scorecardGrant;
    }

    public void setScorecardGrant(ScorecardGrant scorecardGrant) {
        this.scorecardGrant = scorecardGrant;
    }

    public LinkedHashMap<String, String> getAuditorMap() {
        return auditorMap;
    }

    public void setAuditorMap(LinkedHashMap<String, String> auditorMap) {
        this.auditorMap = auditorMap;
    }

}
