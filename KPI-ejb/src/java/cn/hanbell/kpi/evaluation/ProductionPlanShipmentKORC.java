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
public class ProductionPlanShipmentKORC extends ProductionPlanShipment {

    public ProductionPlanShipmentKORC() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("n_code_DC", " ='BA' ");
        queryParams.put("itcls", " in ('3B80') ");
    }
}
