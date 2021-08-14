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
public class ServiceTimeXC5B extends ServiceTime {

    public ServiceTimeXC5B() {
        super();
        queryParams.put("deptno", "like '8A%'");
        queryParams.put("status", "");
    }

}
