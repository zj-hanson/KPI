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
public class FreeServiceWithin1U extends FreeServiceWithin {

    public FreeServiceWithin1U() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("hmark1", "='HD' ");
        queryParams.put("hmark2", " ='S' ");
        queryParams.put("remark1", " ='S' ");
    }   

}