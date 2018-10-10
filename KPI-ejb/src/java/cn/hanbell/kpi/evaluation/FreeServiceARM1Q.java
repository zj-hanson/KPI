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
public class FreeServiceARM1Q extends FreeServiceERP{

    public FreeServiceARM1Q() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='AA' ");
        queryParams.put("n_code_DD", "  in ('00','02') ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {        
       return super.getARM423Value(y, m, d, type, map);
    }
    
}
