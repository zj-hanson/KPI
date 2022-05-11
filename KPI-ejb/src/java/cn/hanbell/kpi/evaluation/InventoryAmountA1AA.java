/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * @description:生产性-空压机组
 */
public class InventoryAmountA1AA extends InventoryAmountA1 {

    public InventoryAmountA1AA() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("genre", "= 'A'");
        queryParams.put("genreno", " ='A1'");
        queryParams.put("genrena", " ='生产目标'");
    }
}
