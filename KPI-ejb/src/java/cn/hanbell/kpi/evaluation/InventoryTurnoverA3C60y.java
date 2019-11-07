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
 * @description 重庆周转天数--年
*/
public class InventoryTurnoverA3C60y extends InventoryTurnoverA3 {

    public InventoryTurnoverA3C60y() {
        super();
        queryParams.put("mm", "y");
        queryParams.put("formid", "重庆制冷库存金额");
        queryParams.put("deptno", "1V000");
        queryParams.put("issevdta", "'Y'");
        queryParams.put("n_code_cd", "in('CQ')");
        queryParams.put("n_code_dc", "not in ('RT','SDS')");
        queryParams.put("n_code_dd", "in ('01')");
    }

}
