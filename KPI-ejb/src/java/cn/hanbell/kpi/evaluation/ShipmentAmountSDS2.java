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
public class ShipmentAmountSDS2 extends ShipmentAmount {

    public ShipmentAmountSDS2() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1T100' ");
        queryParams.put("decode", "2");
        queryParams.put("ogdkid", "RL03");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_DC", " ='SDS'");
        queryParams.put("n_code_DD", " in ('00','02') ");
    }

}
