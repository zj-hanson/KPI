/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 汉声 THB其他 实际催货量（吨）
 */
public class ShipmentDelyTonActOTHV2 extends ShipmentDelyTon {

    public ShipmentDelyTonActOTHV2() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cdrcus", "THB");
        queryParams.put("material", "OTH");
        queryParams.put("houtsta", "not in ('W')");
    }
}
