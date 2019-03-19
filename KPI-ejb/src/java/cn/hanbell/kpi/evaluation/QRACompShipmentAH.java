/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 */
public class QRACompShipmentAH extends QRACompShipment {

    public QRACompShipmentAH() {
        super();
        queryParams.put("facno", "C");
        //queryParams.put("decode", "1");
        queryParams.put("n_code_DA", " ='AH' ");
        queryParams.put("n_code_DD", " ='00' ");
    }
}
