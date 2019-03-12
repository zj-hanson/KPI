/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.IndicatorSet;
import cn.hanbell.kpi.web.BscChartManagedBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author C1749
 */
@ManagedBean(name = "qraPercentOfPassReportBean")
@ViewScoped
public class QRAPercentOfPassReportBean extends BscChartManagedBean {

    protected IndicatorDetail jlScore;//进料分数
    protected IndicatorDetail trScore;//试车分数
    protected IndicatorDetail bsScore;//三次元分数
    protected IndicatorDetail olScore;//上线分数
    protected IndicatorDetail tzScore;//涂装分数
    protected IndicatorDetail chScore;//出货分数
    protected IndicatorDetail sumAG;//合计

    protected IndicatorDetail other1Score;//关联指标other1分数
    protected IndicatorDetail other2Score;//关联指标other2分数
    protected IndicatorDetail other3Score;//关联指标other3分数

    protected IndicatorDetail other1Actual;//关联指标other1实际合格率
    protected IndicatorDetail other2Actual;//关联指标other2实际合格率
    protected IndicatorDetail other3Actual;//关联指标other3实际合格率

    protected IndicatorDetail other1Target;//关联指标other1目标合格率
    protected IndicatorDetail other2Target;//关联指标other2目标合格率
    protected IndicatorDetail other3Target;//关联指标other3目标合格率

    protected List<Indicator> indicatorList;
    protected List<IndicatorSet> indicatorSetList;
    protected List<IndicatorDetail> indicatorDetailList;

    protected boolean hasOther;

    public QRAPercentOfPassReportBean() {

    }

