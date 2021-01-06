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
public class ShipmentTonHYOTH extends ShipmentTonHY {

    public ShipmentTonHYOTH() {
        super();
        queryParams.put("facno", "Y");
        queryParams.put("protype", " not in ('HT','QT')");
    }

}
