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
public class ServiceTimeLC1B extends ServiceTime {

    public ServiceTimeLC1B() {
        super();
        queryParams.put("deptno", " in ('1F700','1F800','1B000','1B100','1B200')");
        queryParams.put("status", "LC");
    }

}
