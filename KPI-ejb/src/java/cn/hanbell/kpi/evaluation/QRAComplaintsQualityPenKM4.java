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
public class QRAComplaintsQualityPenKM4 extends QRAComplaintsQualityPen {

    public QRAComplaintsQualityPenKM4() {
        super();
        queryParams.put("BQ197", " ='KM' ");
        queryParams.put("BQ003"," in ('RTZ') ");
        queryParams.put("BQ505", " in ('YX')  ");
        queryParams.put("BQ110"," in ('Y') ");
    }
}
