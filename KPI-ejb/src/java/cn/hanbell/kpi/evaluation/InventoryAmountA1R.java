/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * @description:生产性-冷媒
 */
public class InventoryAmountA1R extends InventoryAmountA1 {

    public InventoryAmountA1R() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("genre", "in ('R','L','RG')");
    }
}
