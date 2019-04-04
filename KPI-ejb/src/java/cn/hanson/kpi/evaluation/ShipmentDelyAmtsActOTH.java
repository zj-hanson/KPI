/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 汉声 其他 实际催货量（金额）
 */
public class ShipmentDelyAmtsActOTH extends ShipmentDelyAmts {

    public ShipmentDelyAmtsActOTH() {
        super();
        queryParams.put("facno", "H");
        //queryParams.put("cdrcus", "SHB");
        queryParams.put("material", "OTH");
        queryParams.put("houtsta", "not in ('W')");
    }

}
