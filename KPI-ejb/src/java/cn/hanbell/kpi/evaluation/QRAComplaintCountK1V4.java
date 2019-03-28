/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * 离心机体客诉台数
 */
public class QRAComplaintCountK1V4 extends QRAComplaintCount {

    public QRAComplaintCountK1V4() {
        super();
        queryParams.put("BQ197", " ='KM' ");
        queryParams.put("BQ003"," in ('RTZ') ");
        queryParams.put("BQ505", " in ('YX')  ");
        queryParams.put("BQ110"," in ('Y') ");
    }
}
