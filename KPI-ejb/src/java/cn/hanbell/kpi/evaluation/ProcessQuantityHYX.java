/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * 圆型
 */
public class ProcessQuantityHYX extends ProcessQuantity {

    public ProcessQuantityHYX() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*生产地
        queryParams.put("prono", "1");
        //* 回报制程式号
        queryParams.put("prosscode", "  = 'Y9' ");
        //回报工作单位
        queryParams.put("wrcode", " = 'M3' ");
        //状态码 1未确认 2 数量确认 3工时确认 4 作废
        queryParams.put("stats", " IN('2','3') ");

    }

}
