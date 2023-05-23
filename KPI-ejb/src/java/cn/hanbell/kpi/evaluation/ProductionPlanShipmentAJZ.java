/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * 2019/12/25日 吴晓升提出新增大类3586
 */
public class ProductionPlanShipmentAJZ extends ProductionPlanShipment {

    public ProductionPlanShipmentAJZ() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AA' ");
        queryParams.put("n_code_DD", " ='00' ");
    }
}
