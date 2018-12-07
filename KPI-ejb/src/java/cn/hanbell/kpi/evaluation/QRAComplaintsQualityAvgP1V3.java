/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C0160
 */
public class QRAComplaintsQualityAvgP1V3 extends QRAComplaintsQuality {
    //真空总的年移动平均台数接口
    public QRAComplaintsQualityAvgP1V3() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='P' ");
        queryParams.put("n_code_DC", " ='PL' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
