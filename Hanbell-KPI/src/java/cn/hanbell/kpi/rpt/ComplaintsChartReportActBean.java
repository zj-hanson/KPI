/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.web.BscChartManagedBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author C1749
 */
@ManagedBean(name = "complaintsChartReportActBean")
@ViewScoped
public class ComplaintsChartReportActBean extends BscChartManagedBean {

    public ComplaintsChartReportActBean() {
        super();
    }

    @Override
    public void init() {
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String id = request.getParameter("id");
        if (id == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        indicatorChart = indicatorChartBean.findById(Integer.valueOf(id));
        if (indicatorChart == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }else {
            for (RoleGrantModule m : userManagedBean.getRoleGrantDeptList()) {
                if (m.getDeptno().equals(indicatorChart.getPid())) {
                    deny = false;
                }
            }
        }
        indicator = indicatorBean.findByFormidYearAndDeptno(indicatorChart.getFormid(), getY(), indicatorChart.getDeptno());
        if (indicator == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        //按换算率计算结果
        indicatorBean.divideByRate(indicator, 2);

        actualAccumulated = new IndicatorDetail();
        actualAccumulated.setParent(indicator);
        actualAccumulated.setType("A");

        benchmarkAccumulated = new IndicatorDetail();
        benchmarkAccumulated.setParent(indicator);
        benchmarkAccumulated.setType("B");

        targetAccumulated = new IndicatorDetail();
        targetAccumulated.setParent(indicator);
        targetAccumulated.setType("T");

        AP = new IndicatorDetail();
        AP.setParent(indicator);
        AP.setType("P");

        BG = new IndicatorDetail();
        BG.setParent(indicator);
        BG.setType("P");

        AG = new IndicatorDetail();
        AG.setParent(indicator);
        AG.setType("P");

        try {
            for (int i = 1; i <= getM(); i++) {
                BigDecimal v;
                Method setMethod;
                //实际值累计
                v = indicatorBean.getAccumulatedValue(indicator.getActualIndicator(), i);
                setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(actualAccumulated, v);
                //同期值累计
                v = indicatorBean.getAccumulatedValue(indicator.getBenchmarkIndicator(), i);
                setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(benchmarkAccumulated, v);
                //目标值累计
                v = indicatorBean.getAccumulatedValue(indicator.getTargetIndicator(), i);
                setMethod = getTargetAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(targetAccumulated, v);
                //累计达成
                v = indicatorBean.getAccumulatedPerformance(indicator.getActualIndicator(), indicator.getTargetIndicator(), i);
                setMethod = AP.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(AP, v);
                //同比成长率
                v = indicatorBean.getGrowth(indicator.getActualIndicator(), indicator.getBenchmarkIndicator(), i);
                setMethod = BG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(BG, v);
                //累计成长率
                v = indicatorBean.getGrowth(actualAccumulated, benchmarkAccumulated, i);
                setMethod = AG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(AG, v);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            log4j.error("bscReportManagedBean", ex);
        }

        chartModel = new LineChartModel();
        ChartSeries t = new ChartSeries();
        t.setLabel("目标");
        switch (getIndicator().getFormkind()) {
            case "M":
                t.set("M01", getIndicator().getTargetIndicator().getN01().doubleValue());
                t.set("M02", getIndicator().getTargetIndicator().getN02().doubleValue());
                t.set("M03", getIndicator().getTargetIndicator().getN03().doubleValue());
                t.set("M04", getIndicator().getTargetIndicator().getN04().doubleValue());
                t.set("M05", getIndicator().getTargetIndicator().getN05().doubleValue());
                t.set("M06", getIndicator().getTargetIndicator().getN06().doubleValue());
                t.set("M07", getIndicator().getTargetIndicator().getN07().doubleValue());
                t.set("M08", getIndicator().getTargetIndicator().getN08().doubleValue());
                t.set("M09", getIndicator().getTargetIndicator().getN09().doubleValue());
                t.set("M10", getIndicator().getTargetIndicator().getN10().doubleValue());
                t.set("M11", getIndicator().getTargetIndicator().getN11().doubleValue());
                t.set("M12", getIndicator().getTargetIndicator().getN12().doubleValue());
                break;
            case "Q":
                t.set("Q1", getIndicator().getTargetIndicator().getNq1().doubleValue());
                t.set("Q2", getIndicator().getTargetIndicator().getNq2().doubleValue());
                t.set("Q3", getIndicator().getTargetIndicator().getNq3().doubleValue());
                t.set("Q4", getIndicator().getTargetIndicator().getNq4().doubleValue());
                break;
        }

        ChartSeries a = new ChartSeries();
        a.setLabel("实际");
        switch (getIndicator().getFormkind()) {
            case "M":
                if (1 <= m) {
                    a.set("M01", getIndicator().getActualIndicator().getN01().doubleValue());
                }
                if (2 <= m) {
                    a.set("M02", getIndicator().getActualIndicator().getN02().doubleValue());
                }
                if (3 <= m) {
                    a.set("M03", getIndicator().getActualIndicator().getN03().doubleValue());
                }
                if (4 <= m) {
                    a.set("M04", getIndicator().getActualIndicator().getN04().doubleValue());
                }
                if (5 <= m) {
                    a.set("M05", getIndicator().getActualIndicator().getN05().doubleValue());
                }
                if (6 <= m) {
                    a.set("M06", getIndicator().getActualIndicator().getN06().doubleValue());
                }
                if (7 <= m) {
                    a.set("M07", getIndicator().getActualIndicator().getN07().doubleValue());
                }
                if (8 <= m) {
                    a.set("M08", getIndicator().getActualIndicator().getN08().doubleValue());
                }
                if (9 <= m) {
                    a.set("M09", getIndicator().getActualIndicator().getN09().doubleValue());
                }
                if (10 <= m) {
                    a.set("M10", getIndicator().getActualIndicator().getN10().doubleValue());
                }
                if (11 <= m) {
                    a.set("M11", getIndicator().getActualIndicator().getN11().doubleValue());
                }
                if (12 <= m) {
                    a.set("M12", getIndicator().getActualIndicator().getN12().doubleValue());
                }
                break;
            case "Q":
                a.set("Q1", getIndicator().getActualIndicator().getNq1().doubleValue());
                a.set("Q2", getIndicator().getActualIndicator().getNq2().doubleValue());
                a.set("Q3", getIndicator().getActualIndicator().getNq3().doubleValue());
                a.set("Q4", getIndicator().getActualIndicator().getNq4().doubleValue());
                break;
        }

        ChartSeries b = new ChartSeries();
        b.setLabel("同期");
        switch (getIndicator().getFormkind()) {
            case "M":
                b.set("M01", getIndicator().getBenchmarkIndicator().getN01().doubleValue());
                b.set("M02", getIndicator().getBenchmarkIndicator().getN02().doubleValue());
                b.set("M03", getIndicator().getBenchmarkIndicator().getN03().doubleValue());
                b.set("M04", getIndicator().getBenchmarkIndicator().getN04().doubleValue());
                b.set("M05", getIndicator().getBenchmarkIndicator().getN05().doubleValue());
                b.set("M06", getIndicator().getBenchmarkIndicator().getN06().doubleValue());
                b.set("M07", getIndicator().getBenchmarkIndicator().getN07().doubleValue());
                b.set("M08", getIndicator().getBenchmarkIndicator().getN08().doubleValue());
                b.set("M09", getIndicator().getBenchmarkIndicator().getN09().doubleValue());
                b.set("M10", getIndicator().getBenchmarkIndicator().getN10().doubleValue());
                b.set("M11", getIndicator().getBenchmarkIndicator().getN11().doubleValue());
                b.set("M12", getIndicator().getBenchmarkIndicator().getN12().doubleValue());
                break;
            case "Q":
                b.set("Q1", getIndicator().getBenchmarkIndicator().getNq1().doubleValue());
                b.set("Q2", getIndicator().getBenchmarkIndicator().getNq2().doubleValue());
                b.set("Q3", getIndicator().getBenchmarkIndicator().getNq3().doubleValue());
                b.set("Q4", getIndicator().getBenchmarkIndicator().getNq4().doubleValue());
                break;
        }

        ChartSeries f = new ChartSeries();
        f.setLabel("预测");
        switch (getIndicator().getFormkind()) {
            case "M":
                if (getIndicator().getForecastIndicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M01", getIndicator().getForecastIndicator().getN01().doubleValue());
                }
                if (getIndicator().getForecastIndicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M02", getIndicator().getForecastIndicator().getN02().doubleValue());
                }
                if (getIndicator().getForecastIndicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M03", getIndicator().getForecastIndicator().getN03().doubleValue());
                }
                if (getIndicator().getForecastIndicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M04", getIndicator().getForecastIndicator().getN04().doubleValue());
                }
                if (getIndicator().getForecastIndicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M05", getIndicator().getForecastIndicator().getN05().doubleValue());
                }
                if (getIndicator().getForecastIndicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M06", getIndicator().getForecastIndicator().getN06().doubleValue());
                }
                if (getIndicator().getForecastIndicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M07", getIndicator().getForecastIndicator().getN07().doubleValue());
                }
                if (getIndicator().getForecastIndicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M08", getIndicator().getForecastIndicator().getN08().doubleValue());
                }
                if (getIndicator().getForecastIndicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M09", getIndicator().getForecastIndicator().getN09().doubleValue());
                }
                if (getIndicator().getForecastIndicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M10", getIndicator().getForecastIndicator().getN10().doubleValue());
                }
                if (getIndicator().getForecastIndicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M11", getIndicator().getForecastIndicator().getN11().doubleValue());
                }
                if (getIndicator().getForecastIndicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M12", getIndicator().getForecastIndicator().getN12().doubleValue());
                }
                break;
            case "Q":
                if (getIndicator().getForecastIndicator().getNq1().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("Q1", getIndicator().getForecastIndicator().getNq1().doubleValue());
                }
                if (getIndicator().getForecastIndicator().getNq2().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("Q2", getIndicator().getForecastIndicator().getNq2().doubleValue());
                }
                if (getIndicator().getForecastIndicator().getNq3().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("Q3", getIndicator().getForecastIndicator().getNq3().doubleValue());
                }
                if (getIndicator().getForecastIndicator().getNq4().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("Q4", getIndicator().getForecastIndicator().getNq4().doubleValue());
                }
                break;
        }

        getChartModel().addSeries(t);//目标
        getChartModel().addSeries(b);//同期
        getChartModel().addSeries(a);//实际
        getChartModel().addSeries(f);//预测
        getChartModel().setTitle(getIndicator().getName());
        getChartModel().setLegendPosition("e");
        getChartModel().setShowPointLabels(true);
        getChartModel().setBreakOnNull(true);

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
}
