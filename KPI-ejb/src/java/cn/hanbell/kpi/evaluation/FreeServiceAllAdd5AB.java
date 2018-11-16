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
public class FreeServiceAllAdd5AB extends FreeServiceAllAdd{

    public FreeServiceAllAdd5AB() {
        super();
        queryParams.put("OuterFormid", "A-柯茂服务成本");
        queryParams.put("deptno", "50000");
        queryParams.put("WithinFormid", "A-柯茂维修成本");
    }

}
