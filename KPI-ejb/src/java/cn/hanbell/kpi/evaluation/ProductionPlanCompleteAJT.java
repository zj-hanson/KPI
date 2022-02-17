/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author C1879
 */
public class ProductionPlanCompleteAJT extends ProductionPlanComplete {

    public ProductionPlanCompleteAJT() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("stats", "2");
        queryParams.put("linecode", " in('AH','AH2') ");
        queryParams.put("typecode", " ='01' ");
        queryParams.put("prosscode", " in ('S1','S1-1','FAA09','FAA08') ");
        queryParams.put("wrcode", "  in ('S4','S8') ");
        //#ITCLS CHANGE TODO #
        queryParams.put("itcls", " NOT IN ('3476','3479','3480') ");
    }
}
