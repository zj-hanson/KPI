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
 * @description 柯茂涡轮在制库存金额
 */
public class InventoryAmountA1KB40RT extends InventoryAmountA1 {

    public InventoryAmountA1KB40RT() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("genre", "in('RT','HC','HT','HM')");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String genre = map.get("genre") != null ? map.get("genre").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        // 如果中类编号是B40 生产在制品
        sb.append(" SELECT ifnull(sum(amount+amamount),0) AS num FROM inventoryproduct WHERE facno = '${facno}' and trtype = 'ZZ'   ");
        if (!genre.equals("")) {
            sb.append(" AND genre ").append(genre);
        }
        sb.append(" AND wareh = 'SCZZ' ");
        sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}",
                facno);
        Query query = superEJBForKPI.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
        } catch (Exception ex) {
            log4j.error("InventoryAmountA1.getValue()类异常", ex.toString());
        }
        return result;
    }

}
