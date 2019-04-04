/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 汉声 SHB灰铁实际催货量（吨）
 */
public class ShipmentDelyTonActHTV1 extends ShipmentDelyTon {

    public ShipmentDelyTonActHTV1() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cdrcus", "SHB");
        queryParams.put("material", "HT");
        queryParams.put("houtsta", "not in ('W')");
    }
}
