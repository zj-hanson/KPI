/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author C1749
 */
public class QRACubicElementAmountPP extends QRACubicElementAmount {

    public QRACubicElementAmountPP() {
        super();
        queryParams.put("genre1", "P");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            Actual erp = (Actual) QRACubicElementAmountBadR.class.newInstance();
            BigDecimal badev = erp.getValue(y, m, d, type, erp.getQueryParams());
            BigDecimal allem = super.getValue(y, m, d, type, map);
            return badev.divide(allem).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception ex) {
            log4j.error("QRACubicElementAmountPP", ex);
        }
        return BigDecimal.ZERO;
    }

}

class QRACubicElementAmountBadPP extends QRACubicElementAmountBad {

    public QRACubicElementAmountBadPP() {
        super();
        queryParams.put("SOURCEDPIP", "真空");
    }

}
