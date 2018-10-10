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
 * @author C1879 免费服务金额厂外
 */
public abstract class FreeServiceOuterERP extends FreeServiceERP {

    // 服务成本（厂外）借出单运费
    protected BigDecimal cdrN20 = BigDecimal.ZERO;
    //服务领退料厂外
    protected BigDecimal inv310outer = BigDecimal.ZERO;

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            //每月3-5号自动更新上个月的数据
            int year, month;
            if (m == 1) {
                year = y - 1;
                month = 12;
            } else {
                year = y;
                month = m;
            }
            String facno = map.get("facno") != null ? map.get("facno").toString() : "";
            if("C".equals(facno) || "K".equals(facno)){
                cdrN20 = this.getCDRN20Value(y, m, d, type, map);
                inv310outer = this.getINV310outer(y, m, d, type, map);
            }else{
                inv310outer = this.getINV310outer(y, m, d, type, map);
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.evaluation.FreeserveOuter.getValue()" + e);
        }
        return cdrN20.add(inv310outer);
    }

}
