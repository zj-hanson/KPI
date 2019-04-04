/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.web.BscChartManagedBean;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Objects;
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
@ManagedBean(name = "complaintsChartReportBean")
@ViewScoped
public class ComplaintsChartReportBean extends BscChartManagedBean {

    protected IndicatorDetail threeMIS;
    protected IndicatorDetail threeMISRatio;
    protected IndicatorDetail sixMIS;
    protected IndicatorDetail sixMISRatio;
    protected IndicatorDetail twelveMIS;
    protected IndicatorDetail twelveMISRatio;
    protected IndicatorDetail period;

    protected LineChartModel chartMIS;
    protected boolean hasMIS;

    public ComplaintsChartReportBean() {
    }

    @Override
    public void init() {
        //获取基础图表数据
        super.init();
        //计算移动平均MIS
        String formid = indicator.getAssociatedIndicator();
        if (formid != null && !"".equals(formid)) {
            Indicator lastMIS = indicatorBean.findByFormidYearAndDeptno(formid, y - 1, indicator.getDeptno());
            Indicator thisMIS = indicatorBean.findByFormidYearAndDeptno(formid, y, indicator.getDeptno());
            if (lastMIS != null && thisMIS != null) {
                hasMIS = true;
                String mon;
                BigDecimal v;
                Field f;
                Method setMethod;
                int j;
                threeMIS = new IndicatorDetail();
                threeMIS.setParent(thisMIS);
                threeMIS.setType("A");
                threeMISRatio = new IndicatorDetail();
                threeMISRatio.setParent(thisMIS);
                threeMISRatio.setType("P");
                sixMIS = new IndicatorDetail();
                sixMIS.setParent(thisMIS);
                sixMIS.setType("A");
                sixMISRatio = new IndicatorDetail();
                sixMISRatio.setParent(thisMIS);
                sixMISRatio.setType("P");
                twelveMIS = new IndicatorDetail();
                twelveMIS.setParent(thisMIS);
                twelveMIS.setType("A");
                twelveMISRatio = new IndicatorDetail();
                twelveMISRatio.setParent(thisMIS);
                twelveMISRatio.setType("P");
                period = new IndicatorDetail();
                period.setParent(thisMIS);
                period.setType("P");
                try {
                    j = 12 - getM();
                    //今年向后位移
                    for (int i = getM(); i > 0; i--) {
                        mon = indicatorBean.getIndicatorColumn("N", i);

                        f = thisMIS.getOther1Indicator().getClass().getDeclaredField(mon);
                        f.setAccessible(true);
                        v = BigDecimal.valueOf(Double.valueOf(f.get(thisMIS.getOther1Indicator()).toString()));
                        setMethod = threeMIS.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i + j).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(threeMIS, v);

                        f = thisMIS.getOther2Indicator().getClass().getDeclaredField(mon);
                        f.setAccessible(true);
                        v = BigDecimal.valueOf(Double.valueOf(f.get(thisMIS.getOther2Indicator()).toString()));
                        setMethod = threeMISRatio.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i + j).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(threeMISRatio, v);

                        f = thisMIS.getOther3Indicator().getClass().getDeclaredField(mon);
                        f.setAccessible(true);
                        v = BigDecimal.valueOf(Double.valueOf(f.get(thisMIS.getOther3Indicator()).toString()));
                        setMethod = sixMIS.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i + j).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(sixMIS, v);

                        f = thisMIS.getOther4Indicator().getClass().getDeclaredField(mon);
                        f.setAccessible(true);
                        v = BigDecimal.valueOf(Double.valueOf(f.get(thisMIS.getOther4Indicator()).toString()));
                        setMethod = sixMISRatio.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i + j).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(sixMISRatio, v);

                        f = thisMIS.getOther5Indicator().getClass().getDeclaredField(mon);
                        f.setAccessible(true);
                        v = BigDecimal.valueOf(Double.valueOf(f.get(thisMIS.getOther5Indicator()).toString()));
                        setMethod = twelveMIS.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i + j).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(twelveMIS, v);

