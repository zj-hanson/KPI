/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author C1879 R冷媒均价
 */
public abstract class RAveragePriceMailBean extends MailNotification {

    protected Indicator indicator;
    protected Indicator sumIndicator;
    protected List<Indicator> sumlistIndicators;
    protected String servicecss = "<style type='text/css'>body{font-size:14px;font-weight:bold;}div.content{margin:auto;text-align:center;}div.tbl{margin-bottom:20px;}table{margin:auto;border-spacing:0px;border:1px solid #A2C0DA;}th,td{padding:5px;border-collapse:collapse;text-align:left;}th{border:1px solid #000000;text-align:center;font-weight:bold;}td{border:1px solid #000000;text-align:right;}.title{font-size:14px;font-weight:bold;}.foot{font-size:14px;color:Red;}.divFoot{text-align:right;font-weight:bold;height:20px;width:100%;}.divFoot1{text-align:left;height:20px;width:100%;font-weight:bold;}div.tableTitle{float:left;font-size:14px;font-weight:bold;text-align:left;}</style>";

    public RAveragePriceMailBean() {

    }

    @Override
    protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Hanbell</title>");
        sb.append(servicecss);
        sb.append("</head><body><div style=\"margin: auto;text-align: center;\">");
        sb.append("<div style=\"width:100%\" class=\"title\">");
        sb.append("<div style=\"text-align:center;width:100%\">上海汉钟精机股份有限公司</div>");
        sb.append("<div style=\"text-align:center;width:100%\">").append(mailSubject).append("</div>");
        sb.append("<div style=\"text-align:center;width:100%; color:Red;\">日期:").append(BaseLib.formatDate("yyyy-MM-dd", getD())).append("</div>");
        sb.append("</div>");

