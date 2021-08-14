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
 * @author C1879 客服单结案单
 */
public abstract class ServiceCustomer extends Service {

    public ServiceCustomer() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String status = map.get("status") != null ? map.get("status").toString() : "";
        BigDecimal closing = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select ISNULL(count(distinct BQ001),0) from SERBQ where 1=1 ");
        //JA 结案
        if ("JA".equals(status)) {
            sb.append(" AND BQ035='Y' ");
        }
        sb.append(" and BQ017 ").append(deptno);
        sb.append(" and year(BQ021) =${y} ");
        if ("JA".equals(status)) {
            if (m >= 1 && m <= 3) {
                sb.append(" AND  month(BQ021) BETWEEN 1 and 3 ");
            } else if (m >= 4 && m <= 6) {
                sb.append(" AND  month(BQ021) BETWEEN 4 and 6 ");
            } else if (m >= 7 && m <= 9) {
                sb.append(" AND  month(BQ021) BETWEEN 7 and 9 ");
            } else if (m >= 10 && m <= 12) {
                sb.append(" AND  month(BQ021) BETWEEN 10 and 12 ");
            }
        } else {
            sb.append(" AND  month(BQ021) =${m} ");
        }

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));;

        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            closing = new BigDecimal(o1.toString());
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return closing;
    }

    public BigDecimal getQuarterValue(int y, int m, LinkedHashMap<String, Object> map) {
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String status = map.get("status") != null ? map.get("status").toString() : "";
        BigDecimal closing = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select ISNULL(count(distinct BQ001),0) from SERBQ where 1=1 AND BQ035='Y' ");
        sb.append(" and BQ017 ").append(deptno);
        sb.append("and year(BQ021) =${y} ");
        if ("nh1".equals(status)) {
            sb.append(" AND  month(BQ021) BETWEEN 1 and 6 ");
        }
        if ("nh2".equals(status)) {
            sb.append(" AND  month(BQ021) BETWEEN 7 and 12 ");
        }
        if ("nfy".equals(status)) {
            sb.append(" AND  month(BQ021) BETWEEN 1 and 12 ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y));

        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            closing = new BigDecimal(o1.toString());
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return closing;
    }
}
