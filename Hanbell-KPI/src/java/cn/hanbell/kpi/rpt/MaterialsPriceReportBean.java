/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.IndicatorSet;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.web.BscSheetManagedBean;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "materialsPriceReportBean")
@ViewScoped
public class MaterialsPriceReportBean extends BscSheetManagedBean {

    /**
     * Creates a new instance of ShipmentReportBean
     */
    public MaterialsPriceReportBean() {

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

        mon = indicatorBean.getIndicatorColumn("N", getM());
        BigDecimal v;
        Method setMethod;
        //计算每个指标的累计
        for (Indicator e : indicatorList) {
            BG = new IndicatorDetail();
            BG.setParent(e);
            BG.setType("P");

            AG = new IndicatorDetail();
            AG.setParent(e);
            AG.setType("P");

            //平均值 = 当月累计/当前月份
            BigDecimal avgBigDecimal = indicatorBean.getAccumulatedValue(e.getActualIndicator(), m).divide(BigDecimal.valueOf(m), 2);

            BigDecimal nowAvg, pastAvg;
            //计算累计
            try {
                for (int i = getM(); i > 0; i--) {
                    //月累计涨跌比例
                    v = this.getGrowth(e.getActualIndicator(), i);
                    setMethod = BG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(BG, v);
                    //年涨跌比例
                    pastAvg = e.getBenchmarkIndicator().getNfy();
                    nowAvg = indicatorBean.getAccumulatedValue(e.getActualIndicator(), i).divide(BigDecimal.valueOf(i), 2);
                    v = this.getGrowth(nowAvg, pastAvg);
                    setMethod = AG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(AG, v);
                }

            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                log4j.error("materialsPriceReportBean", ex);
            }
            //价格
            e.getActualIndicator().setType(e.getUnit());
            //当前年平均值
            e.getActualIndicator().setNfy(avgBigDecimal);
            indicatorDetailList.add(e.getActualIndicator());
            BG.setType("月跌涨比例");
            indicatorDetailList.add(BG);
            AG.setType("年跌涨比例");
            indicatorDetailList.add(AG);

        }
        try {
            //根据指标ID加载指标说明、指标分析
            analysisList = indicatorAnalysisBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标分析
            if (analysisList != null) {
                this.analysisCount = analysisList.size();
            }
            summaryList = indicatorSummaryBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标说明
            if (summaryList != null) {
                this.summaryCount = summaryList.size();
            }

        } catch (SecurityException | IllegalArgumentException ex) {
            log4j.error("bscReportManagedBean", ex);
        }
    }

    @Override
    public String format(String type, BigDecimal value, int i) {
        switch (type) {
            case "月跌涨比例":
            case "年跌涨比例":
                return percentFormat(value, i);
            default:
                return format(value, i);
        }
    }

    public String getName(String name) {
        return name.replace("价格趋势", "");
    }

    //月涨跌比例数本月与1月比较
    protected BigDecimal getGrowth(IndicatorDetail e, int i) {
        try {
            //一月份价格
            Double a1 = getActualValue(e, 1);
            //当前月份价格
            Double ai = getActualValue(e, i);
            return BigDecimal.valueOf((ai - a1) / a1 * 100);
        } catch (Exception ex) {
        }
        return BigDecimal.ZERO;
    }

    //年涨跌比例数本年平均与上年平均比较
    protected BigDecimal getGrowth(BigDecimal nowAvg, BigDecimal pastAvg) {
        try {
            Double now, past;
            now = Double.parseDouble(nowAvg.toString());
            past = Double.parseDouble(pastAvg.toString());
            if (pastAvg != BigDecimal.ZERO) {
                return BigDecimal.valueOf(((now - past) / past * 100));
            } else {
                return BigDecimal.ZERO;
            }
        } catch (Exception ex) {
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getForecastValue(IndicatorDetail e) {
        BigDecimal a;
        switch (m) {
            case 1:
                a = e.getN01();
                break;
            case 2:
                a = e.getN02();
                break;
            case 3:
                a = e.getN03();
                break;
            case 4:
                a = e.getN04();
                break;
            case 5:
                a = e.getN05();
                break;
            case 6:
                a = e.getN06();
                break;
            case 7:
                a = e.getN07();
                break;
            case 8:
                a = e.getN08();
                break;
            case 9:
                a = e.getN09();
                break;
            case 10:
                a = e.getN10();
                break;
            case 11:
                a = e.getN11();
                break;
            case 12:
                a = e.getN12();
                break;
            default:
                a = BigDecimal.ZERO;
        }
        return a;
    }

    protected Double getActualValue(IndicatorDetail e, int m) {
        Double a;
        switch (m) {
            case 1:
                a = Double.parseDouble(e.getN01().toString());
                break;
            case 2:
                a = Double.parseDouble(e.getN02().toString());
                break;
            case 3:
                a = Double.parseDouble(e.getN03().toString());
                break;
            case 4:
                a = Double.parseDouble(e.getN04().toString());
                break;
            case 5:
                a = Double.parseDouble(e.getN05().toString());
                break;
            case 6:
                a = Double.parseDouble(e.getN06().toString());
                break;
            case 7:
                a = Double.parseDouble(e.getN07().toString());
                break;
            case 8:
                a = Double.parseDouble(e.getN08().toString());
                break;
            case 9:
                a = Double.parseDouble(e.getN09().toString());
                break;
            case 10:
                a = Double.parseDouble(e.getN10().toString());
                break;
            case 11:
                a = Double.parseDouble(e.getN11().toString());
                break;
            case 12:
                a = Double.parseDouble(e.getN12().toString());
                break;
            default:
                a = 0.0;
        }
        return a;
    }

}
