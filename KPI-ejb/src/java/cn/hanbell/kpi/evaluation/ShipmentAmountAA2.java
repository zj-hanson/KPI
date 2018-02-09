/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C0160
 */
public class ShipmentAmountAA2 extends ShipmentAmountAA {

    public ShipmentAmountAA2() {
        super();
        queryParams.put("decode", "2");
        queryParams.put("ogdkid", "RL03");
        queryParams.put("n_code_CD", " ='WX' ");
    }

}
