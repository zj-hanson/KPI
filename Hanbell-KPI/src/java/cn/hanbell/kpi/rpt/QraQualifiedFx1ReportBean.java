/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorAnalysis;
import cn.hanbell.kpi.entity.IndicatorSummary;
import cn.hanbell.kpi.web.BscChartManagedBean;
import java.math.BigDecimal;
import java.util.List;
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
@ManagedBean(name = "qraQualifiedFx1ReportBean")
@ViewScoped
public class QraQualifiedFx1ReportBean extends BscChartManagedBean {

    protected Indicator sunIndicator;
    protected LineChartModel sunChartModel;
    protected List<IndicatorAnalysis> analysisList1;
    protected List<IndicatorSummary> summaryList1;
    protected int analysisCount1;
    protected int summaryCount1;

    public QraQualifiedFx1ReportBean() {

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
        String sunfromid = indicator.getAssociatedIndicator();
        if (sunfromid != null && !"".equals(sunfromid)) {
            sunIndicator = indicatorBean.findByFormidYearAndDeptno(sunfromid, getY(), indicatorChart.getDeptno());
        }

        //按换算率计算结果
        indicatorBean.divideByRate(indicator, 2);
        indicatorBean.divideByRate(sunIndicator, 2);
        //总实际平均
        BigDecimal avg1 = indicatorBean.getAccumulatedValue(indicator.getActualIndicator(), m).divide(BigDecimal.valueOf(m), 2);
        getIndicator().getActualIndicator().setNfy(avg1);
        //精工实际平均
        BigDecimal avg2 = indicatorBean.getAccumulatedValue(sunIndicator.getActualIndicator(), m).divide(BigDecimal.valueOf(m), 2);
        getSunIndicator().getActualIndicator().setNfy(avg2);

        chartModel = new LineChartModel();
        ChartSeries b = new ChartSeries();
        b.setLabel((y - 1) + "三次元总");
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
        a.setLabel(y + "三次元总");
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
//三次元精工
        sunChartModel = new LineChartModel();
        ChartSeries sb = new ChartSeries();
        sb.setLabel((y - 1) + "三次元精工");
        switch (getSunIndicator().getFormkind()) {
            case "M":
                sb.set("M01", getSunIndicator().getBenchmarkIndicator().getN01().doubleValue());
                sb.set("M02", getSunIndicator().getBenchmarkIndicator().getN02().doubleValue());
                sb.set("M03", getSunIndicator().getBenchmarkIndicator().getN03().doubleValue());
                sb.set("M04", getSunIndicator().getBenchmarkIndicator().getN04().doubleValue());
                sb.set("M05", getSunIndicator().getBenchmarkIndicator().getN05().doubleValue());
                sb.set("M06", getSunIndicator().getBenchmarkIndicator().getN06().doubleValue());
                sb.set("M07", getSunIndicator().getBenchmarkIndicator().getN07().doubleValue());
                sb.set("M08", getSunIndicator().getBenchmarkIndicator().getN08().doubleValue());
                sb.set("M09", getSunIndicator().getBenchmarkIndicator().getN09().doubleValue());
                sb.set("M10", getSunIndicator().getBenchmarkIndicator().getN10().doubleValue());
                sb.set("M11", getSunIndicator().getBenchmarkIndicator().getN11().doubleValue());
                sb.set("M12", getIndicator().getBenchmarkIndicator().getN12().doubleValue());
                break;
        }

        ChartSeries sa = new ChartSeries();
        sa.setLabel(y + "三次元精工");
        switch (getSunIndicator().getFormkind()) {
            case "M":
                if (getSunIndicator().getActualIndicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
                    sa.set("M01", getSunIndicator().getActualIndicator().getN01().doubleValue());
                }
                if (getSunIndicator().getActualIndicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
                    sa.set("M02", getSunIndicator().getActualIndicator().getN02().doubleValue());
                }
                if (getSunIndicator().getActualIndicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
                    sa.set("M03", getSunIndicator().getActualIndicator().getN03().doubleValue());
                }
                if (getSunIndicator().getActualIndicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
                    sa.set("M04", getSunIndicator().getActualIndicator().getN04().doubleValue());
                }
                if (getSunIndicator().getActualIndicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
                    sa.set("M05", getSunIndicator().getActualIndicator().getN05().doubleValue());
                }
                if (getSunIndicator().getActualIndicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
                    sa.set("M06", getSunIndicator().getActualIndicator().getN06().doubleValue());
                }
                if (getSunIndicator().getActualIndicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
                    sa.set("M07", getSunIndicator().getActualIndicator().getN07().doubleValue());
                }
                if (getSunIndicator().getActualIndicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
                    sa.set("M08", getSunIndicator().getActualIndicator().getN08().doubleValue());
                }
                if (getSunIndicator().getActualIndicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
                    sa.set("M09", getSunIndicator().getActualIndicator().getN09().doubleValue());
                }
                if (getSunIndicator().getActualIndicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
                    sa.set("M10", getSunIndicator().getActualIndicator().getN10().doubleValue());
                }
                if (getSunIndicator().getActualIndicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
                    sa.set("M11", getSunIndicator().getActualIndicator().getN11().doubleValue());
                }
                if (getSunIndicator().getActualIndicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
                    sa.set("M12", getSunIndicator().getActualIndicator().getN12().doubleValue());
                }
                break;
        }

        getChartModel().addSeries(b);//三次元总基准
        getChartModel().addSeries(a);//三次元总实际
        getChartModel().setTitle(getIndicator().getName());
        getChartModel().setLegendPosition("e");
        getChartModel().setShowPointLabels(true);
        getChartModel().setBreakOnNull(true);
        
        getSunChartModel().addSeries(sb);//三次元精工基准
        getSunChartModel().addSeries(sa);//三次元精工实际
        getSunChartModel().setTitle(getSunIndicator().getName());
        getSunChartModel().setLegendPosition("e");
        getSunChartModel().setShowPointLabels(true);
        getSunChartModel().setBreakOnNull(true);

        //三次元总根据指标ID加载指标说明、指标分析
        analysisList = indicatorAnalysisBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标分析
        if (analysisList != null) {
            this.analysisCount = analysisList.size();
        }
        summaryList = indicatorSummaryBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标说明
        if (summaryList != null) {
            this.summaryCount = summaryList.size();
        }

        //三次元精工根据指标ID加载指标说明、指标分析
        analysisList1 = indicatorAnalysisBean.findByPIdAndMonth(sunIndicator.getId(), this.getM());//指标分析
        if (analysisList1 != null) {
            this.analysisCount1 = analysisList1.size();
        }
        summaryList1 = indicatorSummaryBean.findByPIdAndMonth(sunIndicator.getId(), this.getM());//指标说明
        if (summaryList1 != null) {
            this.summaryCount1 = summaryList1.size();
        }

    }

