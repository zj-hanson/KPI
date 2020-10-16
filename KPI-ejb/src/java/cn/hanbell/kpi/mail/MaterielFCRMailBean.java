/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Field;
import java.math.BigDecimal;
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
public class MaterielFCRMailBean extends MailNotification {

    public MaterielFCRMailBean() {
    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getMailBody() {
        Indicator indicator;
        indicator = indicatorBean.findByFormidYearAndDeptno("客诉物料结案率", m != 1 ? y : y - 1, "1A000");
        if (indicator == null) {
            throw new NullPointerException(String.format("指标编号%s:考核部门%s:不存在", "客诉物料结案率", "1A000"));
        }
        indicators.clear();
        indicators = indicatorBean.findByPIdAndYear(indicator.getId(), m != 1 ? y : y - 1);
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
        sb.append("<div class=\"tableTitle\">单位：%</div>");
        if (m == 1) {
            sb.append(getHtmlTable(indicators, y - 1, 12, d, true));
        } else {
            sb.append(getHtmlTable(indicators, y, m - 1, d, true));
        }
        return sb.toString();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        StringBuilder sb = new StringBuilder();
        int size = 0;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th width=\"8%\">区域</th> <th width=\"7%\">类型</th> <th width=\"6%\">01月</th> <th width=\"6%\">02月</th> <th width=\"6%\">03月</th>");
            sb.append("<th width=\"6%\">04月</th> <th width=\"6%\">05月</th> <th width=\"6%\">06月</th> <th width=\"6%\">07月</th> <th width=\"6%\">08月</th>");
            sb.append("<th width=\"6%\">09月</th> <th width=\"6%\">10月</th> <th width=\"6%\">11月</th> <th width=\"6%\">12月</th></tr>");
            for (Indicator i : indicatorList) {
                size++;
                if (size % 2 != 0) {
                    sb.append(getHtmlTableRow(i, y, m, d));
                } else {
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
        String col;
        StringBuilder sb = new StringBuilder();
        IndicatorDetail actual = indicator.getActualIndicator();
        IndicatorDetail o1 = indicator.getOther1Indicator();
        IndicatorDetail o2 = indicator.getOther2Indicator();
        Field f;
        try {
            sb.append("<tr style=\"background:").append("").append(";\"><td  rowspan=\"3\" style=\"text-align: center;\">").append(indicator.getDescript()).append("</td>");
            sb.append("<td style=\"text-align: left;\">").append(indicator.getOther1Label()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(indicator.getFormtype(), i);
                f = o1.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(f.get(o1)).append("</td>");
                } else if (i > m) {
                        sb.append("<td>").append(f.get(o1).toString().equals("0.00") ? "" : f.get(o1)).append("</td>");
                } else {
                    sb.append("<td>").append(f.get(o1)).append("</td>");
                }
            }
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append("").append(";\"><td style=\"text-align: left;\">").append(indicator.getOther2Label()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(indicator.getFormtype(), i);
                f = o2.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(f.get(o2)).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(f.get(o2).toString().equals("0.00") ? "" : f.get(o2)).append("</td>");
                } else {
                    sb.append("<td>").append(f.get(o2)).append("</td>");
                }
            }
            sb.append("</tr>");
            sb.append("<tr>").append("<td style=\"text-align:left;background:#F7EED6;\">").append("结案率").append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(indicator.getFormtype(), i);
                f = actual.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red;background:#F7EED6\">").append(percentFormat(f.get(actual))).append("</td>");
                } else if (i > m) {
                    sb.append("<td style=\"background:#F7EED6\">").append(percentFormat(f.get(actual)).equals("0.00%") ? "" : percentFormat(f.get(actual))).append("</td>");
                } else {
                    sb.append("<td style=\"background:#F7EED6\">").append(percentFormat(f.get(actual))).append("</td>");
                }
            }
            sb.append("</tr>");
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex) {
            return ex.toString();
        }
        return sb.toString();

    }
}
