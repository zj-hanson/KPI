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
 * @author C1749
 */
// 营业目标周转天数
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

    //获取当月销售成本
    public BigDecimal getMonthSellingCost(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_da = map.get("n_code_da") != null ? map.get("n_code_da").toString() : "";
        String n_code_dc = map.get("n_code_dc") != null ? map.get("n_code_dc").toString() : "";
        String n_code_dd = map.get("n_code_dd") != null ? map.get("n_code_dd").toString() : "";
        String issevdta = map.get("issevdta") != null ? map.get("issevdta").toString() : "";//是否纳入服务
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT isnull(sum(otramt),0) FROM N_CDRX666_armdta WHERE 1=1 ");
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

    //获取年度销售成本
    public BigDecimal getYearSellingCost(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_da = map.get("n_code_da") != null ? map.get("n_code_da").toString() : "";
        String n_code_dc = map.get("n_code_dc") != null ? map.get("n_code_dc").toString() : "";
        String n_code_dd = map.get("n_code_dd") != null ? map.get("n_code_dd").toString() : "";
        String issevdta = map.get("issevdta") != null ? map.get("issevdta").toString() : "";//是否纳入服务
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT isnull(sum(otramt),0) FROM N_CDRX666_armdta WHERE 1=1 ");
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
