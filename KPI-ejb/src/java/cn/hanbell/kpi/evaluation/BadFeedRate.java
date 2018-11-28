/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
public class BadFeedRate extends BadFeed{

    public BadFeedRate() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String SYSTEMID = map.get("SYSTEMID") != null ? map.get("SYSTEMID").toString() : "";
        String SEQUENCE = map.get("SEQUENCE") != null ? map.get("SEQUENCE").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" select ");
        sb.append(" MONTH").append("${m}");
        sb.append(" FROM REPORTVALUE WHERE 1=1 ");
        if(!"".equals(SYSTEMID)){
            sb.append(" AND SYSTEMID = ").append(SYSTEMID);
        }
        if(!"".equals(SEQUENCE)){
            sb.append(" AND SEQUENCE = ").append(SEQUENCE);
        }
        sb.append(" AND year(REPORTYEAR) = ${y} ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query1 = superEJB.getEntityManager().createNativeQuery(sql);
        
        try {
            Object o1 = query1.getSingleResult();
            String s1 = o1.toString();
            Double value = Double.valueOf(s1.substring(0, s1.length()-1));
            result = BigDecimal.valueOf(value);
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result; 
    }
}
