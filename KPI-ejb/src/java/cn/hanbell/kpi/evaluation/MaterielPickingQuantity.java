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
 * @author C1749
 */
public class MaterielPickingQuantity extends Materiel {

    public MaterielPickingQuantity() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String deptno = "";//部门代号
        String id = map.get("id") != null ? map.get("id").toString() : "";//指标的ID
        Indicator entity = indicatorBean.findById(Integer.parseInt(id));
        if (entity != null) {
            deptno = entity.getDeptno().substring(0, 2);
        }
        BigDecimal qty = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" select sum(TB009) as num from WARTA h,WARTB d,SERBQ q ");
        sb.append(" where h.TA001 = d.TB001 and h.TA002 = d.TB002 and q.BQ001 = h.TA039 and TA001 not like  'CX%' and q.BQ500 <> '2' ");
        if (!"".equals(deptno)) {
            sb.append(" and TA004 like '").append(deptno).append("%'");
        }
        sb.append(" AND year(h.TA003) = ${y} and month(h.TA003)= ${m} ");
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
