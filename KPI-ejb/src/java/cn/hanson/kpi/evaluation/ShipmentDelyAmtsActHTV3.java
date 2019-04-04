/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 汉声 OTHER灰铁 实际催货量（金额）
 */
public class ShipmentDelyAmtsActHTV3 extends ShipmentDelyAmts {

    public ShipmentDelyAmtsActHTV3() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cdrcus", "OTHER");
        queryParams.put("material", "HT");
        queryParams.put("houtsta", "not in ('W')");
    }

}
