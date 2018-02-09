/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C0160
 */
public class ShipmentAmountAA2VN extends ShipmentAmountAA {

    public ShipmentAmountAA2VN() {
        super();
        queryParams.put("decode", "2");
        queryParams.put("ogdkid", "RLXX");
        queryParams.put("n_code_CD", " ='WXVN' ");
    }

}
