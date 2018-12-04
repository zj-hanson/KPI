/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * 总客诉笔数
 */
public class QRAComplaintsQualityPenP1V1 extends QRAComplaintsQualityPen {

    public QRAComplaintsQualityPenP1V1() {
        super();
        queryParams.put("BQ197", " '=P' ");
        queryParams.put("BQ003", " in ('PZK') ");
        queryParams.put("BQ505", " in ('YX','-1') ");
        queryParams.put("BQ110", " in ('Y') ");
    }

}
