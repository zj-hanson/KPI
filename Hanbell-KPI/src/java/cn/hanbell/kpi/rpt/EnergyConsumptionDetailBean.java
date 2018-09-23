/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorSet;
import cn.hanbell.kpi.web.BscSheetManagedBean;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "energyConsumptionDetailBean")
@ViewScoped
public class EnergyConsumptionDetailBean extends BscSheetManagedBean {

    protected final DecimalFormat Format;
    protected final DecimalFormat DoubleFormat;

    public EnergyConsumptionDetailBean() {
        this.Format = new DecimalFormat("#,###");
        this.DoubleFormat = new DecimalFormat("0.00");
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
                return 1;
            } else {
                return -1;
            }
        });
        //计算每个指标的累计
        for (Indicator e : indicatorList) {
            if ("全公司能耗用量".equals(e.getName())) {
                e.getTargetIndicator().setType("目标电费");
                indicatorDetailList.add(e.getTargetIndicator());
                e.getBenchmarkIndicator().setType("基准电费");
                indicatorDetailList.add(e.getBenchmarkIndicator());
            }
            e.getActualIndicator().setType("实际电费");
            indicatorDetailList.add(e.getActualIndicator());
            if (e.getOther1Label() != null) {
                e.getOther1Indicator().setType(e.getOther1Label());
                indicatorDetailList.add(e.getOther1Indicator());
            }
            if (e.getOther2Label() != null) {
                e.getOther2Indicator().setType(e.getOther2Label());
                indicatorDetailList.add(e.getOther2Indicator());
            }
            if (e.getOther3Label() != null) {
                e.getOther3Indicator().setType(e.getOther3Label());
                indicatorDetailList.add(e.getOther3Indicator());
            }
            if (e.getOther4Label() != null) {
                e.getOther4Indicator().setType(e.getOther4Label());
                indicatorDetailList.add(e.getOther4Indicator());
            }

        }

    }

    @Override
    public String format(BigDecimal value, int i) {
        if (value == null) {
            return "";
        } else if (i <= m) {
            return DoubleFormat.format(value);
        } else if (value.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        } else {
            return DoubleFormat.format(value);
        }
    }

    @Override
    public String format(String type, BigDecimal value, int i) {
        switch (type) {
            case "吨标煤/万元产值":
            case "能耗达标率":
            case "实际（吨标煤/万元产值）":
            case "基准（吨标煤/万元产值）":
            case "目标（吨标煤/万元产值）":
                return format(value, i);
            default:
                if (value == null) {
                    return "";
                } else if (i <= m) {
                    return Format.format(value);
                } else if (value.compareTo(BigDecimal.ZERO) == 0) {
                    return "";
                } else {
                    return Format.format(value);
                }
        }
    }

}
