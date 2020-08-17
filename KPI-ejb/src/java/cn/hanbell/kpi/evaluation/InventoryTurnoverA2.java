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
 * @description 营业目标周转天数
 */
public class InventoryTurnoverA2 extends InventoryTurnover {

    public InventoryTurnoverA2() {
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
     *
     * @param y
     * @param m
     * @param d
     * @param map
     * @param type
     * @return BigDecimal
     * @description 获取当月销售成本
     */
    public BigDecimal getMonthSellingCost(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_da = map.get("n_code_da") != null ? map.get("n_code_da").toString() : "";
        String n_code_dc = map.get("n_code_dc") != null ? map.get("n_code_dc").toString() : "";
        String n_code_dd = map.get("n_code_dd") != null ? map.get("n_code_dd").toString() : "";
        String issevdta = map.get("issevdta") != null ? map.get("issevdta").toString() : "";//是否纳入服务
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT isnull(sum(amt),0) FROM ( ");
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_armdta WHERE 1=1 ");
        sb.append(" AND cusno not IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        if (!"".equals(issevdta)) {
            sb.append(" AND issevdta = ").append(issevdta);
        }
        if (!"".equals(n_code_da)) {
            sb.append(" AND n_code_DA ").append(n_code_da);
        }
        if (!"".equals(n_code_dc)) {
            sb.append(" AND n_code_DC ").append(n_code_dc);
        }
        if (!"".equals(n_code_dd)) {
            sb.append(" AND n_code_DD ").append(n_code_dd);
        }
        sb.append(" and yea = ${y} and mon= ${m} ");
        sb.append(" UNION ALL ");
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_other WHERE 1=1 ");
        sb.append(" AND cusno not IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        if (!"".equals(issevdta)) {
            sb.append(" AND issevdta = ").append(issevdta);
        }
        if (!"".equals(n_code_da)) {
            sb.append(" AND n_code_DA ").append(n_code_da);
        }
        if (!"".equals(n_code_dc)) {
            sb.append(" AND n_code_DC ").append(n_code_dc);
        }
        if (!"".equals(n_code_dd)) {
            sb.append(" AND n_code_DD ").append(n_code_dd);
        }
        sb.append(" and yea = ${y} and mon= ${m} ");
        sb.append(" ) a ");
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
     *
     * @param y
     * @param m
     * @param d
     * @param map
     * @param type
     * @return BigDecimal
     * @description 获取年度销售成本
     */
    public BigDecimal getYearSellingCost(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_da = map.get("n_code_da") != null ? map.get("n_code_da").toString() : "";
        String n_code_dc = map.get("n_code_dc") != null ? map.get("n_code_dc").toString() : "";
        String n_code_dd = map.get("n_code_dd") != null ? map.get("n_code_dd").toString() : "";
        String issevdta = map.get("issevdta") != null ? map.get("issevdta").toString() : "";//是否纳入服务
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT isnull(sum(amt),0) FROM ( ");
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_armdta WHERE 1=1 ");
        sb.append(" AND cusno not IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        if (!"".equals(issevdta)) {
            sb.append(" AND issevdta = ").append(issevdta);
        }
        if (!"".equals(n_code_da)) {
            sb.append(" AND n_code_DA ").append(n_code_da);
        }
        if (!"".equals(n_code_dc)) {
            sb.append(" AND n_code_DC ").append(n_code_dc);
        }
        if (!"".equals(n_code_dd)) {
            sb.append(" AND n_code_DD ").append(n_code_dd);
        }
        sb.append(" and yea = ${y} and mon <= ${m} ");
        sb.append(" UNION ALL ");
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_other WHERE 1=1 ");
        sb.append(" AND cusno not IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        if (!"".equals(issevdta)) {
            sb.append(" AND issevdta = ").append(issevdta);
        }
        if (!"".equals(n_code_da)) {
            sb.append(" AND n_code_DA ").append(n_code_da);
        }
        if (!"".equals(n_code_dc)) {
            sb.append(" AND n_code_DC ").append(n_code_dc);
        }
        if (!"".equals(n_code_dd)) {
            sb.append(" AND n_code_DD ").append(n_code_dd);
        }
        sb.append(" and yea = ${y} and mon <= ${m} ");
        sb.append(" ) a ");
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
     *
     * @param y
     * @param m
     * @param d
     * @param map
     * @param type
     * @return BigDecimal
     * @description 真空维修和真空出租的成本 --月
     */
    public BigDecimal getMonthRepairCost(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        StringBuilder sb = new StringBuilder();
        /**
         * 出租的
         */
        sb.append(" SELECT isnull(sum(amt),0) FROM ( ");
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_armdta WHERE 1=1 ");
        sb.append(" and itnbr = 'ARM270' ");
        sb.append(" and yea = ${y} and mon = ${m} ");
        sb.append(" UNION ALL ");
        /**
         * 维修的
         */
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_other WHERE 1=1 ");
        sb.append(" AND dmark1 = 'INV555' ");
        sb.append(" and yea = ${y} and mon = ${m} ");
        sb.append(" ) a ");
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
     *
     * @param y
     * @param m
     * @param d
     * @param map
     * @param type
     * @return BigDecimal
     * @description 真空维修和真空出租的成本 --年
     */
    public BigDecimal getYearRepairCost(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        StringBuilder sb = new StringBuilder();
        /**
         * 出租的
         */
        sb.append(" SELECT isnull(sum(amt),0) FROM ( ");
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_armdta WHERE 1=1 ");
        sb.append(" AND facno = '${facno}' AND itnbr = 'ARM270' ");
        sb.append(" AND yea = ${y} AND mon <= ${m} ");
        sb.append(" UNION ALL ");
        /**
         * 维修的
         */
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_other WHERE 1=1 ");
        sb.append(" AND facno = '${facno}' AND dmark1 = 'INV555' ");
        sb.append(" AND yea = ${y} AND mon <= ${m} ");
        sb.append(" ) a ");
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
