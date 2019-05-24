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
 * @author C1749 移动平均出货台数
 */
public class QRAShipmentAvg extends QRA {

    public QRAShipmentAvg() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        BigDecimal result = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  sum(quantity) as num from salestable WHERE 1=1 and  type='Shipment' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and n_code_DA = '").append(n_code_DA).append("' ");
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and n_code_DC = '").append(n_code_DC).append("' ");
        }
        sb.append(" and DATE_FORMAT(cdrdate,'%Y-%m-%d') BETWEEN ");
        sb.append(" date_add(curdate()-day(curdate())+1,interval -12 month) ");
        sb.append(" and ");
        sb.append(" last_day(date_sub(now(),interval 1 month)) ");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(d);
//        calendar.add(Calendar.YEAR, -1);
//        Date pastDay = calendar.getTime();
//        sb.append(" and cdrdate BETWEEN  '");
//        sb.append(BaseLib.formatDate("yyyyMMdd", pastDay));
//        sb.append("'  and  '");
//        calendar.clear();
//        calendar.add(Calendar.YEAR, 1);
//        calendar.add(Calendar.MONTH, -1);
//        calendar.add(Calendar.DAY_OF_MONTH, -1);
//        Date nowDay = calendar.getTime();
//        sb.append(BaseLib.formatDate("yyyyMMdd", d));
//        sb.append("'  ");
        String sql = sb.toString();
        Query query = superEJBForKPI.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = (BigDecimal) o1;
            if (result != null) {
                result = result.divide(BigDecimal.valueOf(12), 0, BigDecimal.ROUND_HALF_UP);
            }
            return result;
        } catch (Exception ex) {
            log4j.error("QRAComplaintActual2", ex);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public int getUpdateMonth(int y, int m) {
        return m;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        return y;
    }
}
