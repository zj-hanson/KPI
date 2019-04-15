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
public class ServiceAmountMailBean extends ServiceMail {

    public ServiceAmountMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getMailBody() {
        indicator = indicatorBean.findByFormidYearAndDeptno("TA-服务时间费用汇总", y, "1A000");
        if (indicator == null) {
            throw new NullPointerException(String.format("指标编号%s:考核部门%s:不存在", "TA-服务时间费用汇总", "1A000"));
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
        sb.append("<div class=\"content\">统计内容为：当月服务员报销核准的数据，报销对应的服务案可能是非本月发生的</div>");
        sb.append("<div class=\"tableTitle\">单位：元</div>");
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
            sb.append("<th>09月</th><th>10月</th><th>11月</th><th>12月</th><th>总计</th></tr>");
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
        IndicatorDetail o3 = e.getOther3Indicator();
        IndicatorDetail o4 = e.getOther4Indicator();
        Field f;
        try {
            o3.setType(e.getOther3Label());
            o4.setType(e.getOther4Label());
            sb.append("<tr style=\"background:").append(color).append(";\"><td  rowspan=\"3\" colspan=\"1\" style=\"text-align: center;\">").append(e.getName()).append("</td>");
            sb.append("<td style=\"text-align: left;\">").append(o3.getType()).append("</td>");       
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
            indicatorBean.addValue(o3, o4, "M");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">合计</td>");
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

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

}
