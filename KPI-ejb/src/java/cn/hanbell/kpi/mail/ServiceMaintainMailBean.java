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
import java.lang.reflect.Method;
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
public class ServiceMaintainMailBean extends ServiceMail {

    private  DecimalFormat df;
    public ServiceMaintainMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
       df = new DecimalFormat("#,##0.00");
        super.init();
    }

    @Override
    protected String getMailBody() {
        indicator = indicatorBean.findByFormidYearAndDeptno("T-服务部维修数据", y, "1A000");
        if (indicator == null) {
            throw new NullPointerException(String.format("指标编号%s:考核部门%s:不存在", "T-服务部维修数据", "1A000"));
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
        for (Indicator e : indicators) {
            //按换算率计算结果
            indicatorBean.divideByRate(e, 2);
        }
        StringBuilder sb = new StringBuilder();
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
            sb.append("<tr><th width=\"8%\">产品/区域</th><th width=\"8%\">项目</th><th>01月</th><th>02月</th><th>03月</th><th>04月</th><th>05月</th><th>06月</th><th>07月</th><th>08月</th>");
            sb.append("<th>09月</th><th>10月</th><th>11月</th><th>12月</th><th>合计</th></tr>");
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
    protected String getMailFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("</div>");//对应Head中的div.content
        sb.append("<div class=\"divFoot1\">维修平均天数：维修工时 / 8H / 维修完工台数</div>");
        sb.append("<div class=\"divFoot1\">同比成长率：(实际完工台数 - 同期完工台数) / 同期完工台数*100%</div>");
        sb.append("<div class=\"divFoot\">此报表由系统自动发送,请不要直接回复</div>");
        sb.append("<div class=\"divFoot\">报表管理员</div>");
        sb.append("</div></body></html>");
        return sb.toString();
    }

    @Override
    protected String getHtmlTableRow(Indicator e, int y, int m, Date d, String color) throws Exception {
        //获取需要取值栏位
        String col, mon;
        StringBuilder sb = new StringBuilder();
        IndicatorDetail nowt = e.getTargetIndicator();
        IndicatorDetail nowa = e.getActualIndicator();
        IndicatorDetail nowf;
        IndicatorDetail pastt = e.getOther1Indicator();
        IndicatorDetail pasta = e.getOther2Indicator();
        IndicatorDetail pastdc;
        IndicatorDetail BG;
        IndicatorDetail gs = e.getOther3Indicator();
        IndicatorDetail avgday;
        Field f;
        mon = indicatorBean.getIndicatorColumn("N", getM());
        BigDecimal v;
        Method setMethod;
        try {
            nowf = new IndicatorDetail();
            nowf.setParent(e);
            nowf.setType("维修达标率");
            pastdc = new IndicatorDetail();
            pastdc.setParent(e);
            pastdc.setType("同期达标率");
            BG = new IndicatorDetail();
            BG.setParent(e);
            BG.setType("同比成长率");
            avgday = new IndicatorDetail();
            avgday.setParent(e);
            avgday.setType("平均维修天数");
            for (int i = getM(); i > 0; i--) {
                // 维修达标率
                v = getAccumulatedValue(nowa, nowt, i);
                setMethod = nowf.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(nowf, v);
                // 同期达标率
                v = getAccumulatedValue(pasta, pastt, i);
                setMethod = pastdc.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(pastdc, v);
                // 平均维修天数 按工作时长8小时计算为一天
                v = getAvgDay(gs, nowa, i);
                setMethod = avgday.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(avgday, v);
                // 同比成长率
                v = indicatorBean.getGrowth(nowa, pasta, i);
                setMethod = BG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(BG, v);
            }

            nowt.setType("目标维修台数");
            nowa.setType("实际完工台数");
            pastt.setType(e.getOther1Label());
            pasta.setType(e.getOther2Label());
            gs.setType(e.getOther3Label());
            //目标维修台数
            sb.append("<tr style=\"background:").append(color).append(";\"><td  rowspan=\"9\" colspan=\"1\" style=\"text-align: center;\">").append(e.getName()).append("</td>");
            sb.append("<td style=\"text-align: left;\">").append(nowt.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = nowt.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(nowt))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(nowt)).equals("0") ? "" : decimalFormat.format(f.get(nowt))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(nowt))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(nowt.getNfy())).append("</td>");
            sb.append("</tr>");
            //实际完工台数
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(nowa.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = nowa.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(nowa))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(nowa)).equals("0") ? "" : decimalFormat.format(f.get(nowa))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(nowa))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(nowa.getNfy())).append("</td>");
            sb.append("</tr>");
            //维修达成率
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(nowf.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = nowf.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(nowf))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(percentFormat(f.get(nowf)).equals("0.00%") ? "" : percentFormat(f.get(nowf))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(nowf))).append("</td>");
                }
            }
            sb.append("<td>").append(percentFormat(sumPertformance(nowa.getNfy(), nowt.getNfy()))).append("</td>");
            sb.append("</tr>");
            //同期目标
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(pastt.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = pastt.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(pastt))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(pastt)).equals("0") ? "" : decimalFormat.format(f.get(pastt))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(pastt))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(pastt.getNfy())).append("</td>");
            sb.append("</tr>");
            //同期完工台数
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(pasta.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = pasta.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(pasta))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(pasta)).equals("0") ? "" : decimalFormat.format(f.get(pasta))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(pasta))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(pasta.getNfy())).append("</td>");
            sb.append("</tr>");
            //同期达标率
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(pastdc.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = pastdc.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(pastdc))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(percentFormat(f.get(pastdc)).equals("0.00%") ? "" : percentFormat(f.get(pastdc))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(pastdc))).append("</td>");
                }
            }
            sb.append("<td>").append(percentFormat(sumPertformance(pasta.getNfy(), pastt.getNfy()))).append("</td>");
            sb.append("</tr>");
            //同比成长率
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(BG.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = BG.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(BG))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(percentFormat(f.get(BG)).equals("0.00%") ? "" : percentFormat(f.get(BG))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(BG))).append("</td>");
                }
            }
            sb.append("<td>").append(percentFormat(this.getGrowth(nowa.getNfy(),  pasta.getNfy()))).append("</td>");
            sb.append("</tr>");
            //维修工时
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(gs.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = gs.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(gs))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(gs)).equals("0") ? "" : decimalFormat.format(f.get(gs))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(gs))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(gs.getNfy())).append("</td>");
            sb.append("</tr>");
            //平均维修天数
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: left;\">").append(avgday.getType()).append("</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = avgday.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(df.format(f.get(avgday))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(avgday)).equals("0") ? "" : df.format(f.get(avgday))).append("</td>");
                } else {
                    sb.append("<td>").append(df.format(f.get(avgday))).append("</td>");
                }
            }
            sb.append("<td>").append(df.format(getAvgDay(gs.getNfy(), nowa.getNfy()))).append("</td>");
            sb.append("</tr>");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

    private BigDecimal getAvgDay(IndicatorDetail gs, IndicatorDetail a, int m) {
        String mon;
        BigDecimal totalgs = BigDecimal.ZERO;
        BigDecimal totala = BigDecimal.ZERO;
        Field f;
        try {
            mon = indicatorBean.getIndicatorColumn("N", m);
            f = gs.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            totalgs = BigDecimal.valueOf(Double.valueOf(f.get(gs).toString()));
            mon = indicatorBean.getIndicatorColumn("N", m);
            f = a.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            totala = BigDecimal.valueOf(Double.valueOf(f.get(a).toString()));
            if (totala.compareTo(BigDecimal.ZERO) != 0) {
                return totalgs.divide(BigDecimal.valueOf(8), 4, RoundingMode.HALF_UP).divide(totala, 4, RoundingMode.HALF_UP);
            } else {
                return BigDecimal.ZERO;
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAvgDay(BigDecimal a, BigDecimal b) {
        try {
            if (b.compareTo(BigDecimal.ZERO) != 0) {
                return a.divide(BigDecimal.valueOf(8), 4, RoundingMode.HALF_UP).divide(b, 4, RoundingMode.HALF_UP);
            } else {
                return BigDecimal.ZERO;
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getGrowth(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) != 0) {
            return b.subtract(a).divide(b, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d));
        } else {
            if (a.compareTo(BigDecimal.ZERO) != 0) {
                return BigDecimal.ONE.multiply(BigDecimal.valueOf(100d));
            } else {
                return BigDecimal.ZERO;
            }
        }
    }
}
