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
 * @author C1749
 */
public class QRAOnLineNumberR2 extends QRABadFeedRate{
    public QRAOnLineNumberR2(){
        super();
        queryParams.put("SYSTEMID", "'QC_SXBLReport'");
        queryParams.put("SEQUENCE", " in ('1','2') ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return super.getValue(y, m, d, type, map).multiply(BigDecimal.valueOf(1000000));
    }
    
}
