/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * 柯茂水冷机组移动平均出货台数
 */
public class QRAComplaintShipmentK1M1 extends QRAComplaintShipment {

    public QRAComplaintShipmentK1M1() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("n_code_DA", " in ('RT','OH') ");
        queryParams.put("n_code_DC", " in ('WC') ");
        queryParams.put("n_code_DD", " ='00' ");    
    }
}
