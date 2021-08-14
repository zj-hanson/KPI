/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.entity.Indicator;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
public class MaterielFCRHD extends Materiel {

    public MaterielFCRHD() {
        super();
        queryParams.put("deptno", " in ('1F700','1F800','1B000','1B100','1B200')");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        BigDecimal fcr = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" select sum(TB009) as num from WARTA h,WARTB d,SERBQ q ");
        sb.append(" where h.TA001 = d.TB001 and h.TA002 = d.TB002 and q.BQ001 = h.TA039 and TA001 not like  'CX%' and q.BQ500 <> '2' ");
        if (!"".equals(deptno)) {
            sb.append(" and TA004 ").append(deptno);
        }
        sb.append(" AND year(h.TA003) = ${y} and month(h.TA003)<= ${m} ");
        //领料
        String sql1 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        sb.setLength(0);
        sb.append(" select sum(d.TD500) as num from REPTC h, REPTD d ");
        sb.append(" where h.TC001 = d.TD001 and h.TC002 = d.TD002 and  TC001 not like 'CX%' and TD502 <> '' ");
        if (!"".equals(deptno)) {
            sb.append(" and TC017 ").append(deptno);
        }
        sb.append(" AND year(h.TC003) = ${y} and month(h.TC003)<= ${m} ");
        //退料
        String sql2 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query1 = superEJB.getEntityManager().createNativeQuery(sql1);
        Query query2 = superEJB.getEntityManager().createNativeQuery(sql2);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            fcr = BigDecimal.valueOf(Double.valueOf(o2.toString()) / Double.valueOf(o1.toString())).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (NumberFormatException e) {
            log4j.error("MaterielFCR---NumberFormatException", e);
        }
        return fcr;
    }

}
