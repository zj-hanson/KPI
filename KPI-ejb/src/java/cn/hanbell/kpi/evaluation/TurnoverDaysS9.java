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
public class TurnoverDaysS9 extends TurnoverDays {

    public TurnoverDaysS9() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='S' ");
        queryParams.put("issevdta", " ='Y' ");

    }

}

