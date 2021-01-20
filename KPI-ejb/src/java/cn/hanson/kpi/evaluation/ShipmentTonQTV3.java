/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749
 */
public class ShipmentTonQTV3 extends ShipmentTon {

    public ShipmentTonQTV3() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("protype", "in ('QT')");
        queryParams.put("cusno", " not in ('HSH00003','HTW00001','YZJ00001')");
    }

}
