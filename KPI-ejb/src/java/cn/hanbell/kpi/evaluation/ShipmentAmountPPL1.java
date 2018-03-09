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
 * @author C0160
 */
public class ShipmentAmountPPL1 extends ShipmentAmount {

    public ShipmentAmountPPL1() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("decode", "1");
        queryParams.put("deptno", " '1H000','1H100' ");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='P' ");
        queryParams.put("n_code_DC", " ='PL' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

    @Override
    public BigDecimal getARM423Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getARM270Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return BigDecimal.ZERO;
    }

}
