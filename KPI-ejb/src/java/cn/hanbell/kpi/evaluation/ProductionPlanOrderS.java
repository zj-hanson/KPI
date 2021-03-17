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
public class ProductionPlanOrderS extends ProductionPlanOrder {

    public ProductionPlanOrderS() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AH' ");
        queryParams.put("n_code_DC", " like 'SAM%' ");
        queryParams.put("itcls", " in('3879','3979','3179','3886','3976','3176','3890','3180','3980') ");

    }
}
