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
public class ProcessRatioFXO1 extends ProcessRatio {

    //方型件不良数
    public ProcessRatioFXO1() {
        super();
        //*责任单位
        queryParams.put("RESPONSIBILITYDP", " IN ('方型加工课') ");
        //*检验判断结果
        queryParams.put("ANALYSISJUDGEMENTRESULT", " IN ('特采','自行重工') ");

    }
}