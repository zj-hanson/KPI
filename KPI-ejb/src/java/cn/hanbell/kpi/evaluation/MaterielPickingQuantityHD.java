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
public class MaterielPickingQuantityHD extends Materiel {

    public MaterielPickingQuantityHD() {
        super();
        queryParams.put("deptno", " in ('1F700','1F800','1B000','1B100','1B200')");

    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        BigDecimal qty = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" select sum(TB009) as num from WARTA h,WARTB d,SERBQ q ");
        sb.append(" where h.TA001 = d.TB001 and h.TA002 = d.TB002 and q.BQ001 = h.TA039 and TA001 not like  'CX%' and q.BQ500 <> '2' ");
        if (!"".equals(deptno)) {
            sb.append(" and TA004 ").append(deptno);
        }
        sb.append(" AND year(h.TA003) = ${y} and month(h.TA003)<= ${m} ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            qty = BigDecimal.valueOf(Double.valueOf(o.toString()));
        } catch (NumberFormatException e) {
            log4j.error("MaterielPickingQuantity---NumberFormatException", e);
        }
        return qty;
    }

}
