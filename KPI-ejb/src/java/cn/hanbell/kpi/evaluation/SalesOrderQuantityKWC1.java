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
public class SalesOrderQuantityKWC1 extends SalesOrderQuantity{

    public SalesOrderQuantityKWC1() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("deptno", " '' ");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DC", " ='WC' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
