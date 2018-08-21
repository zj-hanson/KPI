/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879 NL+CG耗用金额
 */
public class ConsumptionCutterYXo3 extends ConsumptionCutter {

    public ConsumptionCutterYXo3() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*出库码 2代表出库
        queryParams.put("iocode", "2");
        //* 单据类别
        queryParams.put("trtype", "IAB");
        //部门
        queryParams.put("depno", " IN('1P121' ,'1P122') ");
        //刀具型号
        queryParams.put("qualification", " ((t.itnbr like 'B101-06%' or t.itnbr like 'B101-07%' or t.itnbr like 'B101-08%' or  t.itnbr like 'B101-09%' or  t.itnbr like 'B108-01%' or  t.itnbr like 'B104-03%' or  t.itnbr like 'B109%'  or  t.itnbr like 'B110%') and (t.itnbr  not like 'B101-01%' and t.itnbr not like '52%')) ");
       
    }

}
