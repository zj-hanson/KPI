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
public class MaterielReturnQuantityHD extends Materiel {

    public MaterielReturnQuantityHD() {
        super();
        queryParams.put("deptno", " in ('1F700','1F800','1B000','1B100','1B200')");

    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        BigDecimal qty = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" select sum(d.TD500) as num from REPTC h, REPTD d ");
        sb.append(" where h.TC001 = d.TD001 and h.TC002 = d.TD002 and  TC001 not like 'CX%' and TD502 <> '' ");
        if (!"".equals(deptno)) {
            sb.append(" and TC017 ").append(deptno);
        }
        sb.append(" AND year(h.TC003) = ${y} and month(h.TC003)<= ${m} ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            qty = BigDecimal.valueOf(Double.valueOf(o.toString()));
        } catch (NumberFormatException e) {
            log4j.error("MaterielReturnQuantity---NumberFormatException", e);
        }
        return qty;
    }

}
