/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.ejb.ppm;

import cn.hanbell.kpi.comm.SuperEJBForPPM;
import cn.hanson.kpi.entity.ppm.Quality;
import cn.hanson.kpi.entity.ppm.SafetyIncident;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author FredJie
 */
@Stateless
@LocalBean
public class SafetyIncidentBean extends SuperEJBForPPM<SafetyIncident> {
    public SafetyIncidentBean() {
        super(SafetyIncident.class);
    }
    public BigDecimal getNum(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String deptId = map.get("deptId") != null ? map.get("deptId").toString() : "";
        BigDecimal num = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(1) FROM safety_incident a where a.company = '${f}' and a.status='N' ");
        sb.append(" AND YEAR(a.incident_date)=${y} AND MONTH(a.incident_date)=${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND DAYOFMONTH(a.incident_date) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND DAYOFMONTH(a.incident_date) = ${d} ");
                break;
            default:
                sb.append(" AND DAYOFMONTH(a.incident_date) <= ${d} ");
        }
        if (deptId != "") {
            sb.append(" AND a.responsibility_dept_id ='${deptId}' ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}",
                BaseLib.formatDate("dd", d)).replace("${f}", facno).replace("${deptId}", deptId);
        Query query = getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            num = BigDecimal.valueOf(Double.valueOf(o1.toString()));
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return num;
    }
}