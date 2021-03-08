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
public class FreeServiceOuterCL5B extends FreeServiceOuterCL {

    public FreeServiceOuterCL5B() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("depno", " like '5B%' ");
    }
    
    /**
     * @return 
     * @decription 2020年9月21号顺应陈海英需求，增加浙江柯茂的数据，有效期本年年底。
     */
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal temp1, temp2;
        //SHComer
        temp1 = super.getValue(y, m, d, type, map);
        queryParams.remove("depno");
        queryParams.put("depno", " like '8A%' ");
        //ZJComer
        temp2 = super.getValue(y, m, d, type, map);
        //SHComer + ZJComer
        return temp1.add(temp2);
    }

}
