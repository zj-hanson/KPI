/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.IndicatorSet;
import cn.hanbell.kpi.web.BscSheetManagedBean;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "freeserveReportBean")
@ViewScoped
public class FreeServiceReportBean extends BscSheetManagedBean {

    public FreeServiceReportBean() {
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
            if (o1.getSortid() < o2.getSortid()) {
                return -1;
            } else {
                return 1;
            }
        });

        for (Indicator e : indicatorList) {
            //按换算率计算结果
            this.divideByRate(e, 2);
        }

        //计算产品合计
        sumIndicator = indicatorBean.getSumValue(indicatorList);
        //合计本月达成
        indicatorBean.updatePerformance(sumIndicator);

        sumActualAccumulated = new IndicatorDetail();
        sumActualAccumulated.setParent(sumIndicator);
        sumActualAccumulated.setType("A");

        sumTargetAccumulated = new IndicatorDetail();
        sumTargetAccumulated.setParent(sumIndicator);
        sumTargetAccumulated.setType("T");

        sumAP = new IndicatorDetail();
        sumAP.setParent(sumIndicator);
        sumAP.setType("P");

        sumAG = new IndicatorDetail();
        sumAG.setParent(sumIndicator);
        sumAG.setType("P");

        mon = indicatorBean.getIndicatorColumn("N", getM());
        BigDecimal v;
        Method setMethod;
        //计算每个指标的累计
        for (Indicator e : indicatorList) {

            actualAccumulated = new IndicatorDetail();
            actualAccumulated.setParent(e);
            actualAccumulated.setType("A");

            targetAccumulated = new IndicatorDetail();
            targetAccumulated.setParent(e);
            targetAccumulated.setType("T");

            AP = new IndicatorDetail();
            AP.setParent(e);
            AP.setType("P");

            AG = new IndicatorDetail();
            AG.setParent(e);
            AG.setType("P");

            //计算累计
            try {
                for (int i = getM(); i > 0; i--) {
                    //顺序计算的话会导致累计值重复累加
                    //实际值累计
                    v = indicatorBean.getAccumulatedValue(e.getActualIndicator(), i);
                    setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(actualAccumulated, v);
                    //目标值累计
                    v = indicatorBean.getAccumulatedValue(e.getTargetIndicator(), i);
                    setMethod = getTargetAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(targetAccumulated, v);
                    v = indicatorBean.getAccumulatedPerformance(e.getTargetIndicator(), e.getActualIndicator(), i);
                    setMethod = AP.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(AP, v);
                }
                //按当前月份累计值重设全年累计
                f = actualAccumulated.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                actualAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(actualAccumulated).toString())));

                f = targetAccumulated.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                targetAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(targetAccumulated).toString())));

                //M表示月份型
                indicatorBean.addValue(sumActualAccumulated, actualAccumulated, "M");
                indicatorBean.addValue(sumTargetAccumulated, targetAccumulated, "M");
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException ex) {
                log4j.error("bscReportManagedBean", ex);
            }
            e.getTargetIndicator().setType("目标");
            indicatorDetailList.add(e.getTargetIndicator());
            targetAccumulated.setType("目标累计");
            indicatorDetailList.add(targetAccumulated);
            if (e.getOther1Label() != null || e.getOther2Label() != null) {
                if (e.getOther2Label().equals("质量扣款")) {
                    e.getOther1Indicator().setType("质量扣款");
                    indicatorDetailList.add(e.getOther2Indicator());
                }
                if (e.getOther1Label().equals("厂内+厂外")) {
                    e.getOther2Indicator().setType("当月");
                    indicatorDetailList.add(e.getOther1Indicator());
                }
                if (e.getOther1Label().equals("厂内+厂外") || e.getOther2Label().equals("质量扣款")) {
                    e.getActualIndicator().setType("当月合计");
                } else {
                    e.getActualIndicator().setType("当月");
                }
            } else {
                e.getActualIndicator().setType("当月");
            }
            indicatorDetailList.add(e.getActualIndicator());
            actualAccumulated.setType("当月累计");
            indicatorDetailList.add(actualAccumulated);
            e.getPerformanceIndicator().setType("当月控制率");
            indicatorDetailList.add(e.getPerformanceIndicator());
            AP.setType("累计控制率");
            indicatorDetailList.add(AP);

        }
        //产品合计逻辑
        try {
            for (int i = getM(); i > 0; i--) {
                //合计累计达成
                v = indicatorBean.getAccumulatedPerformance(sumIndicator.getTargetIndicator(), sumIndicator.getActualIndicator(), i);
                setMethod = sumAP.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(sumAP, v);
            }
            //按当前月份累计值重设全年累计
            f = sumTargetAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            sumTargetAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(sumTargetAccumulated).toString())));

            f = sumActualAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            sumActualAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(sumActualAccumulated).toString())));

            sumIndicator.getTargetIndicator().setType("目标");
            indicatorDetailList.add(sumIndicator.getTargetIndicator());
            sumTargetAccumulated.setType("目标累计");
            indicatorDetailList.add(sumTargetAccumulated);
            sumIndicator.getActualIndicator().setType("实际");
            indicatorDetailList.add(sumIndicator.getActualIndicator());
            sumActualAccumulated.setType("实际累计");
            indicatorDetailList.add(sumActualAccumulated);
            sumIndicator.getPerformanceIndicator().setType("当月控制率");
            indicatorDetailList.add(sumIndicator.getPerformanceIndicator());
            sumAP.setType("累计控制率");
            indicatorDetailList.add(sumAP);

            //根据指标ID加载指标说明、指标分析
            analysisList = indicatorAnalysisBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标分析
            if (analysisList != null) {
                this.analysisCount = analysisList.size();
            }
            summaryList = indicatorSummaryBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标说明
            if (summaryList != null) {
                this.summaryCount = summaryList.size();
            }

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException ex) {
            log4j.error("bscReportManagedBean", ex);
        }
    }

    @Override
    public String format(String type, BigDecimal value, int i) {
        switch (type) {
            case "当月控制率":
            case "累计控制率":
                return percentFormat(value, i);
            default:
                return format(value, i);
        }
    }

    private void divideByRate(Indicator i, int scale) {
        divideByRate(i.getActualIndicator(), i.getRate(), scale);
        divideByRate(i.getBenchmarkIndicator(), i.getRate(), scale);
        divideByRate(i.getForecastIndicator(), i.getRate(), scale);
        divideByRate(i.getTargetIndicator(), i.getRate(), scale);
        if (i.getOther1Indicator() != null) {
            divideByRate(i.getOther1Indicator(), i.getRate(), scale);
        }
        if (i.getOther2Indicator() != null) {
            divideByRate(i.getOther2Indicator(), i.getRate(), scale);
        }
    }

    private void divideByRate(IndicatorDetail id, BigDecimal rate, int scale) {
        //先算汇总字段再算每月字段,A和S类型会重算汇总
        id.setNfy(id.getNfy().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNh2(id.getNh2().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNh1(id.getNh1().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNq4(id.getNq4().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNq3(id.getNq3().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNq2(id.getNq2().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNq1(id.getNq1().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN01(id.getN01().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN02(id.getN02().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN03(id.getN03().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN04(id.getN04().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN05(id.getN05().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN06(id.getN06().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN07(id.getN07().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN08(id.getN08().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN09(id.getN09().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN10(id.getN10().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN11(id.getN11().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN12(id.getN12().divide(rate, scale, RoundingMode.HALF_UP));
    }

}
