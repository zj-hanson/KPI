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
public class ProductionPlanShipmentS extends ProductionPlanShipment {

    public ProductionPlanShipmentS() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AH' ");
        queryParams.put("n_code_DC", " like 'SAM%' ");
        //#ITCLS CHANGE TODO #
        queryParams.put("itcls", " IN('3B76','3B79','3B80','3C76','3C79','3C80') ");
    }
}
