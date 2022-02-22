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
public class ScrapAnalysisHanyoungCastingIn extends ScrapAnalysis{
    public ScrapAnalysisHanyoungCastingIn() {
        super();
        queryParams.put("facno", "Y");
        queryParams.put("DEFECTGROUPID", " in ('QJ01') ");
        queryParams.put("INOUTFACTORY", " = 'In' ");
        queryParams.put("CASTINGTYPE", " = 'LCCasting' ");
    }
}
