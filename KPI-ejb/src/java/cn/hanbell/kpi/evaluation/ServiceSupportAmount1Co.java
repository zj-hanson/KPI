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
public class ServiceSupportAmount1Co extends ServiceSupportAmount {

    public ServiceSupportAmount1Co() {
        super();
        queryParams.put("deptno", " like '1C%'");
        queryParams.put("status", "other");
    }

}
