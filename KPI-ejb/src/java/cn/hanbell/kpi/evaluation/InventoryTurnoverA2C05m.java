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
 * @description 营业目标合计--月
 */
public class InventoryTurnoverA2C05m extends InventoryTurnoverA2 {

    public InventoryTurnoverA2C05m() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal sell, result;
        try {
            sell = getSellingValue(y, m, d, type, map);
            queryParams.clear();
            queryParams.put("formid", "营业目标合计");
            queryParams.put("deptno", "1K000");
            result = getMonthValue(y, m, d, type, queryParams, sell);
            return result;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getSellingValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal v1, v2, v3, v4, v5, v6, v7, v8;
        BigDecimal result;
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("issevdta", "'N'");
        queryParams.put("n_code_da", "= 'R'");
        queryParams.put("n_code_dc", "<> 'RT'");
        queryParams.put("n_code_dd", "in ('00','02')");
        v1 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("issevdta", "'N'");
        queryParams.put("n_code_da", "= 'AH'");
        queryParams.put("n_code_dc", "= 'AJ'");
        queryParams.put("n_code_dd", "in ('00','02')");
        v2 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_da", "= 'AH'");
        queryParams.put("n_code_dc", "= 'SDS'");
        queryParams.put("n_code_dd", "in ('00','02','01')");
        v3 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("issevdta", "'N'");
        queryParams.put("n_code_da", "= 'AA'");
        queryParams.put("n_code_dd", "in ('00','02')");
        v4 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_da", "= 'P'");
        queryParams.put("n_code_dd", "in ('00','02','01')");
        v5 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("issevdta", "'N'");
        queryParams.put("n_code_da", "= 'S'");
        queryParams.put("n_code_dd", "in ('00','02')");
        v6 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_da", "= 'R'");
        queryParams.put("n_code_dc", "= 'RT'");
        queryParams.put("n_code_dd", "in ('00','02','01')");
        v7 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        v8 = getMonthRepairCost(y, m, d, type, queryParams);
        try {
            result = v1.add(v2).add(v3).add(v4).add(v5).add(v6).add(v7).add(v8);
            return result;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }
}
