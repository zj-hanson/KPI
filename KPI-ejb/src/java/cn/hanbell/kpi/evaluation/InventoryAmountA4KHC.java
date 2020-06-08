/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * @description 柯茂服务性质离心机组库存金额
 */
public class InventoryAmountA4KHC extends InventoryAmountA4 {

    public InventoryAmountA4KHC() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("trtype", "CA");
        queryParams.put("genre", "in('HC')");
    }

}
