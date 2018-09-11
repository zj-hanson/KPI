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
public class SalesOrderQuantityR1D1 extends SalesOrderQuantity{

    public SalesOrderQuantityR1D1() {
        super();
        queryParams.put("facno", "G");
        queryParams.put("deptno", " '1D000' ");
        queryParams.put("decode", "1");
        queryParams.put("n_code_DA", " ='R' ");
        //queryParams.put("n_code_CD", " ='GZ' ");
        queryParams.put("n_code_DC", " ='R' ");
        queryParams.put("n_code_DD", " ='00' ");
    }
    
}
