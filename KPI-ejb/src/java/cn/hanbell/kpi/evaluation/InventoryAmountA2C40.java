/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author C1749
 */
public class InventoryAmountA2C40 extends InventoryAmountA2 {

    public InventoryAmountA2C40() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("categories", "A2");
        queryParams.put("genre", "='A'");
    }


}
