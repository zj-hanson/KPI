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
public class ComplaintsZLQualityR12Mis  extends ComplaintsZLQuality{

    public ComplaintsZLQualityR12Mis() {
        super();
        queryParams.put("BQ197", "%R%");
        queryParams.put("BQ003", " in ('RLM') ");
        queryParams.put("BQ134", " in ('YX')  ");
        queryParams.put("mis","12");
    }
    


}
