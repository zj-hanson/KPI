/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * 2019年7月9日生管提出更改完工逻辑
 */
public class ProductionPlanCompleteZJKM extends ProductionPlanComplete {

    public ProductionPlanCompleteZJKM() {
        super();
        queryParams.put("facno", "E");
        queryParams.put("prono", "1");
        queryParams.put("stats", "2");
        queryParams.put("linecode", " IN('AT','ORC','RT','A8','A9') ");
        queryParams.put("typecode", " ='01' ");
        queryParams.put("prosscode", " IN ('OR08','KQ10','LG08') ");
        //queryParams.put("wrcode", " IN ('ZP01','ZP02','ZP03','WL2','ZS1') ");

    }
}
