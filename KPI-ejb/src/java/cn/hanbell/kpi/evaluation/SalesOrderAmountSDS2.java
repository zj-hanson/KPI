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
public class SalesOrderAmountSDS2 extends SalesOrderAmount{

    public SalesOrderAmountSDS2() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("decode", "2");
        queryParams.put("deptno", " '1G000','1G500' ");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_DC", " ='SDS' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
