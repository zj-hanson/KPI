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
public class ServiceMaintainTimeR extends ServiceMaintainTime {

    public ServiceMaintainTimeR() {
        super();
        queryParams.put("CProductType", "R冷媒");
    }
}
