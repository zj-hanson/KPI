/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879 钻头丝攻铣刀费用
 */
public class ConsumptionCutterFXo3 extends ConsumptionCutter {

    public ConsumptionCutterFXo3() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*出库码 2代表出库
        queryParams.put("iocode", "2");
        //* 单据类别
        queryParams.put("trtype", "IAB");
        //部门
        queryParams.put("depno", " IN ('1P100') ");
        //刀具型号
        queryParams.put("qualification", " (t.itnbr like 'BS102%' or t.itnbr like 'BS103%' or t.itnbr like 'BS104%' or t.itnbr like 'BS105%' ) ");
       
    }

}
