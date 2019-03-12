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
public class QRAHansonRotor extends QRAConnMES {

    public QRAHansonRotor() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" SELECT MONTH5,MONTH6  from REPORTVALUE WHERE SYSTEMID='HSLMBlReport' and SYSTEMNAME like '%转子%'  ");
        sb.append(" and convert(VARCHAR(7),REPORTYEAR,112) = ");
        sb.append(" '").append("${y}").append("/");
        if (m < 10) {
            sb.append("0").append("${m}").append("'");
        } else {
            sb.append("${m}").append("'");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query1 = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            List o1 = query1.getResultList();
            for (int i = 0; i < o1.size(); i++) {
                Object[] row = (Object[]) o1.get(i);
                Double value1 = Double.valueOf(row[0].toString());
                Double value2 = Double.valueOf(row[1].toString());
                result = BigDecimal.ONE.subtract(BigDecimal.valueOf(value1 / value2).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        } catch (Exception ex) {
            Logger.getLogger(QRAHansonRotor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
