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
public class FundRecoveryRateRectotalAA extends FundRecoveryRateRectotal {

    public FundRecoveryRateRectotalAA() {
        super();
        queryParams.put("facno", "C,C4,N,G,J");
        queryParams.put("n_code_DA", " ='AA' ");
        queryParams.put("issevdta", " ='N' ");
    }

}

