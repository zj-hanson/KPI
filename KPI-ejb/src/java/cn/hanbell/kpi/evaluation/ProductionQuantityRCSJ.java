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
public class ProductionQuantityRCSJ extends ProductionQuantity {

    /**
     * R冷媒+世纪东元
     */
    public ProductionQuantityRCSJ() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*生产地
        queryParams.put("prono", "1");
        //*生产线别
        queryParams.put("linecode", " = 'RC' ");
        //制令等级
        queryParams.put("typecode", "= '01' ");
        //2019年6月3日应陆夏玲要求增加品号大类3015（世纪东元部分制令）
        queryParams.put("itcls", " IN ('3015','3176','3177','3179','3180','3276','3279','3280','3083','4079')");

    }
}
