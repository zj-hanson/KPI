/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * @description:柯茂其他性质库存金额
 */
public class InventoryAmountA5KF05 extends InventoryAmountA5 {

    public InventoryAmountA5KF05() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("categories", "A5");
    }

}
