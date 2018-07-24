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
public class ManmasQuantityVP extends ManmasQuantity {

    /**
     * P机体湿式正空
     */
    public ManmasQuantityVP() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*生产地
        queryParams.put("prono", "1");
        //*生产线别
        queryParams.put("linecode", " = 'VP' ");
        //制令种类
        queryParams.put("mankind", "");
        //制令等级
        queryParams.put("typecode", "= '01' ");
    

    }
}
