/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * 旋片泵机体的客诉笔数
 */
public class QRACompCountP1V2 extends QRACompCount {

    public QRACompCountP1V2() {
        super();
        queryParams.put("BQ197", " ='P' ");
        queryParams.put("BQ003", " in ('PZK') ");
        queryParams.put("BQ505", " in ('YX','-1') ");
        queryParams.put("BQ110", " in ('Y') ");
        queryParams.put("CA500", " 干式 ");
    }

}
