/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * 厂内服务维修课代替
 */
public class FreeServiceWithin1A extends FreeServiceWithin {

    public FreeServiceWithin1A() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("hmark1", "='HD' ");
        queryParams.put("hmark2", " ='R' ");
        queryParams.put("remark1", " ='R' ");
    }   

}
