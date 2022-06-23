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
public class ShipmentQuantityKHG1 extends ShipmentQuantityZJComer {

    public ShipmentQuantityKHG1() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("deptno", " '5B000' ");
        queryParams.put("ogdkid", "RL01");
        //上海柯茂的出货中客户名称不能是浙江柯茂
        queryParams.put("cusno", " NOT IN ('KZJ00029')");
        queryParams.put("n_code_DA", " ='OH' ");
        queryParams.put("n_code_DC", " ='HG' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal temp1, temp2;
        //ComerERP
        temp1 = super.getValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.remove("deptno");
        queryParams.remove("cusno");
        queryParams.put("facno", "E");
        queryParams.put("deptno", " '8A000' ");
        //浙江柯茂中客户名称不能是上海柯茂
        queryParams.put("cusno", " NOT IN ('ESH00031')");
        //ZJComerERP
        temp2 = super.getValue(y, m, d, type, queryParams);
        //ComerERP + ZJComerERP
        return temp1.add(temp2);
    }

}
