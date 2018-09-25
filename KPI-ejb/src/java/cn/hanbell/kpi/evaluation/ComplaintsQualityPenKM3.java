/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * 柯茂ORC客诉接口
 */
public class ComplaintsQualityPenKM3 extends ComplaintsQualityPen {

    public ComplaintsQualityPenKM3() {
        super();
        queryParams.put("BQ197", "%KM%");
        queryParams.put("BQ003"," in ('BAZ','ORC') ");
    }
}
