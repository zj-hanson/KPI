/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 汉声 灰铁实际催货量（吨）
 */
public class DeliveryActualTonHYOTH extends ShipmentDelyTon {

    public DeliveryActualTonHYOTH() {
        super();
        queryParams.put("facno", "Y");
        queryParams.put("protype", "not in ('HT','QT') ");
        queryParams.put("houtsta", "not in ('W')");
    }

}
