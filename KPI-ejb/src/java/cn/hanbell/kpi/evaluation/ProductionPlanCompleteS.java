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
public class ProductionPlanCompleteS extends ProductionPlanComplete {

    public ProductionPlanCompleteS() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("stats", "2");
        queryParams.put("linecode", " ='SJ' ");
        queryParams.put("typecode", " ='01' ");
        queryParams.put("prosscode", " ='FSSC'");
        queryParams.put("wrcode", "  ='S6' ");
        queryParams.put("itcls", " IN('3879','3979','3179','3886','3976','3176','3890','3180','3980') ");
    }
}
