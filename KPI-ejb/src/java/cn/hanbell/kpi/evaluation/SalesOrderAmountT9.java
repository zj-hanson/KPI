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
public class SalesOrderAmountT9 extends SalesOrderAmount {

    public SalesOrderAmountT9() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("decode", "2");
        queryParams.put("n_code_DA", " IN ('AA','AH') ");
        queryParams.put("n_code_CD", " NOT LIKE '%TW%' ");
        queryParams.put("n_code_DD", " ='01' ");//00是整机-01是零件-02是后处理
    }
}
