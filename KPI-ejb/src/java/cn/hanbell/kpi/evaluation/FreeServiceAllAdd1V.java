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
public class FreeServiceAllAdd1V extends FreeServiceAllAdd {

    public FreeServiceAllAdd1V() {
        super();
        queryParams.put("OuterFormid", "A-重庆服务成本");
        queryParams.put("deptno", "1V000");
        queryParams.put("WithinFormid", "A-重庆维修成本");
    }

}
