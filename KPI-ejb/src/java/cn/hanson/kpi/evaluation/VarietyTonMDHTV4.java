/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 未来几天催货量（吨）
 */
public class VarietyTonMDHTV4 extends ShipmentDelyTon {

    public VarietyTonMDHTV4() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("houtsta", "in ('N')");
        queryParams.put("variety", "MDHT");
    }

}
