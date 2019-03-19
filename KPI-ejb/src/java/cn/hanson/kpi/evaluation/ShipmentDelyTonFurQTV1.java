/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 汉声 SHB球铁 未来几天催货量（吨）
 */
public class ShipmentDelyTonFurQTV1 extends ShipmentDelyTon {

    public ShipmentDelyTonFurQTV1() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cdrcus", "SHB");
        queryParams.put("material", "QT");
        queryParams.put("houtsta", "in ('N')");
    }

}
