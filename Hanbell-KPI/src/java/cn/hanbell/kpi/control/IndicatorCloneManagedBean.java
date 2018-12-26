/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorDepartmentBean;
import cn.hanbell.kpi.ejb.IndicatorSetBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.IndicatorSet;
import cn.hanbell.kpi.lazy.IndicatorModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "indicatorCloneManagedBean")
@SessionScoped
public class IndicatorCloneManagedBean extends SuperSingleBean<Indicator> {

    @EJB
    private IndicatorBean indicatorBean;
    @EJB
    private IndicatorDepartmentBean indicatorDepartmentBean;
    @EJB
    private IndicatorSetBean indicatorSetBean;

    private boolean updateBenchmark;

    Calendar lastC;
    Calendar newC;

    /**
     * Creates a new instance of IndicatorCloneManagedBean
     */
    public IndicatorCloneManagedBean() {
        super(Indicator.class);
        lastC = Calendar.getInstance();
        newC = Calendar.getInstance();
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
        if (indicatorBean.getRowCount(newC.get(Calendar.YEAR)) != 0) {
            showErrorMsg("Error", "目的年度已有资料");
            return false;
        }
        return true;
    }

    @Override
    public void init() {
        this.superEJB = indicatorBean;
        this.model = new IndicatorModel(indicatorBean, userManagedBean);
        super.init();
    }

    @Override
    public void verify() {
        try {
            if (!doBeforeVerify()) {
                return;
            }
            int lastY = lastC.get(Calendar.YEAR);
            int newY = newC.get(Calendar.YEAR);
            List<Indicator> indicatorList;
            //复制产品指标
            indicatorList = indicatorBean.findRootByCompany(userManagedBean.getCompany(), "P", lastY);
            if (indicatorList == null || indicatorList.isEmpty()) {
                showErrorMsg("Error", "获取产品参考指标失败");
                return;
            }
            for (Indicator e : indicatorList) {
                initIndicator(e, newY);
            }
            //复制部门指标
            indicatorList = indicatorBean.findRootByCompany(userManagedBean.getCompany(), "D", lastY);
            if (indicatorList == null || indicatorList.isEmpty()) {
                showErrorMsg("Error", "获取部门参考指标失败");
                return;
            }
            for (Indicator e : indicatorList) {
                initIndicator(e, newY);
            }
            showInfoMsg("Info", "指标复制成功");
        } catch (Exception ex) {
            showErrorMsg("Error", ex.toString());
        }
    }

    protected void initIndicator(Indicator refIndicator, int y) throws Exception {
        try {
            if (refIndicator.isAssigned()) {
                //Assigned理论上不会有IndicatorSet
                List<Indicator> details = indicatorBean.findByPId(refIndicator.getId());
                if (details != null && !details.isEmpty()) {
                    for (Indicator d : details) {
                        initIndicator(d, y);
                    }
                }//先生成子阶，再生成父阶
                Indicator newIndicator = (Indicator) BeanUtils.cloneBean(refIndicator);
                newIndicator.setId(null);
                newIndicator.setSeq(y);//设置新的年度
                if (newIndicator.getHasOther() > 0) {
                    newIndicator.setOther1Indicator(null);
                    newIndicator.setOther2Indicator(null);
                    newIndicator.setOther3Indicator(null);
                    newIndicator.setOther4Indicator(null);
                    newIndicator.setOther5Indicator(null);
                    newIndicator.setOther6Indicator(null);
                }
                newIndicator.setActualIndicator(null);
                newIndicator.setBenchmarkIndicator(null);
                newIndicator.setForecastIndicator(null);
                newIndicator.setTargetIndicator(null);
                newIndicator.setPerformanceIndicator(null);
                newIndicator.setStatus("N");
                newIndicator.setCreator(userManagedBean.getCurrentUser().getUsername());
                newIndicator.setCredateToNow();
                indicatorBean.persist(newIndicator);
                //参考实际更新目的基准
                if (updateBenchmark) {
                    IndicatorDetail refA = refIndicator.getActualIndicator();
                    IndicatorDetail newB = newIndicator.getBenchmarkIndicator();
                    newB.setParent(newIndicator);
                    indicatorBean.addValue(newB, refA, newIndicator.getFormkind());
                    indicatorBean.update(newIndicator);
                }
                //找到新的子阶,重设新的PId
                List<Indicator> newDetails = indicatorBean.findByPIdAndYear(refIndicator.getId(), y);
                if (newDetails != null && !newDetails.isEmpty()) {
                    for (Indicator nc : newDetails) {
                        nc.setPid(newIndicator.getId());
                    }
                    indicatorBean.update(newDetails);
                }
            } else {
                Indicator newIndicator = (Indicator) BeanUtils.cloneBean(refIndicator);
                newIndicator.setId(null);
                newIndicator.setSeq(y);//设置新的年度
                if (newIndicator.getHasOther() > 0) {
                    newIndicator.setOther1Indicator(null);
                    newIndicator.setOther2Indicator(null);
                    newIndicator.setOther3Indicator(null);
                    newIndicator.setOther4Indicator(null);
                    newIndicator.setOther5Indicator(null);
                    newIndicator.setOther6Indicator(null);
                }
                newIndicator.setActualIndicator(null);
                newIndicator.setBenchmarkIndicator(null);
                newIndicator.setForecastIndicator(null);
                newIndicator.setTargetIndicator(null);
                newIndicator.setPerformanceIndicator(null);
                newIndicator.setStatus("N");
                newIndicator.setCreator(userManagedBean.getCurrentUser().getUsername());
                newIndicator.setCredateToNow();
                indicatorBean.persist(newIndicator);
                //参考实际更新目的基准
                if (updateBenchmark) {
                    IndicatorDetail refA = refIndicator.getActualIndicator();
                    IndicatorDetail newB = newIndicator.getBenchmarkIndicator();
                    newB.setParent(newIndicator);
                    indicatorBean.addValue(newB, refA, newIndicator.getFormkind());
                    indicatorBean.update(newIndicator);
                }
                //处理指标归集
                List<IndicatorSet> setList = indicatorSetBean.findByPId(refIndicator.getId());
                if (setList != null && !setList.isEmpty()) {
                    for (IndicatorSet is : setList) {
                        IndicatorSet newIS = (IndicatorSet) BeanUtils.cloneBean(is);
                        newIS.setId(null);
                        newIS.setPid(newIndicator.getId());
                        indicatorSetBean.persist(newIS);
                    }
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @return the updateBenchmark
     */
    public boolean isUpdateBenchmark() {
        return updateBenchmark;
    }

    /**
     * @param updateBenchmark the updateBenchmark to set
     */
    public void setUpdateBenchmark(boolean updateBenchmark) {
        this.updateBenchmark = updateBenchmark;
    }

}
