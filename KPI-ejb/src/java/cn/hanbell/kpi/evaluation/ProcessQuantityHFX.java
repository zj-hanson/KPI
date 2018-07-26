/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879 方型
 */
public class ProcessQuantityHFX extends ProcessQuantity {

    public ProcessQuantityHFX() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*生产地
        queryParams.put("prono", "1");
        //* 回报制程式号
        queryParams.put("prosscode", " IN('F2','F3')");
        //回报工作单位
        queryParams.put("wrcode", " = 'M1' ");
        //状态码 1未确认 2 数量确认 3工时确认 4 作废
        queryParams.put("stats", " IN('2','3') ");

    }

}
