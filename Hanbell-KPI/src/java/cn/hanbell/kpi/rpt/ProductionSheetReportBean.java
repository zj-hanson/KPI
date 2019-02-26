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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "productionSheetReportBean")
@ViewScoped
public class ProductionSheetReportBean extends BscSheetManagedBean {

    public ProductionSheetReportBean() {

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

        for (Indicator e : indicatorList) {
            //按换算率计算结果
            indicatorBean.divideByRate(e, 2);
        }

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

        mon = indicatorBean.getIndicatorColumn("N", getM());
        BigDecimal v;
        Method setMethod;
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
                for (int i = getM(); i > 0; i--) {
                    //顺序计算的话会导致累计值重复累加
                    //实际值累计
                    v = indicatorBean.getAccumulatedValue(e.getActualIndicator(), i);
                    setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(actualAccumulated, v);
                    //同期值累计
                    v = indicatorBean.getAccumulatedValue(e.getBenchmarkIndicator(), i);
                    setMethod = getBenchmarkAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(benchmarkAccumulated, v);
                    //目标值累计
                    v = indicatorBean.getAccumulatedValue(e.getTargetIndicator(), i);
                    setMethod = getTargetAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(targetAccumulated, v);
                    //累计达成
                    v = indicatorBean.getAccumulatedPerformance(e.getActualIndicator(), e.getTargetIndicator(), i);
                    setMethod = AP.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(AP, v);
                    //同比成长率
                    v = indicatorBean.getGrowth(e.getActualIndicator(), e.getBenchmarkIndicator(), i);
                    setMethod = BG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(BG, v);
                    //累计成长率
                    v = indicatorBean.getGrowth(actualAccumulated, benchmarkAccumulated, i);
                    setMethod = AG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(AG, v);
                }
                //按当前月份累计值重设全年累计
                f = actualAccumulated.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                actualAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(actualAccumulated).toString())));

                f = benchmarkAccumulated.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                benchmarkAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(benchmarkAccumulated).toString())));

                f = targetAccumulated.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                targetAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(targetAccumulated).toString())));
                //M表示月份型
                indicatorBean.addValue(sumActualAccumulated, actualAccumulated, "M");
                indicatorBean.addValue(sumBenchmarkAccumulated, benchmarkAccumulated, "M");
                indicatorBean.addValue(sumTargetAccumulated, targetAccumulated, "M");

            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException ex) {
                log4j.error("bscReportManagedBean", ex);
            }
            e.getForecastIndicator().setType("营业预测");
            indicatorDetailList.add(e.getForecastIndicator());
            e.getTargetIndicator().setType("制令目标");
            indicatorDetailList.add(e.getTargetIndicator());
            e.getActualIndicator().setType("制令实际");
            indicatorDetailList.add(e.getActualIndicator());
            e.getPerformanceIndicator().setType("本月达成率");
            indicatorDetailList.add(e.getPerformanceIndicator());
            targetAccumulated.setType("制令目标累计");
            indicatorDetailList.add(targetAccumulated);
            actualAccumulated.setType("制令实际累计");
            indicatorDetailList.add(actualAccumulated);
            AP.setType("累计达成率");
            indicatorDetailList.add(AP);
            e.getBenchmarkIndicator().setType("去年同期(制令)");
            indicatorDetailList.add(e.getBenchmarkIndicator());
            benchmarkAccumulated.setType("去年累计(制令)");
            indicatorDetailList.add(benchmarkAccumulated);
            BG.setType("同比增长率");
            indicatorDetailList.add(BG);
            AG.setType("累计增长率");
            indicatorDetailList.add(AG);

        }
        //产品合计逻辑
        try {
            for (int i = getM(); i > 0; i--) {
                //合计累计达成
                v = indicatorBean.getAccumulatedPerformance(sumIndicator.getActualIndicator(), sumIndicator.getTargetIndicator(), i);
                setMethod = sumAP.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(sumAP, v);
                //合计同期成长
                v = indicatorBean.getGrowth(sumIndicator.getActualIndicator(), sumIndicator.getBenchmarkIndicator(), i);
                setMethod = sumBG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(sumBG, v);
                //合计累计成长
                v = indicatorBean.getGrowth(sumActualAccumulated, sumBenchmarkAccumulated, i);
                setMethod = sumAG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(sumAG, v);
            }
            //按当前月份累计值重设全年累计
            f = sumTargetAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            sumTargetAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(sumTargetAccumulated).toString())));

            f = sumActualAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            sumActualAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(sumActualAccumulated).toString())));

            f = sumBenchmarkAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            sumBenchmarkAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(sumBenchmarkAccumulated).toString())));

            sumIndicator.getForecastIndicator().setType("营业预测");
            indicatorDetailList.add(sumIndicator.getForecastIndicator());
            sumIndicator.getTargetIndicator().setType("制令目标");
            indicatorDetailList.add(sumIndicator.getTargetIndicator());
            sumIndicator.getActualIndicator().setType("制令实际");
            indicatorDetailList.add(sumIndicator.getActualIndicator());
            sumIndicator.getPerformanceIndicator().setType("本月达成率");
            indicatorDetailList.add(sumIndicator.getPerformanceIndicator());
            sumTargetAccumulated.setType("制令目标累计");
            indicatorDetailList.add(sumTargetAccumulated);
            sumActualAccumulated.setType("制令实际累计");
            indicatorDetailList.add(sumActualAccumulated);
            sumAP.setType("累计达成率");
            indicatorDetailList.add(sumAP);
            sumIndicator.getBenchmarkIndicator().setType("去年同期(制令)");
            indicatorDetailList.add(sumIndicator.getBenchmarkIndicator());
            sumBenchmarkAccumulated.setType("同期累计(制令)");
            indicatorDetailList.add(sumBenchmarkAccumulated);
            sumBG.setType("同比增长率");
            indicatorDetailList.add(sumBG);
            sumAG.setType("累计增长率");
            indicatorDetailList.add(sumAG);

            //根据指标ID加载指标说明、指标分析
            analysisList = indicatorAnalysisBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标分析
            if (analysisList != null) {
                this.analysisCount = analysisList.size();
            }
            summaryList = indicatorSummaryBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标说明
            if (summaryList != null) {
                this.summaryCount = summaryList.size();
            }

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException ex) {
            log4j.error("bscReportManagedBean", ex);
        }
    }

    @Override
     public String format(String type, BigDecimal value, int i) {
        switch (type) {
            case "P":
            case "本月达成率":
            case "累计达成率":
            case "同比增长率":
            case "累计增长率":
                return percentFormat(value, i);
            default:
                return format(value, i);
        }
    }
    
}
