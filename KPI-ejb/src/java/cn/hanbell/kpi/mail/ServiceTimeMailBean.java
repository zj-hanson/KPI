/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.ServiceMail;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
public class ServiceTimeMailBean extends ServiceMail {

    public ServiceTimeMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
        decimalFormat = new DecimalFormat("#,##0.00");
    }

    @Override
    protected String getMailBody() {
        indicator = indicatorBean.findByFormidYearAndDeptno("TA-服务时间费用汇总", m != 1 ? y : y - 1, "1A000");
        if (indicator == null) {
            throw new NullPointerException(String.format("指标编号%s:考核部门%s:不存在", "TA-服务时间费用汇总", "1A000"));
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
        sb.append("<div class=\"tableTitle\">单位：小时</div>");
        if (m == 1) {
            sb.append(getHtmlTable(indicators, y - 1, 12, d, true));
        } else {
            sb.append(getHtmlTable(indicators, y, m - 1, d, true));
        }
        return sb.toString();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        getData().clear();
        StringBuilder sb = new StringBuilder();
        int size = 0;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th>部门</th> <th>统计项目</th>");
            for (int i = 1; i <= m; i++) {
                sb.append("<th>").append(i).append("月</th>");
            }
            sb.append("<th>合计</th></tr>");
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
        Field f,f1,f2;
        try {
            o1.setType(e.getOther1Label());
            o2.setType(e.getOther2Label());
            sb.append("<tr style=\"background:").append(color).append(";\"><td  rowspan=\"3\" colspan=\"1\" style=\"text-align: center;\">").append(e.getName()).append("</td>");
            sb.append("<td style=\"text-align: left;\">").append(o1.getType()).append("</td>");
            for (int i = 1; i <= m; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = o1.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(o1))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(o1)).equals("0.00") ? "" : decimalFormat.format(f.get(o1))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(o1))).append("</td>");
                }
            }
            //服务部需求不需要月平均 20210301
            //sb.append("<td>").append(decimalFormat.format(o1.getNfy().divide(new BigDecimal(m), 2, BigDecimal.ROUND_HALF_UP))).append("</td>");
            sb.append("<td>").append(decimalFormat.format(o1.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(o2.getType()).append("</td>");
            for (int i = 1; i <= m; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = o2.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(o2))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(o2)).equals("0.00") ? "" : decimalFormat.format(f.get(o2))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(o2))).append("</td>");
                }
            }
            //服务部需求不需要月平均 20210301
            //sb.append("<td>").append(decimalFormat.format(o2.getNfy().divide(new BigDecimal(m), 2, BigDecimal.ROUND_HALF_UP))).append("</td>");
            sb.append("<td>").append(decimalFormat.format(o2.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append("合计").append("</td>");
            for (int i = 1; i <= m; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f1 = o1.getClass().getDeclaredField(col);
                f1.setAccessible(true);
                f2 = o2.getClass().getDeclaredField(col);
                f2.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(BigDecimal.valueOf(Double.valueOf(f1.get(o1).toString())).add(BigDecimal.valueOf(Double.valueOf(f1.get(o2).toString()))).setScale(2, BigDecimal.ROUND_HALF_UP)).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(BigDecimal.valueOf(Double.valueOf(f1.get(o1).toString())).add(BigDecimal.valueOf(Double.valueOf(f1.get(o2).toString()))).setScale(2, BigDecimal.ROUND_HALF_UP)).append("</td>");
                } else {
                    sb.append("<td>").append(BigDecimal.valueOf(Double.valueOf(f1.get(o1).toString())).add(BigDecimal.valueOf(Double.valueOf(f1.get(o2).toString()))).setScale(2, BigDecimal.ROUND_HALF_UP)).append("</td>");
                }
            }
            //服务部需求不需要月平均 20210301
            //sb.append("<td>").append(decimalFormat.format(o1.getNfy().divide(new BigDecimal(m), 2, BigDecimal.ROUND_HALF_UP).add(o2.getNfy().divide(new BigDecimal(m), 2, BigDecimal.ROUND_HALF_UP)))).append("</td>");
            sb.append("<td>").append(decimalFormat.format(o1.getNfy().add(o2.getNfy()))).append("</td>");
            sb.append("</tr>");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

}
