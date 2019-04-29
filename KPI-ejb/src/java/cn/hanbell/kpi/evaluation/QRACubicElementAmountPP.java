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
 * @author C1749 真空三次元合格率
 */
public class QRACubicElementAmountPP extends QRA {

    public QRACubicElementAmountPP() {
        super();

    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            BigDecimal result = BigDecimal.ZERO;
            //MES的加工不良数
            Actual crm = (Actual) QRACubicElementAmountBadPP1.class.newInstance();
            BigDecimal ev = crm.getValue(y, m, d, type, crm.getQueryParams());
            //ERP的加工入库总数
            Actual kpi = (Actual) QRACubicElementAmountP1.class.newInstance();
            BigDecimal ov = kpi.getValue(y, m, d, type, kpi.getQueryParams());
            //合格率
            if (ov != null && ov.compareTo(BigDecimal.ZERO) != 0) {
                result = BigDecimal.ONE.subtract(ev.divide(ov).setScale(4, BigDecimal.ROUND_HALF_UP)).multiply(BigDecimal.valueOf(100));
            }
            return result;
        } catch (Exception ex) {
            log4j.error("QRACubicElementAmountPP-getValue()！", ex);
        }
        return BigDecimal.ZERO;
    }

}

class QRACubicElementAmountBadPP1 extends QRACubicElementAmountBad {

    public QRACubicElementAmountBadPP1() {
        super();
        queryParams.put("SOURCEDPIP", "真空");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return super.getValue(y, m, d, type, map);
    }

}

class QRACubicElementAmountP1 extends QRACubicElementAmount {

    public QRACubicElementAmountP1() {
        super();
        queryParams.put("genre1", "P");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return super.getValue(y, m, d, type, map);
    }

}
