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
public class FreeServiceAllAdd1H extends FreeServiceAllAdd{

    public FreeServiceAllAdd1H() {
        super();
        queryParams.put("OuterFormid", "A-真空服务成本");
        queryParams.put("deptno", "1H000");
        queryParams.put("WithinFormid", "A-真空维修成本");
    }

}
