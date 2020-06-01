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
public class ServiceMaintainTimeP extends ServiceMaintainTime {

    public ServiceMaintainTimeP() {
        super();
        queryParams.put("CProductType", "in('P机体','P机组')");
    }
}
