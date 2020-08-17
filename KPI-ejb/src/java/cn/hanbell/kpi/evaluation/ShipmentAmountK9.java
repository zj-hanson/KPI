/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author C1879
 */
public class ShipmentAmountK9 extends ShipmentAmount9 {

    public ShipmentAmountK9() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " IN('OH','RT') ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal temp1, temp2;
        //ComerERP
        temp1 = super.getValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.put("facno", "E");
        //ZJComerERP
        temp2 = super.getValue(y, m, d, type, queryParams);
        //ComerERP + ZJComerERP
        return temp1.add(temp2);
    }

}
