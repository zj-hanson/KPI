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
 * @description 服务合计周转天数--月
 */
public class InventoryTurnoverA3C05m extends InventoryTurnoverA3 {

    public InventoryTurnoverA3C05m() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal sell, result;
        try {
            sell = getSellingValue(y, m, d, type, map);
            queryParams.clear();
            queryParams.put("formid", "服务目标合计");
            queryParams.put("deptno", "1K000");
            result = getMonthValue(y, m, d, type, queryParams, sell);
            return result;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getSellingValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal v1, v2, v3, v4, v5, v6;
        BigDecimal result;
        queryParams.clear();//服务部
        queryParams.put("facno", "='C'");
        queryParams.put("issevdta", "'Y'");
        queryParams.put("n_code_da", "IN ('R','AH','AA','S')");
        queryParams.put("n_code_dc", "not in ('RT','SDS')");
        queryParams.put("n_code_dd", "in ('01','00')");
        v1 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();//南京分公司
        queryParams.put("issevdta", "'Y'");
        queryParams.put("n_code_cd", "in('NJ')");
        queryParams.put("n_code_dc", "not in ('RT','SDS')");
        queryParams.put("n_code_dd", "in ('01')");
        v2 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();//广州分公司
        queryParams.put("issevdta", "'Y'");
        queryParams.put("n_code_cd", "in('GZ')");
        queryParams.put("n_code_dc", "not in ('RT','SDS')");
        queryParams.put("n_code_dd", "in ('01')");
        v3 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();//济南分公司
        queryParams.put("issevdta", "'Y'");
        queryParams.put("n_code_cd", "in('JN')");
        queryParams.put("n_code_dc", "not in ('RT','SDS')");
        queryParams.put("n_code_dd", "in ('01')");
        v4 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();//华东分公司
        queryParams.put("facno", "='C'");
        queryParams.put("issevdta", "'Y'");
        queryParams.put("n_code_da", "= 'R'");
        queryParams.put("n_code_cd", "in('HD')");
        queryParams.put("n_code_dc", "not in ('RT','SDS')");
        queryParams.put("n_code_dd", "in ('01')");
        v5 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();//重庆分公司
        queryParams.put("deptno", "1V000");
        queryParams.put("issevdta", "'Y'");
        queryParams.put("n_code_cd", "in('CQ')");
        queryParams.put("n_code_dc", "not in ('RT','SDS')");
        queryParams.put("n_code_dd", "in ('01')");
        v6 = getMonthSellingCost(y, m, d, type, queryParams);

        try {
            result = v1.add(v2).add(v3).add(v4).add(v5).add(v6);
            return result;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;

    }

}
