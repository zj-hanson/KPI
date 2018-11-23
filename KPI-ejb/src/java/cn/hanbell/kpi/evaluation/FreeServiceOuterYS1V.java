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
public class FreeServiceOuterYS1V extends FreeServiceERP {

    public FreeServiceOuterYS1V() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            Actual erp = (Actual) FreeServiceOuterYSerp1V.class.newInstance();
            BigDecimal ev = erp.getValue(y, m, d, type, erp.getQueryParams());

            Actual oa = (Actual) FreeServiceOuterYSoa1V.class.newInstance();
            BigDecimal ov = oa.getValue(y, m, d, type, oa.getQueryParams());
            return ev.add(ov);
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(FreeServiceOuterYS1V.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

}

class FreeServiceOuterYSerp1V extends FreeServiceOuterYSerp {

    public FreeServiceOuterYSerp1V() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("depno", " like '1V%' ");
    }
}

class FreeServiceOuterYSoa1V extends FreeServiceOuterYSoa {

    public FreeServiceOuterYSoa1V() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("depno", " like '1V%' ");
    }

}
