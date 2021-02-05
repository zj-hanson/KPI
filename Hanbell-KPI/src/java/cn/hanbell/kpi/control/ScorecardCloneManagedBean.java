/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.ScorecardBean;
import cn.hanbell.kpi.ejb.ScorecardDetailBean;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.entity.ScorecardDetail;
import cn.hanbell.kpi.lazy.ScorecardModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author C1749
 */
@ManagedBean(name = "scorecardCloneManagedBean")
@SessionScoped
public class ScorecardCloneManagedBean extends SuperSingleBean<Scorecard> {

    @EJB
    ScorecardBean scorecardBean;
    @EJB
    ScorecardDetailBean scorecardDetailBean;

    private boolean updateBenchmark;
        Calendar lastC;
    Calendar newC;

    public ScorecardCloneManagedBean() {
        super(Scorecard.class);
                lastC = Calendar.getInstance();
        newC = Calendar.getInstance();
    }

    @Override
    public void init() {
        superEJB = scorecardBean;
        this.model = new ScorecardModel(superEJB, userManagedBean);
        super.init();
    }

    @Override
    protected boolean doBeforeVerify() throws Exception {
        if (queryDateBegin == null || queryDateEnd == null) {
            showErrorMsg("Error", "请先输入来源年度和目标年度");
            return false;
        }
        lastC.setTime(queryDateBegin);
        newC.setTime(queryDateEnd);
        if (newC.get(Calendar.YEAR) <= lastC.get(Calendar.YEAR)) {
            showErrorMsg("Error", "目标年度需要大于来源年度");
            return false;
        }
        if (scorecardBean.findRowCount(userManagedBean.getCompany(), newC.get(Calendar.YEAR))!=0) {
            showErrorMsg("Error", "目的年度已有资料");
            return false;
        }
        return true;
    }
    

    @Override
    public void verify() {
        try {
            if (!doBeforeVerify()) {
                return;
            }
            int lastY = lastC.get(Calendar.YEAR);
            int newY = newC.get(Calendar.YEAR);
            List<Scorecard> data;
            //复制
            data = scorecardBean.findByCompanyAndSeq(userManagedBean.getCompany(), lastY);
            if (data == null || data.isEmpty()) {
                showErrorMsg("Error", "获取产品参考指标失败");
                return;
            }
            for (Scorecard s : data) {
                cloneScorecard(s, newY);
            }
            showInfoMsg("Info", "指标复制成功");
        } catch (Exception ex) {
            showErrorMsg("Error", ex.toString());
        }
        
    }

    protected void cloneScorecard(Scorecard refScorecard, int y) throws Exception{
        try {
            if (refScorecard != null && refScorecard.getId() != -1) {
                //先生成父阶
                Scorecard newScorecard = (Scorecard) BeanUtils.cloneBean(refScorecard);
                newScorecard.setId(null);
                newScorecard.setSeq(y);
                newScorecard.setStatus("N");
                newScorecard.setCreator(userManagedBean.getCurrentUser().getUsername());
                newScorecard.setCredateToNow();
                newScorecard.setSq1(BigDecimal.ZERO);
                newScorecard.setSq2(BigDecimal.ZERO);
                newScorecard.setSq3(BigDecimal.ZERO);
                newScorecard.setSq4(BigDecimal.ZERO);
                newScorecard.setSh1(BigDecimal.ZERO);
                newScorecard.setSh2(BigDecimal.ZERO);
                newScorecard.setSfy(BigDecimal.ZERO);
                scorecardBean.persist(newScorecard);
                List<ScorecardDetail> details = scorecardDetailBean.findByPId(refScorecard.getId());
                if (details != null && !details.isEmpty()) {
                    for (ScorecardDetail d : details) {
                        ScorecardDetail newDetail = (ScorecardDetail) BeanUtils.cloneBean(d);
                        newDetail.setId(null);
                        newDetail.setPid(newScorecard.getId());//设置子阶的pid
                        newDetail.setStatus("N");
                        newDetail.setSelfScore(null);
                        newDetail.setDeptScore(null);
                        newDetail.setGeneralScore(null);
                        newDetail.setOtherScore(null);
                        newDetail.setCauseScore1(null);
                        newDetail.setCauseScore2(null);
                        newDetail.setSummaryScore1(null);
                        newDetail.setSummaryScore2(null);
                        newDetail.setCreator(userManagedBean.getCurrentUser().getUsername());
                        newDetail.setCredate(queryDateBegin);
                        scorecardDetailBean.persist(newDetail);
                        //参考实际更新目的基准
                        if (updateBenchmark) {
                            cloneActualValue(newDetail, d);
                        }
                    }
                }
            }
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            throw ex;
        }
    }

    public void cloneActualValue(ScorecardDetail a, ScorecardDetail b) {
        //清除实际值
        a.setAq1(null);
        a.setAq2(null);
        a.setAq3(null);
        a.setAq4(null);
        a.setAh1(null);
        a.setAh2(null);
        a.setAfy(null);
        //清除目标值
        a.setTq1(null);
        a.setTq2(null);
        a.setTq3(null);
        a.setTq4(null);
        a.setTh1(null);
        a.setTh2(null);
        a.setTfy(null);
        //清除说明
        a.setFq1(null);
        a.setFq2(null);
        a.setFq3(null);
        a.setFq4(null);
        a.setFh1(null);
        a.setFh2(null);
        a.setFfy(null);
        //清除得分
        a.setPq1(null);
        a.setPq2(null);
        a.setPq3(null);
        a.setPq4(null);
        a.setPh1(null);
        a.setPh2(null);
        a.setPfy(null);
        //清除最终得分
        a.setSq1(BigDecimal.ZERO);
        a.setSq2(BigDecimal.ZERO);
        a.setSq3(BigDecimal.ZERO);
        a.setSq4(BigDecimal.ZERO);
        a.setSh1(BigDecimal.ZERO);
        a.setSh2(BigDecimal.ZERO);
        a.setSfy(BigDecimal.ZERO);
        //初始系数为0.9
        a.setCq1(BigDecimal.valueOf(0.9));
        a.setCq2(BigDecimal.valueOf(0.9));
        a.setCq3(BigDecimal.valueOf(0.9));
        a.setCq4(BigDecimal.valueOf(0.9));
        a.setCh1(BigDecimal.valueOf(0.9));
        a.setCh2(BigDecimal.valueOf(0.9));
        a.setCfy(BigDecimal.valueOf(0.9));
        //去年实绩为新年的基准
        a.setBq1(b.getAq1());
        a.setBq2(b.getAq2());
        a.setBq3(b.getAq3());
        a.setBq4(b.getAq4());
        a.setBh1(b.getAh1());
        a.setBh2(b.getAh2());
        a.setBfy(b.getAfy());
        scorecardDetailBean.update(a);
    }

    public boolean isUpdateBenchmark() {
        return updateBenchmark;
    }

    public void setUpdateBenchmark(boolean updateBenchmark) {
        this.updateBenchmark = updateBenchmark;
    }

    public Calendar getLastC() {
        return lastC;
    }

    public void setLastC(Calendar lastC) {
        this.lastC = lastC;
    }

    public Calendar getNewC() {
        return newC;
    }

    public void setNewC(Calendar newC) {
        this.newC = newC;
    }
    

}
