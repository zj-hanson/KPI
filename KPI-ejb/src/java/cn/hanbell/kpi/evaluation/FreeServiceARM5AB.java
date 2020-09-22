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
public class FreeServiceARM5AB extends FreeServiceERP {

    public FreeServiceARM5AB() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("ogdkid", "('RL01','RL03')");
        queryParams.put("n_code_DA", " IN('OH','RT') ");
        queryParams.put("n_code_DD", "  IN ('00','02') ");
    }

    /**
     * @decription 2020年9月21号顺应陈海英需求，增加浙江柯茂的数据，有效期本年年底。
     */
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal temp1, temp2;
        //SHComer
        temp1 = super.getARM423Value(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.put("facno", "E");
        //ZJComer
        temp2 = super.getARM423Value(y, m, d, type, queryParams);
        //SHComer + ZJComer
        return temp1.add(temp2);
    }

}
