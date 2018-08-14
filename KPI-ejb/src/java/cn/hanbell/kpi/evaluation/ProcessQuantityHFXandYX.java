/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879 
 * 方型圆型总加工数
 */
public class ProcessQuantityHFXandYX extends ProcessQuantity {

    public ProcessQuantityHFXandYX() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*生产地
        queryParams.put("prono", "1");
        //* 生产线别
        queryParams.put("linecode", " IN('FX','YX') ");
        //状态码 1未确认 2 确认 3作废
        queryParams.put("stats", " IN('2') ");

    }

}
