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
public class FreeServiceARM1V extends FreeServiceERP {

    public FreeServiceARM1V() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", " ='CQ' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("hmark1", " ='R' ");
        queryParams.put("hmark2", " ='CQ' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal temp1, temp2;
        //SHB ERP
        temp1 = super.getARM423Value(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.remove("n_code_CD");
        queryParams.put("facno", "C4");
        //CQ ERP
        temp2 = super.getARM423Value(y, m, d, type, queryParams);
        //SHB + CQ
        return temp1.add(temp2);
    }

}
