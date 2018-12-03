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
@ManagedBean(name = "qraQualifiedYxReportBean")
@ViewScoped
public class QraQualifiedYxReportBean extends BscChartManagedBean {

    /**
     * Creates a new instance of ShipmentReportBean
     */
    public QraQualifiedYxReportBean() {

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

        //三次元转子抽检实际平均
        BigDecimal avg1=indicatorBean.getAccumulatedValue(indicator.getActualIndicator(), m).divide(BigDecimal.valueOf(m), 2);
        getIndicator().getActualIndicator().setNfy(avg1);
        //三次元转子首件实际平均
        BigDecimal avg2=indicatorBean.getAccumulatedValue(indicator.getOther6Indicator(), m).divide(BigDecimal.valueOf(m), 2);
        getIndicator().getOther6Indicator().setNfy(avg2);
        
        chartModel = new LineChartModel();
        ChartSeries b = new ChartSeries();
        b.setLabel((y - 1) + "三次元转子抽检");
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
        }

        ChartSeries a = new ChartSeries();
        a.setLabel(y + "三次元转子抽检");
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

        ChartSeries o5 = new ChartSeries();
        o5.setLabel((y - 1) + "三次元转子首件");
        switch (getIndicator().getFormkind()) {
            case "M":
                o5.set("M01", getIndicator().getOther5Indicator().getN01().doubleValue());
                o5.set("M02", getIndicator().getOther5Indicator().getN02().doubleValue());
                o5.set("M03", getIndicator().getOther5Indicator().getN03().doubleValue());
                o5.set("M04", getIndicator().getOther5Indicator().getN04().doubleValue());
                o5.set("M05", getIndicator().getOther5Indicator().getN05().doubleValue());
                o5.set("M06", getIndicator().getOther5Indicator().getN06().doubleValue());
                o5.set("M07", getIndicator().getOther5Indicator().getN07().doubleValue());
                o5.set("M08", getIndicator().getOther5Indicator().getN08().doubleValue());
                o5.set("M09", getIndicator().getOther5Indicator().getN09().doubleValue());
                o5.set("M10", getIndicator().getOther5Indicator().getN10().doubleValue());
                o5.set("M11", getIndicator().getOther5Indicator().getN11().doubleValue());
                o5.set("M12", getIndicator().getOther5Indicator().getN12().doubleValue());
                break;
        }

        ChartSeries o6 = new ChartSeries();
        o6.setLabel(y + "三次元转子首件");
        switch (getIndicator().getFormkind()) {
            case "M":
                if (getIndicator().getOther6Indicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
                    o6.set("M01", getIndicator().getOther6Indicator().getN01().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
                    o6.set("M02", getIndicator().getOther6Indicator().getN02().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
                    o6.set("M03", getIndicator().getOther6Indicator().getN03().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
                    o6.set("M04", getIndicator().getOther6Indicator().getN04().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
                    o6.set("M05", getIndicator().getOther6Indicator().getN05().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
                    o6.set("M06", getIndicator().getOther6Indicator().getN06().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
                    o6.set("M07", getIndicator().getOther6Indicator().getN07().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
                    o6.set("M08", getIndicator().getOther6Indicator().getN08().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
                    o6.set("M09", getIndicator().getOther6Indicator().getN09().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
                    o6.set("M10", getIndicator().getOther6Indicator().getN10().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
                    o6.set("M11", getIndicator().getOther6Indicator().getN11().doubleValue());
                }
                if (getIndicator().getOther6Indicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
                    o6.set("M12", getIndicator().getOther6Indicator().getN12().doubleValue());
                }
                break;
        }

        getChartModel().addSeries(b);//三次元转子抽检基准
        getChartModel().addSeries(a);//三次元转子抽检实际
        getChartModel().addSeries(o5);//三次元转子首件基准
        getChartModel().addSeries(o6);//三次元转子首件实际
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

    @Override
    public LineChartModel initLineChartModel(String xTitle, String yTitle) {
        Axis yAxis;
        getChartModel().setSeriesColors("0000EE,FF00FF,FF6633,33FF66");//自定义颜色
        getChartModel().getAxes().put(AxisType.X, new CategoryAxis(xTitle));
        yAxis = getChartModel().getAxis(AxisType.Y);
        yAxis.setLabel(Objects.equals(getIndicator().getUnit(), "") ? yTitle : yTitle + "(" + getIndicator().getUnit() + ")");

        return getChartModel();
    }

}
