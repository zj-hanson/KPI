/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author C1749
 */
public class QRACubicElementAmountPR extends QRACubicElementAmount {

    public QRACubicElementAmountPR() {
        super();
        queryParams.put("genre1", "R");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            Actual erp = (Actual) QRACubicElementAmountBadR.class.newInstance();
            BigDecimal badev = erp.getValue(y, m, d, type, erp.getQueryParams());
            BigDecimal allem = super.getValue(y, m, d, type, map);
            return badev.divide(allem).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception ex) {
            log4j.error("QRACubicElementAmountPR", ex);
        }
        return BigDecimal.ZERO;
    }

}

class QRACubicElementAmountBadR extends QRACubicElementAmountBad {

    public QRACubicElementAmountBadR() {
        super();
        queryParams.put("SOURCEDPIP", "冷媒");
    }

}
