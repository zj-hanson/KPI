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
public class FeedstockBadQCAH extends FeedstockBadShipmentQC{

    public FeedstockBadQCAH() {
        super();
        queryParams.put("LINE", "'机体'");
        queryParams.put("SOURCEDPIP", "机体");
    }
}
