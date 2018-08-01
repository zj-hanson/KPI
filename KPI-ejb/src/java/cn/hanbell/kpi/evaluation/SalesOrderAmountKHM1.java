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
public class SalesOrderAmountKHM1 extends SalesOrderAmount{

    public SalesOrderAmountKHM1() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("deptno", " '5A000','5A100' ");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='RT' ");
        queryParams.put("n_code_DC", " ='HM' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}