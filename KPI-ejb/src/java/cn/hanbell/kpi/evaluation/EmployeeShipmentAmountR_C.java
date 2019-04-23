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
public class EmployeeShipmentAmountR_C extends EmployeeShipmentAmount {

    public EmployeeShipmentAmountR_C() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " ('1B000','1B100','1T000','1T100') ");
        queryParams.put("ogdkid", "IN ('RL01','RL03')");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
