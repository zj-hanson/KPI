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
public class FreeServiceWithinKS5AB extends FreeServiceWithinKS{

    public FreeServiceWithinKS5AB() {
        super();
        queryParams.put("facno", "K");
//        queryParams.put("hmark1", "='HD' ");
//        queryParams.put("hmark2", " IN('RTZ','RZ','ZSNY') ");
    }   
}
