/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749 3MIS客诉笔数
 */
public class QRAComplaintRatioAA1M1 extends QRAComplaintCount2 {

    public QRAComplaintRatioAA1M1() {
        super();
        queryParams.put("BQ197", "='AA' ");
        queryParams.put("BQ003", " in ('AJZ') ");
        queryParams.put("BQ505", " in ('YX','-1') ");
        queryParams.put("mis", "3");
    }

}
