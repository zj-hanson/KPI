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
public class ProductionQuantityVP extends ProductionQuantity {

    /**
     * P机体湿式正空
     */
    public ProductionQuantityVP() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*生产地
        queryParams.put("prono", "1");
        //*生产线别
        queryParams.put("linecode", " = 'VP' ");
        //制令种类
        queryParams.put("mankind", "");       
        //品号大类
        //#ITCLS CHANGE TODO #
        queryParams.put("itcls", "  IN ('3376','3379','3380','3476','3479','3480') ");
        //制令等级
        queryParams.put("typecode", "= '01' ");
    

    }
}
