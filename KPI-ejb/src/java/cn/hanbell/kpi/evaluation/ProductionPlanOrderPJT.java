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
public class ProductionPlanOrderPJT extends ProductionPlanOrder {

    public ProductionPlanOrderPJT() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='P' ");
        queryParams.put("itcls", " in ('3376','3379','3380','3476','3478','3480') ");
    }
}
