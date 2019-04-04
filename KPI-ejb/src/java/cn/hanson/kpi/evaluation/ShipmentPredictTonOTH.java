/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 其他预估订单量（吨）
 */
public class ShipmentPredictTonOTH extends ShipmentPredictTon{
    public ShipmentPredictTonOTH(){
        super();
        queryParams.put("facno", "H");
        //queryParams.put("cdrcus", "SHB");
        queryParams.put("material", "OTH");
    }
}
