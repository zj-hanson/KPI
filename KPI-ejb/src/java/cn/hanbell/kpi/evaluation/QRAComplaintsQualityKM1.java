/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * 柯茂水冷机组移动平均出货台数
 */
public class QRAComplaintsQualityKM1 extends QRAComplaintsQuality {

    public QRAComplaintsQualityKM1() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " in ('RT','OH') ");
        queryParams.put("n_code_DD", " ='00' ");
    }
}
