/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * A机体PR600
 */
public class ProductionPlanCompleteAJT1 extends ProductionPlanComplete {

    public ProductionPlanCompleteAJT1() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("stats", "2");
        queryParams.put("linecode", " ='AH' ");
        queryParams.put("typecode", " ='01' ");
        queryParams.put("prosscode", " in ('S1','S1-1','FAA09','FAA08') ");
        queryParams.put("wrcode", "  ='S4' ");
        queryParams.put("itcls", " IN ('3476','3479','3480') ");
    }
}
