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
public class ShipmentQuantitySAM1 extends ShipmentQuantity {

    public ShipmentQuantitySAM1() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_CD", " NOT LIKE 'WX%' ");
        queryParams.put("n_code_DC", " IN ('SAM-5HP','SAM-7HP') ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
