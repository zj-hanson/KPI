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
public class SalesOrderQuantitySAM2 extends SalesOrderQuantity {

    public SalesOrderQuantitySAM2() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_CD", " LIKE 'WX%' ");
        //20201004 暂时将外销的并到一起
        queryParams.put("n_code_DC", " IN ('SAM-5HP','SAM-7HP') ");
        queryParams.put("n_code_DD", " ='00' ");
    }
}
