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
public class SalesOrderAmountR1D1 extends SalesOrderAmount{

    public SalesOrderAmountR1D1() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1D000' ");
        queryParams.put("decode", "1");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", " ='GZ' ");
        queryParams.put("n_code_DC", " ='R' ");
        queryParams.put("n_code_DD", " ='00' ");
    }
    
}