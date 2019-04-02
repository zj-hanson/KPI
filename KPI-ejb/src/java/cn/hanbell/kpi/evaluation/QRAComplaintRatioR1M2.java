/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749 6MIS客诉笔数
 */
public class QRAComplaintRatioR1M2 extends QRAComplaintCount2 {

    public QRAComplaintRatioR1M2() {
        super();
        queryParams.put("BQ197", "='R'");
        queryParams.put("BQ003", " in ('RLM') ");
        queryParams.put("BQ505", " in ('YX')  ");
        queryParams.put("mis", "6");
    }

}
