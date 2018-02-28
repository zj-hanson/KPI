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
public class ShipmentQuantityR1T1 extends ShipmentQuantityR {

    public ShipmentQuantityR1T1() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1T000' ");
        queryParams.put("decode", "2");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DC", " ='R' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
