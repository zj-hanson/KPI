/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author C1749 品保进料 上线
 */
public class QRAFeedLine extends QRAAConnMES {

    public QRAFeedLine() {
        super();
    }

    @Override 
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String SYSTEMID = map.get("SYSTEMID") != null ? map.get("SYSTEMID").toString() : "";
        String SEQUENCE = map.get("SEQUENCE") != null ? map.get("SEQUENCE").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        Double value = 0.0;
        sb.append(" select ");
        sb.append(" MONTH").append("${m}");
        sb.append(" FROM REPORTVALUE WHERE 1=1 ");
        if (!"".equals(SYSTEMID)) {
            sb.append(" AND SYSTEMID = ").append(SYSTEMID);
        }
        if (!"".equals(SEQUENCE)) {
            sb.append(" AND SEQUENCE ").append(SEQUENCE);
        }
        sb.append(" AND year(REPORTYEAR) = ${y} ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query1 = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            List o1 = query1.getResultList();
            BigDecimal fenmujl = BigDecimal.valueOf(Double.valueOf(o1.get(0).toString()));//分母
            BigDecimal fenzibd = BigDecimal.valueOf(Double.valueOf(o1.get(1).toString()));//分子
            result =BigDecimal.ONE.subtract(fenzibd.divide(fenmujl, 3, BigDecimal.ROUND_HALF_UP));
        } catch (Exception ex) {
            log4j.error("QRABadFeedRate", ex);
        }
        return result;
    }
}
