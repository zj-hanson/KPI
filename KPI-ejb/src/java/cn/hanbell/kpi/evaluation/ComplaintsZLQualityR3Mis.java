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
public class ComplaintsZLQualityR3Mis  extends ComplaintsZLQuality{

    public ComplaintsZLQualityR3Mis() {
        super();
        queryParams.put("BQ197", 'R');
        queryParams.put("BQ130", 'N');
        queryParams.put("MI017", 'N');
        queryParams.put("BQ035", 'Y');
        queryParams.put("BQ110", 'Y');
        queryParams.put("mis", "3");
    }
    


}
