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
public class FeedstockBadQJAA extends FeedstockBadShipmentQJ{

    public FeedstockBadQJAA() {
        super();
        queryParams.put("LINE", "'机组'");
        queryParams.put("SOURCEDPIP", "机组");
    }
}
