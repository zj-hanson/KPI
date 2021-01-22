/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 */
public class SalesOrderAmountSAMQT extends SalesOrderAmount {

    public SalesOrderAmountSAMQT() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_CD", " NOT LIKE 'WX%' ");
        queryParams.put("n_code_DC", " ='SAM-QT' ");
        queryParams.put("n_code_DD", " IN ('00','02') ");
    }

}
