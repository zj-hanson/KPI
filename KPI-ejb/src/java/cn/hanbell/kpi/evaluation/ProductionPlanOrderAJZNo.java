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
public class ProductionPlanOrderAJZNo extends ProductionPlanOrder {

    public ProductionPlanOrderAJZNo() {
        super();
        queryParams.put("noUpdate", "true");
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AA' ");
        //#ITCLS CHANGE TODO #
        queryParams.put("itcls", " in ('3576','3579','3580','4052','3676','3679','3680') and itnbr <> '35A2C-H0233-08' ");

    }
}
