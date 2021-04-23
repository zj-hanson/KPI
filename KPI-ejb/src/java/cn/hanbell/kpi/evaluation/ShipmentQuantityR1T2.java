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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author C0160
 */
public class ShipmentQuantityR1T2 extends ShipmentQuantity {

    public ShipmentQuantityR1T2() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1T000','1T100' ");
        queryParams.put("decode", "2");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", "  LIKE 'WX%' ");
        queryParams.put("n_code_DC", " ='H' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        Field f;
        String mon;
        Double a;
        //国际营销课的售卖的数量
        BigDecimal count = super.getValue(y, m, d, type, queryParams);
        //KPI中卖到各个分公司中的数据,由制冷维护台数
        Indicator indicator = getIndicatorBean().findByFormidYearAndDeptno("R-国际营销R均价", y, "1F000");
        try {
            IndicatorDetail o = indicator.getOther1Indicator();
            mon = getIndicatorBean().getIndicatorColumn("N", m);
            f = o.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a = Double.valueOf(f.get(o).toString());
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            a = 0.0;
            Logger.getLogger(ShipmentAmountR1E2.class.getName()).log(Level.SEVERE, null, ex);
        }

        return count.add(BigDecimal.valueOf(a));
    }

}
