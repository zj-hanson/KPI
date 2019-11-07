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
 * @description 服务部周转天数（服务部(冷媒、冷冻+空压机组+空压机体+涡旋）--月
 */
public class InventoryTurnoverA3C10m extends InventoryTurnoverA3 {

    public InventoryTurnoverA3C10m() {
        super();
        queryParams.put("mm", "y");
        queryParams.put("formid", "服务部库存金额");
        queryParams.put("deptno", "1A000");
        queryParams.put("facno", "='C'");
        queryParams.put("issevdta", "'Y'");
        queryParams.put("n_code_da", "IN ('R','AH','AA','S')");
        queryParams.put("n_code_dc", "not in ('RT','SDS')");
        queryParams.put("n_code_dd", "in ('01','00')");
    }

}
