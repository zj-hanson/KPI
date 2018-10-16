/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * 柯茂水冷机组客诉接口
 */
public class ComplaintsQualityPenKM1 extends ComplaintsQualityPen {
   
    public ComplaintsQualityPenKM1() {
        super();
        queryParams.put("BQ197", "%KM%");
        queryParams.put("BQ003"," in ('RTZ','WCZ') ");
        queryParams.put("BQ134", " in ('YX')  ");
        queryParams.put("BQ110"," in ('Y') ");
    }
}
