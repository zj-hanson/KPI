/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.ejb.ShoppingAccomuntBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.web.BscChartManagedBean;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
    private List<Object[]> list;
    protected LinkedHashMap<String, String> statusMap;
    private PieChartModel pieModel1;
    private PieChartModel pieModel2;
    private BigDecimal sum1X000Money;
    private BigDecimal sumAllMoney;
    private Date btnDate;
    @EJB
    private ShoppingAccomuntBean shoppingAccomuntBean;

    public ShoppingCenterAmountReportBean() {
        this.floatFormat = new DecimalFormat("#,###.##");
    }

    @Override
    public void init() {

        super.init();
        statusMap = new LinkedHashMap<>();
        statusMap.put("displaydiv1", "block");
        statusMap.put("displaydiv2", "none");
        btnDate = userManagedBean.getBaseDate();
        Calendar c = Calendar.getInstance();
        c.setTime(btnDate);
        statusMap.put("title", String.valueOf(c.get(Calendar.YEAR)));

    }

    public void query() {
        try {
            list = shoppingAccomuntBean.getList(btnDate);
            if (list != null && !list.isEmpty()) {
                statusMap.put("displaydiv1", "none");
                statusMap.put("displaydiv2", "block");
            }
        } catch (Exception e) {
            System.out.println("e==" + e);
        }
    }

    public void reset() {
        statusMap.put("displaydiv1", "block");
        statusMap.put("displaydiv2", "none");
        list.clear();
    }

    private void createPieModel() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        pieModel1 = new PieChartModel();
        pieModel2 = new PieChartModel();

        for (Object[] i : list) {
            if (i[0].equals("采购中心")) {
                //采购中心饼图
                pieModel1.set((String) i[0], (BigDecimal) i[13]);
            } else {
                //采购金额集团饼图
                pieModel2.set((String) i[0], (BigDecimal) i[13]);
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

    public String percentFormat(BigDecimal value1, BigDecimal value2, int i) {
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

    @Override
    public String doubleformat(BigDecimal value, int scale) {
        BigDecimal sc = new BigDecimal(scale);

        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        } else {
            value = value.divide(sc);
            return floatFormat.format(value);
        }
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

    public List<Object[]> getList() {
        return list;
    }

    public void setList(List<Object[]> list) {
        this.list = list;
    }

    public Date getBtnDate() {
        return btnDate;
    }

    public void setBtnDate(Date btnDate) {
        this.btnDate = btnDate;
    }

    public LinkedHashMap<String, String> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(LinkedHashMap<String, String> statusMap) {
        this.statusMap = statusMap;
    }

}
