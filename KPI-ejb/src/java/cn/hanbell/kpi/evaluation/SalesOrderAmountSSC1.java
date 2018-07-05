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
public class SalesOrderAmountSSC1 extends SalesOrderAmount {

    public SalesOrderAmountSSC1() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1U000' ");
        queryParams.put("n_code_DA", "='S' ");
        queryParams.put("n_code_DC", " ='SC' ");
        queryParams.put("n_code_DD", " ='00' ");
    }
    
}
