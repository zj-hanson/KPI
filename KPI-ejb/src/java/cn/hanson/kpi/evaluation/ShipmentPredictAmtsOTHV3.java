/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 汉声 OTHER其他 预估订单量(金额)
 */
public class ShipmentPredictAmtsOTHV3 extends ShipmentPredictAmts {

    public ShipmentPredictAmtsOTHV3() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cdrcus", "OTHER");
        queryParams.put("material", "OTH");
    }

}
