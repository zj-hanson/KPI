/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.mail;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanson.kpi.evaluation.ShipmentPredictAmountHY;
import cn.hanson.kpi.evaluation.ShipmentPredictTonHY;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class CPShipmentMailBean extends ShipmentMail {

    public List<Indicator> sumIndicatorList;//大合计集合
    public Indicator total;//大合计对象
    protected BigDecimal sum1 = BigDecimal.ZERO;
    protected BigDecimal sum2 = BigDecimal.ZERO;
    private BigDecimal totalActualValue;

    public CPShipmentMailBean() {
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
    protected String getTableHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tbl\"><table width=\"100%\">");
        sb.append("<tr><th rowspan=\"2\" colspan=\"1\">类别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
        sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
        sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th>");
        sb.append("<th rowspan=\"2\" colspan=\"1\">订单未交</th><th rowspan=\"2\" colspan=\"1\">年度比重</th></tr>");
        sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("</tr>");
        return sb.toString();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        return getHtmlTable(indicatorList, y, m, d, needsum, "合计");
    }

    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum, String sumTitle) {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        getData().put("sum2", BigDecimal.ZERO);
        StringBuilder sb = new StringBuilder();
        try {
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d));
            }
            if (needsum) {
                sumIndicator = indicatorBean.getSumValue(indicators);
                if (sumIndicator != null) {
                    if (sumTitle != null) {
                        sumIndicator.setName(sumTitle);
                    }
                    indicatorBean.updatePerformance(sumIndicator);
                    sumIndicatorList.add(sumIndicator);
                    sb.append(getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';"));
                }
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        return getHtmlTableRow(indicator, y, m, d, null);
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d, String sumStyle) throws Exception {
        //获取需要取值栏位
        String col = indicatorBean.getIndicatorColumn(indicator.getFormtype(), m);
        StringBuilder sb = new StringBuilder();
        IndicatorDetail a = indicator.getActualIndicator();
        IndicatorDetail b = indicator.getBenchmarkIndicator();
        IndicatorDetail p = indicator.getPerformanceIndicator();
        IndicatorDetail t = indicator.getTargetIndicator();
        Field f;
        try {
            BigDecimal num1, num2, proportion;
            if (indicator.getActualInterface() != null && indicator.getActualEJB() != null && indicator.getId() != -1) {
                //本日出货
                Actual actualInterface = (Actual) Class.forName(indicator.getActualInterface()).newInstance();
                actualInterface.setEJB(indicator.getActualEJB());
                num1 = actualInterface.getValue(y, m, d, Calendar.DATE, actualInterface.getQueryParams()).divide(indicator.getRate(), 2, RoundingMode.HALF_UP);
                //未交订单
                if (salesOrder != null) {
                    salesOrder.setEJB(indicator.getActualEJB());
                    num2 = salesOrder.getNotDelivery(d, actualInterface.getQueryParams()).divide(indicator.getRate(), 2, RoundingMode.HALF_UP);
                } else {
                    num2 = BigDecimal.ZERO;
                }
            } else {
                num1 = BigDecimal.ZERO;
                num2 = BigDecimal.ZERO;
            }
            if (indicator.getId() != -1) {
                sumAdditionalData("sum1", num1);
                sumAdditionalData("sum2", num2);
            }
            sb.append("<tr>");
            sb.append("<td style=${style}>").append(indicator.getName()).append("</td>");
            sb.append("<td style=${style}>").append(decimalFormat.format(indicator.getId() != -1 ? num1 : getData().get("sum1"))).append("</td>");
            //当月
            //实际
            f = a.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td style=${style}>").append(decimalFormat.format(f.get(a))).append("</td>");
            //目标
            f = t.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td style=${style}>").append(decimalFormat.format(f.get(t))).append("</td>");
            //达成
            f = p.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td style=${style}>").append(percentFormat(f.get(p))).append("</td>");
            //同期
            f = b.getClass().getDeclaredField(col);
            f.setAccessible(true);
            //sb.append("<td>").append(decimalFormat.format(f.get(b))).append("</td>");
            //改成按天折算
            sb.append("<td style=${style}>").append(decimalFormat.format(indicatorBean.getValueOfDays(BigDecimal.valueOf(Double.valueOf(f.get(b).toString())), d, 0))).append("</td>");
            //成长
            //sb.append("<td style=${style}>").append(percentFormat(indicatorBean.getGrowth(a, b, m))).append("</td>");
            //改成按天折算
            sb.append("<td style=${style}>").append(percentFormat(indicatorBean.getGrowth(a, b, m, d, 0))).append("</td>");
            //累计
            //实际
            sb.append("<td style=${style}>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(a, m))).append("</td>");
            //目标
            //sb.append("<td style=${style}>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(t, m))).append("</td>");
            //改成按天折算
            sb.append("<td style=${style}>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(t, m, d))).append("</td>");
            //达成
            //sb.append("<td style=${style}>").append(percentFormat(indicatorBean.getAccumulatedPerformance(a, t, m))).append("</td>");
            //改成按天折算
            sb.append("<td style=${style}>").append(percentFormat(indicatorBean.getAccumulatedPerformance(a, false, t, true, m, d))).append("</td>");
            //同期
            //sb.append("<td style=${style}>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(b, m))).append("</td>");
            //改成按天折算
            sb.append("<td style=${style}>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(b, m, d))).append("</td>");
            //成长
            //sb.append("<td style=${style}>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m))).append("</td>");
            //改成按天折算
            sb.append("<td style=${style}>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m, d))).append("</td>");
            //年度目标
            f = t.getClass().getDeclaredField("nfy");
            f.setAccessible(true);
            sb.append("<td style=${style}>").append(decimalFormat.format(f.get(t))).append("</td>");
            //年度达成
            f = p.getClass().getDeclaredField("nfy");
            f.setAccessible(true);
            sb.append("<td style=${style}>").append(percentFormat(f.get(p))).append("</td>");
            sb.append("<td style=${style}>").append(decimalFormat.format(indicator.getId() != -1 ? num2 : getData().get("sum2"))).append("</td>");
            //年度比重
            if (indicator.getId() == -1 && totalActualValue.compareTo(BigDecimal.ZERO) != 0) {
                sb.append("<td style=${style}>").append(percentFormat(indicatorBean.getAccumulatedValue(a, m).divide(totalActualValue, 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(100)))).append("</td>");
            } else {
                sb.append("<td style=${style}></td>");
            }
            sb.append("</tr>");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }

        if (sumStyle != null && !"".equals(sumStyle) && indicator.getId() == -1) {
            return sb.toString().replace("${style}", sumStyle);
        } else {
            return sb.toString().replace("${style}", "");
        }
    }

    public void setTotalActualValue(String aa) {
        String[] arr = aa.split(",");
        try {
            totalActualValue = BigDecimal.ZERO;
            List<Indicator> list = new ArrayList<>();
            for (int i = 0; i < arr.length; i++) {
                List<Indicator> data = indicatorBean.findByCategoryAndYear(arr[i], y);
                indicatorBean.getEntityManager().clear();
                for (Indicator entity : data) {
                    indicatorBean.divideByRate(entity, 2);
                    list.add(entity);
                }
            }
            if (!list.isEmpty()) {
                total = indicatorBean.getSumValue(list);
                totalActualValue = total.getActualIndicator().getNfy();
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
    }

    @Override
    public String getMailBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\"></div>");
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
        sb.append("<div class=\"tableTitle\">本月实际: 本月累计出货 - 本月累计退货</div>");
        sb.append("<div class=\"tableTitle\">本月目标: 年度方针设定的月完成目标</div>");
        sb.append("<div class=\"tableTitle\">本月达成: (本月实际/本月目标) ×100% </div>");
        sb.append("<div class=\"tableTitle\">年累计实际: 累计至报表查询日的出货 - 累计至报表查询日的退货</div>");
        sb.append("<div class=\"tableTitle\">年累计目标: 之前月份的累计目标 + 本月目标/本月天数x当前天数</div>");
        sb.append("<div class=\"tableTitle\">年累计达成: (年累计实际/年累计目标) ×100% </div>");
        sb.append("<div class=\"tableTitle\">年度比重: (分类年实际/分类年合计) ×100% </div>");
        sb.append("<div class=\"tableTitle\"><span style=\"color:red\">注1：报表数据已做合并抵消，扣除汉扬销售汉声部分</span></div>");
        sb.append("<div class=\"tableTitle\"><span style=\"color:red\">注2：因当日出货单可能延后确认，故报表获取截至昨天之出货数据</span></div>");
        return sb.toString();
    }

    protected String getQuantityTable() {
        sum1 = BigDecimal.ZERO;
        sum2 = BigDecimal.ZERO;
        sumIndicatorList.clear();

        salesOrder = new ShipmentPredictTonHY();

        setTotalActualValue("汉声依SHB客户材质出货重量,汉声依THB客户材质出货重量,汉声依OTH客户材质出货重量");

        StringBuilder sb = new StringBuilder();
        sb.append(getTableHead());

        //SHB
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依SHB客户材质出货重量", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(indicators, y, m, d, true, "小计"));
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
        } else {
            sb.append("");
        }
        //THB
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依THB客户材质出货重量", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(indicators, y, m, d, true, "小计"));
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
        } else {
            sb.append("");
        }
        //OTH
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依OTH客户材质出货重量", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(indicators, y, m, d, true, "小计"));
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
        } else {
            sb.append("");
        }
        //最后的总合计
        try {
            total = indicatorBean.getSumValue(sumIndicatorList);
            if (total != null) {
                total.setName("合计");
                getData().put("sum1", sum1);
                getData().put("sum2", sum2);
                indicatorBean.updatePerformance(total);
                sb.append(getHtmlTableRow(total, y, m, d, "'background-color:#ff8e67';"));
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        sb.append("</table></div>");
        return sb.toString();

    }

    protected String getAmountTable() {
        sum1 = BigDecimal.ZERO;
        sum2 = BigDecimal.ZERO;
        sumIndicatorList.clear();

        salesOrder = new ShipmentPredictAmountHY();

        setTotalActualValue("汉声依SHB客户材质出货金额,汉声依THB客户材质出货金额,汉声依OTH客户材质出货金额");

        StringBuilder sb = new StringBuilder();
        sb.append(getTableHead());

        //SHB
        indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("汉声依SHB客户材质出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true, "小计"));
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
        } else {
            sb.append("");
        }
        //THB
        indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("汉声依THB客户材质出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true, "小计"));
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
        } else {
            sb.append("");
        }
        //OTH
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("汉声依OTH客户材质出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true, "小计"));
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
        } else {
            sb.append("");
        }
        //最后的总合计
        try {
            total = indicatorBean.getSumValue(sumIndicatorList);
            if (total != null) {
                getData().put("sum1", sum1);
                getData().put("sum2", sum2);
                total.setName("合计");
                indicatorBean.updatePerformance(total);
                sb.append(getHtmlTableRow(total, y, m, d, "'background-color:#ff8e67';"));
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        sb.append("</table></div>");
        return sb.toString();
    }

    //按产品别分类重量
    protected String getVarietyTonTable() {
        sum1 = BigDecimal.ZERO;
        sum2 = BigDecimal.ZERO;
        sumIndicatorList.clear();

        salesOrder = null;

        setTotalActualValue("汉声依种类别出货重量");

        StringBuilder sb = new StringBuilder();
        sb.append(getTableHead());
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依种类别出货重量", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
            sb.append("</table></div>");
            return sb.toString();
        } else {
            return "汉声依种类别出货重量";
        }
    }

    //按产品别分类金额
    protected String getVarietyAmtsTable() {
        sum1 = BigDecimal.ZERO;
        sum2 = BigDecimal.ZERO;
        sumIndicatorList.clear();

        salesOrder = null;

        setTotalActualValue("汉声依种类别出货金额");

        StringBuilder sb = new StringBuilder();
        sb.append(getTableHead());
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("汉声依种类别出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
            sb.append("</table></div>");
            return sb.toString();
        } else {
            return "汉声依种类别出货金额";
        }
    }

    public List<Indicator> getSumIndicatorList() {
        return sumIndicatorList;
    }

    public Indicator getTotal() {
        return total;
    }

}
