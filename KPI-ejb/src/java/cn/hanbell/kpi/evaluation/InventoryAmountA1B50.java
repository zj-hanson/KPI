/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 */
// 加工刀片库存（含刀柄）
public class InventoryAmountA1B50 extends InventoryAmountA1 {

    public InventoryAmountA1B50() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("indicatorno", "B50");
    }

}
