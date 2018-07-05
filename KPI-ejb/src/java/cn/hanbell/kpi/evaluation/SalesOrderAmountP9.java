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
public class SalesOrderAmountP9 extends SalesOrderAmount{

    public SalesOrderAmountP9() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("decode", "");
        queryParams.put("n_code_DA", " ='P' ");
    }

}
