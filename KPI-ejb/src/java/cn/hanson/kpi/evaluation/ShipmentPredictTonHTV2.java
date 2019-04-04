/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 THB灰铁预估订单量（吨）
 */
public class ShipmentPredictTonHTV2 extends ShipmentPredictTon {

    public ShipmentPredictTonHTV2() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cdrcus", "THB");
        queryParams.put("material", "HT");
    }
}
