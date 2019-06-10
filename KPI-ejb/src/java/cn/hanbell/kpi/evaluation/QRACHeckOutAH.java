/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 */
public class QRACHeckOutAH extends QRACHeckOut{
    public QRACHeckOutAH(){
        super();
        queryParams.put("STEPID", "机体出货检验站");
    }

}
