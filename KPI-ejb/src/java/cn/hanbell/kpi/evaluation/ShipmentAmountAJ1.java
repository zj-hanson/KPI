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
public class ShipmentAmountAJ1 extends ShipmentAmountAJ {

    public ShipmentAmountAJ1() {
        super();
        queryParams.put("decode", "1");
        queryParams.put("ogdkid", "RL01");
    }

}
