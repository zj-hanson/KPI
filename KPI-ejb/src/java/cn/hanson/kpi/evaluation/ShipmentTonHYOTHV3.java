/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 * 汉扬其他客户其他重量
 *
 * @author C0160
 */
public class ShipmentTonHYOTHV3 extends ShipmentTon {

    public ShipmentTonHYOTHV3() {
        super();
        queryParams.put("facno", "Y");
        queryParams.put("protype", " not in ('HT','QT')");
        queryParams.put("cusno", " not in ('YZJ00001')");
    }

}
