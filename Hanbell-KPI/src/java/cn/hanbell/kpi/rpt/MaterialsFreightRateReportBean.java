/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.web.BscChartManagedBean;
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
@ManagedBean(name = "materialsFreightRateReportBean")
@ViewScoped
public class MaterialsFreightRateReportBean extends BscChartManagedBean {

    /**
     * Creates a new instance of ShipmentReportBean
     */
    protected Indicator glIndicator;

    public MaterialsFreightRateReportBean() {

    }

    @Override
    public void init() {
        setGlIndicator(new Indicator());
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
        //获得关联指标 格式为： 指标编号;部门
        String deptno, formid;
        String associatedIndicator = indicator.getAssociatedIndicator();
        if (associatedIndicator != null && !"".equals(associatedIndicator)) {
            String[] arr = associatedIndicator.split(";");
            formid = arr[0];
            deptno = arr[1];
            glIndicator = indicatorBean.findByFormidYearAndDeptno(formid, y, deptno);
        }

        //计算实际率
        Double n01, n02, n03, n04, n05, n06, n07, n08, n09, n10, n11, n12;
        if (getGlIndicator().getActualIndicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
            n01 = getDouble(getGlIndicator().getActualIndicator().getN01().doubleValue(), getIndicator().getOther1Indicator().getN01().doubleValue());
            getIndicator().getActualIndicator().setN01(BigDecimal.valueOf(n01));
        } else {
            n01 = 0.0;
            getIndicator().getActualIndicator().setN01(BigDecimal.ZERO);
        }
        if (getGlIndicator().getActualIndicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
            n02 = getDouble(getGlIndicator().getActualIndicator().getN02().doubleValue(), getIndicator().getOther1Indicator().getN02().doubleValue());
            getIndicator().getActualIndicator().setN02(BigDecimal.valueOf(n02));
        } else {
            n02 = 0.0;
            getIndicator().getActualIndicator().setN02(BigDecimal.ZERO);
        }
        if (getGlIndicator().getActualIndicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
            n03 = getDouble(getGlIndicator().getActualIndicator().getN03().doubleValue(), getIndicator().getOther1Indicator().getN03().doubleValue());
            getIndicator().getActualIndicator().setN03(BigDecimal.valueOf(n03));
        } else {
            n03 = 0.0;
            getIndicator().getActualIndicator().setN03(BigDecimal.ZERO);
        }
        if (getGlIndicator().getActualIndicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
            n04 = getDouble(getGlIndicator().getActualIndicator().getN04().doubleValue(), getIndicator().getOther1Indicator().getN04().doubleValue());
            getIndicator().getActualIndicator().setN04(BigDecimal.valueOf(n04));
        } else {
            n04 = 0.0;
            getIndicator().getActualIndicator().setN04(BigDecimal.ZERO);
        }
        if (getGlIndicator().getActualIndicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
            n05 = getDouble(getGlIndicator().getActualIndicator().getN05().doubleValue(), getIndicator().getOther1Indicator().getN05().doubleValue());
            getIndicator().getActualIndicator().setN05(BigDecimal.valueOf(n05));
        } else {
            n05 = 0.0;
            getIndicator().getActualIndicator().setN05(BigDecimal.ZERO);
        }
        if (getGlIndicator().getActualIndicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
            n06 = getDouble(getGlIndicator().getActualIndicator().getN06().doubleValue(), getIndicator().getOther1Indicator().getN06().doubleValue());
            getIndicator().getActualIndicator().setN06(BigDecimal.valueOf(n06));
        } else {
            n06 = 0.0;
            getIndicator().getActualIndicator().setN06(BigDecimal.ZERO);
        }
        if (getGlIndicator().getActualIndicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
            n07 = getDouble(getGlIndicator().getActualIndicator().getN07().doubleValue(), getIndicator().getOther1Indicator().getN07().doubleValue());
            getIndicator().getActualIndicator().setN07(BigDecimal.valueOf(n07));
        } else {
            n07 = 0.0;
            getIndicator().getActualIndicator().setN07(BigDecimal.ZERO);
        }
        if (getGlIndicator().getActualIndicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
            n08 = getDouble(getGlIndicator().getActualIndicator().getN08().doubleValue(), getIndicator().getOther1Indicator().getN08().doubleValue());
            getIndicator().getActualIndicator().setN08(BigDecimal.valueOf(n08));
        } else {
            n08 = 0.0;
            getIndicator().getActualIndicator().setN08(BigDecimal.ZERO);
        }
        if (getGlIndicator().getActualIndicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
            n09 = getDouble(getGlIndicator().getActualIndicator().getN09().doubleValue(), getIndicator().getOther1Indicator().getN09().doubleValue());
            getIndicator().getActualIndicator().setN09(BigDecimal.valueOf(n09));
        } else {
            n09 = 0.0;
            getIndicator().getActualIndicator().setN09(BigDecimal.ZERO);
        }
        if (getGlIndicator().getActualIndicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
            n10 = getDouble(getGlIndicator().getActualIndicator().getN10().doubleValue(), getIndicator().getOther1Indicator().getN10().doubleValue());
            getIndicator().getActualIndicator().setN10(BigDecimal.valueOf(n10));
        } else {
            n10 = 0.0;
            getIndicator().getActualIndicator().setN10(BigDecimal.ZERO);
        }
        if (getGlIndicator().getActualIndicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
            n11 = getDouble(getGlIndicator().getActualIndicator().getN11().doubleValue(), getIndicator().getOther1Indicator().getN11().doubleValue());
            getIndicator().getActualIndicator().setN11(BigDecimal.valueOf(n11));
        } else {
            n11 = 0.0;
            getIndicator().getActualIndicator().setN11(BigDecimal.ZERO);
        }
        if (getGlIndicator().getActualIndicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
            n12 = getDouble(getGlIndicator().getActualIndicator().getN12().doubleValue(), getIndicator().getOther1Indicator().getN12().doubleValue());
            getIndicator().getActualIndicator().setN12(BigDecimal.valueOf(n12));
        } else {
            n12 = 0.0;
            getIndicator().getActualIndicator().setN12(BigDecimal.ZERO);
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
                if (n01 != 0) {
                    a.set("M01", n01);
                }
                if (n02 != 0) {
                    a.set("M02", n02);
                }
                if (n03 != 0) {
                    a.set("M03", n03);
                }
                if (n04 != 0) {
                    a.set("M04", n04);
                }
                if (n05 != 0) {
                    a.set("M05", n05);
                }
                if (n06 != 0) {
                    a.set("M06", n06);
                }
                if (n07 != 0) {
                    a.set("M07", n07);
                }
                if (n08 != 0) {
                    a.set("M08", n08);
                }
                if (n09 != 0) {
                    a.set("M09", n09);
                }
                if (n10 != 0) {
                    a.set("M10", n10);
                }
                if (n11 != 0) {
                    a.set("M11", n11);
                }
                if (n12 != 0) {
                    a.set("M12", n12);
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

    private Double getDouble(Double one, Double two) {
        return (two / one) * 10000;
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

    /**
     * @return the glIndicator
     */
    public Indicator getGlIndicator() {
        return glIndicator;
    }

    /**
     * @param glIndicator the glIndicator to set
     */
    public void setGlIndicator(Indicator glIndicator) {
        this.glIndicator = glIndicator;
    }

}
