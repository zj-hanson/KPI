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
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.commons.beanutils.BeanUtils;

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
        sb.append("<div class=\"content\">统计内容为：当月服务员报销核准的数据，报销对应的服务案可能是非本月发生的</div>");
        sb.append("<div class=\"tableTitle\">单位：元</div>");
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
            sb.append("<tr><th>部门</th>");
            for (int i = 1; i <= m; i++) {
                sb.append("<th>").append(i).append("月</th>");
            }
            sb.append("<th>总计</th></tr>");
            for (Indicator i : indicatorList) {
                size++;
                if (size % 2 != 0) {
                    sb.append(getHtmlTableRow(i, y, m, d, "#D3D7D4"));
                } else {
                    sb.append(getHtmlTableRow(i, y, m, d, "#FFFFFF"));
                }
            }
            Indicator sumIndicator = this.getSumValue(indicatorList);
            sb.append(getHtmlTableRow(sumIndicator, y, m, d, "#D3D7D4"));
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
        Field f;
        try {
            o4.setType(e.getOther4Label());
            sb.append("<tr style=\"background:").append(color).append(";\"><td  rowspan=\"1\" colspan=\"1\" style=\"text-align: center;\">").append(e.getName()).append("</td>");
            for (int i = 1; i <= m; i++) {
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
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

    //算合计
    public Indicator getSumValue(List<Indicator> indicators) {
        if (indicators.isEmpty()) {
            return null;
        }
        Indicator entity = null;
        IndicatorDetail other4;
        IndicatorDetail sother4;
        try {
            entity = (Indicator) BeanUtils.cloneBean(indicators.get(0));
            entity.setId(-1);
            entity.setName("合计");
            entity.setFormid("合计");
            sother4 = new IndicatorDetail();
            sother4.setParent(entity);
            sother4.setType("O4");
            entity.setOther4Indicator(sother4);
            for (int i = 0; i < indicators.size(); i++) {
                other4 = indicators.get(i).getOther4Indicator();
                addValue(entity.getOther4Indicator(), other4, entity.getFormkind());
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            log4j.error(ex);
        }
        return entity;
    }

    public void addValue(IndicatorDetail a, IndicatorDetail b, String formKind) {
        //先算汇总字段再算每月字段,A和S类型会重算汇总
        switch (formKind) {
            case "M":
                a.setNfy(a.getNfy().add(b.getNfy()));
                a.setNh2(a.getNh2().add(b.getNh2()));
                a.setNh1(a.getNh1().add(b.getNh1()));
                a.setNq4(a.getNq4().add(b.getNq4()));
                a.setNq3(a.getNq3().add(b.getNq3()));
                a.setNq2(a.getNq2().add(b.getNq2()));
                a.setNq1(a.getNq1().add(b.getNq1()));
                a.setN01(a.getN01().add(b.getN01()));
                a.setN02(a.getN02().add(b.getN02()));
                a.setN03(a.getN03().add(b.getN03()));
                a.setN04(a.getN04().add(b.getN04()));
                a.setN05(a.getN05().add(b.getN05()));
                a.setN06(a.getN06().add(b.getN06()));
                a.setN07(a.getN07().add(b.getN07()));
                a.setN08(a.getN08().add(b.getN08()));
                a.setN09(a.getN09().add(b.getN09()));
                a.setN10(a.getN10().add(b.getN10()));
                a.setN11(a.getN11().add(b.getN11()));
                a.setN12(a.getN12().add(b.getN12()));
                break;
            case "Q":
                a.setNfy(a.getNfy().add(b.getNfy()));
                a.setNh2(a.getNh2().add(b.getNh2()));
                a.setNh1(a.getNh1().add(b.getNh1()));
                a.setNq4(a.getNq4().add(b.getNq4()));
                a.setNq3(a.getNq3().add(b.getNq3()));
                a.setNq2(a.getNq2().add(b.getNq2()));
                a.setNq1(a.getNq1().add(b.getNq1()));
                break;
            case "H":
                a.setNfy(a.getNfy().add(b.getNfy()));
                a.setNh2(a.getNh2().add(b.getNh2()));
                a.setNh1(a.getNh1().add(b.getNh1()));
                break;
            case "Y":
                a.setNfy(a.getNfy().add(b.getNfy()));
        }
    }

}
