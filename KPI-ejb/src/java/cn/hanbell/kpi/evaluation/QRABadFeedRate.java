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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
public class QRABadFeedRate extends QRABadFeed {

    public QRABadFeedRate() {
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
            BigDecimal fenmujl = BigDecimal.valueOf(Double.valueOf(o1.get(0).toString()));//总进料数
            BigDecimal fenzibd = BigDecimal.valueOf(Double.valueOf(o1.get(1).toString()));//进料不良数
            result =fenzibd.divide(fenmujl, 6, BigDecimal.ROUND_HALF_UP);
        } catch (Exception ex) {
            Logger.getLogger(QRABadFeedRate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
