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
public class ShipmentAmountR1B3 extends ShipmentAmount {

    public ShipmentAmountR1B3() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1B000','1B100' ");
        //queryParams.put("decode", "1");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", " ='HD' ");
        queryParams.put("n_code_DC", " ='L' ");
        queryParams.put("n_code_DD", " ='00' ");
    }
}
