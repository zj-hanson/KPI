/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 汉声 THB球铁 预估订单量(金额)
 */
public class ShipmentPredictAmtsQTV2 extends ShipmentPredictAmts {

    public ShipmentPredictAmtsQTV2() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cdrcus", "THB");
        queryParams.put("material", "QT");
    }

}
