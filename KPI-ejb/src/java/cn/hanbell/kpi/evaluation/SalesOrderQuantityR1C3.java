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
public class SalesOrderQuantityR1C3 extends SalesOrderQuantity {

    public SalesOrderQuantityR1C3() {
        super();
        queryParams.put("facno", "J");
        queryParams.put("deptno", " '1C000' ");
        queryParams.put("decode", "1");
        queryParams.put("n_code_DA", " ='R' ");
        //queryParams.put("n_code_CD", " ='JN' ");
        queryParams.put("n_code_DC", " ='L' ");
        queryParams.put("n_code_DD", " ='00' ");
    }
}
