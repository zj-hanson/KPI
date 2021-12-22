/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

import java.util.LinkedHashMap;

/**
 *
 * @author FredJie
 */
public class MinorInjuryHansonQA extends MinorInjury{
    public MinorInjuryHansonQA() {
        queryParams = new LinkedHashMap<>();
        queryParams.put("facno", "H");
        queryParams.put("deptId", "2B700");
    }
}
