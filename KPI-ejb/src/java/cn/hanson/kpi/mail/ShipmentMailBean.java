/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.mail;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class ShipmentMailBean extends ShipmentMail {

    public List<Indicator> sumIndicatorList;//大合计集合
    public Indicator allSumIndicator;//大合计对象
    private BigDecimal sumall;
    private BigDecimal sumAllValue;

    public ShipmentMailBean() {
        this.sumIndicatorList = new ArrayList();
    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        if (sumIndicatorList != null) {
            this.sumIndicatorList.clear();
        }
        super.init();
    }

    @Override
    protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Hanson</title>");
        sb.append(css);
        sb.append("</head><body><div style=\"margin: auto;text-align: center;\">");
        sb.append("<div style=\"width:100%\" class=\"title\">");
        sb.append("<div style=\"text-align:center;width:100%\">浙江汉声精密机械有限公司</div>");
        sb.append("<div style=\"text-align:center;width:100%\">").append(mailSubject).append("</div>");
        sb.append("<div style=\"text-align:center;width:100%; color:Red;\">日期:").append(BaseLib.formatDate("yyyy-MM-dd", d)).append("</div>");
        sb.append("</div>");
        return sb.toString();
    }

    //表头
    protected String getHeadTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tbl\"><table width=\"100%\">");
        sb.append("<tr><th rowspan=\"2\" colspan=\"1\">种类</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
        sb.append("<th rowspan=\"1\" colspan=\"8\">本月累計</th><th rowspan=\"1\" colspan=\"5\">年累計</th>");
        sb.append("<th rowspan=\"2\" colspan=\"1\">年比重</th></tr>");
        sb.append("<tr><th colspan=\"1\">實際</th><th colspan=\"1\">目標</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成長率</th>");
        sb.append("<th colspan=\"1\">預計訂單</th><th colspan=\"1\">實際催貨</th><th colspan=\"1\">未來幾天催貨</th>");
        sb.append("<th colspan=\"1\">本年出貨</th><th colspan=\"1\">目標</th>");
        sb.append("<th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成長率</th>");
        sb.append("</tr>");
        return sb.toString();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        getData().put("sum2", BigDecimal.ZERO);
        StringBuilder sb = new StringBuilder();
        try {
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d));
            }
            if (needsum) {
                sumIndicator = this.getAllSumValue(indicatorList);
                if (sumIndicator != null) {
                    sumIndicatorList.add(sumIndicator);
                    indicatorBean.updatePerformance(sumIndicator);
                    sb.append(getHtmlTableRow(sumIndicator, y, m, d));
                }
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getHtmlTable1(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        getData().put("sum2", BigDecimal.ZERO);
        StringBuilder sb = new StringBuilder();
        try {
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow1(i, y, m, d, false));
            }
            if (needsum) {
                sumIndicator = this.getAllSumValue(indicatorList);
                if (sumIndicator != null) {
                    sumIndicatorList.add(sumIndicator);
                    sumall = sumall.add(getData().get("sum1"));
                    indicatorBean.updatePerformance(sumIndicator);
                    sumIndicator.setName("小计");
                    sb.append(getHtmlTableRow1(sumIndicator, y, m, d, true));
                }
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        //获取需要取值栏位
        String col = indicatorBean.getIndicatorColumn(indicator.getFormtype(), m);
        StringBuilder sb = new StringBuilder();
        IndicatorDetail fc = indicator.getForecastIndicator();//预计
        IndicatorDetail a = indicator.getActualIndicator();//实际
        IndicatorDetail b = indicator.getBenchmarkIndicator();//去年同期
        IndicatorDetail p = indicator.getPerformanceIndicator();
        IndicatorDetail t = indicator.getTargetIndicator();//目标
        IndicatorDetail oth1 = indicator.getOther1Indicator();//预计订单量
        IndicatorDetail oth2 = indicator.getOther2Indicator();//实际催货订单量
        IndicatorDetail oth3 = indicator.getOther3Indicator();//未来几天催货订单量
        Field f;
        try {
            BigDecimal num1;
            if (indicator.getActualInterface() != null && indicator.getActualEJB() != null && indicator.getId() != -1) {
                //本日出货
                Actual actualInterface = (Actual) Class.forName(indicator.getActualInterface()).newInstance();
                actualInterface.setEJB(indicator.getActualEJB());
                num1 = actualInterface.getValue(y, m, d, Calendar.DATE, actualInterface.getQueryParams()).divide(indicator.getRate(), 2, RoundingMode.HALF_UP);
            } else {
                num1 = BigDecimal.ZERO;
            }
            if (indicator.getId() != -1) {
                sumAdditionalData("sum1", num1);
            }
            if (indicator.getName().equals("合计")) {
                sb.append("<tr>");
                //种类
                sb.append("<td style='background-color:#ff8e67';>").append(indicator.getName()).append("</td>");
                //本日
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(indicator.getId() != -1 ? num1 : getData().get("sum1"))).append("</td>");
                //当月
                //实际出货量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(f.get(a))).append("</td>");
                //目标
                f = t.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(f.get(t))).append("</td>");
                //达成
                f = p.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(percentFormat(f.get(p))).append("</td>");
                //去年同期
                f = b.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(indicatorBean.getValueOfDays(BigDecimal.valueOf(Double.valueOf(f.get(b).toString())), d, 2))).append("</td>");//改成按天折算
                //成长率
                sb.append("<td style='background-color:#ff8e67';>").append(percentFormat(indicatorBean.getGrowth(a, b, m, d, 0))).append("</td>");//改成按天折算
                //预计订单量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(f.get(oth1))).append("</td>");
                //实际催货订单量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(f.get(oth2))).append("</td>");
                //未来几天催货订单量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(f.get(oth3))).append("</td>");
                //累计
                //实际本年出货量
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(indicatorBean.getAccumulatedValue(a, m))).append("</td>");
                //年度目标
                f = t.getClass().getDeclaredField("nfy");
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(f.get(t))).append("</td>");
                //年度达成率
                f = p.getClass().getDeclaredField("nfy");
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(percentFormat(f.get(p))).append("</td>");
                //去年同期
                //sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(b, m))).append("</td>");
                //改成按天折算
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(indicatorBean.getAccumulatedValue(b, m, d))).append("</td>");
                //成长率
                //sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m))).append("</td>");
                //改成按天折算
                sb.append("<td style='background-color:#ff8e67';>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m, d))).append("</td>");
                //年度比重
                if (sumAllValue.compareTo(BigDecimal.ZERO) != 0) {
                    sb.append("<td style='background-color:#ff8e67';>").append(percentFormat(indicatorBean.getAccumulatedValue(a, m).divide(sumAllValue, 4, BigDecimal.ROUND_HALF_UP)
                            .multiply(BigDecimal.valueOf(100)))).append("</td>");
                } else {
                    sb.append("<td>").append("").append("</td>");
                }
                sb.append("</tr>");
            } else {
                sb.append("<tr>");
                //种类
                sb.append("<td>").append(indicator.getName()).append("</td>");
                //本日
                sb.append("<td>").append(DoublelFormat.format(indicator.getId() != -1 ? num1 : getData().get("sum1"))).append("</td>");
                //当月
                //实际出货量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td>").append(DoublelFormat.format(f.get(a))).append("</td>");
                //目标
                f = t.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td>").append(DoublelFormat.format(f.get(t))).append("</td>");
                //达成
                f = p.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td>").append(percentFormat(f.get(p))).append("</td>");
                //去年同期
                f = b.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td>").append(DoublelFormat.format(indicatorBean.getValueOfDays(BigDecimal.valueOf(Double.valueOf(f.get(b).toString())), d, 2))).append("</td>");//改成按天折算
                //成长率
                sb.append("<td>").append(percentFormat(indicatorBean.getGrowth(a, b, m, d, 0))).append("</td>");//改成按天折算
                //预计订单量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td>").append(DoublelFormat.format(f.get(oth1))).append("</td>");
                //实际催货订单量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td>").append(DoublelFormat.format(f.get(oth2))).append("</td>");
                //未来几天催货订单量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td>").append(DoublelFormat.format(f.get(oth3))).append("</td>");
                //累计
                //实际本年出货量
                sb.append("<td>").append(DoublelFormat.format(indicatorBean.getAccumulatedValue(a, m))).append("</td>");
                //年度目标
                f = t.getClass().getDeclaredField("nfy");
                f.setAccessible(true);
                sb.append("<td>").append(DoublelFormat.format(f.get(t))).append("</td>");
                //年度达成率
                f = p.getClass().getDeclaredField("nfy");
                f.setAccessible(true);
                sb.append("<td>").append(percentFormat(f.get(p))).append("</td>");
                //去年同期
                //sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(b, m))).append("</td>");
                //改成按天折算
                sb.append("<td>").append(DoublelFormat.format(indicatorBean.getAccumulatedValue(b, m, d))).append("</td>");
                //成长率
                //sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m))).append("</td>");
                //改成按天折算
                sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m, d))).append("</td>");
                //年度比重
                if (sumAllValue.compareTo(BigDecimal.ZERO) != 0) {
                    sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedValue(a, m).divide(sumAllValue, 4, BigDecimal.ROUND_HALF_UP)
                            .multiply(BigDecimal.valueOf(100)))).append("</td>");
                } else {
                    sb.append("<td>").append("").append("</td>");
                }
                sb.append("</tr>");
            }

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

    //sumType表示小计算比重的状态
    protected String getHtmlTableRow1(Indicator indicator, int y, int m, Date d, boolean sumType) throws Exception {
        //获取需要取值栏位
        String col = indicatorBean.getIndicatorColumn(indicator.getFormtype(), m);
        StringBuilder sb = new StringBuilder();
        IndicatorDetail fc = indicator.getForecastIndicator();//预计
        IndicatorDetail a = indicator.getActualIndicator();//实际
        IndicatorDetail b = indicator.getBenchmarkIndicator();//去年同期
        IndicatorDetail p = indicator.getPerformanceIndicator();
        IndicatorDetail t = indicator.getTargetIndicator();//目标
        IndicatorDetail oth1 = indicator.getOther1Indicator();//预计订单量
        IndicatorDetail oth2 = indicator.getOther2Indicator();//实际催货订单量
        IndicatorDetail oth3 = indicator.getOther3Indicator();//未来几天催货订单量
        Field f;
        try {
            BigDecimal num1;
            if (indicator.getActualInterface() != null && indicator.getActualEJB() != null && indicator.getId() != -1) {
                //本日出货
                Actual actualInterface = (Actual) Class.forName(indicator.getActualInterface()).newInstance();
                actualInterface.setEJB(indicator.getActualEJB());
                num1 = actualInterface.getValue(y, m, d, Calendar.DATE, actualInterface.getQueryParams()).divide(indicator.getRate(), 2, RoundingMode.HALF_UP);
            } else {
                num1 = BigDecimal.ZERO;
            }
            if (indicator.getId() != -1) {
                sumAdditionalData("sum1", num1);
            }
            if (indicator.getName().equals("小计") || indicator.getName().equals("合计")) {
                sb.append("<tr>");
                //种类
                sb.append("<td style='background-color:#ff8e67';>").append(indicator.getName()).append("</td>");
                //本日
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(indicator.getId() != -1 ? num1 : getData().get("sum1"))).append("</td>");
                //当月
                //实际出货量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(f.get(a))).append("</td>");
                //目标
                f = t.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(f.get(t))).append("</td>");
                //达成
                f = p.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(percentFormat(f.get(p))).append("</td>");
                //去年同期
                f = b.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(indicatorBean.getValueOfDays(BigDecimal.valueOf(Double.valueOf(f.get(b).toString())), d, 2))).append("</td>");//改成按天折算
                //成长率
                sb.append("<td style='background-color:#ff8e67';>").append(percentFormat(indicatorBean.getGrowth(a, b, m, d, 0))).append("</td>");//改成按天折算
                //预计订单量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(f.get(oth1))).append("</td>");
                //实际催货订单量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(f.get(oth2))).append("</td>");
                //未来几天催货订单量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(f.get(oth3))).append("</td>");
                //累计
                //实际本年出货量
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(indicatorBean.getAccumulatedValue(a, m))).append("</td>");
                //年度目标
                f = t.getClass().getDeclaredField("nfy");
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(f.get(t))).append("</td>");
                //年度达成率
                f = p.getClass().getDeclaredField("nfy");
                f.setAccessible(true);
                sb.append("<td style='background-color:#ff8e67';>").append(percentFormat(f.get(p))).append("</td>");
                //去年同期
                //sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(b, m))).append("</td>");
                //改成按天折算
                sb.append("<td style='background-color:#ff8e67';>").append(DoublelFormat.format(indicatorBean.getAccumulatedValue(b, m, d))).append("</td>");
                //成长率
                //sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m))).append("</td>");
                //改成按天折算
                sb.append("<td style='background-color:#ff8e67';>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m, d))).append("</td>");
                //年度比重
                if (sumType) {
                    if (sumAllValue.compareTo(BigDecimal.ZERO) != 0) {
                        sb.append("<td style='background-color:#ff8e67';>").append(percentFormat(indicatorBean.getAccumulatedValue(a, m).divide(sumAllValue, 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(BigDecimal.valueOf(100)))).append("</td>");
                    } else {
                        sb.append("<td>").append("").append("</td>");
                    }
                } else {
                    sb.append("<td>").append("").append("</td>");
                }
                sb.append("</tr>");
            } else {
                sb.append("<tr>");
                //种类
                sb.append("<td>").append(indicator.getName()).append("</td>");
                //本日
                sb.append("<td>").append(DoublelFormat.format(indicator.getId() != -1 ? num1 : getData().get("sum1"))).append("</td>");
                //当月
                //实际出货量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td>").append(DoublelFormat.format(f.get(a))).append("</td>");
                //目标
                f = t.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td>").append(DoublelFormat.format(f.get(t))).append("</td>");
                //达成
                f = p.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td>").append(percentFormat(f.get(p))).append("</td>");
                //去年同期
                f = b.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td>").append(DoublelFormat.format(indicatorBean.getValueOfDays(BigDecimal.valueOf(Double.valueOf(f.get(b).toString())), d, 2))).append("</td>");//改成按天折算
                //成长率
                sb.append("<td>").append(percentFormat(indicatorBean.getGrowth(a, b, m, d, 0))).append("</td>");//改成按天折算
                //预计订单量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td>").append(DoublelFormat.format(f.get(oth1))).append("</td>");
                //实际催货订单量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td>").append(DoublelFormat.format(f.get(oth2))).append("</td>");
                //未来几天催货订单量
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                sb.append("<td>").append(DoublelFormat.format(f.get(oth3))).append("</td>");
                //累计
                //实际本年出货量
                sb.append("<td>").append(DoublelFormat.format(indicatorBean.getAccumulatedValue(a, m))).append("</td>");
                //年度目标
                f = t.getClass().getDeclaredField("nfy");
                f.setAccessible(true);
                sb.append("<td>").append(DoublelFormat.format(f.get(t))).append("</td>");
                //年度达成率
                f = p.getClass().getDeclaredField("nfy");
                f.setAccessible(true);
                sb.append("<td>").append(percentFormat(f.get(p))).append("</td>");
                //去年同期
                //sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(b, m))).append("</td>");
                //改成按天折算
                sb.append("<td>").append(DoublelFormat.format(indicatorBean.getAccumulatedValue(b, m, d))).append("</td>");
                //成长率
                //sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m))).append("</td>");
                //改成按天折算
                sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m, d))).append("</td>");
                //年度比重
                if (sumType) {
                    if (sumAllValue.compareTo(BigDecimal.ZERO) != 0) {
                        sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedValue(a, m).divide(sumAllValue, 4, BigDecimal.ROUND_HALF_UP)
                                .multiply(BigDecimal.valueOf(100)))).append("</td>");
                    } else {
                        sb.append("<td>").append("").append("</td>");
                    }
                } else {
                    sb.append("<td>").append("").append("</td>");
                }
                sb.append("</tr>");
            }

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

    public void getSumBzValue(String aa) {
        String[] arr = aa.split(",");
        try {
            sumAllValue = BigDecimal.ZERO;
            List<Indicator> list = new ArrayList<>();
            for (int i = 0; i < arr.length; i++) {
                List<Indicator> list1 = indicatorBean.findByCategoryAndYear(arr[i], y);
                for (Indicator list11 : list1) {
                    this.divideByRateOther(list11, 2);
                    list.add(list11);
                }
            }
            if (!list.isEmpty()) {
                allSumIndicator = indicatorBean.getSumValue(list);
                sumAllValue = allSumIndicator.getActualIndicator().getNfy();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public String getMailBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：吨</div>");
        sb.append(getAllQuantityTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAllAmountTable());
        sb.append("<div class=\"tableTitle\" ></div>");
        sb.append("<div class=\"tableTitle\" style='text-align:center'>出货统计報表(依客户材质别)</div>");
        sb.append("<div class=\"tableTitle\">单位：吨</div>");
        sb.append(getQuantityTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAmountTable());
        sb.append("<div class=\"tableTitle\" style='text-align:center'>出货统计報表(依产品别)</div>");
        sb.append("<div class=\"tableTitle\">单位：吨</div>");
        sb.append(getVarietyTonTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getVarietyAmtsTable());
        sb.append("<div class=\"tableTitle\">預估訂單量: 客戶提供的意向訂單量&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月實際出貨量: 本月累计出貨量</div>");
        sb.append("<div class=\"tableTitle\"><span>实际催货訂單: 客戶已通知截至昨日需出貨訂單量</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年累計目標: 本年月累计至本月目标的目标值</div>");
        sb.append("<div class=\"tableTitle\"><span>未来几天催货訂單: 客戶已通知今日起需出貨訂單量</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年累計達成率: (年累計實際/ 年累計目標) ×100%</div>");
        sb.append("<div class=\"tableTitle\">本月目標: 與查詢日期同月日累計至月底的目標值&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年比重 ：(年实际/年合计) ×100%。</div>");
        sb.append("<div class=\"tableTitle\">本月達成率: (本月實際/ 本月目標) ×100%   </div>");
        sb.append("<div class=\"tableTitle\"><span style=\"color:red\">注：因當日出貨單會延後確認，故報表抓取截至昨日之出貨數據。</span></div>");
        return sb.toString();
    }

    protected String getAllQuantityTable() {
        getSumBzValue("汉声依材质别出货重量");
        StringBuilder sb = new StringBuilder();
        sb.append(getHeadTable());
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依材质别出货重量", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                this.divideByRateOther(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
            sb.append("</table></div>");
            return sb.toString();
        } else {
            return "汉声依材质别出货重量";
        }
    }

    protected String getAllAmountTable() {
        getSumBzValue("汉声依材质别出货金额");
        StringBuilder sb = new StringBuilder();
        sb.append(getHeadTable());
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("汉声依材质别出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                this.divideByRateOther(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
            sb.append("</table></div>");
            return sb.toString();
        } else {
            return "汉声依材质别出货金额";
        }
    }

    protected String getQuantityTable() {
        sumall = BigDecimal.ZERO;
        sumIndicatorList.clear();//清除总表的list
        getSumBzValue("汉声依SHB客户材质出货重量,汉声依THB客户材质出货重量,汉声依OTH客户材质出货重量");
        StringBuilder sb = new StringBuilder();//表头内容
        sb.append(getHeadTable());
        this.indicators.clear();
        //SHB
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依SHB客户材质出货重量", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                this.divideByRateOther(i, 2);
            }
            sb.append(getHtmlTable1(this.indicators, y, m, d, true));
        } else {
            sb.append("");
        }
        //THB
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依THB客户材质出货重量", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                this.divideByRateOther(i, 2);
            }
            sb.append(getHtmlTable1(this.indicators, y, m, d, true));
        } else {
            sb.append("");
        }
        //OTH
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依OTH客户材质出货重量", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                this.divideByRateOther(i, 2);
            }
            sb.append(getHtmlTable1(this.indicators, y, m, d, true));
        } else {
            sb.append("");
        }
        //最后的总合计 sumIndicator
        try {
            allSumIndicator = this.getAllSumValue(sumIndicatorList);
            if (allSumIndicator != null) {
                allSumIndicator.setName("合计");
                getData().put("sum1", sumall);
                sb.append(getHtmlTableRow1(allSumIndicator, y, m, d, true));
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        sb.append("</table></div>");
        sb.append("");
        return sb.toString();

    }

    protected String getAmountTable() {
        sumall = BigDecimal.ZERO;
        sumIndicatorList.clear();
        getSumBzValue("汉声依SHB客户材质出货金额,汉声依THB客户材质出货金额,汉声依OTH客户材质出货金额");
        StringBuilder sb = new StringBuilder();
        sb.append(getHeadTable());
        //SHB
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("汉声依SHB客户材质出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                this.divideByRateOther(i, 2);
            }
            sb.append(getHtmlTable1(this.indicators, y, m, d, true));
        } else {
            sb.append("");
        }
        //THB
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("汉声依THB客户材质出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                this.divideByRateOther(i, 2);
            }
            sb.append(getHtmlTable1(this.indicators, y, m, d, true));
        } else {
            sb.append("");
        }
        //OTH
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("汉声依OTH客户材质出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                this.divideByRateOther(i, 2);
            }
            sb.append(getHtmlTable1(this.indicators, y, m, d, true));
        } else {
            sb.append("");
        }

        //最后的总合计
        try {
            allSumIndicator = this.getAllSumValue(sumIndicatorList);
            if (allSumIndicator != null) {
                getData().put("sum1", sumall);
                allSumIndicator.setName("合计");
                sb.append(getHtmlTableRow1(allSumIndicator, y, m, d, true));
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        sb.append("</table></div>");
        return sb.toString();
    }

    //按产品别分类 重量
    protected String getVarietyTonTable() {
        getSumBzValue("汉声依种类别出货重量");
        StringBuilder sb = new StringBuilder();
        sb.append(getHeadTable());
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依种类别出货重量", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                this.divideByRateOther(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
            sb.append("</table></div>");
            return sb.toString();
        } else {
            return "汉声依种类别出货重量";
        }
    }

    //按产品别分类 金额
    protected String getVarietyAmtsTable() {
        getSumBzValue("汉声依种类别出货金额");
        StringBuilder sb = new StringBuilder();
        sb.append(getHeadTable());
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依种类别出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                this.divideByRateOther(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
            sb.append("</table></div>");
            return sb.toString();
        } else {
            return "汉声依种类别出货金额";
        }
    }

    public Indicator getAllSumValue(List<Indicator> indicators) {
        if (indicators.isEmpty()) {
            return null;
        }
        Indicator entity = null;
        IndicatorDetail a, b, f, t, o1, o2, o3;
        IndicatorDetail sa, sb, sf, st, sp, so1, so2, so3;
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
            so1.setType("A");
            so2 = new IndicatorDetail();
            so2.setParent(entity);
            so2.setType("A");
            so3 = new IndicatorDetail();
            so3.setParent(entity);
            so3.setType("A");
            entity.setActualIndicator(sa);
            entity.setBenchmarkIndicator(sb);
            entity.setForecastIndicator(sf);
            entity.setTargetIndicator(st);
            entity.setPerformanceIndicator(sp);
            entity.setOther1Indicator(so1);
            entity.setOther2Indicator(so2);
            entity.setOther3Indicator(so3);
            for (int i = 0; i < indicators.size(); i++) {
                a = indicators.get(i).getActualIndicator();
                b = indicators.get(i).getBenchmarkIndicator();
                f = indicators.get(i).getForecastIndicator();
                t = indicators.get(i).getTargetIndicator();

                o1 = indicators.get(i).getOther1Indicator();
                o2 = indicators.get(i).getOther2Indicator();
                o3 = indicators.get(i).getOther3Indicator();
                indicatorBean.addValue(entity.getActualIndicator(), a, entity.getFormkind());
                indicatorBean.addValue(entity.getBenchmarkIndicator(), b, entity.getFormkind());
                indicatorBean.addValue(entity.getForecastIndicator(), f, entity.getFormkind());
                indicatorBean.addValue(entity.getTargetIndicator(), t, entity.getFormkind());
                if (o1 != null) {
                    indicatorBean.addValue(entity.getOther1Indicator(), o1, entity.getFormkind());
                }
                if (o2 != null) {
                    indicatorBean.addValue(entity.getOther2Indicator(), o2, entity.getFormkind());
                }
                if (o3 != null) {
                    indicatorBean.addValue(entity.getOther3Indicator(), o3, entity.getFormkind());
                }

            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            log4j.error(ex);
        }
        return entity;
    }

    public void divideByRateOther(Indicator i, int scale) {
        indicatorBean.divideByRate(i.getActualIndicator(), i.getRate(), scale);
        indicatorBean.divideByRate(i.getBenchmarkIndicator(), i.getRate(), scale);
        indicatorBean.divideByRate(i.getForecastIndicator(), i.getRate(), scale);
        indicatorBean.divideByRate(i.getTargetIndicator(), i.getRate(), scale);
        indicatorBean.divideByRate(i.getOther1Indicator(), i.getRate(), scale);
        indicatorBean.divideByRate(i.getOther2Indicator(), i.getRate(), scale);
        indicatorBean.divideByRate(i.getOther3Indicator(), i.getRate(), scale);
    }

    public List<Indicator> getSumIndicatorList() {
        return sumIndicatorList;
    }

    public void setSumIndicatorList(List<Indicator> sumIndicatorList) {
        this.sumIndicatorList = sumIndicatorList;
    }

    public Indicator getAllSumIndicator() {
        return allSumIndicator;
    }

    public void setAllSumIndicator(Indicator AllSumIndicator) {
        this.allSumIndicator = AllSumIndicator;
    }

    /**
     * @return the sumall
     */
    public BigDecimal getSumall() {
        return sumall;
    }

}
