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
public class FreeServiceAllAdd1D extends FreeServiceAllAdd {

    public FreeServiceAllAdd1D() {
        super();
        queryParams.put("OuterFormid", "A-广州服务成本");
        queryParams.put("deptno", "1D000");
        queryParams.put("WithinFormid", "A-广州维修成本");
    }

}
