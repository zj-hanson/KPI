/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * 2019年7月9日生管提出更改完工逻辑
 */
public class ProductionPlanCompleteKHC extends ProductionPlanComplete {

    public ProductionPlanCompleteKHC() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("prono", "1");
        queryParams.put("stats", "2");
        queryParams.put("linecode", " IN('AT','ORC','RT') ");
        queryParams.put("typecode", " ='01' ");
        queryParams.put("prosscode", " in ('LX08') ");
        queryParams.put("wrcode", "  ='ZP01' ");
        queryParams.put("itcls", " IN('3H76','3H79','3H80')  ");

    }
}
