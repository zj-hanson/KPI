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
public class ServiceAmountSF1G extends ServiceAmount {

    public ServiceAmountSF1G() {
        super();
        queryParams.put("deptno", "1G");
        queryParams.put("status", "Y");
    }

}
