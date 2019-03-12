/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * 离心机体平均出货台数
 */
public class QRAComplaintsQualityKM4 extends QRAComplaintsQuality {

    public QRAComplaintsQualityKM4() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("n_code_DA", " ='RT' ");
        queryParams.put("n_code_DC", "in ('RT') ");
        queryParams.put("n_code_DD", " ='00' ");
    }
}
