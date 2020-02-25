/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * 2020年2月13日RT新统计逻辑需要删除上海柯茂销售给上海汉钟RT部分排除 厂商KSH00004
 */
public class ShipmentAmountKRT9 extends ShipmentAmount9 {

    public ShipmentAmountKRT9() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " IN('RT') ");
    }
   
}
