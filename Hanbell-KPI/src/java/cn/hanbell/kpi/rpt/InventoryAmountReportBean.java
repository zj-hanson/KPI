/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.IndicatorSet;
import cn.hanbell.kpi.web.BscSheetManagedBean;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "inventoryAmountBean")
@ViewScoped
public class InventoryAmountReportBean extends BscSheetManagedBean {

    public InventoryAmountReportBean() {
    }

    @Override
    public void init() {
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String id = request.getParameter("id");
        String mon;
        Field f;
        if (id == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        indicatorChart = indicatorChartBean.findById(Integer.valueOf(id));
        if (indicatorChart == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        indicator = indicatorBean.findByFormidYearAndDeptno(indicatorChart.getFormid(), getY(), indicatorChart.getDeptno());
        if (indicator == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        indicatorList.clear();
        indicatorDetailList.clear();

        if (indicator.isAssigned()) {
            indicatorList = indicatorBean.findByPId(indicator.getId());
        } else {
            //找到指标相关子阶
            indicatorSetList = indicatorSetBean.findByPId(indicator.getId());
            if (indicatorSetList == null || indicatorSetList.isEmpty()) {
                fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
            }
            for (IndicatorSet is : indicatorSetList) {
                indicator = indicatorBean.findByFormidYearAndDeptno(is.getFormid(), getY(), is.getDeptno());
                if (indicator != null) {
                    indicatorList.add(indicator);
                }
            }
        }
        //指标排序
        indicatorList.sort((Indicator o1, Indicator o2) -> {
            if (o1.getSortid() > o2.getSortid()) {
                return 1;
            } else {
                return -1;
            }
        });

        //计算产品合计
        sumIndicator = indicatorBean.getSumValue(indicatorList);
        //合计本月达成
        indicatorBean.updatePerformance(sumIndicator);

        sumActualAccumulated = new IndicatorDetail();
        sumActualAccumulated.setParent(sumIndicator);
        sumActualAccumulated.setType("A");

        sumBenchmarkAccumulated = new IndicatorDetail();
        sumBenchmarkAccumulated.setParent(sumIndicator);
        sumBenchmarkAccumulated.setType("B");

        sumTargetAccumulated = new IndicatorDetail();
        sumTargetAccumulated.setParent(sumIndicator);
        sumTargetAccumulated.setType("T");

        sumAP = new IndicatorDetail();
        sumAP.setParent(sumIndicator);
        sumAP.setType("P");

        sumBG = new IndicatorDetail();
        sumBG.setParent(sumIndicator);
        sumBG.setType("P");

        sumAG = new IndicatorDetail();
        sumAG.setParent(sumIndicator);
        sumAG.setType("P");

        //计算每个指标的累计
        for (Indicator e : indicatorList) {

            actualAccumulated = new IndicatorDetail();
            actualAccumulated.setParent(e);
            actualAccumulated.setType("A");

            benchmarkAccumulated = new IndicatorDetail();
            benchmarkAccumulated.setParent(e);
            benchmarkAccumulated.setType("B");

            targetAccumulated = new IndicatorDetail();
            targetAccumulated.setParent(e);
            targetAccumulated.setType("T");

            AP = new IndicatorDetail();
            AP.setParent(e);
            AP.setType("P");

            BG = new IndicatorDetail();
            BG.setParent(e);
            BG.setType("P");

            AG = new IndicatorDetail();
            AG.setParent(e);
            AG.setType("P");
            //计算累计
            try {
                BigDecimal v;
                Method setMethod;
                for (int i = getM(); i > 0; i--) {
                    //同比成长率
                    v = indicatorBean.getGrowth(e.getActualIndicator(), e.getBenchmarkIndicator(), i);
                    setMethod = BG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(BG, v);
                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger("bscReportManagedBean").log(Level.SEVERE, null, ex);
            }
            e.getTargetIndicator().setType("目标");
            indicatorDetailList.add(e.getTargetIndicator());
            e.getActualIndicator().setType("实际");
            indicatorDetailList.add(e.getActualIndicator());
            e.getPerformanceIndicator().setType("控制率");
            indicatorDetailList.add(e.getPerformanceIndicator());
            e.getBenchmarkIndicator().setType("去年同期");
            indicatorDetailList.add(e.getBenchmarkIndicator());
            BG.setType("同期相比");
            indicatorDetailList.add(BG);

        }
        //产品合计逻辑
        try {
            BigDecimal v;
            Method setMethod;
            for (int i = getM(); i > 0; i--) {
                //合计同期成长
                v = indicatorBean.getGrowth(sumIndicator.getActualIndicator(), sumIndicator.getBenchmarkIndicator(), i);
                setMethod = sumBG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(sumBG, v);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger("bscReportManagedBean").log(Level.SEVERE, null, ex);
        }
        sumIndicator.getTargetIndicator().setType("目标");
        indicatorDetailList.add(sumIndicator.getTargetIndicator());
        sumIndicator.getActualIndicator().setType("实际");
        indicatorDetailList.add(sumIndicator.getActualIndicator());
        sumIndicator.getPerformanceIndicator().setType("控制率");
        indicatorDetailList.add(sumIndicator.getPerformanceIndicator());
        sumIndicator.getBenchmarkIndicator().setType("去年同期");
        indicatorDetailList.add(sumIndicator.getBenchmarkIndicator());
        sumBG.setType("同期相比");
        indicatorDetailList.add(sumBG);

        //根据指标ID加载指标说明、指标分析
        analysisList = indicatorAnalysisBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标分析
        if (analysisList != null) {
            this.analysisCount = analysisList.size();
        }
        summaryList = indicatorSummaryBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标说明
        if (summaryList != null) {
            this.summaryCount = summaryList.size();
        }
    }

    @Override
    public String format(String type, BigDecimal value, int i) {
        switch (type) {
            case "控制率":
            case "同期相比":
                return percentFormat(value, i);
            case "去年同期":
                return i > m ? "" : format(value, i);
            default:
                return format(value, i);
        }
    }

    @Override
    public String percentFormat(BigDecimal value, int i) {
        if (value == null) {
            return "";
        } else if (i <= m) {
            return indicatorBean.percentFormat(value);
        } else {
            return "";
        }
    }

}
