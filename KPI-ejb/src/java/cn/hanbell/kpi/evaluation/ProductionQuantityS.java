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
public class ProductionQuantityS extends ProductionQuantity {

    /**
     * 涡旋机体
     */
    public ProductionQuantityS() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*生产地
        queryParams.put("prono", "1");
        //*生产线别
        queryParams.put("linecode", " = 'SJ' ");
        //制令种类
        queryParams.put("mankind", " IN('MOS','WOS') ");
        //制令等级
        queryParams.put("typecode", "= '01' ");
        
    }
}
