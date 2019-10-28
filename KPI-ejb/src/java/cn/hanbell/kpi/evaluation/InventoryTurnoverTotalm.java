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
// 库存周转天数合计
//本月周转天数 = 30 / (本月销售成本/((本月库存金额+上月库存金额)/2))
public class InventoryTurnoverTotalm extends InventoryTurnoverA3 {

    public InventoryTurnoverTotalm() {
        super();
        queryParams.put("mm", "y");
        queryParams.put("formid", "库存金额合计");
        queryParams.put("deptno", "1K000");
    }

}
