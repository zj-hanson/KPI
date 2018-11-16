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
public class FreeServiceARM1C extends FreeServiceERP{

    public FreeServiceARM1C() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", " ='JN' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("hmark1", " ='R' ");
        queryParams.put("hmark2", " ='HB' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {        
       BigDecimal temp1, temp2;
        //SHB ERP
        temp1 = super.getARM423Value(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.remove("n_code_CD");
        queryParams.put("facno", "J");
        //JN ERP
        temp2 = super.getARM423Value(y, m, d, type, queryParams);
        //SHB + JN
        return temp1.add(temp2);
    }
    
}
