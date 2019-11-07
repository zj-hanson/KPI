/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.persistence.Query;

/**
 *
 * @version V1.0
 * @author C1749
 * @data 2019-10-28
 * @description 总计周转天数
 */
public class InventoryTurnoverTotal extends InventoryTurnover {

    public InventoryTurnoverTotal() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal sell, result;
        try {
            if (map.containsKey("mm")) {
                sell = getMonthSellingCost(y, m, d, type, map);
                result = getMonthValue(y, m, d, type, map, sell);
            } else {
                sell = getYearSellingCost(y, m, d, type, map);
                result = getYearValue(y, m, d, type, map, sell);
            }
            return result;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    /**
     * @param y
     * @param m
     * @param d
     * @param type
     * @param map
     * @return BigDecimal
     * @description 获取当月销售成本
     * @throws:
     */
    public BigDecimal getMonthSellingCost(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(amt),0) from (  ");
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_armdta WHERE 1=1 ");
        sb.append(" and yea = ${y} and mon= ${m} ");
        sb.append("  ");
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_other WHERE 1=1 ");
        sb.append(" and yea = ${y} and mon= ${m} ");
        sb.append(" ) a");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            return (BigDecimal) o;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    /**
     * @param y
     * @param m
     * @param d
     * @param type
     * @param map
     * @return BigDecimal
     * @description 获取年度销售成本
     * @throws:
     */
    public BigDecimal getYearSellingCost(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(amt),0) from (  ");
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_armdta WHERE 1=1 ");
        sb.append(" and yea = ${y} and mon <= ${m} ");
        sb.append("  ");
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_other WHERE 1=1 ");
        sb.append(" and yea = ${y} and mon <= ${m} ");
        sb.append(" ) a");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            return (BigDecimal) o;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

}
