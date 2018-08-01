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
public class SalesOrderAmountAJ9 extends SalesOrderAmount{

    public SalesOrderAmountAJ9() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("decode", "1");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_DC", " LIKE 'AJ%' ");
    }

}