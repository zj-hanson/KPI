/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749
 */
public class VarietyAmtsZCHTV2 extends ShipmentPredictAmts {

    public VarietyAmtsZCHTV2() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("variety", "ZCHT");
    }

}
