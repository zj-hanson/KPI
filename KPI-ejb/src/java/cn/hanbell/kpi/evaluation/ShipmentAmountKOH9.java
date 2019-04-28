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
public class ShipmentAmountKOH9 extends ShipmentAmount9 {

    public ShipmentAmountKOH9() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " IN('OH') ");
    }

}
