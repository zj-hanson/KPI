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
public class ProductionQuantityKWC extends ProductionQuantity {

    /**
     * 螺杆机组
     */
    public ProductionQuantityKWC() {
        super();
        //*公司别
        queryParams.put("facno", "K");
        //*生产地
        queryParams.put("prono", "1");
        //*生产线别
        queryParams.put("linecode", " = '01' ");
        //制令等级
        queryParams.put("typecode", "= '01' ");
        //
        queryParams.put("itcls", " IN ('3W76','3W79','3W80')");
        
        queryParams.put("itnbrf", " and itnbrf in (select itnbr from [comererp].[dbo].borgrp where itnbrgrp  not like 'KMAT%') ");

    }
}