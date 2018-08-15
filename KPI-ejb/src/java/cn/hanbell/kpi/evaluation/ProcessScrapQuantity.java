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
public class ProcessScrapQuantity extends ProcessRatio {

    //报废数
    public ProcessScrapQuantity() {
        super();
        //*责任单位
        queryParams.put("RESPONSIBILITYDP", " IN('圆型加工课','方型加工课') ");
        //*检验判断结果
        queryParams.put("ANALYSISJUDGEMENTRESULT", " LIKE '%报废%' ");
        queryParams.put("stats", "Scrap");
    }
}