                        f = thisMIS.getOther6Indicator().getClass().getDeclaredField(mon);
                        f.setAccessible(true);
                        v = BigDecimal.valueOf(Double.valueOf(f.get(thisMIS.getOther6Indicator()).toString()));
                        setMethod = twelveMISRatio.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i + j).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(twelveMISRatio, v);
                        //记录移动平均月份
                        setMethod = period.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i + j).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(period, BigDecimal.valueOf(i));
                    }
                    //去年补充位移
                    do {
                        mon = indicatorBean.getIndicatorColumn("N", getM() + j);

                        f = lastMIS.getOther1Indicator().getClass().getDeclaredField(mon);
                        f.setAccessible(true);
                        v = BigDecimal.valueOf(Double.valueOf(f.get(lastMIS.getOther1Indicator()).toString()));
                        setMethod = threeMIS.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", j).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(threeMIS, v);

                        f = lastMIS.getOther2Indicator().getClass().getDeclaredField(mon);
                        f.setAccessible(true);
                        v = BigDecimal.valueOf(Double.valueOf(f.get(lastMIS.getOther2Indicator()).toString()));
                        setMethod = threeMISRatio.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", j).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(threeMISRatio, v);

                        f = lastMIS.getOther3Indicator().getClass().getDeclaredField(mon);
                        f.setAccessible(true);
                        v = BigDecimal.valueOf(Double.valueOf(f.get(lastMIS.getOther3Indicator()).toString()));
                        setMethod = sixMIS.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", j).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(sixMIS, v);

                        f = lastMIS.getOther4Indicator().getClass().getDeclaredField(mon);
                        f.setAccessible(true);
                        v = BigDecimal.valueOf(Double.valueOf(f.get(lastMIS.getOther4Indicator()).toString()));
                        setMethod = sixMISRatio.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", j).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(sixMISRatio, v);

                        f = lastMIS.getOther5Indicator().getClass().getDeclaredField(mon);
                        f.setAccessible(true);
                        v = BigDecimal.valueOf(Double.valueOf(f.get(lastMIS.getOther5Indicator()).toString()));
                        setMethod = twelveMIS.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", j).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(twelveMIS, v);

                        f = lastMIS.getOther6Indicator().getClass().getDeclaredField(mon);
                        f.setAccessible(true);
                        v = BigDecimal.valueOf(Double.valueOf(f.get(lastMIS.getOther6Indicator()).toString()));
                        setMethod = twelveMISRatio.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", j).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(twelveMISRatio, v);
                        //记录移动平均月份
                        setMethod = period.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", j).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(period, BigDecimal.valueOf(getM() + j));

                        j--;
                    } while (j > 0);
                    //产生图表
                    chartMIS = new LineChartModel();
                    ChartSeries mr3 = new ChartSeries();
                    mr3.setLabel("3MIS");
                    switch (getIndicator().getFormkind()) {
                        case "M":
                            mr3.set(period.getN01().intValue() > getM() ? "E" + period.getN01().toString() : period.getN01(), threeMISRatio.getN01().doubleValue());
                            mr3.set(period.getN02(), threeMISRatio.getN02().doubleValue());
                            mr3.set(period.getN03(), threeMISRatio.getN03().doubleValue());
                            mr3.set(period.getN04(), threeMISRatio.getN04().doubleValue());
                            mr3.set(period.getN05(), threeMISRatio.getN05().doubleValue());
                            mr3.set(period.getN06(), threeMISRatio.getN06().doubleValue());
                            mr3.set(period.getN07(), threeMISRatio.getN07().doubleValue());
                            mr3.set(period.getN08(), threeMISRatio.getN08().doubleValue());
                            mr3.set(period.getN09(), threeMISRatio.getN09().doubleValue());
                            mr3.set(period.getN10(), threeMISRatio.getN10().doubleValue());
                            mr3.set(period.getN11(), threeMISRatio.getN11().doubleValue());
                            mr3.set(period.getN12(), threeMISRatio.getN12().doubleValue());
                            break;
                    }
                    ChartSeries mr6 = new ChartSeries();
                    mr6.setLabel("6MIS");
                    switch (getIndicator().getFormkind()) {
                        case "M":
                            mr6.set(period.getN01().intValue() > getM() ? "E" + period.getN01().toString() : period.getN01(), sixMISRatio.getN01().doubleValue());
                            mr6.set(period.getN02(), sixMISRatio.getN02().doubleValue());
                            mr6.set(period.getN03(), sixMISRatio.getN03().doubleValue());
                            mr6.set(period.getN04(), sixMISRatio.getN04().doubleValue());
                            mr6.set(period.getN05(), sixMISRatio.getN05().doubleValue());
                            mr6.set(period.getN06(), sixMISRatio.getN06().doubleValue());
                            mr6.set(period.getN07(), sixMISRatio.getN07().doubleValue());
                            mr6.set(period.getN08(), sixMISRatio.getN08().doubleValue());
                            mr6.set(period.getN09(), sixMISRatio.getN09().doubleValue());
                            mr6.set(period.getN10(), sixMISRatio.getN10().doubleValue());
                            mr6.set(period.getN11(), sixMISRatio.getN11().doubleValue());
                            mr6.set(period.getN12(), sixMISRatio.getN12().doubleValue());
                            break;
                    }
                    ChartSeries mr12 = new ChartSeries();
                    mr12.setLabel("12MIS");
                    switch (getIndicator().getFormkind()) {
                        case "M":
                            mr12.set(period.getN01().intValue() > getM() ? "E" + period.getN01().toString() : period.getN01(), twelveMISRatio.getN02().doubleValue());
                            mr12.set(period.getN02(), twelveMISRatio.getN02().doubleValue());
                            mr12.set(period.getN03(), twelveMISRatio.getN03().doubleValue());
                            mr12.set(period.getN04(), twelveMISRatio.getN04().doubleValue());
                            mr12.set(period.getN05(), twelveMISRatio.getN05().doubleValue());
                            mr12.set(period.getN06(), twelveMISRatio.getN06().doubleValue());
                            mr12.set(period.getN07(), twelveMISRatio.getN07().doubleValue());
                            mr12.set(period.getN08(), twelveMISRatio.getN08().doubleValue());
                            mr12.set(period.getN09(), twelveMISRatio.getN09().doubleValue());
                            mr12.set(period.getN10(), sixMISRatio.getN10().doubleValue());
                            mr12.set(period.getN11(), twelveMISRatio.getN11().doubleValue());
                            mr12.set(period.getN12(), twelveMISRatio.getN12().doubleValue());
                            break;
                    }

                    chartMIS.addSeries(mr3);//3mis
                    chartMIS.addSeries(mr6);//6mis
                    chartMIS.addSeries(mr12);//12mis
                    chartMIS.setTitle(thisMIS.getName());
                    chartMIS.setLegendPosition("e");
                    chartMIS.setShowPointLabels(true);
                    chartMIS.setBreakOnNull(true);
                } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                    log4j.error("ComplaintsChartReportBean Init异常", ex);
                }
            }
        }

    }

    public LineChartModel initLineChartMIS(String xTitle, String yTitle) {
        Axis yAxis;
        chartMIS.setSeriesColors("33FF66,FF6633,0000EE");//自定义颜色
        chartMIS.getAxes().put(AxisType.X, new CategoryAxis(xTitle));
        yAxis = chartMIS.getAxis(AxisType.Y);
        yAxis.setLabel(Objects.equals(getIndicator().getUnit(), "") ? yTitle : yTitle + "(" + getIndicator().getUnit() + ")");

        return chartMIS;
    }

    /**
     * @return the threeMIS
     */
    public IndicatorDetail getThreeMIS() {
        return threeMIS;
    }

    /**
     * @return the threeMISRatio
     */
    public IndicatorDetail getThreeMISRatio() {
        return threeMISRatio;
    }

    /**
     * @return the sixMIS
     */
    public IndicatorDetail getSixMIS() {
        return sixMIS;
    }

    /**
     * @return the sixMISRatio
     */
    public IndicatorDetail getSixMISRatio() {
        return sixMISRatio;
    }

    /**
     * @return the twelveMIS
     */
    public IndicatorDetail getTwelveMIS() {
        return twelveMIS;
    }

    /**
     * @return the twelveMISRatio
     */
    public IndicatorDetail getTwelveMISRatio() {
        return twelveMISRatio;
    }

    /**
     * @return the period
     */
    public IndicatorDetail getPeriod() {
        return period;
    }

    /**
     * @return the chartMIS
     */
    public LineChartModel getChartMIS() {
        return chartMIS;
    }

    /**
     * @return the hasMIS
     */
    public boolean isHasMIS() {
        return hasMIS;
    }

    /**
     * @param hasMIS the hasMIS to set
     */
    public void setHasMIS(boolean hasMIS) {
        this.hasMIS = hasMIS;
    }
}
