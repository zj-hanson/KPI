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
 * @author C1879
 */
public class EmployeeSalesOrderAmountR_J extends EmployeeSalesOrderAmount {

    public EmployeeSalesOrderAmountR_J() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal amount1, amount2;
        amount1 = super.getValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.put("facno", "J");
        amount2 = super.getValue(y, m, d, type, map);
        return amount1.add(amount2);
    }
}
