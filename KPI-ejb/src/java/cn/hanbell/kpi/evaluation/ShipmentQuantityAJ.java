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
public abstract class ShipmentQuantityAJ extends ShipmentQuantity {

    public ShipmentQuantityAJ() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_DC", " like 'AJ%' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