        return sb.toString();
        
    }

    @Override
    protected String getMailBody() {
        indicator = indicatorBean.findByFormidYearAndDeptno("R-R冷媒销售均价", y, "1F000");
        if (indicator == null) {
            throw new NullPointerException(String.format("指标编号%s:考核部门%s:不存在", "R-R冷媒销售均价", "1F000"));
        }
        indicators.clear();
        indicators = indicatorBean.findByPId(indicator.getId());
        indicatorBean.getEntityManager().clear();
        //指标排序
        indicators.sort((Indicator o1, Indicator o2) -> {
            if (o1.getSortid() > o2.getSortid()) {
                return 1;
            } else {
                return -1;
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：").append(indicator.getUnit()).append("</div>");
        sb.append(getHtmlTable(indicators, y, m, d, true));
        return sb.toString();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        getData().clear();
        StringBuilder sb = new StringBuilder();
        sumlistIndicators = new ArrayList<>();
        int size = 0;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th width=\"5%\">项目</th><th width=\"5%\" >统计项</th><th>全年目标</th><th>01月</th><th>02月</th><th>03月</th><th>04月</th><th>05月</th><th>06月</th><th>07月</th><th>08月</th>");
            sb.append("<th>09月</th><th>10月</th><th>11月</th><th>12月</th><th>合计</th><th>达成率</th></tr>");
            for (Indicator i : indicatorList) {
                size++;
                if (size % 2 != 0) {
                    sb.append(getHtmlTableRow(i, y, m, d, "#D3D7D4"));
                } else {
                    sb.append(getHtmlTableRow(i, y, m, d, "#FFFFFF"));
                }
            }
            //计算产品合计
            sumIndicator = indicatorBean.getSumValue(sumlistIndicators);
            sb.append(getHtmlTableRow(sumIndicator, y, m, d, "#D3D7D4"));
            sb.append("</table></div>");
            addAttachments(null);
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        return "";
    }

    
    protected String getHtmlTableRow(Indicator e, int y, int m, Date d, String color) throws Exception {
        //获取需要取值栏位
        String col, mon;
        StringBuilder sb = new StringBuilder();
        Field f;
        mon = indicatorBean.getIndicatorColumn("N", getM());
        BigDecimal v;
        Method setMethod;
        try {
            String associatedIndicator = e.getAssociatedIndicator();
            if (associatedIndicator != null && !"".equals(associatedIndicator) && e.getId() != -1) {
                Indicator quantityi, amounti;
                String[] arr = associatedIndicator.split(";");
                quantityi = indicatorBean.findByFormidYearAndDeptno(arr[0].trim(), y, arr[2].trim());
                amounti = indicatorBean.findByFormidYearAndDeptno(arr[1].trim(), y, arr[2].trim());
                //实际台数
                IndicatorDetail qa = new IndicatorDetail();
                qa.setType("A");
                qa.setParent(quantityi);
                //实际金额
                IndicatorDetail ab = new IndicatorDetail();
                ab.setType("A");
                ab.setParent(amounti);
                if (e.getOther3Indicator() != null && e.getOther4Indicator() != null) {
                    for (int i = getM(); i > 0; i--) {
                        ///实际台数 + 录入柯茂数据 - 销往柯茂数据
                        v = getNValue(quantityi.getActualIndicator(), i).add(getNValue(e.getOther1Indicator(), i)).subtract(getNValue(e.getOther3Indicator(), i));
                        setMethod = qa.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(qa, v);
                        //实际金额  + 录入柯茂数据 - 销往柯茂数据
                        v = getNValue(amounti.getActualIndicator(), i).add(getNValue(e.getOther2Indicator(), i)).subtract(getNValue(e.getOther4Indicator(), i));
                        setMethod = ab.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(ab, v);
                    }
                } else {
                    for (int i = getM(); i > 0; i--) {
                        ///实际台数
                        v = getNValue(quantityi.getActualIndicator(), i).add(getNValue(e.getOther1Indicator(), i));
                        setMethod = qa.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(qa, v);
                        //实际金额
                        v = getNValue(amounti.getActualIndicator(), i).add(getNValue(e.getOther2Indicator(), i));
                        setMethod = ab.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                        setMethod.invoke(ab, v);
                    }
                }
                //实际台数
                e.setActualIndicator(qa);
                //实际金额
                e.setBenchmarkIndicator(ab);
                //目标台数
                e.setTargetIndicator(quantityi.getTargetIndicator());
                //目标金额
                e.setForecastIndicator(amounti.getTargetIndicator());
                sumlistIndicators.add(e);
            }
            //实际台数
            IndicatorDetail q = e.getActualIndicator();
            //实际金额
            IndicatorDetail a = e.getBenchmarkIndicator();
            //目标台数
            IndicatorDetail tq = e.getTargetIndicator();
            //目标金额
            IndicatorDetail ta = e.getForecastIndicator();
            //均价
            IndicatorDetail avg = new IndicatorDetail();
            avg.setType("A");
            avg.setParent(e);
            for (int i = getM(); i > 0; i--) {
                //实际台数
                v = getAvgPrice(getNValue(q, i), getNValue(a, i));
                setMethod = avg.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(avg, v);
            }
            sb.append("<tr style=\"background:").append(color).append(";\"><td  rowspan=\"3\" colspan=\"1\" style=\"text-align: center;\">").append(e.getName().replace("R销售均价", "")).append("</td>");
            sb.append("<td  style=\"text-align: right;\">台数</td>");
            sb.append("<td>").append(decimalFormat.format(tq.getNfy())).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = q.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(q))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(q)).equals("0") ? "" : decimalFormat.format(f.get(q))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(q))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(q.getNfy())).append("</td>");
            sb.append("<td>").append(percentFormat(getPerformance(q.getNfy(), tq.getNfy()))).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: right;\">金额</td>");
            sb.append("<td>").append(decimalFormat.format(ta.getNfy())).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(a))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(a)).equals("0") ? "" : decimalFormat.format(f.get(a))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(a))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(a.getNfy())).append("</td>");
            sb.append("<td>").append(percentFormat(getPerformance(a.getNfy(), ta.getNfy()))).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: right;\">平均售价</td>");
            sb.append("<td>").append(decimalFormat.format(getAvgPrice(tq.getNfy(), ta.getNfy()))).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = avg.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(avg))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(avg)).equals("0") ? "" : decimalFormat.format(f.get(avg))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(avg))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(getAvgPrice(q.getNfy(), a.getNfy()))).append("</td>");
            sb.append("<td>").append(percentFormat(getPerformance(getAvgPrice(q.getNfy(), a.getNfy()), getAvgPrice(tq.getNfy(), ta.getNfy())))).append("</td>");
            sb.append("</tr>");

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

    public BigDecimal getPerformance(BigDecimal a1, BigDecimal a2) {
        //计算
        if (a2.compareTo(BigDecimal.ZERO) != 0) {
            return a1.divide(a2, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d));
        } else {
            if (a1.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            } else {
                return BigDecimal.ONE.multiply(BigDecimal.valueOf(100d));
            }
        }
    }

    public BigDecimal getAvgPrice(Object quantity, Object amount) {
        double q = Double.valueOf(quantity.toString());
        double a = Double.valueOf(amount.toString());
        if (q > 0 && a > 0) {
            return BigDecimal.valueOf(a).divide(BigDecimal.valueOf(q), 2, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getNValue(IndicatorDetail entity, int m) {
        String mon;
        BigDecimal value = BigDecimal.ZERO;
        Field f;
        for (int i = 1; i <= m; i++) {
            try {
                mon = indicatorBean.getIndicatorColumn("N", i);
                f = entity.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                value = BigDecimal.valueOf(Double.valueOf(f.get(entity).toString()));
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                value = BigDecimal.ZERO;
            }
        }
        return value;
    }
    
    @Override
    public void setD(Date d) {
        this.d = d;
        c.setTime(d);
        this.y = c.get(Calendar.YEAR);
        this.m = c.get(Calendar.MONTH) + 1;
    }

    /**
     * @return the sumIndicator
     */
    public Indicator getSumIndicator() {
        return sumIndicator;
    }

}
