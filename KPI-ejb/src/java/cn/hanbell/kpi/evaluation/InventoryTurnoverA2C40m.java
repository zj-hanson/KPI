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
 * @description 空压机组周转天数（不含服务）--月
 */
public class InventoryTurnoverA2C40m extends InventoryTurnoverA2 {

    public InventoryTurnoverA2C40m() {
        super();
        queryParams.put("mm", "y");
        queryParams.put("formid", "空压机组库存金额");
        queryParams.put("deptno", "1Q000");
        queryParams.put("facno", "C");
        queryParams.put("issevdta", "'N'");
        queryParams.put("n_code_da", "= 'AA'");
        queryParams.put("n_code_dd", "in ('00','02')");
    }

}
