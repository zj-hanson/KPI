/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.web.BscChartManagedBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "complaintsChartReportPzkBean")
@ViewScoped
public class ComplaintsChartReportPzkBean extends BscChartManagedBean {

    /**
     * Creates a new instance of ShipmentReportBean
     */
    public ComplaintsChartReportPzkBean() {

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
                v = indicatorBean.getAccumulatedValue(indicator.getOther1Indicator(), i);
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
            Logger.getLogger("bscReportManagedBean").log(Level.SEVERE, null, ex);
        }

        chartModel = new LineChartModel();
        ChartSeries t = new ChartSeries();
        t.setLabel("客诉比率");
        switch (getIndicator().getFormkind()) {
            case "M":
                if (getIndicator().getPerformanceIndicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M01", getIndicator().getPerformanceIndicator().getN01().doubleValue());
                }
                if (getIndicator().getPerformanceIndicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M02", getIndicator().getPerformanceIndicator().getN02().doubleValue());
                }
                if (getIndicator().getPerformanceIndicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M03", getIndicator().getPerformanceIndicator().getN03().doubleValue());
                }
                if (getIndicator().getPerformanceIndicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M04", getIndicator().getPerformanceIndicator().getN04().doubleValue());
                }
                if (getIndicator().getPerformanceIndicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M05", getIndicator().getPerformanceIndicator().getN05().doubleValue());
                }
                if (getIndicator().getPerformanceIndicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M06", getIndicator().getPerformanceIndicator().getN06().doubleValue());
                }
                if (getIndicator().getPerformanceIndicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M07", getIndicator().getPerformanceIndicator().getN07().doubleValue());
                }
                if (getIndicator().getPerformanceIndicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M08", getIndicator().getPerformanceIndicator().getN08().doubleValue());
                }
                if (getIndicator().getPerformanceIndicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M09", getIndicator().getPerformanceIndicator().getN09().doubleValue());
                }
                if (getIndicator().getPerformanceIndicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M10", getIndicator().getPerformanceIndicator().getN10().doubleValue());
                }
                if (getIndicator().getPerformanceIndicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M11", getIndicator().getPerformanceIndicator().getN11().doubleValue());
                }
                if (getIndicator().getPerformanceIndicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M12", getIndicator().getPerformanceIndicator().getN12().doubleValue());
                }
                break;
            case "Q":
                if (getIndicator().getPerformanceIndicator().getNq1().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("Q1", getIndicator().getPerformanceIndicator().getNq1().doubleValue());
                }
                if (getIndicator().getPerformanceIndicator().getNq2().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("Q2", getIndicator().getPerformanceIndicator().getNq2().doubleValue());
                }
                if (getIndicator().getPerformanceIndicator().getNq3().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("Q3", getIndicator().getPerformanceIndicator().getNq3().doubleValue());
                }
                if (getIndicator().getPerformanceIndicator().getNq4().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("Q4", getIndicator().getPerformanceIndicator().getNq4().doubleValue());
                }
                break;
        }
        getChartModel().addSeries(t);//比率
        getChartModel().setTitle(getIndicator().getName());
        getChartModel().setLegendPosition("e");
        getChartModel().setShowPointLabels(true);
        getChartModel().setBreakOnNull(true);
    }
}
