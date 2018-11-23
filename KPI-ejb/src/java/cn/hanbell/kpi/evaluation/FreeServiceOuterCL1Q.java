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
public class FreeServiceOuterCL1Q extends FreeServiceOuterCL {

    public FreeServiceOuterCL1Q() {
        super();
//        queryParams.put("facno", "C");
//        queryParams.put("depno", " like '%' ");为找到合理的方法去确认离心机免费服务差旅费统计，待定
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return BigDecimal.ONE;
    }

    
    
}
