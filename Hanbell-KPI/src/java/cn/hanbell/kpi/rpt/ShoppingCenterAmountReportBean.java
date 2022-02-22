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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "shoppingCenterAmountReportBean")
@ViewScoped
public class ShoppingCenterAmountReportBean extends BscChartManagedBean {

    protected final DecimalFormat floatFormat;
    private List<Indicator> indicatorList;
    private PieChartModel pieModel1;
    private PieChartModel pieModel2;
    private BigDecimal sum1X000Money;
    private BigDecimal sumAllMoney;

    public ShoppingCenterAmountReportBean() {
        this.floatFormat = new DecimalFormat("#,###.00");
    }

    @Override
    public void init() {
        super.init();
        this.monthchecked = false;
        indicatorList = indicatorBean.findByCategoryAndYear("采购金额", y);
        if (indicatorList.isEmpty()) {
            showInfoMsg("Info", "没有数据可展示");
        }
        //计算各数值的百分比
        for(int i=indicatorList.size()-1;i>=0;i--){
            if(indicatorList.get(i).getName().contains("采购中心")){
                //采购中心的交易额/该公司的交易额
                indicatorList.get(i).getActualIndicator().setRemark1(percentFormat(indicatorList.get(i).getActualIndicator().getNfy(),indicatorList.get(i-1).getActualIndicator().getNfy(),2));
            }else{
                 indicatorList.get(i).getActualIndicator().setRemark1(percentFormat(indicatorList.get(i).getActualIndicator().getNfy(),indicatorList.get(6).getActualIndicator().getNfy(),2));
            }        
        }
        //根据指标ID加载指标说明、指标分析
        analysisList = indicatorAnalysisBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标分析
        if (analysisList != null) {
            this.analysisCount = analysisList.size();
        }
        summaryList = indicatorSummaryBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标说明
        if (summaryList != null) {
            this.summaryCount = summaryList.size();
        }
        try {
            createPieModel1(indicatorList);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {

        }
    }

    private void createPieModel1(List<Indicator> indicatorList) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        pieModel1 = new PieChartModel();
        pieModel2 = new PieChartModel();

        for (Indicator i : indicatorList) {
            if ("采购中心".equals(i.getDescript())) {
                //采购中心饼图
                pieModel1.set(i.getName(), i.getActualIndicator().getNfy());
            } else if ("采购金额".equals(i.getDescript())) {
                //采购金额集团饼图
                pieModel2.set(i.getName(), i.getActualIndicator().getNfy());
            }
            if ("A-集团采购中心合计".equals(i.getFormid())) {
                this.sum1X000Money = i.getActualIndicator().getNfy();
            } else if ("A-集团采购金额合计".equals(i.getFormid())) {
                this.sumAllMoney = i.getActualIndicator().getNfy();
            }
        }
        pieModel1.setTitle("集团采购中心采购金额");
        pieModel1.setLegendPosition("e");
        pieModel1.setFill(true);
        pieModel1.setShowDataLabels(true);
        pieModel1.setDiameter(250);
        pieModel1.setShadow(false);

        pieModel2.setTitle("集团总采购金额");
        pieModel2.setLegendPosition("e");
        pieModel2.setFill(true);
        pieModel2.setShowDataLabels(true);
        pieModel2.setDiameter(250);
        pieModel2.setShadow(false);

    }
    
    

    public String percentFormat(BigDecimal value1,BigDecimal value2, int i) {
        if (value1 == null || value1 == BigDecimal.ZERO) {
            return "0.00%";
        }
          if (value2 == null || value2 == BigDecimal.ZERO) {
            return "0.00%";
        }  
          if (i == 0 || value2 == BigDecimal.ZERO) {
            return "0.00%";
        }
          
        return percentFormat(value1.multiply(BigDecimal.valueOf(100)).divide(value2, i, BigDecimal.ROUND_HALF_UP), 2);

    }

    public double getMonthSUM(IndicatorDetail indicatorDetail) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        Field f;
        double a1;
        String mon = "";
        mon = indicatorBean.getIndicatorColumn("N", m);
        f = indicatorDetail.getClass().getDeclaredField(mon);
        f.setAccessible(true);
        a1 = Double.valueOf(f.get(indicatorDetail).toString());
        return a1;
    }

    public String doubleformat(BigDecimal value, BigDecimal scale) {
            if (scale == null || scale.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        } 
        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        } else {
             value=value.divide(scale);
            return floatFormat.format(value);
        }
    }

    public List<Indicator> getIndicatorList() {
        return indicatorList;
    }

    public void setIndicatorList(List<Indicator> indicatorList) {
        this.indicatorList = indicatorList;
    }

    public PieChartModel getPieModel1() {
        return pieModel1;
    }

    public void setPieModel1(PieChartModel pieModel1) {
        this.pieModel1 = pieModel1;
    }

    public PieChartModel getPieModel2() {
        return pieModel2;
    }

    public void setPieModel2(PieChartModel pieModel2) {
        this.pieModel2 = pieModel2;
    }

}
