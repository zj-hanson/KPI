/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.web;

import cn.hanbell.kpi.ejb.IndicatorAnalysisBean;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorSummaryBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorAnalysis;
import cn.hanbell.kpi.entity.IndicatorChart;
import cn.hanbell.kpi.entity.IndicatorSummary;
import com.sun.mail.imap.protocol.ID;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author C1879
 */
public class BscQueryTableManageBean {

    @EJB
    protected IndicatorAnalysisBean indicatorAnalysisBean;
    @EJB
    protected IndicatorSummaryBean indicatorSummaryBean;
    @EJB
    protected IndicatorBean indicatorBean;

    protected Indicator indicator;
    protected List<IndicatorAnalysis> analysisList;
    protected List<IndicatorSummary> summaryList;
    protected int analysisCount;
    protected int summaryCount;

    /**
     * 根据时间查询备注
     * @param indicatorChart
     * @param y
     * @param m
     */
    protected void getRemarkOne(IndicatorChart indicatorChart , int y, int m) {
        indicator = indicatorBean.findByFormidYearAndDeptno(indicatorChart.getFormid(), y, indicatorChart.getDeptno());
        if (indicator != null) {
            //根据指标ID加载指标说明、指标分析
            analysisList = indicatorAnalysisBean.findByPIdAndMonth(indicator.getId(), m);//指标分析
            if (getAnalysisList() != null) {
                this.analysisCount = getAnalysisList().size();
            }
            summaryList = indicatorSummaryBean.findByPIdAndMonth(indicator.getId(), m);//指标说明
            if (getSummaryList() != null) {
                this.summaryCount = getSummaryList().size();
            }
        }
    }

    /**
     * @return the indicator
     */
    public Indicator getIndicator() {
        return indicator;
    }

    /**
     * @return the analysisList
     */
    public List<IndicatorAnalysis> getAnalysisList() {
        return analysisList;
    }

    /**
     * @return the summaryList
     */
    public List<IndicatorSummary> getSummaryList() {
        return summaryList;
    }

    /**
     * @return the analysisCount
     */
    public int getAnalysisCount() {
        return analysisCount;
    }

    /**
     * @return the summaryCount
     */
    public int getSummaryCount() {
        return summaryCount;
    }

}
