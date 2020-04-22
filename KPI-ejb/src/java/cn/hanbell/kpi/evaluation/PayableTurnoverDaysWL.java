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
 * @author C1879 应付周转天数物料除开工程类
 */
public class PayableTurnoverDaysWL extends PayableTurnoverDays {

    public PayableTurnoverDaysWL() {
        super();
        queryParams.put("facno", "C");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        //周转天数= 期间天数 /（ 当月销售成本*2 /（当月期初应付账款+期末应付账款））
        //累计周转= 期间天数 / (累计销售成本*2 /（1月期初应付账款+期末应付账款）)
        String id = map.get("id") != null ? map.get("id").toString() : "";
        IndicatorBean indicatorBean = lookupIndicatorBeanBean();
        BigDecimal value = BigDecimal.ZERO;
        BigDecimal engineeringValue = BigDecimal.ZERO;
        BigDecimal beignTurnoverValue = BigDecimal.ZERO;
        BigDecimal endTurnoverValue = BigDecimal.ZERO;
        BigDecimal sellingCostValue = BigDecimal.ZERO;
        BigDecimal sellingCostValuelj = BigDecimal.ZERO;
        BigDecimal valuelj = BigDecimal.ZERO;
        map.put("type", "");
        //应付工程款
        engineeringValue = engineeringValue.add(getEngineeringValue(y, m, map));
        //1月期初不用减去工程款 其他反之
        if(m==1){
            beignTurnoverValue = beignTurnoverValue.add(getBeignTurnoverValue(y, m, map));
        }else{
            beignTurnoverValue = beignTurnoverValue.add(getBeignTurnoverValue(y, m, map)).subtract(engineeringValue);
        }
        //期末应付
        endTurnoverValue = endTurnoverValue.add(getEndTurnoverValue(y, m, map)).subtract(engineeringValue);
        //销售成本
        sellingCostValue = sellingCostValue.add(getSellingCostValue(y, m, map));
        //销售成本累计
        map.put("type", "LJ");
        sellingCostValuelj = sellingCostValuelj.add(getSellingCostValue(y, m, map));
        
        //aa -当月期初应付账款+期末应付账款
        BigDecimal day = BigDecimal.valueOf(day(y, m));
        BigDecimal aa = beignTurnoverValue.add(endTurnoverValue);
        BigDecimal bb = sellingCostValue.multiply(BigDecimal.valueOf(2));
      if (aa.compareTo(BigDecimal.ZERO) != 0 && bb.compareTo(BigDecimal.ZERO) != 0) {
            value = day.divide((bb.divide(aa, 4, RoundingMode.HALF_UP)), 4, RoundingMode.HALF_UP);
        }
        //累计cc-（1月期初应付账款+期末应付账款）
        BigDecimal days = BigDecimal.valueOf(days(y, m));
        BigDecimal cc = getBeignTurnoverValue(y, 1, map).add(endTurnoverValue);
        BigDecimal dd = sellingCostValuelj.multiply(BigDecimal.valueOf(2));
        if (cc.compareTo(BigDecimal.ZERO) != 0 && dd.compareTo(BigDecimal.ZERO) != 0) {
            valuelj = days.divide((dd.divide(cc, 4, RoundingMode.HALF_UP)), 4, RoundingMode.HALF_UP);
        }
        
        if (!"".equals(id)) {
            Indicator entity = indicatorBean.findById(Integer.valueOf(id));
            if (entity != null) {
                //累计应付周转天数
                updateIndicatorDaily(m, valuelj, entity.getOther1Indicator());
                //期初应付
                updateIndicatorDaily(m, beignTurnoverValue, entity.getOther2Indicator());
                //期末应付
                updateIndicatorDaily(m, endTurnoverValue, entity.getOther3Indicator());
                //销售成本
                updateIndicatorDaily(m, sellingCostValue, entity.getOther4Indicator());
                //累计销售成本
                updateIndicatorDaily(m, sellingCostValuelj, entity.getOther5Indicator());
            }
        }
        return value;
    }
}
