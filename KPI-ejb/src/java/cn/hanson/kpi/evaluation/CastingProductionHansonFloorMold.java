/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */

package cn.hanson.kpi.evaluation;

/**
 *
 * @author C0160
 */
public class CastingProductionHansonFloorMold extends CastingProduction {

    public CastingProductionHansonFloorMold() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("line", " LIKE '%地模%' ");
        queryParams.put("step", " = '造型' ");
    }

}
