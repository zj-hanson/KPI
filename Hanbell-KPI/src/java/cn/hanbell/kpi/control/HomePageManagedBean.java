/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.web.SuperQueryBean;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "homePageManagedBean")
@ViewScoped
public class HomePageManagedBean extends SuperQueryBean<Indicator> {

    @EJB
    private IndicatorBean indicatorBean;
    private Calendar c = Calendar.getInstance();
    private int y;

    public HomePageManagedBean() {
        super(Indicator.class);
    }

    @PostConstruct
    public void construct() {
        c.setTime(userManagedBean.getBaseDate());
        y = c.get(Calendar.YEAR);
    }

    public LineChartModel initLineChartModel(String id, String objno, String xTitle, String yTitle) {

        Axis yAxis;
        Indicator indicator = indicatorBean.findByFormidYearAndDeptno(id, y, objno);
        LineChartModel chartModel = new LineChartModel();
        ChartSeries t = new ChartSeries();
        t.setLabel("目标");
        t.set("Q1", indicator.getTargetIndicator().getNq1().divide(indicator.getRate(), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        t.set("Q2", indicator.getTargetIndicator().getNq2().divide(indicator.getRate(), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        t.set("Q3", indicator.getTargetIndicator().getNq3().divide(indicator.getRate(), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        t.set("Q4", indicator.getTargetIndicator().getNq4().divide(indicator.getRate(), 2, BigDecimal.ROUND_HALF_UP).doubleValue());

        ChartSeries a = new ChartSeries();
        a.setLabel("实际");
        if (indicator.getActualIndicator().getNq1().compareTo(BigDecimal.ZERO) != 0) {
            a.set("Q1", indicator.getActualIndicator().getNq1().divide(indicator.getRate(), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        if (indicator.getActualIndicator().getNq2().compareTo(BigDecimal.ZERO) != 0) {
            a.set("Q2", indicator.getActualIndicator().getNq2().divide(indicator.getRate(), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        if (indicator.getActualIndicator().getNq3().compareTo(BigDecimal.ZERO) != 0) {
            a.set("Q3", indicator.getActualIndicator().getNq3().divide(indicator.getRate(), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        if (indicator.getActualIndicator().getNq4().compareTo(BigDecimal.ZERO) != 0) {
            a.set("Q4", indicator.getActualIndicator().getNq4().divide(indicator.getRate(), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }

        chartModel.addSeries(t);
        chartModel.addSeries(a);
        chartModel.setTitle(String.valueOf(y) + indicator.getName());
        chartModel.setLegendPosition("e");
        chartModel.setShowPointLabels(true);
        chartModel.setBreakOnNull(true);
        //salesAmountModel.setSeriesColors("33FF66,FF6633");自定义颜色
        chartModel.getAxes().put(AxisType.X, new CategoryAxis(xTitle));
        yAxis = chartModel.getAxis(AxisType.Y);
        yAxis.setLabel(Objects.equals(indicator.getUnit(), "") ? yTitle : yTitle + "(" + indicator.getUnit() + ")");

        return chartModel;
    }

}
