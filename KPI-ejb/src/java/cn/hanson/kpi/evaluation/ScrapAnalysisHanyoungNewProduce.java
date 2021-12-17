/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author FredJie
 */
public class ScrapAnalysisHanyoungNewProduce extends ScrapAnalysis {
    
    public ScrapAnalysisHanyoungNewProduce() {
        super();
        queryParams.put("facno", "Y");
        queryParams.put("CASTINGTYPE", " = 'SMCasting' ");
    }
}
