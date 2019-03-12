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
 * @author C1749 旧版的试车数据
 */
public class QRATrialRunPercentOfPass extends QRAConnMES {

    public QRATrialRunPercentOfPass() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String STEPID = map.get("STEPID") != null ? map.get("STEPID").toString() : "";//试车站别
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal divisor1 = BigDecimal.ZERO;
        BigDecimal divisor2 = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        //试车不合格数
        sb.append(" select sum(NUM) from ( ");
        sb.append(" SELECT  B.PRODUCTMODEL, COUNT(*) NUM FROM  PROCESS_TR A INNER JOIN MPRODUCT B ON A.PRODUCTID=B.PRODUCTID WHERE 1=1 ");
        sb.append(" and A.PRODUCTID not like '%GB%' and A.TR_TIMES='1' ");
        sb.append(" AND A.TRRESULT='不合格' ");
        if (!"".equals(STEPID)) {
            sb.append(" AND A.STEPID LIKE '%").append(STEPID).append("%'");
        }
        sb.append(" AND year(A.MODIFYTIME) = ${y} AND month(A.MODIFYTIME)= ${m} ");
        sb.append(" )as a ");
        String sqlDivisor1 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        sb.setLength(0);
        //全部试车数
        sb.append(" select sum(NUM) from ( ");
        sb.append(" SELECT  B.PRODUCTMODEL, COUNT(*) NUM FROM ");
        if (STEPID.contains("机组")) {
            sb.append(" PROCESS_TR_JZ A ");
        } else {
            sb.append(" PROCESS_TR A");
        }
        sb.append(" INNER JOIN MPRODUCT B ON A.PRODUCTID=B.PRODUCTID WHERE 1=1 ");
        sb.append(" and A.PRODUCTID not like '%GB%' and A.TR_TIMES='1' ");
        //sb.append(" AND A.TRRESULT='不合格' ");
        if (!"".equals(STEPID)) {
            sb.append(" AND A.STEPID LIKE '%").append(STEPID).append("%'");
        }
        sb.append(" AND year(A.MODIFYTIME) = ${y} AND month(A.MODIFYTIME)= ${m} ");
        sb.append(" )as a ");
        String sqlDivisor2 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query1 = superEJB.getEntityManager().createNativeQuery(sqlDivisor1);
        Query query2 = superEJB.getEntityManager().createNativeQuery(sqlDivisor2);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            divisor1 = (BigDecimal) o1;
            divisor2 = (BigDecimal) o2;
            if (divisor2.compareTo(BigDecimal.ZERO) == 1) {
                return divisor1.divide(divisor2, 3, BigDecimal.ROUND_HALF_UP);
            }
        } catch (Exception ex) {
            Logger.getLogger(QRATrialRunPercentOfPass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

}
