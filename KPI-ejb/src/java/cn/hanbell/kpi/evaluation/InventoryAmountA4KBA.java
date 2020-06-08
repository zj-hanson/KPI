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
public class InventoryAmountA4KBA extends InventoryAmountA4 {

    public InventoryAmountA4KBA() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("trtype", "CA");
        queryParams.put("genre", "in('BA')");
    }

}
