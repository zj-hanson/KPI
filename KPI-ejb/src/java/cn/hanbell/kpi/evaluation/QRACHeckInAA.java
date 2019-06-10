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
public class QRACHeckInAA extends QRACHeckIn{
        public QRACHeckInAA(){
        super();
        queryParams.put("STEPID", "机组入库前检验站");
    }

}
