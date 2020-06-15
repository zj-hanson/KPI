/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.web;

import cn.hanbell.kpi.ejb.IndicatorAnalysisBean;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.ejb.IndicatorSummaryBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorAnalysis;
import cn.hanbell.kpi.entity.IndicatorChart;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.IndicatorSummary;
import cn.hanbell.kpi.entity.RoleGrantModule;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
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
public abstract class BscChartManagedBean extends SuperQueryBean<Indicator> {

    @EJB
    protected IndicatorBean indicatorBean;
    @EJB
    protected IndicatorChartBean indicatorChartBean;
    @EJB
    protected IndicatorAnalysisBean indicatorAnalysisBean;
    @EJB
    protected IndicatorSummaryBean indicatorSummaryBean;

    protected Indicator indicator;
    protected IndicatorChart indicatorChart;
    protected IndicatorDetail actualAccumulated;
    protected IndicatorDetail benchmarkAccumulated;
    protected IndicatorDetail targetAccumulated;
    protected IndicatorDetail AP;
    protected IndicatorDetail BG;
    protected IndicatorDetail AG;

    protected final DecimalFormat decimalFormat;
    protected final DecimalFormat percentFormat;
    protected LineChartModel chartModel;
    protected LineChartModel accumulatedChartModel;

    protected List<IndicatorAnalysis> analysisList;
    protected List<IndicatorSummary> summaryList;
    protected int analysisCount;
    protected int summaryCount;

    protected int y;
    protected int m;

    protected int scale;

    public BscChartManagedBean() {
        super(Indicator.class);
        this.decimalFormat = new DecimalFormat("#,###");
        this.percentFormat = new DecimalFormat("#0.00％");
    }

