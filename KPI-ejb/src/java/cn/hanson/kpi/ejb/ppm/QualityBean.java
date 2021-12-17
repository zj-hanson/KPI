/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.ejb.ppm;

import cn.hanbell.kpi.comm.SuperEJBForPPM;
import cn.hanson.kpi.entity.ppm.Quality;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author FredJie
 */
@Stateless
@LocalBean
public class QualityBean extends SuperEJBForPPM<Quality> {

    public QualityBean() {
        super(Quality.class);
    }
    
    public BigDecimal getFinishNum(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        BigDecimal finishNum = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(1) AS num FROM quality q WHERE q.status = 'V' AND   q.company = '${f}'");
        sb.append(" AND YEAR(q.endDate)=${y} AND MONTH(q.endDate)=${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND DAYOFMONTH(endDate) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND DAYOFMONTH(endDate) = ${d} ");
                break;
            default:
                sb.append(" AND DAYOFMONTH(endDate) <= ${d} ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}",
                BaseLib.formatDate("dd", d)).replace("${f}", facno);

        Query query = getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            finishNum = BigDecimal.valueOf(Double.valueOf(o1.toString()));
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return finishNum;
    }

    public BigDecimal getAllNum(LinkedHashMap<String, Object> map) {
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        BigDecimal allNum = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT count(1) AS num FROM quality q WHERE q.status = 'N' AND   q.company = '${f}' ");
        String sql = sb.toString().replace("${f}", facno);
        Query query = getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            allNum = BigDecimal.valueOf(Double.valueOf(o1.toString()));
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return allNum;
    }
}
