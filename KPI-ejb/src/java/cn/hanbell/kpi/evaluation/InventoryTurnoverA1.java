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
 * @description 生产周转天数类 本月周转天数 = 30 / (本月销售成本/((本月库存金额+上月库存金额)/2))
 *
 */
public class InventoryTurnoverA1 extends InventoryTurnover {

    public InventoryTurnoverA1() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return BigDecimal.ZERO;
    }

    /**
     * @param y
     * @param m
     * @param d
     * @param type
     * @param map
     * @return BigDecimal
     * @description: 获取月销售成本汇总
     * @throws:
     */
    public BigDecimal getMonthSellingValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal v1, v2, v3, v4, v5, v6, v7, v8, v9;
        BigDecimal result;
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("issevdta", "'N'");
        queryParams.put("n_code_da", "= 'R'");
        queryParams.put("n_code_dc", "<> 'RT'");
        queryParams.put("n_code_dd", "in ('00','02')");
        v1 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("issevdta", "'N'");
        queryParams.put("n_code_da", "= 'AH'");
        queryParams.put("n_code_dc", "= 'AJ'");
        queryParams.put("n_code_dd", "in ('00','02')");
        v2 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_da", "= 'AH'");
        queryParams.put("n_code_dc", "= 'SDS'");
        queryParams.put("n_code_dd", "in ('00','02','01')");
        v3 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("issevdta", "'N'");
        queryParams.put("n_code_da", "= 'AA'");
        queryParams.put("n_code_dd", "in ('00','02')");
        v4 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_da", "= 'P'");
        queryParams.put("n_code_dd", "in ('00','02','01')");
        v5 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("issevdta", "'N'");
        queryParams.put("n_code_da", "= 'S'");
        queryParams.put("n_code_dd", "in ('00','02')");
        v6 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_da", "= 'RT'");
        queryParams.put("n_code_dc", "= 'RT'");
        queryParams.put("n_code_dd", "in ('00','02','01')");
        v7 = getMonthSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("depno", "like '1T%'");
        queryParams.put("n_code_dd", "in ('01')");
        v8 = getMonthSellingCostOfWX(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        v9 = getMonthRepairCost(y, m, d, type, queryParams);
        try {
            result = v1.add(v2).add(v3).add(v4).add(v5).add(v6).add(v7).add(v8).add(v9);
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
     * @description: 获取年度销售成本汇总
     * @throws:
     */
    public BigDecimal getYearSellingValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal v1, v2, v3, v4, v5, v6, v7, v8, v9;
        BigDecimal result;
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("issevdta", "'N'");
        queryParams.put("n_code_da", "= 'R'");
        queryParams.put("n_code_dc", "<> 'RT'");
        queryParams.put("n_code_dd", "in ('00','02')");
        v1 = getYearSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("issevdta", "'N'");
        queryParams.put("n_code_da", "= 'AH'");
        queryParams.put("n_code_dc", "= 'AJ'");
        queryParams.put("n_code_dd", "in ('00','02')");
        v2 = getYearSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_da", "= 'AH'");
        queryParams.put("n_code_dc", "= 'SDS'");
        queryParams.put("n_code_dd", "in ('00','02','01')");
        v3 = getYearSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("issevdta", "'N'");
        queryParams.put("n_code_da", "= 'AA'");
        queryParams.put("n_code_dd", "in ('00','02')");
        v4 = getYearSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_da", "= 'P'");
        queryParams.put("n_code_dd", "in ('00','02','01')");
        v5 = getYearSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("issevdta", "'N'");
        queryParams.put("n_code_da", "= 'S'");
        queryParams.put("n_code_dd", "in ('00','02')");
        v6 = getYearSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_da", "= 'RT'");
        queryParams.put("n_code_dc", "= 'RT'");
        queryParams.put("n_code_dd", "in ('00','02','01')");
        v7 = getYearSellingCost(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("depno", "like '1T%'");
        queryParams.put("n_code_dd", "in ('01')");
        v8 = getYearSellingCostOfWX(y, m, d, type, queryParams);

        queryParams.clear();
        queryParams.put("facno", "C");
        v9 = getYearRepairCost(y, m, d, type, queryParams);
        try {
            result = v1.add(v2).add(v3).add(v4).add(v5).add(v6).add(v7).add(v8).add(v9);
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
     * @description: 获取当月销售成本
     * @throws:
     */
    public BigDecimal getMonthSellingCost(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_da = map.get("n_code_da") != null ? map.get("n_code_da").toString() : "";
        String n_code_dc = map.get("n_code_dc") != null ? map.get("n_code_dc").toString() : "";
        String n_code_dd = map.get("n_code_dd") != null ? map.get("n_code_dd").toString() : "";
        String issevdta = map.get("issevdta") != null ? map.get("issevdta").toString() : "";//是否纳入服务
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT isnull(sum(amt),0) from ( ");
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_armdta WHERE 1=1 ");
        sb.append(" AND cusno not IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
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
        sb.append(" AND cusno not IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
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
     * @param y
     * @param m
     * @param d
     * @param type
     * @param map
     * @return BigDecimal
     * @description: 获取年度销售成本
     * @throws:
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
        sb.append(" AND cusno not IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
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
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_armdta WHERE 1=1 ");
        sb.append(" AND cusno not IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
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
     * @param y
     * @param m
     * @param d
     * @param type
     * @param map
     * @return BigDecimal
     * @description: 国际营销的外销台湾和香港部分 月的
     * @throws:
     */
    public BigDecimal getMonthSellingCostOfWX(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String depno = map.get("depno") != null ? map.get("depno").toString() : "";
        String n_code_da = map.get("n_code_da") != null ? map.get("n_code_da").toString() : "";
        String n_code_dc = map.get("n_code_dc") != null ? map.get("n_code_dc").toString() : "";
        String n_code_dd = map.get("n_code_dd") != null ? map.get("n_code_dd").toString() : "";
        String issevdta = map.get("issevdta") != null ? map.get("issevdta").toString() : "";//是否纳入服务
        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(amt),0) from ( ");
        sb.append(" select isnull(sum(otramt),0) as amt from N_CDRX666_armdta where ");
        sb.append(" cusno IN ('STW00003','SXG00002') and facno = '${facno}' ");
        if (!"".equals(issevdta)) {
            sb.append(" AND issevdta = ").append(issevdta);
        }
        if (!"".equals(depno)) {
            sb.append(" AND depno ").append(depno);
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
        sb.append(" select isnull(sum(otramt),0) as amt from N_CDRX666_other where ");
        sb.append(" cusno IN ('STW00003','SXG00002') and facno = '${facno}' ");
        if (!"".equals(issevdta)) {
            sb.append(" AND issevdta = ").append(issevdta);
        }
        if (!"".equals(depno)) {
            sb.append(" AND depno ").append(depno);
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
     * @param y
     * @param m
     * @param d
     * @param type
     * @param map
     * @return BigDecimal
     * @description: 国际营销的外销台湾和香港部分 年的
     * @throws:
     */
    public BigDecimal getYearSellingCostOfWX(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String depno = map.get("depno") != null ? map.get("depno").toString() : "";
        String n_code_da = map.get("n_code_da") != null ? map.get("n_code_da").toString() : "";
        String n_code_dc = map.get("n_code_dc") != null ? map.get("n_code_dc").toString() : "";
        String n_code_dd = map.get("n_code_dd") != null ? map.get("n_code_dd").toString() : "";
        String issevdta = map.get("issevdta") != null ? map.get("issevdta").toString() : "";//是否纳入服务
        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(amt),0) from ( ");
        sb.append(" select isnull(sum(otramt),0) as amt from N_CDRX666_armdta where ");
        sb.append(" cusno IN ('STW00003','SXG00002') and facno = '${facno}' ");
        if (!"".equals(issevdta)) {
            sb.append(" AND issevdta = ").append(issevdta);
        }
        if (!"".equals(depno)) {
            sb.append(" AND depno ").append(depno);
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
        sb.append(" select isnull(sum(otramt),0) as amt from N_CDRX666_armdta where ");
        sb.append(" cusno IN ('STW00003','SXG00002') and facno = '${facno}' ");
        if (!"".equals(issevdta)) {
            sb.append(" AND issevdta = ").append(issevdta);
        }
        if (!"".equals(depno)) {
            sb.append(" AND depno ").append(depno);
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
     * @param y
     * @param m
     * @param d
     * @param type
     * @param map
     * @return BigDecimal
     * @description: 真空维修和真空出租的成本--月
     * @throws:
     */
    public BigDecimal getMonthRepairCost(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        StringBuilder sb = new StringBuilder();
        //出租的
        sb.append(" SELECT isnull(sum(amt),0) FROM ( ");
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_armdta WHERE 1=1 ");
        sb.append(" and itnbr = 'ARM270' ");
        sb.append(" and yea = ${y} and mon = ${m} ");
        sb.append(" UNION ALL ");
        //维修的
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
     * @param y
     * @param m
     * @param d
     * @param type
     * @param map
     * @return BigDecimal
     * @description: 真空维修和真空出租的成本--年
     * @throws:
     */
    public BigDecimal getYearRepairCost(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        StringBuilder sb = new StringBuilder();
        //出租的
        sb.append(" SELECT isnull(sum(amt),0) FROM ( ");
        sb.append(" SELECT isnull(sum(otramt),0) as amt FROM N_CDRX666_armdta WHERE 1=1 ");
        sb.append(" AND facno = '${facno}' AND itnbr = 'ARM270' ");
        sb.append(" AND yea = ${y} AND mon <= ${m} ");
        sb.append(" UNION ALL ");
        //维修的
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
