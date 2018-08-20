/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 */
public class ProductivitySumMinuteKAPP extends ProductivitySumMinute {

    public ProductivitySumMinuteKAPP() {
        super();
        //加工所运行设备数量
        queryParams.put("equipmentnumber", "7");
    }
}
