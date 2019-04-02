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
public class ServiceSupportAmount1Eo extends ServiceSupportAmount {

    public ServiceSupportAmount1Eo() {
        super();
        queryParams.put("deptno", "1E");
        queryParams.put("status", "other");
    }

}
