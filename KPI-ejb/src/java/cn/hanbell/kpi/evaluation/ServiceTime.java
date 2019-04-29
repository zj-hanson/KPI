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
 * @author C1879 服务时间统计
 */
public abstract class ServiceTime extends Service {

    public ServiceTime() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String status = map.get("status") != null ? map.get("status").toString() : "";
        BigDecimal time = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();

        //LC路程时间
        if ("LC".equals(status)) {
            sb.append(" select isnull( Sum(Case when isnumeric(datediff(HH,convert(VARCHAR(20),(CASE WHEN substring(a.day1,1,2) <> '00' then a.day1 else convert(VARCHAR(20),getdate(),112) end),112), ");
            sb.append(" convert(VARCHAR(20),(CASE WHEN substring(a.day2,1,2) <> '00' then a.day2 else convert(VARCHAR(20),getdate(),112) end),112)) )=0 ");
            sb.append(" then 0 Else Cast(datediff(HH,convert(VARCHAR(20),(CASE WHEN substring(a.day1,1,2) <> '00' then a.day1 else convert(VARCHAR(20),getdate(),112) end),112), ");
            sb.append(" convert(VARCHAR(20),(CASE WHEN substring(a.day2,1,2) <> '00' then a.day2 else convert(VARCHAR(20),getdate(),112) end),112))  as decimal(10,2)) End),0)  from (   select REPTC.TC015,REPTC.TC026,  ");
        } else {
            sb.append(" select isnull(Sum(Case when isnumeric(a.TC026 )=0 then 0 Else Cast(a.TC026  as decimal(10,2)) End),0) from (   select REPTC.TC015,REPTC.TC026, ");
        }
        sb.append(" (CASE when REPTE.TE502 <> '' then (substring(REPTE.TE502,1,4) + '/' + substring(REPTE.TE502,5,2) + '/' + substring(REPTE.TE502,7,2) + ' ') else '0000/00/00 ' end ) + ");
        sb.append(" (CASE when REPTE.TE504 <> '' then + substring(REPTE.TE504,1,2) + ':' + substring(REPTE.TE504,3,2) else '00:00' end)   as day1, ");
        sb.append(" (CASE when REPTE.TE503 <> '' then (substring(REPTE.TE503,1,4) + '/' + substring(REPTE.TE503,5,2) + '/' + substring(REPTE.TE503,7,2) + ' ') else '0000/00/00 ' end ) + ");
        sb.append(" (CASE when REPTE.TE505 <> '' then + substring(REPTE.TE505,1,2) + ':' + substring(REPTE.TE505,3,2) else '00:00' end)   as day2  ");
        sb.append(" from REPTC LEFT JOIN REPTE on REPTC.TC002=REPTE.TE002 and REPTC.TC001= REPTE.TE001 where ");
        sb.append(" REPTC.TC017 like '").append(deptno).append("%'");
        sb.append(" and year(REPTC.TC015) =${y} AND  month(REPTC.TC015)=${m} ");
        sb.append(" GROUP BY REPTC.TC015,REPTC.TC026,REPTE.TE502,REPTE.TE503,REPTE.TE504,REPTE.TE505) as a");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));

        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            time = new BigDecimal(o1.toString());
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return time;
    }

}
