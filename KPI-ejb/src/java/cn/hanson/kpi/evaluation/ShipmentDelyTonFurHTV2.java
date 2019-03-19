/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 汉声 THB灰铁 未来几天催货量（吨）
 */
public class ShipmentDelyTonFurHTV2 extends ShipmentDelyTon {

    public ShipmentDelyTonFurHTV2() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cdrcus", "THB");
        queryParams.put("material", "HT");
        queryParams.put("houtsta", "in ('N')");
    }

}
