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
public class SalesOrderAmountAA2 extends SalesOrderAmount{

    public SalesOrderAmountAA2() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1T000','1T100' ");
        queryParams.put("n_code_DA", " ='AA' ");
        queryParams.put("n_code_CD", " ='WX' ");
        queryParams.put("n_code_DC", " <> 'SDS' ");
        queryParams.put("n_code_DD", " in ('00','02') ");
    }

}
