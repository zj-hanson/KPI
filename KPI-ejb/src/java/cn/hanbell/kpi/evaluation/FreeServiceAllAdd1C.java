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
public class FreeServiceAllAdd1C extends FreeServiceAllAdd {

    public FreeServiceAllAdd1C() {
        super();
        queryParams.put("OuterFormid", "A-济南服务成本");
        queryParams.put("deptno", "1C000");
        queryParams.put("WithinFormid", "A-济南维修成本");
    }

}
