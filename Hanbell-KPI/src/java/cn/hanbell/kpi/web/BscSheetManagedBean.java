/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.web;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.ejb.IndicatorSetBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorChart;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.IndicatorSet;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C0160
 */
public abstract class BscSheetManagedBean extends SuperQueryBean<Indicator> {

    @EJB
    protected IndicatorBean indicatorBean;

    @EJB
    protected IndicatorChartBean indicatorChartBean;

    @EJB
    protected IndicatorSetBean indicatorSetBean;

    protected Indicator indicator;
    protected IndicatorChart indicatorChart;
    protected IndicatorDetail actualAccumulated;
    protected IndicatorDetail benchmarkAccumulated;
    protected IndicatorDetail targetAccumulated;
    protected IndicatorDetail AP;
    protected IndicatorDetail BG;
    protected IndicatorDetail AG;
    //合计
    protected Indicator sumIndicator;
    protected IndicatorDetail sumActualAccumulated;
    protected IndicatorDetail sumBenchmarkAccumulated;
    protected IndicatorDetail sumTargetAccumulated;
    protected IndicatorDetail sumAP;
    protected IndicatorDetail sumBG;
    protected IndicatorDetail sumAG;

    protected List<Indicator> indicatorList;
    protected List<IndicatorSet> indicatorSetList;

    protected List<IndicatorDetail> indicatorDetailList;

    protected final DecimalFormat decimalFormat;

    protected int y;
    protected int m;

    public BscSheetManagedBean() {
        super(Indicator.class);
        this.decimalFormat = new DecimalFormat("#,###");
        //初始化对象
        indicatorList = new ArrayList<>();
        indicatorDetailList = new ArrayList<>();
    }

    @PostConstruct
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
        String mon;
        Field f;
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
        indicatorList.clear();
        indicatorDetailList.clear();

        if (indicator.isAssigned()) {
            indicatorList = indicatorBean.findByPId(indicator.getId());
        } else {
            //找到指标相关子阶
            indicatorSetList = indicatorSetBean.findByPId(indicator.getId());
            if (indicatorSetList == null || indicatorSetList.isEmpty()) {
                fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
            }
            for (IndicatorSet is : indicatorSetList) {
                indicator = indicatorBean.findByFormidYearAndDeptno(is.getFormid(), getY(), is.getDeptno());
                if (indicator != null) {
                    indicatorList.add(indicator);
                }
            }
        }
        //指标排序
        indicatorList.sort((Indicator o1, Indicator o2) -> {
            if (o1.getSortid() > o2.getSortid()) {
                return 1;
            } else {
                return -1;
            }
        });

        for (Indicator e : indicatorList) {
            //按换算率计算结果
            indicatorBean.divideByRate(e, 2);
        }

        //计算产品合计
        sumIndicator = indicatorBean.getSumValue(indicatorList);
        //合计本月达成
        indicatorBean.updatePerformance(sumIndicator);

        sumActualAccumulated = new IndicatorDetail();
        sumActualAccumulated.setParent(sumIndicator);
        sumActualAccumulated.setType("A");

        sumBenchmarkAccumulated = new IndicatorDetail();
        sumBenchmarkAccumulated.setParent(sumIndicator);
        sumBenchmarkAccumulated.setType("B");

        sumTargetAccumulated = new IndicatorDetail();
        sumTargetAccumulated.setParent(sumIndicator);
        sumTargetAccumulated.setType("T");

        sumAP = new IndicatorDetail();
        sumAP.setParent(sumIndicator);
        sumAP.setType("P");

        sumBG = new IndicatorDetail();
        sumBG.setParent(sumIndicator);
        sumBG.setType("P");

        sumAG = new IndicatorDetail();
        sumAG.setParent(sumIndicator);
        sumAG.setType("P");

