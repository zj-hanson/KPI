/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 */
public class ShipmentAmountAAQT extends ShipmentAmountAA {

    public ShipmentAmountAAQT() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1Q000','1Q100' ");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='AA' ");
        queryParams.put("n_code_CD", " NOT LIKE 'WX%' ");
        queryParams.put("n_code_DC", " <> 'SDS' ");
        queryParams.put("n_code_DD", "  in ('00','02') ");
        queryParams.put("modelcode", "QT");//其他（没有客户品号的）
    }

}
