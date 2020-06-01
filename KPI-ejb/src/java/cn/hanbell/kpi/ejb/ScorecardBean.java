/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.entity.ScorecardContent;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class ScorecardBean extends SuperEJBForKPI<Scorecard> {

    public ScorecardBean() {
        super(Scorecard.class);
    }

    public List<Scorecard> findByMenuAndYear(String value, int y) {
        Query query = getEntityManager().createNamedQuery("Scorecard.findByMenuAndYear");
        query.setParameter("menu", value);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public String getColumn(String type, int i) {
        return type.toLowerCase() + String.format("%01d", i);
    }

    public BigDecimal getScore(List<ScorecardContent> detail, String column) throws Exception {
        BigDecimal weight;
        BigDecimal score;
        BigDecimal total = BigDecimal.ZERO;
        Object value;
        Field f;
        try {
            for (ScorecardContent entity : detail) {
                f = entity.getClass().getDeclaredField(column);
                f.setAccessible(true);
                value = f.get(entity);
                if (value == null || "".equals(value)) {
                    continue;
                }
                if (entity.getWeight() == 0) {
                    total = total.add(BigDecimal.valueOf(Double.valueOf(value.toString())));
                } else {
                    weight = BigDecimal.valueOf(entity.getWeight()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    score = BigDecimal.valueOf(Double.valueOf(value.toString()));
                    total = total.add(score.multiply(weight));
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex) {
            log4j.error(ex);
            throw ex;
        }
        return total;
    }

}
