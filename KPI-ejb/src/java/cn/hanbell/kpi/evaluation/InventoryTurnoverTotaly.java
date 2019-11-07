/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @version V1.0
 * @author C1749
 * @data 2019-10-28
 * @description 库存周转天数合计 年
 */
public class InventoryTurnoverTotaly extends InventoryTurnoverA3 {

    public InventoryTurnoverTotaly() {
        super();
        queryParams.put("formid", "库存金额合计");
        queryParams.put("deptno", "1K000");
    }

}
