/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * ORC移动平均出货台数
 */
public class QRACompShipmentK1M3 extends QRACompShipment {

    public QRACompShipmentK1M3() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("n_code_DA", " ='OH' ");
        queryParams.put("n_code_DC", "in ('BA') ");
        queryParams.put("n_code_DD", " ='00' ");
    }
}
