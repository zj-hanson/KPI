/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.InventoryDepartment;
import cn.hanbell.kpi.entity.InventoryIndicator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author C1749
 *
 */
@ManagedBean(name = "inventoryReportComerBean")
@ViewScoped
public class InventoryReportComerBean extends InventoryReportBean {

    public InventoryReportComerBean() {
        super();
    }

    @Override
    public void construct() {
        super.construct();
    }

    @Override
    public void setCities() {
        cities = null;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public boolean addQueryModel() {
        return super.addQueryModel();
    }

    @Override
    public void setDate() {
        super.setDate();
    }

    // 各事业部和各产品目标的各库别物料库存状况表数据集 新版
    @Override
    public void inventoryDepartmentQuery() {
        setDate();
        // 获取页面显示的时间三个月的时间
        try {
            if (addQueryModel()) {
                int m = getMonth();
                int y = getYear();
                List<InventoryDepartment> list;
                list = inventoryDepartmentBean.getInventoryDepartmentResultList("", "", y, m, "K");
                if (!list.isEmpty()) {
                    setInventoryDepartmentsList(list);
                    if (indicator != null) {
                        analysisList = indicatorAnalysisBean.findByPIdAndMonth(indicator.getId(), month);// 指标分析
                        if (getAnalysisList() != null) {
                            analysisCount = getAnalysisList().size();
                        }
                        summaryList = indicatorSummaryBean.findByPIdAndMonth(indicator.getId(), month);// 指标说明
                        if (getSummaryList() != null) {
                            summaryCount = getSummaryList().size();
                        }
                    }
                } else {
                    showErrorMsg("Error", "无法查询到该日期的数据，请重新查询！");
                }
            }
        } catch (Exception ex) {
            showErrorMsg("Error", ex.toString());
        }

    }

    //柯茂 获取库存金额按总经理室方针目标总表 list
    @Override
    public void inventoryIndicatorQuery() {
        try {
            if (addQueryModel()) {
                int m = getMonth();
                int y = getYear();
                List<InventoryIndicator> list;
                list = inventoryIndicatorBean.getInventoryIndicatorResultList(y, m,"K");
                if (!list.isEmpty()) {
                    inventoryIndicatorList = list;
                } else {
                    showErrorMsg("Error", "无法查询到该日期的数据，请重新查询！");
                }
            }
        } catch (Exception ex) {
            showErrorMsg("Error", ex.toString());
        }
    }

    @Override
    public void inventoryTurnoverQuery() {
        try {
            if (addQueryModel()) {
                int m = getMonth();
                int y = getYear();
                List<String[]> list;
                list = inventoryTurnoverBean.getInventoryTurnoverResultList(y, m);
                if (!list.isEmpty()) {
                    indicatorTurnoverList = list;
                } else {
                    showErrorMsg("Error", "无法查询到该日期的数据，请重新查询！");
                }
            }
        } catch (Exception ex) {
            showErrorMsg("Error", ex.toString());
        }
    }

}
