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
public class FreeServiceOuter5AB extends FreeServiceOuterERP{

    public FreeServiceOuter5AB() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            Actual erp = (Actual)FreeserveOuterERP5AB.class.newInstance();
            BigDecimal ev = erp.getValue(y, m, d, type, erp.getQueryParams());
            
            Actual oa = (Actual)FreeserveOuterOA5AB.class.newInstance();
            BigDecimal ov = oa.getValue(y, m, d, type, oa.getQueryParams());
            return ev.add(ov);
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(FreeServiceOuter1B.class.getName()).log(Level.SEVERE, null, ex);
        }               
        return BigDecimal.ZERO;
    }

}

class FreeserveOuterERP5AB extends FreeServiceOuterERP {

    public FreeserveOuterERP5AB() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("hmark1", "='HD' ");
        queryParams.put("hmark2", " IN('RTZ','RZ','ZSNY') ");
        queryParams.put("depno", " like '5%' ");
    }
}

class FreeserveOuterOA5AB extends FreeServiceOuterOA {

    public FreeserveOuterOA5AB() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("depno", " like '5%' ");
    }

}
