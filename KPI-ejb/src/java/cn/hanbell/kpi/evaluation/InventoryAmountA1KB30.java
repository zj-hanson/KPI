/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * @description 柯茂原材料库存金额
 */
public class InventoryAmountA1KB30 extends InventoryAmountA1 {

    public InventoryAmountA1KB30() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("prono", "1");
        queryParams.put("itclscopde", "='2'");
    }

}
