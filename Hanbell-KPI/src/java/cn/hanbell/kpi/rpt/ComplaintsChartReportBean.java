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
@ManagedBean(name = "complaintsChartReportBean")
@ViewScoped
public class ComplaintsChartReportBean extends BscChartManagedBean {

    /**
     * Creates a new instance of ShipmentReportBean
     */
    public ComplaintsChartReportBean() {

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
            Logger.getLogger("bscReportManagedBean").log(Level.SEVERE, null, ex);
        }

        chartModel = new LineChartModel();
        ChartSeries t = new ChartSeries();
        t.setLabel("3MIS");
        switch (getIndicator().getFormkind()) {
            case "M":
                if (getIndicator().getOther2Indicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M01", getIndicator().getOther2Indicator().getN01().doubleValue());
                }
                if (getIndicator().getOther2Indicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M02", getIndicator().getOther2Indicator().getN02().doubleValue());
                }
                if (getIndicator().getOther2Indicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M03", getIndicator().getOther2Indicator().getN03().doubleValue());
                }
                if (getIndicator().getOther2Indicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M04", getIndicator().getOther2Indicator().getN04().doubleValue());
                }
                if (getIndicator().getOther2Indicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M05", getIndicator().getOther2Indicator().getN05().doubleValue());
                }
                if (getIndicator().getOther2Indicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M06", getIndicator().getOther2Indicator().getN06().doubleValue());
                }
                if (getIndicator().getOther2Indicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M07", getIndicator().getOther2Indicator().getN07().doubleValue());
                }
                if (getIndicator().getOther2Indicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M08", getIndicator().getOther2Indicator().getN08().doubleValue());
                }
                if (getIndicator().getOther2Indicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M09", getIndicator().getOther2Indicator().getN09().doubleValue());
                }
                if (getIndicator().getOther2Indicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M10", getIndicator().getOther2Indicator().getN10().doubleValue());
                }
                if (getIndicator().getOther2Indicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M11", getIndicator().getOther2Indicator().getN11().doubleValue());
                }
                if (getIndicator().getOther2Indicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M12", getIndicator().getOther2Indicator().getN12().doubleValue());
                }
                break;
            case "Q":
                if (getIndicator().getOther2Indicator().getNq1().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("Q1", getIndicator().getOther2Indicator().getNq1().doubleValue());
                }
                if (getIndicator().getOther2Indicator().getNq2().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("Q2", getIndicator().getOther2Indicator().getNq2().doubleValue());
                }
                if (getIndicator().getOther2Indicator().getNq3().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("Q3", getIndicator().getOther2Indicator().getNq3().doubleValue());
                }
                if (getIndicator().getOther2Indicator().getNq4().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("Q4", getIndicator().getOther2Indicator().getNq4().doubleValue());
                }
                break;
        }

        ChartSeries a = new ChartSeries();
        a.setLabel("6MIS");
        switch (getIndicator().getFormkind()) {
            case "M":
                if (getIndicator().getOther4Indicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M01", getIndicator().getOther4Indicator().getN01().doubleValue());
                }
                if (getIndicator().getOther4Indicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M02", getIndicator().getOther4Indicator().getN02().doubleValue());
                }
                if (getIndicator().getOther4Indicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M03", getIndicator().getOther4Indicator().getN03().doubleValue());
                }
                if (getIndicator().getOther4Indicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M04", getIndicator().getOther4Indicator().getN04().doubleValue());
                }
                if (getIndicator().getOther4Indicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M05", getIndicator().getOther4Indicator().getN05().doubleValue());
                }
                if (getIndicator().getOther4Indicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M06", getIndicator().getOther4Indicator().getN06().doubleValue());
                }
                if (getIndicator().getOther4Indicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M07", getIndicator().getOther4Indicator().getN07().doubleValue());
                }
                if (getIndicator().getOther4Indicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M08", getIndicator().getOther4Indicator().getN08().doubleValue());
                }
                if (getIndicator().getOther4Indicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M09", getIndicator().getOther4Indicator().getN09().doubleValue());
                }
                if (getIndicator().getOther4Indicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M10", getIndicator().getOther4Indicator().getN10().doubleValue());
                }
                if (getIndicator().getOther4Indicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M11", getIndicator().getOther4Indicator().getN11().doubleValue());
                }
                if (getIndicator().getOther4Indicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M12", getIndicator().getOther4Indicator().getN12().doubleValue());
                }
                break;
            case "Q":
                if (getIndicator().getOther4Indicator().getNq1().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("Q1", getIndicator().getOther4Indicator().getNq1().doubleValue());
                }
                if (getIndicator().getOther4Indicator().getNq2().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("Q2", getIndicator().getOther4Indicator().getNq2().doubleValue());
                }
                if (getIndicator().getOther4Indicator().getNq3().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("Q3", getIndicator().getOther4Indicator().getNq3().doubleValue());
                }
                if (getIndicator().getOther4Indicator().getNq4().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("Q4", getIndicator().getOther4Indicator().getNq4().doubleValue());
                }
                break;
        }

        ChartSeries f = new ChartSeries();
        f.setLabel("12MIS");
        switch (getIndicator().getFormkind()) {
            case "M":
                if (getIndicator().getOther6Indicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M01", getIndicator().getOther6Indicator().getN01().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M02", getIndicator().getOther6Indicator().getN02().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M03", getIndicator().getOther6Indicator().getN03().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M04", getIndicator().getOther6Indicator().getN04().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M05", getIndicator().getOther6Indicator().getN05().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M06", getIndicator().getOther6Indicator().getN06().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M07", getIndicator().getOther6Indicator().getN07().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M08", getIndicator().getOther6Indicator().getN08().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M09", getIndicator().getOther6Indicator().getN09().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M10", getIndicator().getOther6Indicator().getN10().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M11", getIndicator().getOther6Indicator().getN11().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("M12", getIndicator().getOther6Indicator().getN12().doubleValue());
                }
                break;
            case "Q":
                if (getIndicator().getOther6Indicator().getNq1().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("Q1", getIndicator().getOther6Indicator().getNq1().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getNq2().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("Q2", getIndicator().getOther6Indicator().getNq2().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getNq3().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("Q3", getIndicator().getOther6Indicator().getNq3().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getNq4().compareTo(BigDecimal.ZERO) != 0) {
                    f.set("Q4", getIndicator().getOther6Indicator().getNq4().doubleValue());
                }
                break;
        }

        getChartModel().addSeries(t);//3mis
        getChartModel().addSeries(a);//6mis
        getChartModel().addSeries(f);//12mis
        getChartModel().setTitle(getIndicator().getName());
        getChartModel().setLegendPosition("e");
        getChartModel().setShowPointLabels(true);
        getChartModel().setBreakOnNull(true);
    }
}
