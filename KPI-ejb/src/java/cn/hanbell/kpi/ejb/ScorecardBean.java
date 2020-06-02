/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.entity.ScorecardContent;
import cn.hanbell.kpi.entity.ScorecardDetail;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

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

    public void setScore(ScorecardDetail d) {
        JexlEngine jexl = new JexlBuilder().create();
        // Create an expression
        String jexlExp = "object.parent.tq1";
        JexlExpression e = jexl.createExpression(jexlExp);
        // Create a context and add data
        JexlContext jc = new MapContext();
        jc.set("object", d);
        // Now evaluate the expression, getting the result
        Object o = e.evaluate(jc);
    }

    public void setScore(ScorecardContent d, String n) throws Exception {
        if (d.getScoreJexl() == null || "".equals(d.getScoreJexl())) {
            throw new NullPointerException("表达式为空");
        }
        JexlEngine jexl = new JexlBuilder().create();
        // Create an expression
        String jexlExp = d.getScoreJexl().replace("${n}", n);
        JexlExpression exp = jexl.createExpression(jexlExp);
        // Create a context
        JexlContext jc = new MapContext();
        jc.set("object", d);
        // Evaluate the expression
        try {
            Object value = exp.evaluate(jc);
            BigDecimal score = BigDecimal.valueOf(Double.valueOf(value.toString()));
            if (d.getMinNum() != null && score.compareTo(d.getMinNum()) < 0) {
                score = d.getMinNum();
            }
            if (d.getMaxNum() != null && score.compareTo(d.getMaxNum()) > 0) {
                score = d.getMaxNum();
            }
            switch (n) {
                case "q1":
                    d.getDeptScore().setSq1(score);
                    d.getGeneralScore().setSq1(score);
                    break;
                case "q2":
                    d.getDeptScore().setSq2(score);
                    d.getGeneralScore().setSq2(score);
                    break;
                case "q3":
                    d.getDeptScore().setSq3(score);
                    d.getGeneralScore().setSq3(score);
                    break;
                case "q4":
                    d.getDeptScore().setSq4(score);
                    d.getGeneralScore().setSq4(score);
                    break;
                default:
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

}
