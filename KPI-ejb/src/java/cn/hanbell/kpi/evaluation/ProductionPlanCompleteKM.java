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
public class ProductionPlanCompleteKM extends ProductionPlanComplete {

    public ProductionPlanCompleteKM() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("prono", "1");
        queryParams.put("stats", "2");
        queryParams.put("linecode", " ='01' ");
        queryParams.put("typecode", " ='01' ");
        queryParams.put("prosscode", " in ('08','47')");
        queryParams.put("wrcode", " ='S1'");

    }
}