    @Override
    public LineChartModel initLineChartModel(String xTitle, String yTitle) {
        Axis yAxis;
        getChartModel().setSeriesColors("FF6633,0000EE");//自定义颜色
        getChartModel().getAxes().put(AxisType.X, new CategoryAxis(xTitle));
        yAxis = getChartModel().getAxis(AxisType.Y);
        yAxis.setLabel(Objects.equals(getIndicator().getUnit(), "") ? yTitle : yTitle + "(" + getIndicator().getUnit() + ")");

        return getChartModel();
    }

    public LineChartModel initLineChartModelSun(String xTitle, String yTitle) {
        Axis yAxis;
        getSunChartModel().setSeriesColors("FF6633,0000EE");//自定义颜色
        getSunChartModel().getAxes().put(AxisType.X, new CategoryAxis(xTitle));
        yAxis = getSunChartModel().getAxis(AxisType.Y);
        yAxis.setLabel(Objects.equals(getSunIndicator().getUnit(), "") ? yTitle : yTitle + "(" + getSunIndicator().getUnit() + ")");

        return getSunChartModel();
    }

    /**
     * @return the sunIndicator
     */
    public Indicator getSunIndicator() {
        return sunIndicator;
    }

    /**
     * @return the sunChartModel
     */
    public LineChartModel getSunChartModel() {
        return sunChartModel;
    }

    /**
     * @return the analysisList1
     */
    public List<IndicatorAnalysis> getAnalysisList1() {
        return analysisList1;
    }

    /**
     * @return the summaryList1
     */
    public List<IndicatorSummary> getSummaryList1() {
        return summaryList1;
    }

    /**
     * @return the analysisCount1
     */
    public int getAnalysisCount1() {
        return analysisCount1;
    }

    /**
     * @return the summaryCount1
     */
    public int getSummaryCount1() {
        return summaryCount1;
    }

}
