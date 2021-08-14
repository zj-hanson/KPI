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
public class ServiceClosingLJ5B extends ServiceClosing {

    public ServiceClosingLJ5B() {
        super();
        queryParams.put("deptno", "like '5B%'");
        queryParams.put("status", "LJ");
    }

}
