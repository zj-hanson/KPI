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
public class FreeServiceARM1U extends FreeServiceERP {

    public FreeServiceARM1U() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", "='S' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("hmark1", " ='S' ");
        queryParams.put("hmark2", " ='HD' ");        
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return super.getARM423Value(y, m, d, type, map);
    }

}
