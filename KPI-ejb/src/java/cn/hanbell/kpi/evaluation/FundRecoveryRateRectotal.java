/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author C1879 资金回收率
 */
public class FundRecoveryRateRectotal extends TurnoverDays {

    public FundRecoveryRateRectotal() {
        super();
        queryParams.put("facno", "C");

    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String[] arr = facno.split(",");
        map.remove("facno");
        //资金回收率公式=期间总收款/(期末应收账款+期间总收款) type代表是否累计
        String id = map.get("id") != null ? map.get("id").toString() : "";
        IndicatorBean indicatorBean = lookupIndicatorBeanBean();
        BigDecimal value = BigDecimal.ZERO;
        BigDecimal rectotalValue = BigDecimal.ZERO;
        BigDecimal booamtValue = BigDecimal.ZERO;
        BigDecimal valuelj = BigDecimal.ZERO;
        BigDecimal rectotalValuelj = BigDecimal.ZERO;
        for (String arr1 : arr) {
            map.put("facno", arr1);
            map.put("type", "");
            //期间总收款(实收)
            rectotalValue = rectotalValue.add(getRectotalValue(y, m, map));
            //期末应收账款
            booamtValue = booamtValue.add(getBooamtValue(y, m, d, map));
            //期间总收款(实收)累计
            map.put("type", "LJ");
            rectotalValuelj = rectotalValuelj.add(getRectotalValue(y, m, map));
        }
        //(期末应收账款+期间总收款)
        BigDecimal aa = rectotalValue.add(booamtValue);
        if (aa.compareTo(BigDecimal.ZERO) != 0) {
            value = rectotalValue.divide(aa, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        }
        //累计(期末应收账款+期间总收款)
        BigDecimal bb = rectotalValuelj.add(booamtValue);
        if (bb.compareTo(BigDecimal.ZERO) != 0) {
            valuelj = rectotalValuelj.divide(bb, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        }
        if (!"".equals(id)) {
            Indicator entity = indicatorBean.findById(Integer.valueOf(id));
            if (entity != null) {
                //累计资金回收率
                updateIndicatorDaily(m, valuelj, entity.getForecastIndicator());
                //期间总收款(实收)
                updateIndicatorDaily(m, rectotalValue, entity.getOther1Indicator());
                //期末应收账款
                updateIndicatorDaily(m, booamtValue, entity.getOther2Indicator());
                //期间总收款(实收)累计
                updateIndicatorDaily(m, rectotalValuelj, entity.getOther3Indicator());
            }
        }
        return value;
    }

}
