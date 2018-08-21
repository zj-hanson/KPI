/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879 NSM耗用金额
 */
public class ConsumptionCutterYXo2 extends ConsumptionCutter {

    public ConsumptionCutterYXo2() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*出库码 2代表出库
        queryParams.put("iocode", "2");
        //* 单据类别
        queryParams.put("trtype", "IAB");
        //部门
        queryParams.put("depno", " in('1P121' ,'1P122') ");
        //刀具型号
        queryParams.put("qualification", " ( t.itnbr like 'B101-01%' ) ");
       
    }

}
