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
 * @author C1879 服务单结案单
 */
public abstract class ServiceClosing extends Service {

    public ServiceClosing() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String status = map.get("status") != null ? map.get("status").toString() : "";
        BigDecimal closing = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();

        //JA 结案 LJ 累计concat
        if ("JA".equals(status)) {
            sb.append(" select isnull(count(distinct a.TC001),0) from ( Select  TC004, (TC001+TC002) as TC001 from REPTC ");
            sb.append(" LEFT JOIN PORMZ LEFT JOIN PORMY on PORMZ.MZ001 = PORMY.MY001 and PORMZ.MZ002 = PORMY.MY002 on REPTC.TC001 = PORMZ.MZ005 and REPTC.TC002 = PORMZ.MZ006 ");
            sb.append(" where TC001 like '%FW%'  AND TC015<>'' and TC018='3' AND TC034='Y' ");
            sb.append(" and TC017 like '").append(deptno).append("%'");
            sb.append(" and year(TC004) =${y} AND  month(TC004)=${m} ");
        } else if ("LJ".equals(status)) {
            sb.append(" select isnull(count(distinct a.TC001),0) from ( Select  TC004, (TC001+TC002) as TC001 from REPTC ");
            sb.append(" LEFT JOIN PORMZ LEFT JOIN PORMY on PORMZ.MZ001 = PORMY.MY001 and PORMZ.MZ002 = PORMY.MY002 on REPTC.TC001 = PORMZ.MZ005 and REPTC.TC002 = PORMZ.MZ006 ");
            sb.append(" where TC001 like '%FW%'  AND TC015<>'' and TC018='3' AND TC034='Y' ");
            sb.append(" and TC017 like '").append(deptno).append("%'");
            sb.append(" and year(TC004 ) =${y} ");
            sb.append(" AND  month(TC004) BETWEEN  1 and ${m} ");
        } else {
            sb.append(" Select isnull(count(distinct a.TC001),0) from (  Select (TC001+TC002) as TC001,TC003 from REPTC where TC001 like '%FW%' ");
            sb.append(" and TC017 like '").append(deptno).append("%'");
            sb.append(" and year(TC003) =${y} AND  month(TC003)=${m} ");
        }
        sb.append(" )as a ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));

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
