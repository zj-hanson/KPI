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
public class ShipmentQuantityKWL1C2 extends ShipmentQuantity {

    public ShipmentQuantityKWL1C2() {
        super();
        queryParams.put("facno", "E");
        queryParams.put("deptno", " '8A000' ");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='OH' ");
        queryParams.put("n_code_DC", " ='WL' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
