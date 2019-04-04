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
public class ServiceClosingMailBean extends ServiceMail {

    public ServiceClosingMailBean() {

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
            sb.append("<tr><th width=\"10%\">部门</th><th width=\"12%\">统计项目</th><th>01月</th><th>02月</th><th>03月</th><th>04月</th><th>05月</th><th>06月</th><th>07月</th><th>08月</th>");
            sb.append("<th>09月</th><th>10月</th><th>11月</th><th>12月</th></tr>");
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
        IndicatorDetail o1 = e.getOther1Indicator();
        IndicatorDetail o2 = e.getOther2Indicator();
        IndicatorDetail o3 = e.getOther3Indicator();
        IndicatorDetail o1o2;
        IndicatorDetail o3o4;
        IndicatorDetail ljo1;
        Field f;
        BigDecimal v;
        Method setMethod;
        try {
            o1o2 = new IndicatorDetail();
            o1o2.setParent(e);
            o1o2.setType("当月服务单结案率");
            ljo1 = new IndicatorDetail();
            ljo1.setParent(e);
            ljo1.setType("累计服务单发生数");
            o3o4 = new IndicatorDetail();
            o3o4.setParent(e);
            o3o4.setType("累计服务单结案率");
            for (int i = getM(); i > 0; i--) {
                //顺序计算的话会导致累计值重复累加
                //o1值累计
                if (o1 != null) {
                    v = indicatorBean.getAccumulatedValue(o1, i);
                    setMethod = ljo1.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(ljo1, v);
                }

                //---o2/o1
                v = getAccumulatedValue(o2, o1, i);
                setMethod = o1o2.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(o1o2, v);
                //o3/ljo1
                v = getAccumulatedValue(o3, ljo1, i);
                setMethod = o3o4.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(o3o4, v);
            }

            o1.setType(e.getOther1Label());
            o2.setType(e.getOther2Label());
            o3.setType(e.getOther3Label());
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
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(ljo1.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = ljo1.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(ljo1))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(ljo1)).equals("0") ? "" : decimalFormat.format(f.get(ljo1))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(ljo1))).append("</td>");
                }
            }
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
            sb.append("</tr>");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

}
