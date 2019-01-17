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
public class ProductionPlanOrderKHC extends ProductionPlanOrder {

    public ProductionPlanOrderKHC() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("n_code_DC", " ='HC' ");
        queryParams.put("itcls", " IN('3H76','3H79','3H80')");
    }

}