    @PostConstruct
    @Override
    public void construct() {
        fc = FacesContext.getCurrentInstance();
        ec = fc.getExternalContext();
        if (!fc.isPostback()) {
            Calendar c = Calendar.getInstance();
            c.setTime(userManagedBean.getBaseDate());
            y = c.get(Calendar.YEAR);
            m = c.get(Calendar.MONTH) + 1;
            this.init();
        }
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
            case "Q":
                if (getIndicator().getActualIndicator().getNq1().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("Q1", getIndicator().getActualIndicator().getNq1().doubleValue());
                }
                if (getIndicator().getActualIndicator().getNq2().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("Q2", getIndicator().getActualIndicator().getNq2().doubleValue());
                }
                if (getIndicator().getActualIndicator().getNq3().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("Q3", getIndicator().getActualIndicator().getNq3().doubleValue());
                }
                if (getIndicator().getActualIndicator().getNq4().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("Q4", getIndicator().getActualIndicator().getNq4().doubleValue());
                }
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

    public LineChartModel initLineChartModel(String xTitle, String yTitle) {
        Axis yAxis;
        getChartModel().setSeriesColors("33FF66,FF6633,0000EE");//自定义颜色
        getChartModel().getAxes().put(AxisType.X, new CategoryAxis(xTitle));
        yAxis = getChartModel().getAxis(AxisType.Y);
        yAxis.setLabel(Objects.equals(getIndicator().getUnit(), "") ? yTitle : yTitle + "(" + getIndicator().getUnit() + ")");
        return getChartModel();
    }

    public LineChartModel initAccumulatedLineChartModel(String xTitle, String yTitle) {
        Axis yAxis;
        accumulatedChartModel.setSeriesColors("33FF66,FF6633,0000EE");//自定义颜色
        accumulatedChartModel.getAxes().put(AxisType.X, new CategoryAxis(xTitle));
        yAxis = accumulatedChartModel.getAxis(AxisType.Y);
        yAxis.setLabel(Objects.equals(getIndicator().getUnit(), "") ? yTitle : yTitle + "(" + getIndicator().getUnit() + ")");
        return accumulatedChartModel;
    }

    /**
     *
     * @param a
     * @param b
     * @return a/b
     */
    public BigDecimal getValueA(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) != 0) {
            return a.divide(b, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d));
        } else {
            if (a.compareTo(BigDecimal.ZERO) != 0) {
                return BigDecimal.valueOf(100d);
            } else {
                return BigDecimal.ZERO;
            }
        }
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return (a/b - c)/c
     */
    public BigDecimal getValueA(BigDecimal a, BigDecimal b, BigDecimal c) {
        BigDecimal d = getValueA(a, b);
        if (c.compareTo(BigDecimal.ZERO) != 0) {
            return d.divide(c, 4, RoundingMode.HALF_UP).subtract(BigDecimal.ONE).multiply(BigDecimal.valueOf(100d));
        } else {
            if (d.compareTo(BigDecimal.ZERO) != 0) {
                return BigDecimal.valueOf(100d);
            } else {
                return BigDecimal.ZERO;
            }
        }
    }

    /**
     * @return the indicator
     */
    public Indicator getIndicator() {
        return indicator;
    }

    public String format(BigDecimal value) {
        if (value == null) {
            return "";
        } else {
            return decimalFormat.format(value);
        }
    }

    public String format(BigDecimal value, int i) {
        if (value == null) {
            return "";
        } else if (i <= m) {
            return decimalFormat.format(value);
        } else if (value.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        } else {
            return decimalFormat.format(value);
        }
    }

    public String doubleformat(BigDecimal value) {
        if (value == null) {
            return "";
        } else {
            return percentFormat.format(value);
        }
    }

    public String doubleformat(BigDecimal value, int i) {
        if (value == null) {
            return "";
        } else if (i <= m) {
            return percentFormat.format(value);
        } else if (value.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        } else {
            return percentFormat.format(value);
        }
    }

    public String percentFormat(BigDecimal value, int i) {
        if (value == null) {
            return "";
        } else if (i <= m) {
            return indicatorBean.percentFormat(value);
        } else if (value.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        } else {
            return indicatorBean.percentFormat(value);
        }
    }

    /**
     * @return the indicatorChart
     */
    public IndicatorChart getIndicatorChart() {
        return indicatorChart;
    }

    /**
     * @return the actualAccumulated
     */
    public IndicatorDetail getActualAccumulated() {
        return actualAccumulated;
    }

    /**
     * @return the benchmarkAccumulated
     */
    public IndicatorDetail getBenchmarkAccumulated() {
        return benchmarkAccumulated;
    }

    /**
     * @return the targetAccumulated
     */
    public IndicatorDetail getTargetAccumulated() {
        return targetAccumulated;
    }

    /**
     * @return the AP
     */
    public IndicatorDetail getAP() {
        return AP;
    }

    /**
     * @return the BG
     */
    public IndicatorDetail getBG() {
        return BG;
    }

    /**
     * @return the AG
     */
    public IndicatorDetail getAG() {
        return AG;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the m
     */
    public int getM() {
        return m;
    }

    /**
     * @return the scale
     */
    public int getScale() {
        return scale;
    }

    /**
     * @param scale the scale to set
     */
    public void setScale(int scale) {
        this.scale = scale;
        if (scale == 2) {
            this.decimalFormat.applyPattern("###,##0.00");
            this.construct();
        } else {
            this.decimalFormat.applyPattern("###,###");
            this.construct();
        }
    }

    /**
     * @return the chartModel
     */
    public LineChartModel getChartModel() {
        return chartModel;
    }

    /**
     * @return the accumulativeChartModel
     */
    public LineChartModel getAccumulatedChartModel() {
        return accumulatedChartModel;
    }

    /**
     * @return the analysisList
     */
    public List<IndicatorAnalysis> getAnalysisList() {
        return analysisList;
    }

    /**
     * @return the summaryList
     */
    public List<IndicatorSummary> getSummaryList() {
        return summaryList;
    }

    /**
     * @return the analysisCount
     */
    public int getAnalysisCount() {
        return analysisCount;
    }

    /**
     * @return the summaryCount
     */
    public int getSummaryCount() {
        return summaryCount;
    }

}
