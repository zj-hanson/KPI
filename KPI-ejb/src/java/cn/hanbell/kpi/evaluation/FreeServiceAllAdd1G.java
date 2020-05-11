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
public class FreeServiceAllAdd1G extends FreeServiceAllAdd {

    public FreeServiceAllAdd1G() {
        super();
        queryParams.put("OuterFormid", "A-空压机体服务成本");
        queryParams.put("deptno", "1G100");
        queryParams.put("WithinFormid", "A-空压机体维修成本");
    }

}
