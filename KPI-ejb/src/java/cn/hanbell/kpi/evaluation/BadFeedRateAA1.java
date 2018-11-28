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
public class BadFeedRateAA1 extends BadFeedRate{
    public BadFeedRateAA1(){
        super();
        queryParams.put("SYSTEMID", "'QC_JLBLReport'");
        queryParams.put("SEQUENCE", "'9'");
    }
}
