/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.ScorecardBean;
import cn.hanbell.kpi.ejb.ScorecardContentBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.entity.ScorecardContent;
import cn.hanbell.kpi.lazy.ScorecardContentModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

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

    protected Calendar c;
    private Scorecard scorecard;
    private boolean freezed;

    public ScorecardManagedBean() {
        super(ScorecardContent.class);
        c = Calendar.getInstance();
    }

    public void calcScore() {
        if (currentEntity != null && currentEntity.getFreezeDate() != null && currentEntity.getFreezeDate().after(userManagedBean.getBaseDate())) {
            showErrorMsg("Error", "资料已冻结,不可更新");
            return;
        }
        if (scorecard != null) {
            String col;
            BigDecimal value;
            col = scorecardBean.getColumn("sq", userManagedBean.getQ());
            List<ScorecardContent> detail = scorecardContentBean.findByPId(scorecard.getId());
            if (detail != null && !detail.isEmpty()) {
                for (ScorecardContent c : detail) {

                }
            }
            try {
                value = scorecardBean.getTotalScore(detail, col);
                switch (userManagedBean.getQ()) {
                    case 1:
                        scorecard.setSq1(value);
                        break;
                    case 2:
                        scorecard.setSq2(value);
                        value = scorecardBean.getTotalScore(detail, "sh1");
                        scorecard.setSh1(value);
                        break;
                    case 3:
                        scorecard.setSq3(value);
                        break;
                    case 4:
                        scorecard.setSq4(value);
                        value = scorecardBean.getTotalScore(detail, "sh2");
                        scorecard.setSh2(value);
                        value = scorecardBean.getTotalScore(detail, "sfy");
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

    public void refeshActual() {
        if (currentEntity != null) {
            if (!currentEntity.getType().equals("N")) {
                showWarnMsg("Warn", "数值型才能从指标更新");
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
                    showInfoMsg("Info", "更新达成率成功");
                }
                if (currentEntity.getScoreJexl() != null && !"".equals(currentEntity.getScoreJexl())) {
                    //计算得分
                    scorecardBean.setDeptScore(currentEntity, col);
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
                    if (currentEntity.getDeptScore().getSq1().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq1(currentEntity.getDeptScore().getSq1());
                    }
                    if (currentEntity.getGeneralScore().getSq1().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq1(currentEntity.getGeneralScore().getSq1());
                    } else if (currentEntity.getWeight() == 0) {
                        currentEntity.setSq1(currentEntity.getGeneralScore().getSq1());
                    }
                    break;
                case 2:
                    if (currentEntity.getDeptScore().getSq2().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq2(currentEntity.getDeptScore().getSq2());
                    }
                    if (currentEntity.getGeneralScore().getSq2().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq2(currentEntity.getGeneralScore().getSq2());
                    } else if (currentEntity.getWeight() == 0) {
                        currentEntity.setSq2(currentEntity.getGeneralScore().getSq2());
                    }
                    if (currentEntity.getDeptScore().getSh1().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSh1(currentEntity.getDeptScore().getSh1());
                    }
                    if (currentEntity.getGeneralScore().getSh1().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSh1(currentEntity.getGeneralScore().getSh1());
                    } else if (currentEntity.getWeight() == 0) {
                        currentEntity.setSh1(currentEntity.getGeneralScore().getSh1());
                    }
                    break;
                case 3:
                    if (currentEntity.getDeptScore().getSq3().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq3(currentEntity.getDeptScore().getSq3());
                    }
                    if (currentEntity.getGeneralScore().getSq3().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq3(currentEntity.getGeneralScore().getSq3());
                    } else if (currentEntity.getWeight() == 0) {
                        currentEntity.setSq3(currentEntity.getGeneralScore().getSq3());
                    }
                    break;
                case 4:
                    if (currentEntity.getDeptScore().getSq4().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq4(currentEntity.getDeptScore().getSq4());
                    }
                    if (currentEntity.getGeneralScore().getSq4().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq4(currentEntity.getGeneralScore().getSq4());
                    } else if (currentEntity.getWeight() == 0) {
                        currentEntity.setSq4(currentEntity.getGeneralScore().getSq4());
                    }
                    if (currentEntity.getDeptScore().getSh2().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSh2(currentEntity.getDeptScore().getSh2());
                    }
                    if (currentEntity.getGeneralScore().getSh2().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSh2(currentEntity.getGeneralScore().getSh2());
                    } else if (currentEntity.getWeight() == 0) {
                        currentEntity.setSh2(currentEntity.getGeneralScore().getSh2());
                    }
                    if (currentEntity.getDeptScore().getSfy().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSfy(currentEntity.getDeptScore().getSfy());
                    }
                    if (currentEntity.getGeneralScore().getSfy().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSfy(currentEntity.getGeneralScore().getSfy());
                    } else if (currentEntity.getWeight() == 0) {
                        currentEntity.setSfy(currentEntity.getGeneralScore().getSfy());
                    }
                    break;
            }

        }
        return super.doBeforeUpdate();
    }

    @Override
    public void init() {
        fc = FacesContext.getCurrentInstance();
        ec = fc.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String id = request.getParameter("id");
        if (id == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        scorecard = scorecardBean.findById(Integer.valueOf(id));
        if (getScorecard() == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        c.setTime(userManagedBean.getBaseDate());
        superEJB = scorecardContentBean;
        model = new ScorecardContentModel(scorecardContentBean, this.userManagedBean);
        model.getSortFields().put("seq", "ASC");
        model.getSortFields().put("deptno", "ASC");
        model.getFilterFields().put("parent.seq", c.get(Calendar.YEAR));
        model.getFilterFields().put("pid", getScorecard().getId());
        super.init();
    }

    @Override
    public void setCurrentEntity(ScorecardContent currentEntity) {
        super.setCurrentEntity(currentEntity);
        if (currentEntity != null && userManagedBean != null) {
            if (currentEntity.getFreezeDate() != null && currentEntity.getFreezeDate().after(userManagedBean.getBaseDate())) {
                this.freezed = true;
            } else {
                this.freezed = false;
            }
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

}
