/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * @description 柯茂空气源热泵库存金额
 */
public class InventoryAmountA2KWL extends InventoryAmountA2 {

    public InventoryAmountA2KWL() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("categories", "A2");
        queryParams.put("genre", "in('WL')");
    }
    
}
