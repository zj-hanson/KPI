/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
public class FreeServiceOuterFW5C extends FreeServiceOuterFW {
    
    public FreeServiceOuterFW5C() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("hmark2", " ='RT' ");
    }
    
    //2019年6月5日免费服务离心机服务领退料并入柯茂涡轮
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal rt = super.getValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.remove("hmark2");
        queryParams.put("facno", "C");
        queryParams.put("hmark1", " <> 'CK' ");
        queryParams.put("hmark2", " ='CM' ");
        BigDecimal cm = super.getValue(y, m, d, type, map);
        return rt.add(cm);
    }
    
}
