/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 平湖新纪元订单数量
 *
 * @author C0160
 */
public class SalesOrderTonPHXJY extends SalesOrderTon {

    public SalesOrderTonPHXJY() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cusno", " ='HZJ00104' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal quantity1, quantity2;
        quantity1 = super.getValue(y, m, d, type, map);
        map.remove("facno");
        map.remove("cusno");
        map.put("facno", "Y");
        map.put("cusno", " ='YZJ00003' ");
        quantity2 = super.getValue(y, m, d, type, map);
        return quantity1.add(quantity2);
    }

    @Override
    public BigDecimal getNotDelivery(Date d, LinkedHashMap<String, Object> map) {
        BigDecimal quantity1, quantity2;
        queryParams.put("facno", "H");
        queryParams.put("cusno", " ='HZJ00104' ");
        quantity1 = super.getNotDelivery(d, queryParams);
        queryParams.put("facno", "Y");
        queryParams.put("cusno", " ='YZJ00003' ");
        quantity2 = super.getNotDelivery(d, queryParams);
        return quantity1.add(quantity2);
    }

}
