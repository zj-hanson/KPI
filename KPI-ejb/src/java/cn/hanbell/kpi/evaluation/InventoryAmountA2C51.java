/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author C1749
 */
public class InventoryAmountA2C51 extends InventoryAmountA2 {

    public InventoryAmountA2C51() {
        super();
        queryParams.put("formid", "真空出租库存");
        queryParams.put("deptno", "1H000");
        queryParams.put("facno", "C");
        queryParams.put("categories", "A2");
        queryParams.put("genre", "='P'");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mon;
        Field f;
        BigDecimal v1 = BigDecimal.ZERO;
        BigDecimal result = BigDecimal.ZERO;
        Double a1;
        Indicator i = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y,
                map.get("deptno").toString());
        IndicatorDetail o1 = i.getActualIndicator();// 真空出租的指标
        try {
            mon = indicatorBean.getIndicatorColumn("N", m);
            f = o1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a1 = Double.valueOf(f.get(o1).toString());
            //v1 = BigDecimal.valueOf(a1);// 真空出租库存的实际值 暂不体现在这段逻辑
            result = super.getValue(y, m, d, type, map);
            return result;
        } catch (Exception ex) {
            log4j.error("InventoryAmountA2C50--getValue()异常", ex.toString());
        }
        return result;
    }

}
