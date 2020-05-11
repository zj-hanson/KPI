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
public class FreeServiceAllAdd1E extends FreeServiceAllAdd {

    public FreeServiceAllAdd1E() {
        super();
        queryParams.put("OuterFormid", "A-南京服务成本");
        queryParams.put("deptno", "1E000");
        queryParams.put("WithinFormid", "A-南京维修成本");
    }

}