    @Override
    public void init() {
        super.init();
        String formid = indicator.getAssociatedIndicator();//取到关联指标的ID
        Indicator otherIndicator = new Indicator();
        if (formid != null && !"".equals(formid)) {
            otherIndicator = indicatorBean.findByFormidYearAndDeptno(formid, y, indicator.getDeptno());
            if (otherIndicator != null) {
                hasOther = true;
                other1Score = new IndicatorDetail();
                other1Score.setParent(otherIndicator);
                other1Score.setType("P");

                other2Score = new IndicatorDetail();
                other2Score.setParent(otherIndicator);
                other2Score.setType("P");

                other3Score = new IndicatorDetail();
                other3Score.setParent(otherIndicator);
                other3Score.setType("P");

                other1Actual = new IndicatorDetail();
                other1Actual.setParent(otherIndicator);
                other1Actual.setType("P");

                other2Actual = new IndicatorDetail();
                other2Actual.setParent(otherIndicator);
                other2Actual.setType("P");

                other3Actual = new IndicatorDetail();
                other3Actual.setParent(otherIndicator);
                other3Actual.setType("P");

                other1Target = new IndicatorDetail();
                other1Target.setParent(otherIndicator);
                other1Target.setType("P");

                other2Target = new IndicatorDetail();
                other2Target.setParent(otherIndicator);
                other2Target.setType("P");

                other3Target = new IndicatorDetail();
                other3Target.setParent(otherIndicator);
                other3Target.setType("P");

                try {
                    for (int i = 1; i <= getM(); i++) {
                        String mon;
                        BigDecimal v;
                        Field f;
                        Method setMethod;
                        int j;
                        //关联指标Other1的实际分数
                        v = this.getAccumulatedValue(otherIndicator.getOther1Indicator(), i).multiply(this.getAccumulatedValue(otherIndicator.getForecastIndicator(), 2))
                                .divide(BigDecimal.valueOf(100));
                        setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(other1Score, v);
                        //关联指标Other2的实际分数
                        v = this.getAccumulatedValue(otherIndicator.getOther2Indicator(), i).multiply(this.getAccumulatedValue(otherIndicator.getForecastIndicator(), 4))
                                .divide(BigDecimal.valueOf(100));
                        setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(other2Score, v);
                        //关联指标Other3的实际分数
                        v = this.getAccumulatedValue(otherIndicator.getOther3Indicator(), i).multiply(this.getAccumulatedValue(otherIndicator.getForecastIndicator(), 6))
                                .divide(BigDecimal.valueOf(100));
                        setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(other3Score, v);

                        //关联指标Other1的实际合格率
                        v = this.getAccumulatedValue(otherIndicator.getOther1Indicator(), i);
                        setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(other1Actual, v);
                        //关联指标Other2的实际合格率
                        v = this.getAccumulatedValue(otherIndicator.getOther2Indicator(), i);
                        setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(other2Actual, v);
                        //关联指标Other3的实际合格率
                        v = this.getAccumulatedValue(otherIndicator.getOther3Indicator(), i);
                        setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(other3Actual, v);

                        //关联指标Other1的目标合格率
                        v = this.getAccumulatedValue(otherIndicator.getTargetIndicator(), 1);
                        setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(other1Target, v);
                        //关联指标Other2的目标合格率
                        v = this.getAccumulatedValue(otherIndicator.getTargetIndicator(), 3);
                        setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(other2Target, v);
                        //关联指标Other3的目标合格率
                        v = this.getAccumulatedValue(otherIndicator.getTargetIndicator(), 5);
                        setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(other3Target, v);
                    }
                } catch (Exception ex) {
                    log4j.error("QRAPercentOfPassReportBean", ex);
                }
            }
        }

        actualAccumulated = new IndicatorDetail();//实际值
        actualAccumulated.setParent(indicator);
        actualAccumulated.setType("A");

        benchmarkAccumulated = new IndicatorDetail();//基准值--实际分值
        benchmarkAccumulated.setParent(indicator);
        benchmarkAccumulated.setType("B");

        targetAccumulated = new IndicatorDetail();//目标值
        targetAccumulated.setParent(indicator);
        targetAccumulated.setType("T");

        jlScore = new IndicatorDetail();
        jlScore.setParent(indicator);
        jlScore.setType("实际分数");

        trScore = new IndicatorDetail();
        trScore.setParent(indicator);
        trScore.setType("实际分数");

        bsScore = new IndicatorDetail();
        bsScore.setParent(indicator);
        bsScore.setType("实际分数");

        olScore = new IndicatorDetail();
        olScore.setParent(indicator);
        olScore.setType("实际分数");

        tzScore = new IndicatorDetail();
        tzScore.setParent(indicator);
        tzScore.setType("实际分数");

        chScore = new IndicatorDetail();
        chScore.setParent(indicator);
        chScore.setType("实际分数");

        sumAG = new IndicatorDetail();
        sumAG.setParent(indicator);
        sumAG.setType("合计");

        //计算指标实际分数
        try {
            //计算各类别的实际分数
            for (int i = 1; i <= getM(); i++) {
                String mon;
                BigDecimal v;
                Field f;
                Method setMethod;
                int j;
                //进料实际分数
                v = this.getAccumulatedValue(indicator.getOther1Indicator(), i).multiply(this.getAccumulatedValue(indicator.getForecastIndicator(), 2))
                        .divide(BigDecimal.valueOf(100));
                setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(jlScore, v);
                //试车实际分数
                v = this.getAccumulatedValue(indicator.getOther2Indicator(), i).multiply(this.getAccumulatedValue(indicator.getForecastIndicator(), 4))
                        .divide(BigDecimal.valueOf(100));
                setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(trScore, v);
                //三次元实际分数
                v = this.getAccumulatedValue(indicator.getOther3Indicator(), i).multiply(this.getAccumulatedValue(indicator.getForecastIndicator(), 6))
                        .divide(BigDecimal.valueOf(100));
                setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(bsScore, v);
                //上线实际分数
                v = this.getAccumulatedValue(indicator.getOther4Indicator(), i).multiply(this.getAccumulatedValue(indicator.getForecastIndicator(), 8))
                        .divide(BigDecimal.valueOf(100));
                setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(olScore, v);
                //涂装、入库实际分数
                v = this.getAccumulatedValue(indicator.getOther5Indicator(), i).multiply(this.getAccumulatedValue(indicator.getForecastIndicator(), 10))
                        .divide(BigDecimal.valueOf(100));
                setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(tzScore, v);
                //出货实际分数 机组和真空的统计方式不一致 需统计缺点数
                if (indicator.getFormid().equals("QRA-机组物料") || indicator.getFormid().equals("QRA-真空物料")) {
                    v = (BigDecimal.ONE.subtract(this.getAccumulatedValue(indicator.getOther6Indicator(), i))).multiply(this.getAccumulatedValue(indicator.getForecastIndicator(), 12))
                            .divide(BigDecimal.valueOf(10));
                    setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(chScore, v);
                } else {
                    v = this.getAccumulatedValue(indicator.getOther6Indicator(), i).multiply(this.getAccumulatedValue(indicator.getForecastIndicator(), 12))
                            .divide(BigDecimal.valueOf(100));
                    setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(chScore, v);
                }
                //合计
                v = this.getGrowth(jlScore, trScore, bsScore, olScore, tzScore, chScore, i, 2);
                setMethod = sumAG.getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(sumAG, v);

            }
        } catch (Exception ex) {
            log4j.error("QRAPercentOfPassReportBean", ex);
        }

        //绘制图标格式
        chartModel = new LineChartModel();
        ChartSeries t = new ChartSeries();
        t.setLabel("合计分值");
        switch (getIndicator().getFormkind()) {
            case "M":
                if (sumAG.getN01().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M01", sumAG.getN01().doubleValue());
                }
                if (sumAG.getN02().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M02", sumAG.getN02().doubleValue());
                }
                if (sumAG.getN03().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M03", sumAG.getN03().doubleValue());
                }
                if (sumAG.getN04().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M04", sumAG.getN04().doubleValue());
                }
                if (sumAG.getN05().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M05", sumAG.getN05().doubleValue());
                }
                if (sumAG.getN06().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M06", sumAG.getN06().doubleValue());
                }
                if (sumAG.getN07().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M07", sumAG.getN07().doubleValue());
                }
                if (sumAG.getN08().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M08", sumAG.getN08().doubleValue());
                }
                if (sumAG.getN09().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M09", sumAG.getN09().doubleValue());
                }
                if (sumAG.getN10().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M10", sumAG.getN10().doubleValue());
                }
                if (sumAG.getN11().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M11", sumAG.getN11().doubleValue());
                }
                if (sumAG.getN12().compareTo(BigDecimal.ZERO) != 0) {
                    t.set("M12", sumAG.getN12().doubleValue());
                }
                break;
        }

        ChartSeries b = new ChartSeries();
        b.setLabel("基准值");
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

        getChartModel().addSeries(t);//实际分数折线图
        getChartModel().addSeries(b);//基准值折线图
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

    public String percentFormat(String type, BigDecimal value, int i) {
        switch (type) {
            case "实际分数":
                return format(value, i);
            default:
                if (value == null) {
                    return "";
                } else if (i <= m) {
                    return format(value);
                } else if (value.compareTo(BigDecimal.ZERO) == 0) {
                    return "";
                } else {
                    return format(value);
                }
        }
    }

    public String floatFormat(String type, BigDecimal value, int i) {
        switch (type) {
            case "实际合格率":
                return format(value, i);
            default:
                if (value == null) {
                    return "";
                } else if (i <= m) {
                    return floatFormat.format(value);
                } else if (value.compareTo(BigDecimal.ZERO) == 0) {
                    return "";
                } else {
                    return floatFormat.format(value);
                }
        }
    }

    public String getIndicatorColumn(String formtype, int m) {
        if (formtype.equals("N")) {
            return "n" + String.format("%02d", m);
        } else if (formtype.equals("D")) {
            return "d" + String.format("%02d", m);
        } else {
            return "";
        }
    }

    //从新写过getAccumulatedValue的方法
    public BigDecimal getAccumulatedValue(IndicatorDetail entity, int m) {
        String mon;
        BigDecimal total = BigDecimal.ZERO;
        Field f;
        try {
            mon = this.getIndicatorColumn("N", m);
            f = entity.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            total = BigDecimal.valueOf(Double.valueOf(f.get(entity).toString()));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            log4j.error(ex);
            total = BigDecimal.ZERO;
        }
        return total;
    }

    public BigDecimal getGrowth(IndicatorDetail a, IndicatorDetail b, IndicatorDetail c, IndicatorDetail d, IndicatorDetail e, IndicatorDetail g, int m) {
        return getGrowth(a, b, c, d, e, g, m, scale);
    }

    //计算合计分值的方法
    public BigDecimal getGrowth(IndicatorDetail a, IndicatorDetail b, IndicatorDetail c, IndicatorDetail d, IndicatorDetail e, IndicatorDetail g, int m, int scale) {
        String mon;
        BigDecimal na, nb, nc, nd, ne, ng;
        Field f;
        try {
            mon = this.getIndicatorColumn("N", m);
            //进料分数
            f = a.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            na = BigDecimal.valueOf(Double.valueOf(f.get(a).toString()));
            //试车分数
            f = b.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            nb = BigDecimal.valueOf(Double.valueOf(f.get(b).toString()));
            //三次元分数
            f = c.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            nc = BigDecimal.valueOf(Double.valueOf(f.get(c).toString()));
            //上线分数
            f = d.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            nd = BigDecimal.valueOf(Double.valueOf(f.get(d).toString()));
            //涂装分数
            f = e.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            ne = BigDecimal.valueOf(Double.valueOf(f.get(e).toString()));
            //出货分数
            f = g.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            ng = BigDecimal.valueOf(Double.valueOf(f.get(g).toString()));
            //合计分数
            return na.add(nb).add(nc).add(nd).add(ne).add(ng);

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            log4j.error("合计分数计算错误！", ex);
        }
        return BigDecimal.ZERO;
    }

    public IndicatorDetail getJlScore() {
        return jlScore;
    }

    public void setJlScore(IndicatorDetail jlScore) {
        this.jlScore = jlScore;
    }

    public IndicatorDetail getTrScore() {
        return trScore;
    }

    public void setTrScore(IndicatorDetail trScore) {
        this.trScore = trScore;
    }

    public IndicatorDetail getBsScore() {
        return bsScore;
    }

    public void setBsScore(IndicatorDetail bsScore) {
        this.bsScore = bsScore;
    }

    public IndicatorDetail getOlScore() {
        return olScore;
    }

    public void setOlScore(IndicatorDetail olScore) {
        this.olScore = olScore;
    }

    public IndicatorDetail getTzScore() {
        return tzScore;
    }

    public void setTzScore(IndicatorDetail tzScore) {
        this.tzScore = tzScore;
    }

    public IndicatorDetail getChScore() {
        return chScore;
    }

    public void setChScore(IndicatorDetail chScore) {
        this.chScore = chScore;
    }

    public IndicatorDetail getSumAG() {
        return sumAG;
    }

    public void setSumAG(IndicatorDetail sumAG) {
        this.sumAG = sumAG;
    }

    public IndicatorDetail getOther1Score() {
        return other1Score;
    }

    public void setOther1Score(IndicatorDetail other1Score) {
        this.other1Score = other1Score;
    }

    public IndicatorDetail getOther2Score() {
        return other2Score;
    }

    public void setOther2Score(IndicatorDetail other2Score) {
        this.other2Score = other2Score;
    }

    public IndicatorDetail getOther3Score() {
        return other3Score;
    }

    public void setOther3Score(IndicatorDetail other3Score) {
        this.other3Score = other3Score;
    }

    public List<Indicator> getIndicatorList() {
        return indicatorList;
    }

    public void setIndicatorList(List<Indicator> indicatorList) {
        this.indicatorList = indicatorList;
    }

    public List<IndicatorSet> getIndicatorSetList() {
        return indicatorSetList;
    }

    public void setIndicatorSetList(List<IndicatorSet> indicatorSetList) {
        this.indicatorSetList = indicatorSetList;
    }

    public List<IndicatorDetail> getIndicatorDetailList() {
        return indicatorDetailList;
    }

    public void setIndicatorDetailList(List<IndicatorDetail> indicatorDetailList) {
        this.indicatorDetailList = indicatorDetailList;
    }

    public IndicatorDetail getOther1Actual() {
        return other1Actual;
    }

    public void setOther1Actual(IndicatorDetail other1Actual) {
        this.other1Actual = other1Actual;
    }

    public IndicatorDetail getOther2Actual() {
        return other2Actual;
    }

    public void setOther2Actual(IndicatorDetail other2Actual) {
        this.other2Actual = other2Actual;
    }

    public IndicatorDetail getOther3Actual() {
        return other3Actual;
    }

    public void setOther3Actual(IndicatorDetail other3Actual) {
        this.other3Actual = other3Actual;
    }

    public IndicatorDetail getOther1Target() {
        return other1Target;
    }

    public void setOther1Target(IndicatorDetail other1Target) {
        this.other1Target = other1Target;
    }

    public IndicatorDetail getOther2Target() {
        return other2Target;
    }

    public void setOther2Target(IndicatorDetail other2Target) {
        this.other2Target = other2Target;
    }

    public IndicatorDetail getOther3Target() {
        return other3Target;
    }

    public void setOther3Target(IndicatorDetail other3Target) {
        this.other3Target = other3Target;
    }

    public boolean isHasOther() {
        return hasOther;
    }

    public void setHasOther(boolean hasOther) {
        this.hasOther = hasOther;
    }

}
