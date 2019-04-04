/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 SHB球铁预估订单量（吨）
 */
public class ShipmentPredictTonQTV1 extends ShipmentPredictTon {

    public ShipmentPredictTonQTV1() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cdrcus", "SHB");
        queryParams.put("material", "QT");
    }
}
