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
public class SalesOrderAmountSSM1 extends SalesOrderAmount {

    public SalesOrderAmountSSM1() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1U000' ");
        queryParams.put("n_code_DA", "='S' ");
        queryParams.put("n_code_DC", " ='SM' ");
        queryParams.put("n_code_DD", " ='00' ");
    }
    
}
