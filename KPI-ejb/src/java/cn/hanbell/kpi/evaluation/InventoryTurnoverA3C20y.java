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
 * @description 南京周转天数--年
 */
public class InventoryTurnoverA3C20y extends InventoryTurnoverA3 {

    public InventoryTurnoverA3C20y() {
        super();
        queryParams.put("formid", "南京制冷库存金额");
        queryParams.put("deptno", "1E000");
        queryParams.put("issevdta", "'Y'");
        queryParams.put("n_code_cd", "in('NJ')");
        queryParams.put("n_code_dc", "not in ('RT','SDS')");
        queryParams.put("n_code_dd", "in ('01')");
    }

}
