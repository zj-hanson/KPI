/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author C1879
 */
public class FreeServiceOuter1U extends FreeServiceOuterERP{

    public FreeServiceOuter1U() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            Actual erp = (Actual)FreeserveOuterERP1U.class.newInstance();
            BigDecimal ev = erp.getValue(y, m, d, type, erp.getQueryParams());
            
            Actual oa = (Actual)FreeserveOuterOA1U.class.newInstance();
            BigDecimal ov = oa.getValue(y, m, d, type, oa.getQueryParams());
            return ev.add(ov);
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(FreeServiceOuter1B.class.getName()).log(Level.SEVERE, null, ex);
        }               
        return BigDecimal.ZERO;
    }

}

class FreeserveOuterERP1U extends FreeServiceOuterERP {

    public FreeserveOuterERP1U() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("hmark1", "='HD' ");
        queryParams.put("hmark2", " ='S' ");
        queryParams.put("depno", " like '1U%' ");
    }
}

class FreeserveOuterOA1U extends FreeServiceOuterOA {

    public FreeserveOuterOA1U() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("depno", " like '1U%' ");
    }

}
