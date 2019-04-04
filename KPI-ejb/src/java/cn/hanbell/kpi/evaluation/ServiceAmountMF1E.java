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
public class ServiceAmountMF1E extends ServiceAmount {

    public ServiceAmountMF1E() {
        super();
        queryParams.put("deptno", "1E");
        queryParams.put("status", "N");
    }

}