        //计算每个指标的累计
        for (Indicator e : indicatorList) {

            actualAccumulated = new IndicatorDetail();
            actualAccumulated.setParent(e);
            actualAccumulated.setType("A");

            benchmarkAccumulated = new IndicatorDetail();
            benchmarkAccumulated.setParent(e);
            benchmarkAccumulated.setType("B");

            targetAccumulated = new IndicatorDetail();
            targetAccumulated.setParent(e);
            targetAccumulated.setType("T");

            AP = new IndicatorDetail();
            AP.setParent(e);
            AP.setType("P");

            BG = new IndicatorDetail();
            BG.setParent(e);
            BG.setType("P");

            AG = new IndicatorDetail();
            AG.setParent(e);
            AG.setType("P");
            //计算累计
            try {
                BigDecimal v;
                Method setMethod;
                for (int i = getM(); i > 0; i--) {
                    //顺序计算的话会导致累计值重复累加
                    //实际值累计
                    v = indicatorBean.getAccumulatedValue(e.getActualIndicator(), i);
                    setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(actualAccumulated, v);
                    //同期值累计
                    v = indicatorBean.getAccumulatedValue(e.getBenchmarkIndicator(), i);
                    setMethod = getBenchmarkAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(benchmarkAccumulated, v);
                    //目标值累计
                    v = indicatorBean.getAccumulatedValue(e.getTargetIndicator(), i);
                    setMethod = getTargetAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(targetAccumulated, v);
                    //累计达成
                    v = indicatorBean.getAccumulatedPerformance(e.getActualIndicator(), e.getTargetIndicator(), i);
                    setMethod = AP.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(AP, v);
                    //同比成长率
                    v = indicatorBean.getGrowth(e.getActualIndicator(), e.getBenchmarkIndicator(), i);
                    setMethod = BG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(BG, v);
                    //累计成长率
                    v = indicatorBean.getGrowth(actualAccumulated, benchmarkAccumulated, i);
                    setMethod = AG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(AG, v);
                }
                //按当前月份累计值重设全年累计
                mon = indicatorBean.getIndicatorColumn("N", getM());

                f = actualAccumulated.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                actualAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(actualAccumulated).toString())));

                f = benchmarkAccumulated.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                benchmarkAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(benchmarkAccumulated).toString())));

                f = targetAccumulated.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                targetAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(targetAccumulated).toString())));
                //M表示月份型
                indicatorBean.addValue(sumActualAccumulated, actualAccumulated, "M");
                indicatorBean.addValue(sumBenchmarkAccumulated, benchmarkAccumulated, "M");
                indicatorBean.addValue(sumTargetAccumulated, targetAccumulated, "M");

            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger("bscReportManagedBean").log(Level.SEVERE, null, ex);
            } catch (NoSuchFieldException ex) {
                Logger.getLogger(BscSheetManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            e.getTargetIndicator().setType("目标");
            indicatorDetailList.add(e.getTargetIndicator());
            e.getActualIndicator().setType("实际");
            indicatorDetailList.add(e.getActualIndicator());
            e.getPerformanceIndicator().setType("本月达成");
            indicatorDetailList.add(e.getPerformanceIndicator());
            targetAccumulated.setType("目标累计");
            indicatorDetailList.add(targetAccumulated);
            actualAccumulated.setType("实际累计");
            indicatorDetailList.add(actualAccumulated);
            AP.setType("累计达成");
            indicatorDetailList.add(AP);
            e.getBenchmarkIndicator().setType("去年同期");
            indicatorDetailList.add(e.getBenchmarkIndicator());
            benchmarkAccumulated.setType("去年累计");
            indicatorDetailList.add(benchmarkAccumulated);
            BG.setType("同比成长");
            indicatorDetailList.add(BG);
            AG.setType("累计成长");
            indicatorDetailList.add(AG);

        }
        //产品合计逻辑
        try {
            BigDecimal v;
            Method setMethod;
            for (int i = getM(); i > 0; i--) {
                //合计累计达成
                v = indicatorBean.getAccumulatedPerformance(sumIndicator.getActualIndicator(), sumIndicator.getTargetIndicator(), i);
                setMethod = sumAP.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(sumAP, v);
                //合计同期成长
                v = indicatorBean.getGrowth(sumIndicator.getActualIndicator(), sumIndicator.getBenchmarkIndicator(), i);
                setMethod = sumBG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(sumBG, v);
                //合计累计成长
                v = indicatorBean.getGrowth(sumActualAccumulated, sumBenchmarkAccumulated, i);
                setMethod = sumAG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(sumAG, v);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger("bscReportManagedBean").log(Level.SEVERE, null, ex);
        }
        sumIndicator.getTargetIndicator().setType("目标");
        indicatorDetailList.add(sumIndicator.getTargetIndicator());
        sumIndicator.getActualIndicator().setType("实际");
        indicatorDetailList.add(sumIndicator.getActualIndicator());
        sumIndicator.getPerformanceIndicator().setType("本月达成");
        indicatorDetailList.add(sumIndicator.getPerformanceIndicator());
        sumTargetAccumulated.setType("目标累计");
        indicatorDetailList.add(sumTargetAccumulated);
        sumActualAccumulated.setType("实际累计");
        indicatorDetailList.add(sumActualAccumulated);
        sumAP.setType("累计达成");
        indicatorDetailList.add(sumAP);
        sumIndicator.getBenchmarkIndicator().setType("去年同期");
        indicatorDetailList.add(sumIndicator.getBenchmarkIndicator());
        sumBenchmarkAccumulated.setType("同期累计");
        indicatorDetailList.add(sumBenchmarkAccumulated);
        sumBG.setType("同比成长");
        indicatorDetailList.add(sumBG);
        sumAG.setType("累计成长");
        indicatorDetailList.add(sumAG);
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

    public String format(String type, BigDecimal value, int i) {
        switch (type) {
            case "P":
            case "达成":
            case "本月达成":
            case "当月达成":
            case "累计达成":
            case "同比成长":
            case "累计成长":
                return percentFormat(value, i);
            default:
                return format(value, i);
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
     * @param actualAccumulated the actualAccumulated to set
     */
    public void setActualAccumulated(IndicatorDetail actualAccumulated) {
        this.actualAccumulated = actualAccumulated;
    }

    /**
     * @return the benchmarkAccumulated
     */
    public IndicatorDetail getBenchmarkAccumulated() {
        return benchmarkAccumulated;
    }

    /**
     * @param benchmarkAccumulated the benchmarkAccumulated to set
     */
    public void setBenchmarkAccumulated(IndicatorDetail benchmarkAccumulated) {
        this.benchmarkAccumulated = benchmarkAccumulated;
    }

    /**
     * @return the targetAccumulated
     */
    public IndicatorDetail getTargetAccumulated() {
        return targetAccumulated;
    }

    /**
     * @param targetAccumulated the targetAccumulated to set
     */
    public void setTargetAccumulated(IndicatorDetail targetAccumulated) {
        this.targetAccumulated = targetAccumulated;
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
     * @return the indicatorDetailList
     */
    public List<IndicatorDetail> getIndicatorDetailList() {
        return indicatorDetailList;
    }

    /**
     * @param indicatorDetailList the indicatorDetailList to set
     */
    public void setIndicatorDetailList(List<IndicatorDetail> indicatorDetailList) {
        this.indicatorDetailList = indicatorDetailList;
    }

}
