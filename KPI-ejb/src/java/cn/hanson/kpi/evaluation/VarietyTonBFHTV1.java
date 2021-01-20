/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 实际值（吨）
 */
public class VarietyTonBFHTV1 extends ShipmentTon {

    public VarietyTonBFHTV1() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("variety", "BFHT");
    }

}
