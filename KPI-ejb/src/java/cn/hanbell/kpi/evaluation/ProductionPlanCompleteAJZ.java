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
public class ProductionPlanCompleteAJZ extends ProductionPlanComplete {

    public ProductionPlanCompleteAJZ() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("stats", "2");
        queryParams.put("linecode", " ='AJZ' ");
        queryParams.put("typecode", " ='01' ");
        queryParams.put("prosscode", " in ('S2','AH07')");
        queryParams.put("wrcode", "  ='S2' ");
        //queryParams.put("itcls", " IN('') ");

    }
}
