/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * 机体出货台数 AB-600R AB-780R
 */
public class TrialRunAdverseAHShipmentA1V2 extends TrialRunAdverseAHShipmentA1 {

    public TrialRunAdverseAHShipmentA1V2() {
        super();
        queryParams.put("facno", "'C'");
        queryParams.put("typecode", "600");
    }

}
