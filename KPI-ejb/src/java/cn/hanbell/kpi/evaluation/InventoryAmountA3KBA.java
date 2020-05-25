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
public class InventoryAmountA3KBA extends InventoryAmountA3 {

    public InventoryAmountA3KBA() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("genre", "in('BA')");
    }

}
