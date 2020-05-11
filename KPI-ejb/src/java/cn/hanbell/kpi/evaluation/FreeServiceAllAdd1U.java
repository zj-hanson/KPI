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
public class FreeServiceAllAdd1U extends FreeServiceAllAdd {

    public FreeServiceAllAdd1U() {
        super();
        queryParams.put("OuterFormid", "A-涡旋服务成本");
        queryParams.put("deptno", "1U000");
        queryParams.put("WithinFormid", "A-涡旋维修成本");
    }

}
