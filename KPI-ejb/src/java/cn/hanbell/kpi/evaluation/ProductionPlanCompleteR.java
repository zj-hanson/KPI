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
public class ProductionPlanCompleteR extends ProductionPlanComplete {

    public ProductionPlanCompleteR() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("stats", "2");
        queryParams.put("linecode", " ='RC' ");
        queryParams.put("typecode", " ='01' ");
        queryParams.put("prosscode", " in ('FRZ09','RSL1')");
        //#ITCLS CHANGE TODO #
        queryParams.put("itcls", " IN('3176','3179','3180','3276','3279','3280','3083','4079') ");

    }
}
