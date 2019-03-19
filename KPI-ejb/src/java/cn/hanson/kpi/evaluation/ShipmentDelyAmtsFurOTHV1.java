/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 汉声 SHB其他 未来几天催货量（金额）
 */
public class ShipmentDelyAmtsFurOTHV1 extends ShipmentDelyAmts {

    public ShipmentDelyAmtsFurOTHV1() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cdrcus", "SHB");
        queryParams.put("material", "OTH");
        queryParams.put("houtsta", "in ('N')");
    }

}
