/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author C1879 维修成本（厂内)
 */
public abstract class FreeServiceWithin extends FreeServiceERP {

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {         
            inv310ks = this.getINV310KS(y, m, d, type, map);
            man410and510 = this.getMAN410and510(y, m, d, type, map);
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.evaluation.FreeserveOuter.getValue()" + e);
        }
        return inv310ks.add(man410and510);
    }

}
