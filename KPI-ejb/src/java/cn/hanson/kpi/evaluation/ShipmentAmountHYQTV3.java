/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 * 汉扬其他客户球铁金额
 *
 * @author C0160
 */
public class ShipmentAmountHYQTV3 extends ShipmentAmts {

    public ShipmentAmountHYQTV3() {
        super();
        queryParams.put("facno", "Y");
        queryParams.put("protype", "in ('QT')");
        queryParams.put("cusno", " not in ('HSH00003','HTW00001','YZJ00001')");
    }

}
