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
public class SalesOrderAmountSAM2 extends SalesOrderAmount {

    public SalesOrderAmountSAM2() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_CD", " LIKE 'WX%' ");
        queryParams.put("n_code_DC", " IN ('SAM-5HP','SAM-7HP') ");
        queryParams.put("n_code_DD", " IN ('00','02') ");
    }

}
