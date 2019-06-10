/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
public class QRACHeckOut extends QRA {

    public QRACHeckOut() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal result = BigDecimal.ZERO;
        String STEPID = map.get("STEPID") != null ? map.get("STEPID").toString() : ""; //检验站
        StringBuilder sb = new StringBuilder();
        //不良数
        sb.append(" SELECT count(1) FROM  PROCESS_OQCSTOCKANALYSIS ");
        sb.append(" where STEPID like'").append(STEPID).append("%'");
        sb.append(" and DESCRIPTION <>'' ");
        sb.append(" and year(MODIFYTIME)=${y} and month(MODIFYTIME)=${m} ");
        String sqlBad = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        sb.setLength(0);
        //总数
        sb.append(" SELECT count(1) FROM  PROCESS_OQCSTOCKANALYSIS ");
        sb.append(" where STEPID like '").append(STEPID).append("%'");
        sb.append(" and year(MODIFYTIME)=${y} and month(MODIFYTIME)=${m} ");
        String sqlAll = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query1 = superEJBForMES.getEntityManager().createNativeQuery(sqlBad);
        Query query2 = superEJBForMES.getEntityManager().createNativeQuery(sqlAll);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            BigDecimal badNum = BigDecimal.valueOf(Double.valueOf(o1.toString()));
            BigDecimal AllNum = BigDecimal.valueOf(Double.valueOf(o2.toString()));
            if (AllNum.compareTo(BigDecimal.ZERO) == 1) {
                result = (AllNum.subtract(badNum)).divide(AllNum, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
            }
            return result;
        } catch (Exception ex) {
            log4j.error("QRACHeckOut Exception", ex);
        }
        return BigDecimal.ZERO;
    }

}
