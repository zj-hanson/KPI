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
// 涡旋周转天数（不含服务）
//本月周转天数 = 30 / (本月销售成本/((本月库存金额+上月库存金额)/2))
public class InventoryTurnoverA2C60m extends InventoryTurnoverA2 {

    public InventoryTurnoverA2C60m() {
        super();
        queryParams.put("mm", "y");
        queryParams.put("formid", "无油涡旋库存金额");
        queryParams.put("deptno", "1U000");
        queryParams.put("facno", "C");
        queryParams.put("issevdta", "'N'");
        queryParams.put("n_code_da", "= 'S'");
        queryParams.put("n_code_dd", "in ('00','02')");
    }

}
