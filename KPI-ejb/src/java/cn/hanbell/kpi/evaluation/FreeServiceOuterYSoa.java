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
 * @author C1879
 */
public class FreeServiceOuterYSoa extends FreeServiceOuterOA {

    public FreeServiceOuterYSoa() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            //与FreeServiceOuterYSerp合计一起为运输费用
            //服务成本（厂外）退货通知单（吊装费、运费）+ 服务成本（厂外）OA工作支援单（快递费和运费）
            workSF = getWorkSF(y, m, d, type, map);
            returnSM = getReturnSM(y, m, d, type, map);
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.evaluation.FreeserveOuter.getValue()" + e);
        }
        return workSF.add(returnSM);
    }

}
