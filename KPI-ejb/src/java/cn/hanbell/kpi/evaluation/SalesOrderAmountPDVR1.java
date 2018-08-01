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
public class SalesOrderAmountPDVR1 extends SalesOrderAmount{

    public SalesOrderAmountPDVR1() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1H000','1H100' ");
        queryParams.put("n_code_DA", " ='P' ");
        queryParams.put("n_code_DC", " ='DVR' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
