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
public class ProductionQuantityKWL extends ProductionQuantity {

    /**
     * 空气源热泵
     */
    public ProductionQuantityKWL() {
        super();
        //*公司别
        queryParams.put("facno", "K");
        //*生产地
        queryParams.put("prono", "1");
        //*生产线别
        queryParams.put("linecode", " = '01' ");
        //制令等级
        queryParams.put("typecode", "= '01' ");
        //品号大类
        queryParams.put("itcls", " IN ('3W76','3W79','3W80')");
        
        queryParams.put("itnbrf", " and itnbrf in (select itnbr from borgrp where (itnbrgrp  like 'KMAT%' OR itnbrgrp = 'DA01')) ");

    }
}