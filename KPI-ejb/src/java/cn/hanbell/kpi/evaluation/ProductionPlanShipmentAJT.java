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
public class ProductionPlanShipmentAJT extends ProductionPlanShipment {

    public ProductionPlanShipmentAJT() {
        super();
        queryParams.put("facno", "C");
        //queryParams.put("n_code_DA", " ='AH' ");
        //#ITCLS CHANGE TODO #
        queryParams.put("itcls", " in('3886','3889','3890','3876','3879','3880','3976','3979','3980') and itnbr <> '39011-GBA00' ");
    }
}
