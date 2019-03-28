/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C0160
 * //真空总的年移动平均台数接口
 */
public class QRAComplaintShipmentP1V1 extends QRAComplaintShipment {
    
    public QRAComplaintShipmentP1V1() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='P' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
