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
public class QRACompRatioAH3M2  extends QRACompOrderShip{

    public QRACompRatioAH3M2() {
        super();
        queryParams.put("BQ197", "='AH'");
        queryParams.put("BQ003"," in ('AJT') ");
        queryParams.put("BQ505", " in ('YX','-1') ");
        queryParams.put("mis","12");
    }
    


}
