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
public class FreeServiceWithin1G extends FreeServiceWithin {

    public FreeServiceWithin1G() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("hmark1", "='HD' ");
        queryParams.put("hmark2", " IN('AH','WY') ");
        queryParams.put("remark1", " IN('AH','SDS') ");
    }   

}
