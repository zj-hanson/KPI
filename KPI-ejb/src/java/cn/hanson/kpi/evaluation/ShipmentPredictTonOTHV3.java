/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 OTHER其他预估订单量（吨）
 */
public class ShipmentPredictTonOTHV3 extends ShipmentPredictTon{
    public ShipmentPredictTonOTHV3(){
        super();
        queryParams.put("facno", "H");
        queryParams.put("cdrcus", "OTHER");
        queryParams.put("material", "OTH");
    }
}
