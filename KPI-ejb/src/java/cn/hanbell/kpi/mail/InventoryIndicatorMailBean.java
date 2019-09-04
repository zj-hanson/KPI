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
public class InventoryIndicatorMailBean extends MailNotification {

    public InventoryIndicatorMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getMailHead() {
        return super.getMailHead();
    }

    //表头
    protected String getHeadTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tbl\"><table width=\"100%\">");
        sb.append("<tr><th rowspan=\"2\">目标类型</th><th rowspan=\"2\">责任单位</th>");
        sb.append("<th rowspan=\"2\">分类</th><th rowspan=\"2\">责任人</th><th rowspan=\"2\">当季目标</th>");
        sb.append("<th colspan=\"2\">与目标比</th><th colspan=\"2\">与上月比</th><th colspan=\"2\">与同期比</th></tr>");
        sb.append("<tr><th >当月金额</th><th >差异</th><th >上月金额</th><th >差异</th><th >同期金额</th><th >差异</th></tr>");
        return sb.toString();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        getData().clear();
        StringBuilder sb = new StringBuilder();
        int count = 1;
        try {
            for (Indicator i : indicatorList) {
                //设置合计项突出显示
                String stylecss;
                if (!"".equals(i.getName()) && i.getName().contains("合计")) {
                    stylecss = "style=\"color:Red;font-weight:bold;\"";
                } else {
                    stylecss = "";
                }
                sb.append("<tr ").append(stylecss).append(">");
                //目标类型
                if (count == 1) {
                    sb.append("<td rowspan=\"").append(indicatorList.size()).append("\" style = 'text-align:left';>").append(i.getCategory()).append("</td>");
                }
                sb.append(getHtmlTableRow(i, y, m, d));
                sb.append("</tr>");
                count++;
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        //获取需要取值栏位
        String col = indicatorBean.getIndicatorColumn(indicator.getFormtype(), m);
        String co2 = indicatorBean.getIndicatorColumn(indicator.getFormtype(), m - 1);
        StringBuilder sb = new StringBuilder();
        IndicatorDetail a = indicator.getActualIndicator();//实际
        IndicatorDetail t = indicator.getTargetIndicator();// 目标
        IndicatorDetail b = indicator.getBenchmarkIndicator();// 去年同期
        Field f;
        try {

            //责任单位
            sb.append("<td style = 'text-align:left';>").append(indicator.getDeptname().equals("财务部") ? "无" : indicator.getDeptname()).append("</td>");
            //分类
            sb.append("<td style = 'text-align:left';>").append(indicator.getName()).append("</td>");
            //责任人
            sb.append("<td style = 'text-align:left';>").append(indicator.getUsername() != null ? indicator.getUsername() : "无").append("</td>");
            //目标
            f = t.getClass().getDeclaredField(col);
            f.setAccessible(true);
            BigDecimal tarAmt = BigDecimal.valueOf(Double.valueOf(f.get(t).toString()));
            sb.append("<td>").append(decimalFormat.format(f.get(t))).append("</td>");
            //实际
            f = a.getClass().getDeclaredField(col);
            f.setAccessible(true);
            BigDecimal actAmt = BigDecimal.valueOf(Double.valueOf(f.get(a).toString()));
            sb.append("<td>").append(decimalFormat.format(f.get(a))).append("</td>");
            //与目标比 当月 - 目标
            sb.append("<td>").append(decimalFormat.format(actAmt.subtract(tarAmt))).append("</td>");
            //上月实际值
            f = a.getClass().getDeclaredField(co2);
            f.setAccessible(true);
            BigDecimal upActAmt = BigDecimal.valueOf(Double.valueOf(f.get(a).toString()));
            sb.append("<td>").append(decimalFormat.format(f.get(a))).append("</td>");
            //与上月比
            sb.append("<td>").append(decimalFormat.format(actAmt.subtract(upActAmt))).append("</td>");
            //同期
            f = b.getClass().getDeclaredField(col);
            f.setAccessible(true);
            BigDecimal benAmt = BigDecimal.valueOf(Double.valueOf(f.get(b).toString()));
            sb.append("<td>").append(decimalFormat.format(f.get(b))).append("</td>");
            //与同期比
            sb.append("<td>").append(decimalFormat.format(actAmt.subtract(benAmt))).append("</td>");
            sb.append("</tr>");
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

    @Override
    public String getMailBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getQuantityTable());
        return sb.toString();
    }

    protected String getQuantityTable() {
        indicators.clear();//清除总表的list
        StringBuilder sb = new StringBuilder();//表头内容
        sb.append(getHeadTable());
        this.indicators.clear();
        //生产目标
        this.indicators = indicatorBean.findByCategoryAndYear("生产目标", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            // 指标排序
            indicators.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() > o2.getSortid()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
        } else {
            sb.append("");
        }
        //营业目标
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("营业目标", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            // 指标排序
            indicators.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() > o2.getSortid()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
        } else {
            sb.append("");
        }
        //服务目标
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("服务目标", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            // 指标排序
            indicators.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() > o2.getSortid()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
        } else {
            sb.append("");
        }

        //借出未归
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("借出未归", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            // 指标排序
            indicators.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() > o2.getSortid()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
        } else {
            sb.append("");
        }

        //其他目标
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("其他目标", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            // 指标排序
            indicators.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() > o2.getSortid()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
        } else {
            sb.append("");
        }

        //合计目标
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("库存金额总计", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            sb.append(getHtmlTable(this.indicators, y, m, d, true));
        } else {
            sb.append("");
        }
        sb.append("</table></div>");
        return sb.toString();
    }

}
