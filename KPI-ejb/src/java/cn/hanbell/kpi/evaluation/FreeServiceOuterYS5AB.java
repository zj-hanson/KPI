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
public class FreeServiceOuterYS5AB extends FreeServiceERP {

    public FreeServiceOuterYS5AB() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            Actual erp = (Actual) FreeServiceOuterYSerp5AB.class.newInstance();
            BigDecimal ev = erp.getValue(y, m, d, type, erp.getQueryParams());

            Actual oa = (Actual) FreeServiceOuterYSoa5AB.class.newInstance();
            BigDecimal ov = oa.getValue(y, m, d, type, oa.getQueryParams());
            return ev.add(ov);
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(FreeServiceOuterYS5AB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

}

class FreeServiceOuterYSerp5AB extends FreeServiceOuterYSerp {

    public FreeServiceOuterYSerp5AB() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("depno", " like '5%' ");
    }

    /**
     * @decription 2020年9月21号顺应陈海英需求，增加浙江柯茂的数据，有效期本年年底。
     */
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal temp1, temp2;
        //SHComer
        temp1 = super.getValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.remove("depno");
        queryParams.put("facno", "E");
        queryParams.put("depno", " like '8A%' ");
        //ZJComer
        temp2 = super.getValue(y, m, d, type, queryParams);
        //SHComer + ZJComer
        return temp1.add(temp2);
    }
}

class FreeServiceOuterYSoa5AB extends FreeServiceOuterYSoa {

    public FreeServiceOuterYSoa5AB() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("depno", " like '5%' ");
    }

    /**
     * @decription 2020年9月21号顺应陈海英需求，增加浙江柯茂的数据，有效期本年年底。
     */
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal temp1, temp2;
        //SHComer
        temp1 = super.getValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.remove("deptno");
        queryParams.put("facno", "E");
        queryParams.put("deptno", " like '8A%' ");
        //ZJComer
        temp2 = super.getValue(y, m, d, type, queryParams);
        //SHComer + ZJComer
        return temp1.add(temp2);
    }

}
