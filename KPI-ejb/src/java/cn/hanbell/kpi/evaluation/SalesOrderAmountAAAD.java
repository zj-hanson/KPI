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
public class SalesOrderAmountAAAD extends SalesOrderAmountAA{

    public SalesOrderAmountAAAD() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1Q000' ");
        queryParams.put("n_code_DA", " ='AA' ");
        queryParams.put("n_code_CD", " NOT LIKE 'WX%' ");
        queryParams.put("n_code_DC", " LIKE 'AA%' ");
        queryParams.put("n_code_DD", " in ('00','02') ");
        queryParams.put("modelcode", " in ('AD') ");//两级压缩系列
    }

}
