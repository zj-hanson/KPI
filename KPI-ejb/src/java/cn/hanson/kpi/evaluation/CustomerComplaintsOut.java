/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author FredJie
 */
public class CustomerComplaintsOut extends CustomerComplaintsIn{
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal complaintNum = BigDecimal.ZERO;
        BigDecimal shipNum = BigDecimal.ZERO;

        int id = Integer.parseInt(map.get("id").toString());
        Indicator indicator = indicatorBean.findById(id);
        if (indicator == null) {
            return BigDecimal.ZERO;
        }
        try {
            Method getMethod = IndicatorDetail.class
                    .getMethod("get" + indicatorBean.getIndicatorColumn("N", m).toUpperCase());
            Method setMethod = IndicatorDetail.class
                    .getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", m).toUpperCase(), BigDecimal.class);
            // o1投诉总数		
            IndicatorDetail o1 = indicator.getOther1Indicator();
            if (o1 != null) {
                complaintNum = (BigDecimal)getMethod.invoke(o1);
            }
            // o2总发货件数		
            IndicatorDetail o2 = indicator.getOther2Indicator();
            if (o2 != null) {
                map.put("facno", "H");
                map.put("con", " and h.cusno !='HAH00001'");
                shipNum = getShipNum(y, m, d, type, map);
                map.put("facno", "Y");
                map.put("con", " and h.cusno !='YZJ00001'");
                shipNum = shipNum.add(getShipNum(y, m, d, type, map));
                setMethod.invoke(o2, shipNum);
                indicatorBean.updateIndicatorDetail(o2);
            }
            if (shipNum.compareTo(BigDecimal.ZERO) != 0) {
                return complaintNum.divide(shipNum, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d));
            }
            if (complaintNum.compareTo(BigDecimal.ZERO) != 0) {
                return BigDecimal.valueOf(100d);
            }
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }
}
