/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.ServiceMail;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.evaluation.ServiceCustomer;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class ServiceClosingQuarterMailBean extends ServiceMail {

    public ServiceClosingQuarterMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getMailBody() {
        indicator = indicatorBean.findByFormidYearAndDeptno("Q-服务结案单", y, "1A000");
        if (indicator == null) {
            throw new NullPointerException(String.format("指标编号%s:考核部门%s:不存在", "Q-服务结案单", "1A000"));
        }
        indicators.clear();
        indicators = indicatorBean.findByPIdAndYear(indicator.getId(), y);
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
        sb.append("<div class=\"content\">统计内容包含：客诉、赠送、技术支持、统包服务、巡检、收费服务及新机调试</div>");
        sb.append("<div class=\"divFoot\">制表日期：").append(BaseLib.formatDate("yyyy-MM-dd", d)).append("</div>");
        sb.append(getHtmlTable(indicators, y, m, d, true));
        return sb.toString();

    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        getData().clear();
        StringBuilder sb = new StringBuilder();
        int size = 0;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th>部门</th><th>统计项目</th><th>一季度</th><th>二季度</th><th>上半年</th><th>三季度</th><th>四季度</th><th>下半年</th><th>全年</th></tr>");
            for (Indicator i : indicatorList) {
                size++;
                if (size % 2 != 0) {
                    sb.append(getHtmlTableRow(i, y, m, d, "#D3D7D4"));
                } else {
                    sb.append(getHtmlTableRow(i, y, m, d, "#FFFFFF"));
                }
            }
            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    @Override
    protected String getHtmlTableRow(Indicator e, int y, int m, Date d, String color) throws Exception {
        //获取需要取值栏位
        String col;
        StringBuilder sb = new StringBuilder();
        IndicatorDetail o4 = e.getOther4Indicator();
        IndicatorDetail o5 = e.getOther5Indicator();
        IndicatorDetail o1o2;
        BigDecimal nh1 = BigDecimal.ZERO;
        BigDecimal nh2 = BigDecimal.ZERO;
        BigDecimal nfy = BigDecimal.ZERO;
        Field f;
        BigDecimal v;
        Method setMethod;
        try {
            if (e.getOther1EJB() != null && !"".equals(e.getOther1EJB())) {
                //上半年
                ServiceCustomer other4Interface = (ServiceCustomer) Class.forName(e.getOther4Interface()).newInstance();
                other4Interface.setEJB(e.getOther1EJB());
                other4Interface.getQueryParams().put("status", "nh1");
                nh1 = other4Interface.getQuarterValue(y, m, other4Interface.getQueryParams());
                other4Interface.getQueryParams().remove("status");
                //下半年
                other4Interface.getQueryParams().put("status", "nh2");
                nh2 = other4Interface.getQuarterValue(y, m, other4Interface.getQueryParams());
                other4Interface.getQueryParams().remove("status");
                //全年
                other4Interface.getQueryParams().put("status", "nfy");
                nfy = other4Interface.getQuarterValue(y, m, other4Interface.getQueryParams());
            }
            o1o2 = new IndicatorDetail();
            o1o2.setParent(e);
            o1o2.setType("客服单结案率");
            for (int i = getM(); i > 0; i--) {
                //---o2/o1
                //拿other5的月份值/other4的季度值 得出每月结案率填充到o1o2
                v = this.getAccumulatedValue(o4, o5, getQuarter(i), i);
                setMethod = o1o2.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(o1o2, v);
            }

            o4.setType(e.getOther4Label());
            o5.setType(e.getOther5Label());
            sb.append("<tr style=\"background:").append(color).append(";\"><td  rowspan=\"3\" colspan=\"1\" style=\"text-align: center;\">").append(e.getName()).append("</td>");
            sb.append("<td style=\"text-align: left;\">").append(o4.getType()).append("</td>");
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
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(o5.getType()).append("</td>");
            for (int i = 1; i < 5; i++) {
                col = indicatorBean.getIndicatorColumn("N", getmonth(i, m));
                f = o5.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == getQuarter(m)) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(o5))).append("</td>");
                } else if (i > getQuarter(m)) {
                    sb.append("<td>").append(decimalFormat.format(f.get(o5)).equals("0") ? "" : decimalFormat.format(f.get(o5))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(o5))).append("</td>");
                }
                if (i == 2) {
                    sb.append("<td>").append(decimalFormat.format(nh1)).append("</td>");
                }
                if (i == 4) {
                    if (getQuarter(m) > 2) {
                        sb.append("<td>").append(decimalFormat.format(nh2)).append("</td>");
                    } else {
                        sb.append("<td>").append("").append("</td>");
                    }
                }
            }
            sb.append("<td>").append(decimalFormat.format(nfy)).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(o1o2.getType()).append("</td>");
            for (int i = 1; i < 5; i++) {
                col = indicatorBean.getIndicatorColumn("N", getmonth(i, m));
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
                    sb.append("<td>").append(percentFormat(sumPertformance(nh1, o4.getNh1()))).append("</td>");
                }
                if (i == 4) {
                    if (getQuarter(m) > 2) {
                        sb.append("<td>").append(percentFormat(sumPertformance(nh2, o4.getNh2()))).append("</td>");
                    } else {
                        sb.append("<td>").append("").append("</td>");
                    }
                }
            }
            sb.append("<td>").append(percentFormat(sumPertformance(nfy, o4.getNfy()))).append("</td>");
            sb.append("</tr>");

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

    //季度获取哪个月份的值 q 季度 m 当月值
    public int getmonth(int q, int m) {
        int aa = 0;
        switch (q) {
            case 1:
                if (m > 3) {
                    aa = 3;
                } else {
                    aa = m;
                }
                break;
            case 2:
                if (m > 6) {
                    aa = 6;
                } else if (m <= 6 && m >= 4) {
                    aa = m;
                } else {
                    aa = 4;
                }
                break;
            case 3:
                if (m > 9) {
                    aa = 9;
                } else if (m <= 9 && m >= 7) {
                    aa = m;
                } else {
                    aa = 7;
                }
                break;
            case 4:
                if (m > 9) {
                    aa = m;
                } else {
                    aa = 10;
                }
                break;
        }
        return aa;
    }

    //q 季度  m 月份
    public BigDecimal getAccumulatedValue(IndicatorDetail o1, IndicatorDetail o2, int q, int m) {
        String mon;
        BigDecimal total1 = BigDecimal.ZERO;
        BigDecimal total2 = BigDecimal.ZERO;
        Field f;
        try {
            mon = indicatorBean.getIndicatorColumn("NQ", q);
            f = o1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            total1 = BigDecimal.valueOf(Double.valueOf(f.get(o1).toString()));
            mon = indicatorBean.getIndicatorColumn("N", m);
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
