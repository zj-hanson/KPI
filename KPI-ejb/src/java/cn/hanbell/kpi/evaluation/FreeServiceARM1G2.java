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
public class FreeServiceARM1G2 extends FreeServiceERP {

    public FreeServiceARM1G2() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("ogdkid", "('RL01','RL03')");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_CD", "  in('GZ','HN') ");
        queryParams.put("n_code_DC", " LIKE 'AJ%' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return super.getARM423Value(y, m, d, type, map);
    }

}
