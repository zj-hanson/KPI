/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 */
public class QRACompCountAA extends QRACompCount {

    public QRACompCountAA() {
        super();
        queryParams.put("BQ197", " ='AA' ");
        queryParams.put("BQ003"," in ('AJZ') ");
        queryParams.put("BQ505", " in ('YX','-1') ");
        queryParams.put("BQ110"," in ('Y') ");
    }
}
