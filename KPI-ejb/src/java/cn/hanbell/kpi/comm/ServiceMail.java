/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 *
 * @author C1879
 */
public abstract class ServiceMail extends MailNotification {

    protected Indicator indicator;
    protected String color;

    public ServiceMail() {

    }

    protected String servicecss = "<style type='text/css'>body{font-size:14px;font-weight:bold;}div.content{margin:auto;text-align:center;}div.tbl{margin-bottom:20px;}table{margin:auto;border-spacing:0px;border:1px solid #A2C0DA;}th,td{padding:5px;border-collapse:collapse;text-align:left;}th{border:1px solid #000000;text-align:center;font-weight:bold;}td{border:1px solid #000000;text-align:right;}.title{font-size:14px;font-weight:bold;}.foot{font-size:14px;color:Red;}.divFoot{text-align:right;font-weight:bold;height:20px;width:100%;}.divFoot1{text-align:left;height:20px;width:100%;font-weight:bold;}div.tableTitle{float:left;font-size:14px;font-weight:bold;text-align:left;}</style>";

    @Override
    protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Hanbell</title>");
        sb.append(servicecss);
        sb.append("</head><body><div style=\"margin: auto;text-align: center;\">");
        sb.append("<div style=\"width:100%\" class=\"title\">");
        sb.append("<div style=\"text-align:center;width:100%\">上海汉钟精机股份有限公司</div>");
        sb.append("<div style=\"text-align:center;width:100%\">").append(mailSubject).append("</div>");
        sb.append("<div style=\"text-align:center;width:100%; color:Red;\">日期:").append(String.valueOf(y)).append("年").append(String.valueOf(m)).append("月</div>");
        sb.append("</div>");
        return sb.toString();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        getData().clear();
        StringBuilder sb = new StringBuilder();
        int size = 0;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th width=\"10%\" >区域</th><th width=\"12%\">项目</th><th>01月</th><th>02月</th><th>03月</th><th>04月</th><th>05月</th><th>06月</th><th>07月</th><th>08月</th>");
            sb.append("<th>09月</th><th>10月</th><th>11月</th><th>12月</th><th>总计</th></tr>");
            for (Indicator i : indicatorList) {
                size++;
                if (size % 2 != 0) {
                    color = "#D3D7D4";
                    sb.append(getHtmlTableRow(i, y, m, d));
                } else {
                    color = "#FFFFFF";
                    sb.append(getHtmlTableRow(i, y, m, d));
                }
            }
            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        return getHtmlTableRow(indicator, y, m, d, color);
    }

    protected String getHtmlTableRow(Indicator e, int y, int m, Date d, String color) throws Exception {
        //获取需要取值栏位
        String col, mon;
        StringBuilder sb = new StringBuilder();
        IndicatorDetail o1 = e.getOther1Indicator();
        IndicatorDetail o2 = e.getOther2Indicator();
        IndicatorDetail o3 = e.getOther3Indicator();
        IndicatorDetail o4 = e.getOther4Indicator();
        IndicatorDetail o1o2;
        IndicatorDetail o3o4;
        Field f;
        mon = indicatorBean.getIndicatorColumn("N", getM());
        BigDecimal v;
        Method setMethod;
        try {
            o1o2 = new IndicatorDetail();
            o1o2.setParent(e);
            o1o2.setType("跨区服务件数占比");
            o3o4 = new IndicatorDetail();
            o3o4.setParent(e);
            o3o4.setType("跨区服务金额占比");
            for (int i = getM(); i > 0; i--) {
                //---o1/o2
                v = getAccumulatedValue(o1, o2, i);
                setMethod = o1o2.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(o1o2, v);
                //o3 o4
                v = getAccumulatedValue(o3, o4, i);
                setMethod = o3o4.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(o3o4, v);
            }

            o1.setType(e.getOther1Label());
            o2.setType(e.getOther2Label());
            o3.setType(e.getOther3Label());
            o4.setType(e.getOther4Label());
            sb.append("<tr style=\"background:").append(color).append(";\"><td  rowspan=\"6\" colspan=\"1\" style=\"text-align: center;\">").append(e.getName()).append("</td>");
            sb.append("<td style=\"text-align: left;\">").append(o1.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = o1.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(o1))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(o1)).equals("0") ? "" : decimalFormat.format(f.get(o1))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(o1))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(o1.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(o2.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = o2.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(o2))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(o2)).equals("0") ? "" : decimalFormat.format(f.get(o2))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(o2))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(o2.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(o1o2.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = o1o2.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(o1o2))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(percentFormat(f.get(o1o2)).equals("0.00%") ? "" : percentFormat(f.get(o1o2))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(o1o2))).append("</td>");
                }
            }
            sb.append("<td>").append(percentFormat(sumPertformance(o1.getNfy(), o2.getNfy()))).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(o3.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = o3.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(o3))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(o3)).equals("0") ? "" : decimalFormat.format(f.get(o3))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(o3))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(o3.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(o4.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = o4.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(o4))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(o4)).equals("0") ? "" : decimalFormat.format(f.get(o4))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(o4))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(o4.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(o3o4.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = o3o4.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(o3o4))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(percentFormat(f.get(o3o4)).equals("0.00%") ? "" : percentFormat(f.get(o3o4))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(o3o4))).append("</td>");
                }
            }
            sb.append("<td>").append(percentFormat(sumPertformance(o3.getNfy(), o4.getNfy()))).append("</td>");
            sb.append("</tr>");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

    // a1/a2
    public BigDecimal sumPertformance(BigDecimal a1, BigDecimal a2) {
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

    public BigDecimal getAccumulatedValue(IndicatorDetail o1, IndicatorDetail o2, int m) {
        String mon;
        BigDecimal total1 = BigDecimal.ZERO;
        BigDecimal total2 = BigDecimal.ZERO;
        Field f;
        try {
            mon = indicatorBean.getIndicatorColumn("N", m);
            f = o1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            total1 = BigDecimal.valueOf(Double.valueOf(f.get(o1).toString()));
            mon = indicatorBean.getIndicatorColumn("N", m);
            f = o2.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            total2 = BigDecimal.valueOf(Double.valueOf(f.get(o2).toString()));
            if (total2.compareTo(BigDecimal.ZERO) != 0) {
                return total1.divide(total2, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d));
            } else {
                if (total1.compareTo(BigDecimal.ZERO) == 0) {
                    return BigDecimal.ZERO;
                } else {
                    return BigDecimal.ONE.multiply(BigDecimal.valueOf(100d));
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            log4j.error(ex);
        }
        return total1;
    }

}
