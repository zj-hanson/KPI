/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */

package cn.hanson.kpi.mail;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class CastingProductionMailBean extends MailNotification {

    protected List<Indicator> sumIndicatorList;
    protected Indicator sumIndicator;
    protected Indicator total;
    protected BigDecimal sum1 = BigDecimal.ZERO;
    protected BigDecimal sum2 = BigDecimal.ZERO;

    public CastingProductionMailBean() {
        this.sumIndicatorList = new ArrayList();
    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum)
        throws Exception {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        StringBuilder sb = new StringBuilder();
        try {
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d));
            }
            if (needsum) {
                sumIndicator = indicatorBean.getSumValue(indicators);
                if (sumIndicator != null) {
                    indicatorBean.updatePerformance(sumIndicator);
                    sb.append(getHtmlTableRow(getSumIndicator(), y, m, d, "'background-color:#ff8e67';"));
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

    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d, String sumStyle) throws Exception {
        // 获取需要取值栏位
        String col = indicatorBean.getIndicatorColumn(indicator.getFormtype(), m);
        StringBuilder sb = new StringBuilder();
        IndicatorDetail a = indicator.getActualIndicator();
        IndicatorDetail b = indicator.getBenchmarkIndicator();
        IndicatorDetail p = indicator.getPerformanceIndicator();
        IndicatorDetail t = indicator.getTargetIndicator();
        Field f;
        try {
            BigDecimal num1;
            if (indicator.getActualInterface() != null && indicator.getActualEJB() != null && indicator.getId() != -1) {
                // 本日出货
                Actual actualInterface = (Actual)Class.forName(indicator.getActualInterface()).newInstance();
                actualInterface.setEJB(indicator.getActualEJB());
                num1 = actualInterface.getValue(y, m, d, Calendar.DATE, actualInterface.getQueryParams())
                    .divide(indicator.getRate(), 2, RoundingMode.HALF_UP);
            } else {
                num1 = BigDecimal.ZERO;
            }
            if (indicator.getId() != -1) {
                sumAdditionalData("sum1", num1);
            }
            sb.append("<tr>");
            sb.append("<td style=${style}>").append(indicator.getName()).append("</td>");
            sb.append("<td style=${style}>")
                .append(decimalFormat.format(indicator.getId() != -1 ? num1 : getData().get("sum1"))).append("</td>");
            // 当月
            // 实际
            f = a.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td style=${style}>").append(decimalFormat.format(f.get(a))).append("</td>");
            // 目标
            f = t.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td style=${style}>").append(decimalFormat.format(f.get(t))).append("</td>");
            // 达成
            f = p.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td style=${style}>").append(percentFormat(f.get(p))).append("</td>");
            // 同期
            f = b.getClass().getDeclaredField(col);
            f.setAccessible(true);
            // sb.append("<td>").append(decimalFormat.format(f.get(b))).append("</td>");
            // 改成按天折算
            sb.append("<td style=${style}>")
                .append(decimalFormat.format(
                    indicatorBean.getValueOfDays(BigDecimal.valueOf(Double.valueOf(f.get(b).toString())), d, 0)))
                .append("</td>");
            // 成长
            // sb.append("<td style=${style}>").append(percentFormat(indicatorBean.getGrowth(a, b, m))).append("</td>");
            // 改成按天折算
            sb.append("<td style=${style}>").append(percentFormat(indicatorBean.getGrowth(a, b, m, d, 0)))
                .append("</td>");
            // 累计
            // 实际
            sb.append("<td style=${style}>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(a, m)))
                .append("</td>");
            // 目标
            // sb.append("<td style=${style}>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(t,
            // m))).append("</td>");
            // 改成按天折算
            sb.append("<td style=${style}>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(t, m, d)))
                .append("</td>");
            // 达成
            // sb.append("<td style=${style}>").append(percentFormat(indicatorBean.getAccumulatedPerformance(a, t,
            // m))).append("</td>");
            // 改成按天折算
            sb.append("<td style=${style}>")
                .append(percentFormat(indicatorBean.getAccumulatedPerformance(a, false, t, true, m, d)))
                .append("</td>");
            // 同期
            // sb.append("<td style=${style}>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(b,
            // m))).append("</td>");
            // 改成按天折算
            sb.append("<td style=${style}>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(b, m, d)))
                .append("</td>");
            // 成长
            // sb.append("<td style=${style}>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b,
            // m))).append("</td>");
            // 改成按天折算
            sb.append("<td style=${style}>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m, d)))
                .append("</td>");
            // 年度目标
            f = t.getClass().getDeclaredField("nfy");
            f.setAccessible(true);
            sb.append("<td style=${style}>").append(decimalFormat.format(f.get(t))).append("</td>");
            // 年度达成
            f = p.getClass().getDeclaredField("nfy");
            f.setAccessible(true);
            sb.append("<td style=${style}>").append(percentFormat(f.get(p))).append("</td>");
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

    @Override
    protected String getMailBody() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：吨</div>");
        sb.append(getCastingProductionTable());
        sb.append("<div class=\"tableTitle\">本月实际: MES实际报工数量</div>");
        sb.append("<div class=\"tableTitle\">本月目标: 生管每月计划数量</div>");
        sb.append("<div class=\"tableTitle\">本月达成: (本月实际/本月目标) ×100% </div>");
        sb.append("<div class=\"tableTitle\">年累计实际: 累计至报表查询日的实际生产数量</div>");
        sb.append("<div class=\"tableTitle\">年累计目标: 之前月份的累计目标 + 本月目标/本月天数 ×当前天数</div>");
        sb.append("<div class=\"tableTitle\">年累计达成: (年累计实际/年累计目标) ×100% </div>");
        return sb.toString();
    }

    protected String getCastingProductionTable() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\">类别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th></tr>");
            sb.append(
                "<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append(
                "<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");

            sum1 = BigDecimal.ZERO;
            sum2 = BigDecimal.ZERO;
            sumIndicatorList.clear();

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS铸造", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    indicatorBean.divideByRate(i, 2);
                }
                sb.append(getHtmlTable(indicators, y, m, d, true));
                total = getSumIndicator();
                total.setName("HS小计");
                sumIndicatorList.add(total);
                sum1 = sum1.add(getData().get("sum1"));
            }

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HY铸造", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    indicatorBean.divideByRate(i, 2);
                }
                sb.append(getHtmlTable(indicators, y, m, d, true));
                total = getSumIndicator();
                total.setName("HY小计");
                sumIndicatorList.add(total);
                sum1 = sum1.add(getData().get("sum1"));
            }

            total = indicatorBean.getSumValue(sumIndicatorList);
            if (total != null) {
                indicatorBean.updatePerformance(total);
                total.setName("HS/HY总计");
                getData().put("sum1", sum1);
                sb.append(getHtmlTableRow(total, y, m, d, "'background-color:#ff8e67';"));
            }

            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    /**
     * @return the sumIndicator
     */
    public Indicator getSumIndicator() {
        return sumIndicator;
    }

}
