/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 汉声 SHB球铁 实际催货量（金额）
 */
public class ShipmentDelyAmtsActQTV1 extends ShipmentDelyAmts {

    public ShipmentDelyAmtsActQTV1() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cdrcus", "SHB");
        queryParams.put("material", "QT");
        queryParams.put("houtsta", "not in ('W')");
    }

}
