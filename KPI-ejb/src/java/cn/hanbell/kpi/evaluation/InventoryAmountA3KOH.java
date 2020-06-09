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
public class InventoryAmountA3KOH extends InventoryAmountA3 {

    public InventoryAmountA3KOH() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("indicatorno", "D30");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String indicatorno = map.get("indicatorno") != null ? map.get("indicatorno").toString() : "";
        String genre = map.get("genre") != null ? map.get("genre").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" SELECT ifnull(sum(amount+amamount),0) FROM inventoryproduct  ");
        sb.append(" WHERE facno = '${facno}' ");
        sb.append(" AND categories = 'A3'  ");
        if (!"".equals(indicatorno)) {
            sb.append(" AND indicatorno = '").append(indicatorno).append("'");
        }
        if (!"".equals(genre)) {
            sb.append(" AND genre ").append(genre);
        }
        sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}",
                facno);
        Query query = superEJBForKPI.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            result = (BigDecimal) o;
        } catch (Exception ex) {
            log4j.error("InventoryAmountA3.getValue()异常", ex.toString());
        }
        return result;
    }

}
