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
public class SalesOrderAmountR1D2 extends SalesOrderAmount{

    public SalesOrderAmountR1D2() {
        super();
        queryParams.put("facno", "G");
        queryParams.put("deptno", " '1D000' ");
        queryParams.put("decode", "1");
        queryParams.put("n_code_DA", " ='R' ");
        //queryParams.put("n_code_CD", " ='GZ' ");
        queryParams.put("n_code_DC", " ='H' ");
        queryParams.put("n_code_DD", " ='00' ");
    }
}
