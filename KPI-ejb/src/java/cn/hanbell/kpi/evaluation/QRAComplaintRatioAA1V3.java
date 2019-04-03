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
 * @author C1749 A机组12MIS的客诉比率
 */
public class QRAComplaintRatioAA1V3 extends QRAAConnERP {

    public QRAComplaintRatioAA1V3() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            BigDecimal result = BigDecimal.ZERO;
            //CRM的制造号码在3MIS里的笔数
            Actual crm = (Actual) QRAComplaintOrderAA3.class.newInstance();
            BigDecimal ev = crm.getValue(y, m, d, type, crm.getQueryParams());
            //KPI的移动平均出货台数
            Actual kpi = (Actual) QRAShipmentAvgAllAA3.class.newInstance();
            BigDecimal ov = kpi.getValue(y, m, d, type, kpi.getQueryParams());
            //客诉率
            if (ov != null && ov.compareTo(BigDecimal.ZERO) != 0) {
                result = ev.divide(ov, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
            }
            return result;
        } catch (Exception ex) {
            log4j.error("QRAComplaintRatioAA1V3-getValue()！", ex);
        }
        return BigDecimal.ZERO;
    }

}

class QRAComplaintOrderAA3 extends QRAComplaintOrder {

    public QRAComplaintOrderAA3() {
        super();
        queryParams.put("BQ197", " ='AA' ");
        queryParams.put("BQ003", " in ('AJZ') ");
        queryParams.put("BQ505", " in ('YX','-1') ");
        queryParams.put("BQ110", " in ('Y') ");
        queryParams.put("mis", "12");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return super.getValue(y, m, d, type, map);
    }

}

class QRAShipmentAvgAllAA3 extends QRAShipmentAvgAll {

    public QRAShipmentAvgAllAA3() {
        super();
        queryParams.put("n_code_DA", "AA");
        queryParams.put("mis", "12");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return super.getValue(y, m, d, type, map);
    }

}
