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
 * @version V1.0
 * @author C1749
 * @data 2019-10-28
 * @description 原材料周转天数
 */
public class InventoryTurnoverA1B20y extends InventoryTurnoverA1 {

    public InventoryTurnoverA1B20y() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal sell, result;
        try {
            sell = getYearSellingValue(y, m, d, type, map);
            queryParams.clear();
            queryParams.put("formid", "原材料库存金额");
            queryParams.put("deptno", "1P000");
            result = getYearValue(y, m, d, type, queryParams, sell);
            return result;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

}
