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
public class ProductionPlanOrderR extends ProductionPlanOrder {

    public ProductionPlanOrderR() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='R' ");
        //#ITCLS CHANGE TODO #
        queryParams.put("itcls", " IN('3176','3179','3180','3276','3279','3280','3083','4079') ");

    }
}
