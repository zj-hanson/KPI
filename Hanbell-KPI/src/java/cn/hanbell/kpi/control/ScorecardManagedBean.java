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

    public ScorecardManagedBean() {
        super(ScorecardContent.class);
        c = Calendar.getInstance();
    }

    public void calcScore() {
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
                value = scorecardBean.getScore(detail, col);
                switch (userManagedBean.getQ()) {
                    case 1:
                        scorecard.setSq1(value);
                        break;
                    case 2:
                        scorecard.setSq2(value);
                        value = scorecardBean.getScore(detail, "sh1");
                        scorecard.setSh1(value);
                        break;
                    case 3:
                        scorecard.setSq3(value);
                        break;
                    case 4:
                        scorecard.setSq4(value);
                        value = scorecardBean.getScore(detail, "sh2");
                        scorecard.setSh2(value);
                        value = scorecardBean.getScore(detail, "sfy");
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
        if (currentEntity != null && currentEntity.getIndicator() != null && !"".equals(currentEntity.getIndicator())) {
            if (!currentEntity.getType().equals("N")) {
                showWarnMsg("Warn", "数值型才能从指标更新");
                return;
            }
            Indicator i = indicatorBean.findByFormidYearAndDeptno(currentEntity.getIndicator(), currentEntity.getParent().getSeq(), currentEntity.getDeptno());
            if (i != null) {
                String col = scorecardBean.getColumn("q", userManagedBean.getQ());
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
                try {
                    if (currentEntity.getScoreJexl() != null && !"".equals(currentEntity.getScoreJexl())) {
                        scorecardBean.setScore(currentEntity, col);
                        showInfoMsg("Info", "更新部门分数成功");
                    }
                } catch (Exception ex) {
                    showErrorMsg("Error", ex.getMessage());
                }
            } else {
                showErrorMsg("Error", "找不到相关指标,更新失败");
            }
        }
    }

    @Override
    protected boolean doBeforeUpdate() throws Exception {
        if (currentEntity != null) {
            if (currentEntity.getDeptScore().getSfy().compareTo(BigDecimal.ZERO) != 0) {
                currentEntity.setSfy(currentEntity.getDeptScore().getSfy());
            }
            if (currentEntity.getDeptScore().getSh2().compareTo(BigDecimal.ZERO) != 0) {
                currentEntity.setSh2(currentEntity.getDeptScore().getSh2());
            }
            if (currentEntity.getDeptScore().getSh1().compareTo(BigDecimal.ZERO) != 0) {
                currentEntity.setSh1(currentEntity.getDeptScore().getSh1());
            }
            if (currentEntity.getDeptScore().getSq1().compareTo(BigDecimal.ZERO) != 0) {
                currentEntity.setSq1(currentEntity.getDeptScore().getSq1());
            }
            if (currentEntity.getDeptScore().getSq2().compareTo(BigDecimal.ZERO) != 0) {
                currentEntity.setSq2(currentEntity.getDeptScore().getSq2());
            }
            if (currentEntity.getDeptScore().getSq3().compareTo(BigDecimal.ZERO) != 0) {
                currentEntity.setSq3(currentEntity.getDeptScore().getSq3());
            }
            if (currentEntity.getDeptScore().getSq4().compareTo(BigDecimal.ZERO) != 0) {
                currentEntity.setSq4(currentEntity.getDeptScore().getSq4());
            }
            if (currentEntity.getGeneralScore().getSfy().compareTo(BigDecimal.ZERO) != 0) {
                currentEntity.setSfy(currentEntity.getGeneralScore().getSfy());
            } else if (currentEntity.getWeight() == 0) {
                currentEntity.setSfy(currentEntity.getGeneralScore().getSfy());
            }
            if (currentEntity.getGeneralScore().getSh2().compareTo(BigDecimal.ZERO) != 0) {
                currentEntity.setSh2(currentEntity.getGeneralScore().getSh2());
            } else if (currentEntity.getWeight() == 0) {
                currentEntity.setSh2(currentEntity.getGeneralScore().getSh2());
            }
            if (currentEntity.getGeneralScore().getSh1().compareTo(BigDecimal.ZERO) != 0) {
                currentEntity.setSh1(currentEntity.getGeneralScore().getSh1());
            } else if (currentEntity.getWeight() == 0) {
                currentEntity.setSh1(currentEntity.getGeneralScore().getSh1());
            }
            if (currentEntity.getGeneralScore().getSq1().compareTo(BigDecimal.ZERO) != 0) {
                currentEntity.setSq1(currentEntity.getGeneralScore().getSq1());
            } else if (currentEntity.getWeight() == 0) {
                currentEntity.setSq1(currentEntity.getGeneralScore().getSq1());
            }
            if (currentEntity.getGeneralScore().getSq2().compareTo(BigDecimal.ZERO) != 0) {
                currentEntity.setSq2(currentEntity.getGeneralScore().getSq2());
            } else if (currentEntity.getWeight() == 0) {
                currentEntity.setSq2(currentEntity.getGeneralScore().getSq2());
            }
            if (currentEntity.getGeneralScore().getSq3().compareTo(BigDecimal.ZERO) != 0) {
                currentEntity.setSq3(currentEntity.getGeneralScore().getSq3());
            } else if (currentEntity.getWeight() == 0) {
                currentEntity.setSq3(currentEntity.getGeneralScore().getSq3());
            }
            if (currentEntity.getGeneralScore().getSq4().compareTo(BigDecimal.ZERO) != 0) {
                currentEntity.setSq4(currentEntity.getGeneralScore().getSq4());
            } else if (currentEntity.getWeight() == 0) {
                currentEntity.setSq4(currentEntity.getGeneralScore().getSq4());
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
        setScorecard(scorecardBean.findById(Integer.valueOf(id)));
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

    /**
     * @return the scorecard
     */
    public Scorecard getScorecard() {
        return scorecard;
    }

    /**
     * @param scorecard the scorecard to set
     */
    public void setScorecard(Scorecard scorecard) {
        this.scorecard = scorecard;
    }

}
