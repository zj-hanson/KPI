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
public class QRACompCountK1V1 extends QRACompCount {
   
    public QRACompCountK1V1() {
        super();
        queryParams.put("BQ197", " ='KM' ");
        queryParams.put("BQ003"," in ('RTZ','WCZ') ");
        queryParams.put("BQ505", " in ('YX')  ");
        queryParams.put("BQ110"," in ('Y') ");
    }
}
