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
public class ProductionPlanCompletePJT extends ProductionPlanComplete {

    public ProductionPlanCompletePJT() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("stats", "2");
        queryParams.put("linecode", " ='VP' ");
        queryParams.put("typecode", " ='01' ");
        queryParams.put("prosscode", " in ('PSC3')");
        queryParams.put("itcls", " in('3376','3379','3380','3476','3479','3480') ");

    }
}
