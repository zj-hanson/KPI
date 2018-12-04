/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * 柯茂空气源客诉接口
 */
public class QRAComplaintsQualityPenKM2 extends QRAComplaintsQualityPen {
    
    public QRAComplaintsQualityPenKM2() {
        super();
        queryParams.put("BQ197", " '=KM' ");
        queryParams.put("BQ003"," in ('WLZ') ");
        queryParams.put("BQ505", " in ('YX')  ");
        queryParams.put("BQ110"," in ('Y') ");
    }
}
