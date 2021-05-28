/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.ejb.IndicatorDailyBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDaily;
import cn.hanbell.kpi.entity.IndicatorDetail;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class MachiningEfficiencyMailBean extends MailNotification {

    protected Indicator sumIndicator;

    public MachiningEfficiencyMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    protected IndicatorDaily findIndicatorDaily(IndicatorDetail entity, int m) {
        return indicatorBean.findIndicatorDailyByPIdDateAndType(entity.getId(), entity.getSeq(), m, entity.getType());
    }

    protected String getEfficiencyTable() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr>");
            sb.append("<th>类别</th><th>机台</th><th>计划工时</th><th>标准工时</th><th>报工工时</th>");
            sb.append("<th>设备停机</th><th>停线等待</th><th>除外换模</th><th>作业效率</th><th>生产效率</th><th>报工数量</th>");
            sb.append("<th>不良数</th><th>稼动率</th><th>产能效率</th><th>良率</th><th>OEE</th><th>机台数</th>");
            sb.append("</tr>");
            String detail, category;
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-HMC", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                detail = getHtmlTable(indicators, y, m, d, false);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
            }
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-VMC", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                detail = getHtmlTable(indicators, y, m, d, false);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
            }
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-HNL", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                detail = getHtmlTable(indicators, y, m, d, false);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
            }
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-VNL", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                detail = getHtmlTable(indicators, y, m, d, false);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
            }
            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) throws Exception {
        getData().clear();
        getData().put("sumPlanned", BigDecimal.ZERO);
        getData().put("sumStandard", BigDecimal.ZERO);
        getData().put("sumActual", BigDecimal.ZERO);
        getData().put("sumQuantity", BigDecimal.ZERO);
        getData().put("count", BigDecimal.ZERO);
        StringBuilder sb = new StringBuilder();
        try {
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d));
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    @Override
    protected String getHtmlTableRow(Indicator e, int y, int m, Date d) throws Exception {
        return getHtmlTableRow(e, y, m, d, null);
    }

    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d, String sumStyle) throws Exception {
        //获取需要取值栏位
        String col, mon;
        StringBuilder sb = new StringBuilder();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        IndicatorDaily td, bd, ad, o5d;
        Field f;
        mon = indicatorBean.getIndicatorColumn("N", m);
        col = indicatorBean.getIndicatorColumn("D", day);
        String value;
        if (indicator.getId() != -1) {
            //计划工时
            td = findIndicatorDaily(indicator.getTargetIndicator(), m);
            //标准工时
            bd = findIndicatorDaily(indicator.getBenchmarkIndicator(), m);
            //报工工时
            ad = findIndicatorDaily(indicator.getActualIndicator(), m);
            //报工数量
            o5d = findIndicatorDaily(indicator.getOther5Indicator(), m);
            try {
                BigDecimal plannedTime, standardTime, actualTime, qty;
                sb.append("<tr>");
                sb.append("<td>").append("").append("</td>");
                sb.append("<td>").append(indicator.getName()).append("</td>");
                //计划工时
                f = td.getClass().getDeclaredField(col);
                f.setAccessible(true);
                plannedTime = BigDecimal.valueOf(Double.parseDouble(f.get(td).toString()));
                value = decimalFormat.format(plannedTime);
                sb.append("<td>").append(value.equals("0") ? "" : value).append("</td>");
                //标准工时
                f = bd.getClass().getDeclaredField(col);
                f.setAccessible(true);
                standardTime = BigDecimal.valueOf(Double.parseDouble(f.get(bd).toString()));
                value = decimalFormat.format(standardTime);
                sb.append("<td>").append(value.equals("0") ? "-" : value).append("</td>");
                //报工工时
                f = ad.getClass().getDeclaredField(col);
                f.setAccessible(true);
                actualTime = BigDecimal.valueOf(Double.parseDouble(f.get(ad).toString()));
                value = decimalFormat.format(actualTime);
                sb.append("<td>").append(value.equals("0") ? "-" : value).append("</td>");
                //设备停机
                sb.append("<td>").append("").append("</td>");
                //停线等待
                sb.append("<td>").append("").append("</td>");
                //除外换模
                sb.append("<td>").append("").append("</td>");
                //作业效率
                sb.append("<td>").append("").append("</td>");

                //生产效率
                sb.append("<td>").append("").append("</td>");

                //报工数量
                f = o5d.getClass().getDeclaredField(col);
                f.setAccessible(true);
                qty = BigDecimal.valueOf(Double.parseDouble(f.get(o5d).toString()));
                value = decimalFormat.format(qty);
                sb.append("<td>").append(value.equals("0") ? "-" : value).append("</td>");
                //不良数量
                sb.append("<td>").append("").append("</td>");
                //稼动率
                sb.append("<td>").append("-").append("</td>");
                //产能效率
                if (actualTime.compareTo(BigDecimal.ZERO) != 0) {
                    sb.append("<td>").append(percentFormat(standardTime.divide(actualTime, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)))).append("</td>");
                } else {
                    sb.append("<td>").append("").append("</td>");
                }
                //良率
                sb.append("<td>").append("").append("</td>");
                //OEE
                sb.append("<td>").append("OEE").append("</td>");
                //机台数量
                if (indicator.getId() != -1) {
                    sb.append("<td>").append("").append("</td>");
                } else {
                    sb.append("<td>").append(getData().get("count")).append("</td>");
                }
                sb.append("</tr>");

                if (indicator.getId() != -1) {
                    sumAdditionalData("sumPlanned", plannedTime);
                    sumAdditionalData("sumStandard", standardTime);
                    sumAdditionalData("sumActual", actualTime);
                    sumAdditionalData("sumQuantity", qty);
                    sumAdditionalData("count", BigDecimal.ONE);
                }
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                throw ex;
            }
        } else {
            sb.append("<tr>");
            sb.append("<td style=${style}>").append(indicator.getCategory()).append("</td>");
            sb.append("<td style=${style}>").append("</td>");
            //计划工时
            value = decimalFormat.format(getData().get("sumPlanned"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            //标准工时
            value = decimalFormat.format(getData().get("sumStandard"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            //报工工时
            value = decimalFormat.format(getData().get("sumActual"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            //设备停机
            sb.append("<td style=${style}>").append("").append("</td>");
            //停线等待
            sb.append("<td style=${style}>").append("").append("</td>");
            //除外换模
            sb.append("<td style=${style}>").append("").append("</td>");
            //作业效率
            sb.append("<td style=${style}>").append("").append("</td>");

            //生产效率
            sb.append("<td style=${style}>").append("").append("</td>");

            //报工数量
            value = decimalFormat.format(getData().get("sumQuantity"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            //不良数量
            sb.append("<td style=${style}>").append("").append("</td>");
            //稼动率
            sb.append("<td style=${style}>").append("-").append("</td>");
            //产能效率
            if (getData().get("sumActual").compareTo(BigDecimal.ZERO) != 0) {
                sb.append("<td style=${style}>").append(percentFormat(getData().get("sumStandard").divide(getData().get("sumActual"), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)))).append("</td>");
            } else {
                sb.append("<td style=${style}>").append("").append("</td>");
            }
            //良率
            sb.append("<td style=${style}>").append("").append("</td>");
            //OEE
            sb.append("<td style=${style}>").append("OEE").append("</td>");
            //机台数量
            sb.append("<td style=${style}>").append(getData().get("count")).append("</td>");
            sb.append("</tr>");
        }
        if (sumStyle != null && !"".equals(sumStyle) && indicator.getId() == -1) {
            return sb.toString().replace("${style}", sumStyle);
        } else {
            return sb.toString().replace("${style}", "");
        }
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
        sb.append("<div style=\"text-align:center;width:100%; color:Red;\">日期:")
                .append(BaseLib.formatDate("yyyy-MM-dd", d)).append("</div>");
        sb.append("</div>");
        return sb.toString();
    }

    @Override
    protected String getMailBody() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：分钟</div>");
        sb.append(getEfficiencyTable());
        return sb.toString();
    }

}
