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
public class FreeServiceAllAddRT extends FreeServiceAllAdd{

    public FreeServiceAllAddRT() {
        super();
        queryParams.put("OuterFormid", "A-离心机体服务成本");
        queryParams.put("deptno", "1F000");
        queryParams.put("WithinFormid", "A-离心机体维修成本");
    }

}
