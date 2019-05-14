/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.IndicatorSet;
import cn.hanbell.kpi.web.BscSheetManagedBean;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "freeServiceReportBean")
@ViewScoped
public class FreeServiceReportBean extends BscSheetManagedBean {

    protected Indicator glIndicator;
    protected IndicatorDetail glActualAccumulated;

    protected IndicatorDetail sumOther1Accumulated;
    protected IndicatorDetail sumOther2Accumulated;
    protected IndicatorDetail sumOther4Accumulated;
    protected IndicatorDetail sumOther5Accumulated;
    //R合计
    protected Indicator RsumIndicator;
    protected List<Indicator> RindicatorList;
    protected IndicatorDetail RsumActualAccumulated;
    protected IndicatorDetail RsumTargetAccumulated;
    protected IndicatorDetail RsumOther1Accumulated;
    protected IndicatorDetail RsumOther2Accumulated;
    protected IndicatorDetail RsumAP;

    public FreeServiceReportBean() {
    }

    @Override
    public void init() {
        Boolean Other1 = false;
        Boolean Other2 = false;
        Boolean Other4 = false;
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
        //获得关联指标 格式为： 指标编号;部门
        String deptno, formid;
        String associatedIndicator = indicator.getAssociatedIndicator();
        if (associatedIndicator != null && !"".equals(associatedIndicator)) {
            String[] arr = associatedIndicator.split(";");
            formid = arr[0];
            deptno = arr[1];
            glIndicator = indicatorBean.findByFormidYearAndDeptno(formid, y, deptno);

            divideByRate(glIndicator, 2);
            glActualAccumulated = new IndicatorDetail();
            glActualAccumulated.setParent(glIndicator);
            glActualAccumulated.setType("A");
            try {
                for (int i = 1; i <= getM(); i++) {
                    BigDecimal v;
                    Method setMethod;
                    //实际值累计
                    v = indicatorBean.getAccumulatedValue(glIndicator.getActualIndicator(), i);
                    setMethod = getGlActualAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(glActualAccumulated, v);
                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                log4j.error("freeServiceReportBean", ex);
            }
        }

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
        sumIndicator = getSumValue(indicatorList);
        //合计本月达成
        indicatorBean.updatePerformance(sumIndicator);

        sumActualAccumulated = new IndicatorDetail();
        sumActualAccumulated.setParent(sumIndicator);
        sumActualAccumulated.setType("A");

        sumTargetAccumulated = new IndicatorDetail();
        sumTargetAccumulated.setParent(sumIndicator);
        sumTargetAccumulated.setType("T");

        sumOther1Accumulated = new IndicatorDetail();
        sumOther1Accumulated.setParent(sumIndicator);
        sumOther1Accumulated.setType("O1");

        sumOther2Accumulated = new IndicatorDetail();
        sumOther2Accumulated.setParent(sumIndicator);
        sumOther2Accumulated.setType("O2");

        sumOther4Accumulated = new IndicatorDetail();
        sumOther4Accumulated.setParent(sumIndicator);
        sumOther4Accumulated.setType("O4");

        setSumOther5Accumulated(new IndicatorDetail());
        getSumOther5Accumulated().setParent(sumIndicator);
        getSumOther5Accumulated().setType("O5");

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
        int lji = 0;
        RindicatorList = new ArrayList<>();
        for (Indicator e : indicatorList) {
            lji++;
            if (lji < 7) {
                RindicatorList.add(e);
            }
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
            Other1 = false;
            Other2 = false;
            Other4 = false;
            e.getTargetIndicator().setType("目标");
            indicatorDetailList.add(e.getTargetIndicator());
            if (e.getOther1Label() != null || e.getOther2Label() != null) {
                if (e.getOther1Label().equals("厂内+厂外")) {
                    e.getOther1Indicator().setType("当月");
                    indicatorDetailList.add(e.getOther1Indicator());
                    Other1 = true;
                }
                if (e.getOther2Label().equals("质量扣款")) {
                    e.getOther2Indicator().setType("质量扣款");
                    indicatorDetailList.add(e.getOther2Indicator());
                    Other2 = true;
                }
                if (e.getOther1Label().equals("厂内+厂外") || e.getOther2Label().equals("质量扣款")) {
                    e.getActualIndicator().setType("当月合计");
                } else if (e.getOther4Label() != null && e.getOther5Label() != null && e.getOther4Label().equals("质量扣款") && e.getOther5Label().equals("合计")) {
                    e.getOther5Indicator().setType("当月");
                    indicatorDetailList.add(e.getOther5Indicator());
                    e.getOther4Indicator().setType("质量扣款");
                    indicatorDetailList.add(e.getOther4Indicator());
                    Other4 = true;
                    e.getActualIndicator().setType("当月合计");
                } else {
                    e.getActualIndicator().setType("当月");
                }
            } else {
                e.getActualIndicator().setType("当月");
            }
            indicatorDetailList.add(e.getActualIndicator());
            e.getPerformanceIndicator().setType("当月控制");
            indicatorDetailList.add(e.getPerformanceIndicator());
            targetAccumulated.setType("目标累计");
            indicatorDetailList.add(targetAccumulated);
            actualAccumulated.setType("当月累计");
            indicatorDetailList.add(actualAccumulated);
            AP.setType("累计控制");
            indicatorDetailList.add(AP);
            //重庆综合服务后加上合计
            if (lji == 6 && e.getFormid().equals("A-重庆综合成本")) {
                //计算产品合计
                RsumIndicator = getSumValue(RindicatorList);
                RsumIndicator.setName("R冷媒合计");
                //合计本月达成
                indicatorBean.updatePerformance(RsumIndicator);

                RsumActualAccumulated = new IndicatorDetail();
                RsumActualAccumulated.setParent(RsumIndicator);
                RsumActualAccumulated.setType("A");

                RsumTargetAccumulated = new IndicatorDetail();
                RsumTargetAccumulated.setParent(RsumIndicator);
                RsumTargetAccumulated.setType("T");

                RsumOther1Accumulated = new IndicatorDetail();
                RsumOther1Accumulated.setParent(RsumIndicator);
                RsumOther1Accumulated.setType("O1");

                RsumOther2Accumulated = new IndicatorDetail();
                RsumOther2Accumulated.setParent(RsumIndicator);
                RsumOther2Accumulated.setType("O2");

                RsumAP = new IndicatorDetail();
                RsumAP.setParent(RsumIndicator);
                RsumAP.setType("P");

                //合计累计控制
                try {
                    for (int i = getM(); i > 0; i--) {
                        //实际值累计
                        v = indicatorBean.getAccumulatedValue(RsumIndicator.getActualIndicator(), i);
                        setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(RsumActualAccumulated, v);
                        //目标值累计
                        v = indicatorBean.getAccumulatedValue(RsumIndicator.getTargetIndicator(), i);
                        setMethod = getTargetAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(RsumTargetAccumulated, v);

                        v = indicatorBean.getAccumulatedPerformance(RsumIndicator.getTargetIndicator(), RsumIndicator.getActualIndicator(), i);
                        setMethod = RsumAP.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(RsumAP, v);
                    }
                    //按当前月份累计值重设全年累计
                    f = RsumTargetAccumulated.getClass().getDeclaredField(mon);
                    f.setAccessible(true);
                    RsumTargetAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(RsumTargetAccumulated).toString())));

                    f = RsumActualAccumulated.getClass().getDeclaredField(mon);
                    f.setAccessible(true);
                    RsumActualAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(RsumActualAccumulated).toString())));

                    RsumIndicator.getTargetIndicator().setType("目标");
                    indicatorDetailList.add(RsumIndicator.getTargetIndicator());
                    if (Other1 && Other2) {
                        RsumIndicator.getOther1Indicator().setType("当月");
                        indicatorDetailList.add(RsumIndicator.getOther1Indicator());
                        RsumIndicator.getOther2Indicator().setType("质量扣款");
                        indicatorDetailList.add(RsumIndicator.getOther2Indicator());
                        RsumIndicator.getActualIndicator().setType("当月合计");
                        indicatorDetailList.add(RsumIndicator.getActualIndicator());
                    }
                    RsumIndicator.getPerformanceIndicator().setType("当月控制");
                    indicatorDetailList.add(RsumIndicator.getPerformanceIndicator());
                    RsumTargetAccumulated.setType("目标累计");
                    indicatorDetailList.add(RsumTargetAccumulated);
                    RsumActualAccumulated.setType("当月累计");
                    indicatorDetailList.add(RsumActualAccumulated);
                    RsumAP.setType("累计控制");
                    indicatorDetailList.add(RsumAP);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException ex) {
                    log4j.error("FreeServiceReportBean__R合计", ex);
                }
            }
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
            if (Other1 && Other2) {
                sumIndicator.getOther1Indicator().setType("当月");
                indicatorDetailList.add(sumIndicator.getOther1Indicator());
                sumIndicator.getOther2Indicator().setType("质量扣款");
                indicatorDetailList.add(sumIndicator.getOther2Indicator());
                sumIndicator.getActualIndicator().setType("当月合计");
                indicatorDetailList.add(sumIndicator.getActualIndicator());
            } else if (Other4) {
                sumIndicator.getOther5Indicator().setType("当月");
                indicatorDetailList.add(sumIndicator.getOther5Indicator());
                sumIndicator.getOther4Indicator().setType("质量扣款");
                indicatorDetailList.add(sumIndicator.getOther4Indicator());
                sumIndicator.getActualIndicator().setType("当月合计");
                indicatorDetailList.add(sumIndicator.getActualIndicator());
            } else {
                sumIndicator.getActualIndicator().setType("当月");
                indicatorDetailList.add(sumIndicator.getActualIndicator());
            }
            sumIndicator.getPerformanceIndicator().setType("当月控制");
            indicatorDetailList.add(sumIndicator.getPerformanceIndicator());
            sumTargetAccumulated.setType("目标累计");
            indicatorDetailList.add(sumTargetAccumulated);
            sumActualAccumulated.setType("当月累计");
            indicatorDetailList.add(sumActualAccumulated);
            sumAP.setType("累计控制");
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
            log4j.error("FreeServiceReportBean", ex);
        }
    }

    @Override
    public String format(String type, BigDecimal value, int i) {
        switch (type) {
            case "当月控制":
            case "累计控制":
                return percentFormat(value, i);
            default:
                return format(value, i);
        }
    }
    
    public String formatnfy(String type, BigDecimal value, int i) {
        switch (type) {
            case "目标累计":
            case "当月累计":
            case "累计控制":
                return "";
            default:
                return format(type, value, i);
        }
    }

    @Override
    public String percentFormat(BigDecimal value, int i) {
        if (value == null) {
            return "";
        } else if (i <= m) {
            return indicatorBean.percentFormat(value);
        } else {
           return "";
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
        if (i.getOther3Indicator() != null) {
            divideByRate(i.getOther3Indicator(), i.getRate(), scale);
        }
        if (i.getOther4Indicator() != null) {
            divideByRate(i.getOther4Indicator(), i.getRate(), scale);
        }
        if (i.getOther5Indicator() != null) {
            divideByRate(i.getOther5Indicator(), i.getRate(), scale);
        }
        if (i.getOther6Indicator() != null) {
            divideByRate(i.getOther6Indicator(), i.getRate(), scale);
        }
    }

    public String getNewName(String name) {
        String aaString;
        if (name.contains("免费服务")) {
            aaString = name.replace("免费服务", "");
        } else if (name.contains("服务成本")) {
            aaString = name.replace("服务成本", "");
        } else if (name.contains("维修成本")) {
            aaString = name.replace("维修成本", "");
        } else {
            aaString = name;
        }
        return aaString;
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

    public Indicator getSumValue(List<Indicator> indicators) {
        if (indicators.isEmpty()) {
            return null;
        }
        Indicator entity = null;
        IndicatorDetail a, b, f, t, o1, o2, o4, o5;
        IndicatorDetail sa, sb, sf, st, sp, so1, so2, so4, so5;
        try {
            entity = (Indicator) BeanUtils.cloneBean(indicators.get(0));
            entity.setId(-1);
            entity.setName("合计");
            sa = new IndicatorDetail();
            sa.setParent(entity);
            sa.setType("A");
            sb = new IndicatorDetail();
            sb.setParent(entity);
            sb.setType("B");
            sf = new IndicatorDetail();
            sf.setParent(entity);
            sf.setType("F");
            st = new IndicatorDetail();
            st.setParent(entity);
            st.setType("T");
            sp = new IndicatorDetail();
            sp.setParent(entity);
            sp.setType("P");
            so1 = new IndicatorDetail();
            so1.setParent(entity);
            so1.setType("O1");
            so2 = new IndicatorDetail();
            so2.setParent(entity);
            so2.setType("O2");
            so4 = new IndicatorDetail();
            so4.setParent(entity);
            so4.setType("O4");
            so5 = new IndicatorDetail();
            so5.setParent(entity);
            so5.setType("O5");
            entity.setActualIndicator(sa);
            entity.setBenchmarkIndicator(sb);
            entity.setForecastIndicator(sf);
            entity.setTargetIndicator(st);
            entity.setPerformanceIndicator(sp);
            entity.setOther1Indicator(so1);
            entity.setOther2Indicator(so2);
            entity.setOther4Indicator(so4);
            entity.setOther5Indicator(so5);

            for (int i = 0; i < indicators.size(); i++) {
                a = indicators.get(i).getActualIndicator();
                b = indicators.get(i).getBenchmarkIndicator();
                f = indicators.get(i).getForecastIndicator();
                t = indicators.get(i).getTargetIndicator();
                if (indicators.get(i).getOther1Label() != null && indicators.get(i).getOther2Label() != null) {
                    o1 = indicators.get(i).getOther1Indicator();
                    o2 = indicators.get(i).getOther2Indicator();
                    addValue(entity.getOther1Indicator(), o1, entity.getFormkind());
                    addValue(entity.getOther2Indicator(), o2, entity.getFormkind());
                }
                if (indicators.get(i).getOther4Label() != null && indicators.get(i).getOther5Label() != null) {
                    o4 = indicators.get(i).getOther4Indicator();
                    o5 = indicators.get(i).getOther5Indicator();
                    addValue(entity.getOther4Indicator(), o4, entity.getFormkind());
                    addValue(entity.getOther5Indicator(), o5, entity.getFormkind());
                }
                addValue(entity.getActualIndicator(), a, entity.getFormkind());
                addValue(entity.getBenchmarkIndicator(), b, entity.getFormkind());
                addValue(entity.getForecastIndicator(), f, entity.getFormkind());
                addValue(entity.getTargetIndicator(), t, entity.getFormkind());

            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            Logger.getLogger(IndicatorBean.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return entity;
    }

    public void addValue(IndicatorDetail a, IndicatorDetail b, String formKind) {
        //先算汇总字段再算每月字段,A和S类型会重算汇总
        switch (formKind) {
            case "M":
                a.setNfy(a.getNfy().add(b.getNfy()));
                a.setNh2(a.getNh2().add(b.getNh2()));
                a.setNh1(a.getNh1().add(b.getNh1()));
                a.setNq4(a.getNq4().add(b.getNq4()));
                a.setNq3(a.getNq3().add(b.getNq3()));
                a.setNq2(a.getNq2().add(b.getNq2()));
                a.setNq1(a.getNq1().add(b.getNq1()));
                a.setN01(a.getN01().add(b.getN01()));
                a.setN02(a.getN02().add(b.getN02()));
                a.setN03(a.getN03().add(b.getN03()));
                a.setN04(a.getN04().add(b.getN04()));
                a.setN05(a.getN05().add(b.getN05()));
                a.setN06(a.getN06().add(b.getN06()));
                a.setN07(a.getN07().add(b.getN07()));
                a.setN08(a.getN08().add(b.getN08()));
                a.setN09(a.getN09().add(b.getN09()));
                a.setN10(a.getN10().add(b.getN10()));
                a.setN11(a.getN11().add(b.getN11()));
                a.setN12(a.getN12().add(b.getN12()));
                break;
            case "Q":
                a.setNfy(a.getNfy().add(b.getNfy()));
                a.setNh2(a.getNh2().add(b.getNh2()));
                a.setNh1(a.getNh1().add(b.getNh1()));
                a.setNq4(a.getNq4().add(b.getNq4()));
                a.setNq3(a.getNq3().add(b.getNq3()));
                a.setNq2(a.getNq2().add(b.getNq2()));
                a.setNq1(a.getNq1().add(b.getNq1()));
                break;
            case "H":
                a.setNfy(a.getNfy().add(b.getNfy()));
                a.setNh2(a.getNh2().add(b.getNh2()));
                a.setNh1(a.getNh1().add(b.getNh1()));
                break;
            case "Y":
                a.setNfy(a.getNfy().add(b.getNfy()));
        }
    }

    /**
     * @return the glIndicator
     */
    public Indicator getGlIndicator() {
        return glIndicator;
    }

    /**
     * @return the glActualAccumulated
     */
    public IndicatorDetail getGlActualAccumulated() {
        return glActualAccumulated;
    }

    /**
     * @param glActualAccumulated the glActualAccumulated to set
     */
    public void setGlActualAccumulated(IndicatorDetail glActualAccumulated) {
        this.glActualAccumulated = glActualAccumulated;
    }

    /**
     * @return the sumOther1Accumulated
     */
    public IndicatorDetail getSumOther1Accumulated() {
        return sumOther1Accumulated;
    }

    /**
     * @param sumOther1Accumulated the sumOther1Accumulated to set
     */
    public void setSumOther1Accumulated(IndicatorDetail sumOther1Accumulated) {
        this.sumOther1Accumulated = sumOther1Accumulated;
    }

    /**
     * @return the sumOther2Accumulated
     */
    public IndicatorDetail getSumOther2Accumulated() {
        return sumOther2Accumulated;
    }

    /**
     * @param sumOther2Accumulated the sumOther2Accumulated to set
     */
    public void setSumOther2Accumulated(IndicatorDetail sumOther2Accumulated) {
        this.sumOther2Accumulated = sumOther2Accumulated;
    }

    /**
     * @return the sumOther4Accumulated
     */
    public IndicatorDetail getSumOther4Accumulated() {
        return sumOther4Accumulated;
    }

    /**
     * @param sumOther4Accumulated the sumOther5Accumulated to set
     */
    public void setSumOther4Accumulated(IndicatorDetail sumOther4Accumulated) {
        this.sumOther4Accumulated = sumOther4Accumulated;
    }

    /**
     * @return the sumOther5Accumulated
     */
    public IndicatorDetail getSumOther5Accumulated() {
        return sumOther5Accumulated;
    }

    /**
     * @param sumOther5Accumulated the sumOther5Accumulated to set
     */
    public void setSumOther5Accumulated(IndicatorDetail sumOther5Accumulated) {
        this.sumOther5Accumulated = sumOther5Accumulated;
    }

}
