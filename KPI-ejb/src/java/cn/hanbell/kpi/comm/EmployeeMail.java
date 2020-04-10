/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author C1879
 */
public abstract class EmployeeMail extends MailNotification {

    protected Indicator sumIndicator;

    protected BigDecimal sum1;
    protected BigDecimal sum2 = BigDecimal.ZERO;
    protected List<Indicator> sumList;

    public EmployeeMail() {

    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        getData().put("sum2", BigDecimal.ZERO);
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<tr>");
            sb.append("<td rowspan=\"").append(indicatorList.size() + 1).append("\" colspan=\"1\" >").append(sumIndicator.getName()).append("</td>");
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d));
                sb.append("</tr>");
            }
            if (needsum) {
                sumIndicator = indicatorBean.getSumValue(indicatorList);
                if (sumIndicator != null) {
                    sumList.add(sumIndicator);
                    sum1 = sum1.add(getData().get("sum1"));
                    sum2 = sum2.add(getData().get("sum2"));
                    sumIndicator.setUsername("合计");
                    sb.append("<tr>");
                    sb.append(getHtmlTableRow(sumIndicator, y, m, d));
                    sb.append("</tr>");
                }
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum, String name) {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        getData().put("sum2", BigDecimal.ZERO);
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<tr>");
            sb.append("<td style=\"text-align:center;\" rowspan=\"").append(indicatorList.size() == 1 ? 1 : indicatorList.size() + 1).append("\" colspan=\"1\" >").append(name).append("</td>");
            for (Indicator i : indicatorList) {
                indicatorBean.divideByRate(i, 2);
                sb.append(getHtmlTableRow(i, y, m, d));
                sb.append("</tr>");
            }
            if (needsum) {
                sumIndicator = getSumValue(indicatorList);
                if (sumIndicator != null) {
                    sumList.add(sumIndicator);
                    sum1 = sum1.add(getData().get("sum1"));
                    sum2 = sum2.add(getData().get("sum2"));
                    sumIndicator.setUsername("小计");
                    sb.append("<tr style=\"font-weight:bold;\">");
                    sb.append(getHtmlTableRow(sumIndicator, y, m, d));
                    sb.append("</tr>");
                }
            } else {
                sumIndicator = indicatorBean.getSumValue(indicatorList);
                if (sumIndicator != null) {
                    sumList.add(sumIndicator);
                    sum1 = sum1.add(getData().get("sum1"));
                    sum2 = sum2.add(getData().get("sum2"));
                }
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
        StringBuilder sb = new StringBuilder();
        IndicatorDetail a = indicator.getActualIndicator();
        IndicatorDetail t = indicator.getTargetIndicator();
        IndicatorDetail o1 = indicator.getOther1Indicator();
        IndicatorDetail o2 = indicator.getOther2Indicator();
        Field f;
        try {
            BigDecimal num1, num2;
            if (indicator.getActualInterface() != null && indicator.getActualEJB() != null && indicator.getId() != -1) {
                //本日出货
                String userid = indicator.getUserid();
                Actual actualInterface = (Actual) Class.forName(indicator.getActualInterface()).newInstance();
                actualInterface.setEJB(indicator.getActualEJB());
                actualInterface.getQueryParams().put("userid", userid);
                num1 = actualInterface.getValue(y, m, d, Calendar.DATE, actualInterface.getQueryParams()).divide(indicator.getRate(), 2, RoundingMode.HALF_UP);
                //本日订单
                Actual otherInterface = (Actual) Class.forName(indicator.getOther1Interface()).newInstance();
                otherInterface.setEJB(indicator.getActualEJB());
                otherInterface.getQueryParams().put("userid", userid);
                num2 = otherInterface.getValue(y, m, d, Calendar.DATE, otherInterface.getQueryParams()).divide(indicator.getRate(), 2, RoundingMode.HALF_UP);
            } else {
                num1 = BigDecimal.ZERO;
                num2 = BigDecimal.ZERO;
            }
            if (indicator.getId() != -1) {
                sumAdditionalData("sum1", num1);
                sumAdditionalData("sum2", num2);
            }
            //业务员
            sb.append("<td>").append(indicator.getUsername()).append("</td>");
            //当日订单
            sb.append("<td>").append(decimalFormat.format((indicator.getId() != -1 ? num2 : getData().get("sum2")))).append("</td>");
            //当日出货
            sb.append("<td>").append(decimalFormat.format(indicator.getId() != -1 ? num1 : getData().get("sum1"))).append("</td>");
            //本月订单
            f = o1.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td>").append(decimalFormat.format(f.get(o1))).append("</td>");
            //本月出货
            f = a.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td>").append(decimalFormat.format(f.get(a))).append("</td>");
            //累计实际
            BigDecimal a1 = indicatorBean.getAccumulatedValue(a, m);
            sb.append("<td>").append(decimalFormat.format(a1)).append("</td>");
            //目标
            BigDecimal t1 = t.getNfy();
            sb.append("<td>").append(decimalFormat.format(t1)).append("</td>");
            //达成
            sb.append("<td>").append(percentFormat(achievingRate(a1, t1))).append("</td>");
            //年累计应收账款
            if ("年累计应收账款".equals(indicator.getOther2Label() == null ? "" : indicator.getOther2Label().trim())) {
                sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(o2, m))).append("</td>");
            }

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

    public Indicator getSumValue(List<Indicator> indicators) {
        if (indicators.isEmpty()) {
            return null;
        }
        Indicator entity = null;
        IndicatorDetail a, b, f, t, o1, o2;
        try {
            entity = (Indicator) BeanUtils.cloneBean(indicators.get(0));
            entity.setId(-1);
            entity.setName("合计");
            for (int i = 1; i < indicators.size(); i++) {
                a = indicators.get(i).getActualIndicator();
                b = indicators.get(i).getBenchmarkIndicator();
                f = indicators.get(i).getForecastIndicator();
                t = indicators.get(i).getTargetIndicator();
                o1 = indicators.get(i).getOther1Indicator();
                o2 = indicators.get(i).getOther2Indicator();

                indicatorBean.addValue(entity.getActualIndicator(), a, entity.getFormkind());
                indicatorBean.addValue(entity.getBenchmarkIndicator(), b, entity.getFormkind());
                indicatorBean.addValue(entity.getForecastIndicator(), f, entity.getFormkind());
                indicatorBean.addValue(entity.getTargetIndicator(), t, entity.getFormkind());
                if (o1 != null) {
                    indicatorBean.addValue(entity.getOther1Indicator(), o1, entity.getFormkind());
                }
                if (o2 != null) {
                    indicatorBean.addValue(entity.getOther2Indicator(), o2, entity.getFormkind());
                }
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return entity;
    }

    private BigDecimal achievingRate(BigDecimal a, BigDecimal t) {
        if (t.compareTo(BigDecimal.ZERO) == 0) {
            if (a.compareTo(BigDecimal.ZERO) > 0) {
                return BigDecimal.valueOf(100d);
            } else if (a.compareTo(BigDecimal.ZERO) < 0) {
                return BigDecimal.valueOf(-100d);
            } else {
                return BigDecimal.valueOf(0d);
            }
        } else {
            return a.divide(t, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d));
        }
    }

    /**
     * @return the sumIndicator
     */
    public Indicator getSumIndicator() {
        return sumIndicator;
    }

    /**
     * @param sumIndicator the sumIndicator to set
     */
    public void setSumIndicator(Indicator sumIndicator) {
        this.sumIndicator = sumIndicator;
    }

    /**
     * @return the sumList
     */
    public List<Indicator> getSumList() {
        return sumList;
    }

    /**
     * @param sumList the sumList to set
     */
    public void setSumList(List<Indicator> sumList) {
        this.sumList = sumList;
    }

    /**
     * @return the sum1
     */
    public BigDecimal getSum1() {
        return sum1;
    }

    /**
     * @param sum1 the sum1 to set
     */
    public void setSum1(BigDecimal sum1) {
        this.sum1 = sum1;
    }

    /**
     * @return the sum2
     */
    public BigDecimal getSum2() {
        return sum2;
    }

    /**
     * @param sum2 the sum2 to set
     */
    public void setSum2(BigDecimal sum2) {
        this.sum2 = sum2;
    }

}
