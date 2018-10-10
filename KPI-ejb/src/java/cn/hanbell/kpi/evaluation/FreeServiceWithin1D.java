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
public class FreeServiceWithin1D extends FreeServiceWithin {

    public FreeServiceWithin1D() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("hmark1", "='HN' ");
        queryParams.put("hmark2", " ='R' ");
    }

    @Override
    public BigDecimal getMAN410and510(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal temp1, temp2;
        //SHB ERP
        temp1 = super.getValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.put("facno", "G");
        //GZ ERP
        temp2 = super.getValue(y, m, d, type, queryParams);
        //SHB + GZ
        return temp1.add(temp2);
    }
}
