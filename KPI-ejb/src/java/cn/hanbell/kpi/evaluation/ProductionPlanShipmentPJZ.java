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
public class ProductionPlanShipmentPJZ extends ProductionPlanShipment {

    public ProductionPlanShipmentPJZ() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='P' ");
        //#ITCLS CHANGE TODO #
        queryParams.put("itcls", " in('3776','3780','4079','4052','6053','3779','3A76','3A79','3A80','3379','3476','3376','3480') ");
    }
}
