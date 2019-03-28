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
public class QRACubicElementAmountPAH extends QRACubicElementAmount {

    public QRACubicElementAmountPAH() {
        super();
        queryParams.put("genre1", "AH");
        queryParams.put("SOURCEDPIP", "空压");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            Actual erp = (Actual) QRACubicElementAmountBadR.class.newInstance();
            BigDecimal badev = erp.getValue(y, m, d, type, erp.getQueryParams());
            BigDecimal allem = super.getValue(y, m, d, type, map);
            return badev.divide(allem).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception ex) {
            log4j.error("QRACubicElementAmountPAH", ex);
        }
        return BigDecimal.ZERO;
    }

}

class QRACubicElementAmountBadPAH extends QRACubicElementAmountBad {

    public QRACubicElementAmountBadPAH() {
        super();
        queryParams.put("SOURCEDPIP", "空压");
    }

}
