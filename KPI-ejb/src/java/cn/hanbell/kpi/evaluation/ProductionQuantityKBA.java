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
public class ProductionQuantityKBA extends ProductionQuantity {

    /**
     * ORC机组
     */
    public ProductionQuantityKBA() {
        super();
        //*公司别
        queryParams.put("facno", "K");
        //*生产地
        queryParams.put("prono", "1");
        //*生产线别
        //陆夏玲2019年6月19日提出更改生产线别01 IN('AT','ORC','RT')
        queryParams.put("linecode", " IN('AT','ORC','RT') ");
        //制令等级
        queryParams.put("typecode", "= '01' ");
        //
        queryParams.put("itcls", " IN ('3B80')");

    }
}