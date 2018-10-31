/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;
/**
 *
 * @author C1879
 */
public class FeedstockBadQZR extends FeedstockBadShipmentQZ{

    public FeedstockBadQZR() {
        super();
        queryParams.put("LINE", "'冷媒'");
        queryParams.put("SOURCEDPIP", "冷媒");
    }

}
