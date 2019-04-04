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
public class ShipmentAmtsOTHV1 extends ShipmentAmts {

    public ShipmentAmtsOTHV1() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("protype", "其他");
        queryParams.put("cusno", "S");
    }
}
