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
public class ScrapAnalysisHanyoungProcessIn extends ScrapAnalysis {

    public ScrapAnalysisHanyoungProcessIn() {
        super();
        queryParams.put("facno", "Y");
        queryParams.put("DEFECTGROUPID", " in ('QJ02','QJ03')  ");
        queryParams.put("INOUTFACTORY", " = 'In' ");
        queryParams.put("CASTINGTYPE", " = 'LCCasting' ");
    }

}
