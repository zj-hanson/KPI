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
public class FreeServiceAllAdd1B extends FreeServiceAllAdd{

    public FreeServiceAllAdd1B() {
        super();
        queryParams.put("OuterFormid", "A-华东服务成本");
        queryParams.put("OuterDeptno", "1B000");
        queryParams.put("WithinFormid", "A-华东服务成本");
    }

}
