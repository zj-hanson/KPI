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
public class FreeServiceOuterYSRT extends FreeServiceERP {

    public FreeServiceOuterYSRT() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //暂时无法统计离心机免费服务运输费用
//        try {
//            Actual erp = (Actual) FreeServiceOuterYSerpRT.class.newInstance();
//            BigDecimal ev = erp.getValue(y, m, d, type, erp.getQueryParams());
//
//            Actual oa = (Actual) FreeServiceOuterYSoaRT.class.newInstance();
//            BigDecimal ov = oa.getValue(y, m, d, type, oa.getQueryParams());
//            return ev.add(ov);
//        } catch (InstantiationException | IllegalAccessException ex) {
//            Logger.getLogger(FreeServiceOuterYSRT.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return BigDecimal.ZERO;
    }

}
//
//class FreeServiceOuterYSerpRT extends FreeServiceOuterYSerp {
//
//    public FreeServiceOuterYSerpRT() {
//        super();
//        queryParams.put("facno", "C");
//        queryParams.put("depno", " like '%' ");
//    }
//}
//
//class FreeServiceOuterYSoaRT extends FreeServiceOuterYSoa {
//
//    public FreeServiceOuterYSoaRT() {
//        super();
//        queryParams.put("facno", "C");
//        queryParams.put("depno", " like '%' ");
//    }
//
//}
