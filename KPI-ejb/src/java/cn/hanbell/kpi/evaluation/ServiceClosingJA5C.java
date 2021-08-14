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
public class ServiceClosingJA5C extends ServiceClosing {

    public ServiceClosingJA5C() {
        super();
        queryParams.put("deptno", "like '5C%'");
        queryParams.put("status", "JA");
    }

}
