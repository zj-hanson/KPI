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
public class ServiceAmountMF1D extends ServiceAmount {

    public ServiceAmountMF1D() {
        super();
        queryParams.put("deptno", "like '1D%'");
        queryParams.put("status", "N");
    }

}
