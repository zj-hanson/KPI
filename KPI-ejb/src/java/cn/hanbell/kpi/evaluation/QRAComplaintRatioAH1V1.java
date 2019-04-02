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
 * @author C1749 A机体3MIS的客诉比率
 */
public class QRAComplaintRatioAH1V1 extends QRAAConnERP {

    public QRAComplaintRatioAH1V1() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            BigDecimal result = BigDecimal.ZERO;
            //CRM的制造号码在3MIS里的笔数
            Actual crm = (Actual) QRAComplaintCount2AH1V1.class.newInstance();
            BigDecimal ev = crm.getValue(y, m, d, type, crm.getQueryParams());
            //KPI的移动平均出货台数
            Actual kpi = (Actual) QRAComplaintAvgAH1V1.class.newInstance();
            BigDecimal ov = kpi.getValue(y, m, d, type, kpi.getQueryParams());
            //客诉率
            if (ov != null && ov.compareTo(BigDecimal.ZERO) != 0) {
                result = ev.divide(ov, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
            }
            return result;
        } catch (Exception ex) {
            log4j.error("QRAComplaintRatioAH1V1-getValue()！", ex);
        }
        return BigDecimal.ZERO;
    }

}

class QRAComplaintCount2AH1V1 extends QRAComplaintCount2 {

    public QRAComplaintCount2AH1V1() {
        super();
        queryParams.put("BQ197", " ='AH' ");
        queryParams.put("BQ003", " in ('AJT') ");
        queryParams.put("BQ505", " in ('YX','-1') ");
        queryParams.put("BQ110", " in ('Y') ");
        queryParams.put("mis", "3");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return super.getValue(y, m, d, type, map);
    }

}

class QRAComplaintAvgAH1V1 extends QRAComplaintAvg {

    public QRAComplaintAvgAH1V1() {
        super();
        queryParams.put("n_code_DA", "AH");
        queryParams.put("mis", "3");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return super.getValue(y, m, d, type, map);
    }

}
