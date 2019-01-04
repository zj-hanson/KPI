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
public class FreeServiceAllAddSDS extends FreeServiceAllAdd{

    public FreeServiceAllAddSDS() {
        super();
        queryParams.put("OuterFormid", "A-无油机组服务成本");
        queryParams.put("deptno", "1G500");
        queryParams.put("WithinFormid", "A-无油机组维修成本");
    }

}
