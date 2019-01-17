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
public class ProductionPlanCompletePJZ extends ProductionPlanComplete {

    public ProductionPlanCompletePJZ() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("stats", "2");
        queryParams.put("linecode", " ='VPZ' ");
        queryParams.put("typecode", " ='01' ");
        queryParams.put("prosscode", " in ('PS07')");
        queryParams.put("itcls", " in('3776','3A76','3780','3A80') ");

    }
}
