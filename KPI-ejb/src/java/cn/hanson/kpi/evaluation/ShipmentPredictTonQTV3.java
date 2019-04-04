/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 OTHER球铁预估订单量（吨）
 */
public class ShipmentPredictTonQTV3 extends ShipmentPredictTon {

    public ShipmentPredictTonQTV3() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cdrcus", "OTHER");
        queryParams.put("material", "QT");
    }
}
