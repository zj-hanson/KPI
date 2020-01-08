/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.ServiceMail;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class ServiceClosingQuarterSpecialMailBean extends ServiceMail {

    public ServiceClosingQuarterSpecialMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Hanbell</title>");
        sb.append(css);
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
        return "";
    }

    @Override
    protected String getMailBody() {
        indicator = indicatorBean.findByFormidYearAndDeptno("Q-A机体服务结案", y, "1A000");
        if (indicator == null) {
            throw new NullPointerException(String.format("指标编号%s:考核部门%s:不存在", "Q-A机体服务结案", "1A000"));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"content\">统计内容包含：客诉、赠送、技术支持、统包服务、巡检、收费服务及新机调试</div>");
        sb.append("<div class=\"divFoot\">制表日期：").append(BaseLib.formatDate("yyyy-MM-dd", new Date())).append("</div>");
        try {
            sb.append(getHtmlTableRow(indicator, y, m, d, "#FFFFFF"));
        } catch (Exception ex) {
            Logger.getLogger(ServiceClosingQuarterSpecialMailBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sb.toString();

    }

    @Override
    protected String getHtmlTableRow(Indicator e, int y, int m, Date d, String color) throws Exception {
        //获取需要取值栏位
        String col;
        StringBuilder sb = new StringBuilder();
        IndicatorDetail o3 = e.getOther3Indicator();
        IndicatorDetail o4 = e.getOther4Indicator();
        IndicatorDetail o1o2;
        Field f;
        BigDecimal v;
        Method setMethod;
        try {
            o1o2 = new IndicatorDetail();
            o1o2.setParent(e);
            o1o2.setType("客服单结案率");
            for (int i = getQuarter(getM()); i > 0; i--) {
                //---o2/o1
                //拿other4的季度值/other3的季度值 得出每月结案率填充到o1o2
                v = this.getAccumulatedValue(o3, o4, i);
                setMethod = o1o2.getClass().getDeclaredMethod(("setNq" + i), BigDecimal.class);
                setMethod.invoke(o1o2, v);
            }

            o3.setType(e.getOther3Label());
            o4.setType(e.getOther4Label());
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th>部门</th><th>统计项目</th><th>一季度</th><th>二季度</th><th>上半年</th><th>三季度</th><th>四季度</th><th>下半年</th><th>全年</th></tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td  rowspan=\"3\" colspan=\"1\" style=\"text-align: center;\">").append(e.getName()).append("</td>");
            sb.append("<td style=\"text-align: left;\">").append(o3.getType()).append("</td>");
            for (int i = 1; i < 5; i++) {
                col = indicatorBean.getIndicatorColumn("NQ", i);
                f = o3.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == getQuarter(m)) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(o3))).append("</td>");
                } else if (i > getQuarter(m)) {
                    sb.append("<td>").append(decimalFormat.format(f.get(o3)).equals("0") ? "" : decimalFormat.format(f.get(o3))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(o3))).append("</td>");
                }
                if (i == 2) {
                    sb.append("<td>").append(decimalFormat.format(o3.getNh1())).append("</td>");
                }
                if (i == 4) {
                    if (getQuarter(m) > 2) {
                        sb.append("<td>").append(decimalFormat.format(o3.getNh2())).append("</td>");
                    } else {
                        sb.append("<td>").append("").append("</td>");
                    }
                }
            }
            sb.append("<td>").append(decimalFormat.format(o3.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(o4.getType()).append("</td>");
            for (int i = 1; i < 5; i++) {
                col = indicatorBean.getIndicatorColumn("NQ", i);
                f = o4.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == getQuarter(m)) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(o4))).append("</td>");
                } else if (i > getQuarter(m)) {
                    sb.append("<td>").append(decimalFormat.format(f.get(o4)).equals("0") ? "" : decimalFormat.format(f.get(o4))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(o4))).append("</td>");
                }
                if (i == 2) {
                    sb.append("<td>").append(decimalFormat.format(o4.getNh1())).append("</td>");
                }
                if (i == 4) {
                    if (getQuarter(m) > 2) {
                        sb.append("<td>").append(decimalFormat.format(o4.getNh2())).append("</td>");
                    } else {
                        sb.append("<td>").append("").append("</td>");
                    }
                }
            }
            sb.append("<td>").append(decimalFormat.format(o4.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(o1o2.getType()).append("</td>");
            for (int i = 1; i < 5; i++) {
                col = indicatorBean.getIndicatorColumn("NQ", i);
                f = o1o2.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == getQuarter(m)) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(o1o2))).append("</td>");
                } else if (i > getQuarter(m)) {
                    sb.append("<td>").append(percentFormat(f.get(o1o2)).equals("0.00%") ? "" : percentFormat(f.get(o1o2))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(o1o2))).append("</td>");
                }
                if (i == 2) {
                    sb.append("<td>").append(percentFormat(sumPertformance(o4.getNh1(), o3.getNh1()))).append("</td>");
                }
                if (i == 4) {
                    if (getQuarter(m) > 2) {
                        sb.append("<td>").append(percentFormat(sumPertformance(o4.getNh2(), o3.getNh2()))).append("</td>");
                    } else {
                        sb.append("<td>").append("").append("</td>");
                    }
                }
            }
            sb.append("<td>").append(percentFormat(sumPertformance(o4.getNfy(), o3.getNfy()))).append("</td>");
            sb.append("</tr>");
            sb.append("</table></div>");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

    public int getQuarter(int month) {
        int aa = 0;
        if (month >= 1 && month <= 3) {
            return aa = 1;
        } else if (month >= 4 && month <= 6) {
            return aa = 2;
        } else if (month >= 7 && month <= 9) {
            return aa = 3;
        } else if (month >= 10 && month <= 12) {
            return aa = 4;
        }
        return aa;
    }

    //q 季度  m 月份
    public BigDecimal getAccumulatedValue(IndicatorDetail o1, IndicatorDetail o2, int q) {
        String mon;
        BigDecimal total1 = BigDecimal.ZERO;
        BigDecimal total2 = BigDecimal.ZERO;
        Field f;
        try {
            mon = indicatorBean.getIndicatorColumn("NQ", q);
            f = o1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            total1 = BigDecimal.valueOf(Double.valueOf(f.get(o1).toString()));
            mon = indicatorBean.getIndicatorColumn("NQ", q);
            f = o2.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            total2 = BigDecimal.valueOf(Double.valueOf(f.get(o2).toString()));
            if (total1.compareTo(BigDecimal.ZERO) != 0) {
                return total2.divide(total1, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d));
            } else {
                if (total2.compareTo(BigDecimal.ZERO) == 0) {
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
