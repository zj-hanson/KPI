/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 汉声 灰铁 未来几天催货量（吨）
 */
public class ShipmentDelyTonFurHT extends ShipmentDelyTon {

    public ShipmentDelyTonFurHT() {
        super();
        queryParams.put("facno", "H");
        //queryParams.put("cdrcus", "SHB");
        queryParams.put("material", "HT");
        queryParams.put("houtsta", "in ('N')");
    }

}
