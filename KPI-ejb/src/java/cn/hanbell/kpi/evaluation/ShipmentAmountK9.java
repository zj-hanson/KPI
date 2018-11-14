/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 */
public class ShipmentAmountK9 extends ShipmentAmount9 {

    public ShipmentAmountK9() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("ogdkid", " IN('RL01','RL03') ");
        queryParams.put("n_code_DA", " IN('OH','RT') ");
    }

}