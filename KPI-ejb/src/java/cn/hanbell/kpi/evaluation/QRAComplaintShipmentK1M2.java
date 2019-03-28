/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * 空气源移动平均出货台数
 */
public class QRAComplaintShipmentK1M2 extends QRAComplaintShipment {

    public QRAComplaintShipmentK1M2() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("n_code_DA", " in('RT','OH') ");
        queryParams.put("n_code_DC", "in ('WL') ");
        queryParams.put("n_code_DD", " ='00' ");
    }
}
