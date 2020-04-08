/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * @description:真空新机 库号为W01 和 EW01
 */
public class InventoryAmountA2C51 extends InventoryAmountA2 {

    public InventoryAmountA2C51() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("categories", "A2");
        queryParams.put("genre", "='P'");
        queryParams.put("itclscode", "<>'L'");
        queryParams.put("wareh", "in ('W01','EW01')");
    }

}
