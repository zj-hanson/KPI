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
public class TurnoverDaysAJ9 extends TurnoverDays {

    public TurnoverDaysAJ9() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AH' ");
        queryParams.put("n_code_CD", " <> 'SDS' ");
        queryParams.put("issevdta", " ='Y' ");
    }

}

