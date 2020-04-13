/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.web.BscChartManagedBean;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "materialsProjectReportBean")
@ViewScoped
public class MaterialsProjectReportBean extends BscChartManagedBean {

    /**
     * Creates a new instance of ShipmentReportBean
     */
    public MaterialsProjectReportBean() {

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
        } else {
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
        }

        ChartSeries a = new ChartSeries();
        a.setLabel("实际");
        switch (getIndicator().getFormkind()) {
            case "M":
                if (getIndicator().getActualIndicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M01", getIndicator().getActualIndicator().getN01().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M02", getIndicator().getActualIndicator().getN02().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M03", getIndicator().getActualIndicator().getN03().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M04", getIndicator().getActualIndicator().getN04().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M05", getIndicator().getActualIndicator().getN05().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M06", getIndicator().getActualIndicator().getN06().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M07", getIndicator().getActualIndicator().getN07().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M08", getIndicator().getActualIndicator().getN08().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M09", getIndicator().getActualIndicator().getN09().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M10", getIndicator().getActualIndicator().getN10().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M11", getIndicator().getActualIndicator().getN11().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M12", getIndicator().getActualIndicator().getN12().doubleValue());
                }
                break;
        }

        ChartSeries b = new ChartSeries();
        b.setLabel("同期");
        switch (getIndicator().getFormkind()) {
            case "M":
                if (getIndicator().getBenchmarkIndicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
                    b.set("M01", getIndicator().getBenchmarkIndicator().getN01().doubleValue());
                }
                if (getIndicator().getBenchmarkIndicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
                    b.set("M02", getIndicator().getBenchmarkIndicator().getN02().doubleValue());
                }
                if (getIndicator().getBenchmarkIndicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
                    b.set("M03", getIndicator().getBenchmarkIndicator().getN03().doubleValue());
                }
                if (getIndicator().getBenchmarkIndicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
                    b.set("M04", getIndicator().getBenchmarkIndicator().getN04().doubleValue());
                }
                if (getIndicator().getBenchmarkIndicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
                    b.set("M05", getIndicator().getBenchmarkIndicator().getN05().doubleValue());
                }
                if (getIndicator().getBenchmarkIndicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
                    b.set("M06", getIndicator().getBenchmarkIndicator().getN06().doubleValue());
                }
                if (getIndicator().getBenchmarkIndicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
                    b.set("M07", getIndicator().getBenchmarkIndicator().getN07().doubleValue());
                }
                if (getIndicator().getBenchmarkIndicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
                    b.set("M08", getIndicator().getBenchmarkIndicator().getN08().doubleValue());
                }
                if (getIndicator().getBenchmarkIndicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
                    b.set("M09", getIndicator().getBenchmarkIndicator().getN09().doubleValue());
                }
                if (getIndicator().getBenchmarkIndicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
                    b.set("M10", getIndicator().getBenchmarkIndicator().getN10().doubleValue());
                }
                if (getIndicator().getBenchmarkIndicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
                    b.set("M11", getIndicator().getBenchmarkIndicator().getN11().doubleValue());
                }
                if (getIndicator().getBenchmarkIndicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
                    b.set("M12", getIndicator().getBenchmarkIndicator().getN12().doubleValue());
                }
                break;
        }

        getChartModel().addSeries(t);//目标
        getChartModel().addSeries(b);//同期
        getChartModel().addSeries(a);//实际
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

    public BigDecimal divideIndicator(BigDecimal o5, BigDecimal o6) {
        return o5.subtract(o6).divide(o5, 6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    @Override
    public LineChartModel initLineChartModel(String xTitle, String yTitle) {
        Axis yAxis;
        getChartModel().setSeriesColors("33FF66,FF6633,0000EE");//自定义颜色
        getChartModel().getAxes().put(AxisType.X, new CategoryAxis(xTitle));
        yAxis = getChartModel().getAxis(AxisType.Y);
        yAxis.setLabel(Objects.equals(getIndicator().getUnit(), "") ? yTitle : yTitle + "(%)");

        return getChartModel();
    }

}
