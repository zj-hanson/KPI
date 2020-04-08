/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * @description:生产性-真空
 */

public class InventoryAmountA1P extends InventoryAmountA1 {

    public InventoryAmountA1P() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("genre", "='P'");
    }
}
