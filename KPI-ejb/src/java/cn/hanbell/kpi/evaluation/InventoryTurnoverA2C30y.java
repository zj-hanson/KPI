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
 * @description 无油机组周转天数(含服务)--年
 */
public class InventoryTurnoverA2C30y extends InventoryTurnoverA2 {

    public InventoryTurnoverA2C30y() {
        super();
        queryParams.put("formid", "无油机组库存金额");
        queryParams.put("deptno", "1G500");
        queryParams.put("facno", "C");
        queryParams.put("n_code_da", "= 'AH'");
        queryParams.put("n_code_dc", "= 'SDS'");
        queryParams.put("n_code_dd", "in ('00','02','01')");
    }

}
