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
public class ShipmentAmountS9 extends ShipmentAmount9 {

    public ShipmentAmountS9() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='AH' ");
        queryParams.put("n_code_DC", " in ('SAM-5HP','SAM-7HP') ");
    }

}
