/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 * 机体出货台数 AB-1030R、AB-1200R
 */
public class TrialRunAdverseAHShipmentA1V3 extends TrialRunAdverseAHShipmentA1 {

    public TrialRunAdverseAHShipmentA1V3() {
        super();
        queryParams.put("facno", "'C'");
        queryParams.put("typecode", "1030");
    }

}
