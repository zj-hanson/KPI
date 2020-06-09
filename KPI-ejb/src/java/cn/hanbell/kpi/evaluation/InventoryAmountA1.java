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
 * @author C1749 生产目标计算接口
 */
public class InventoryAmountA1 extends Inventory {

    public InventoryAmountA1() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String indicatorno = map.get("indicatorno") != null ? map.get("indicatorno").toString() : "";
        String genre = map.get("genre") != null ? map.get("genre").toString() : "";
        String itclscode = map.get("itclscode") != null ? map.get("itclscode").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        // 正常生产性物料部分
        sb.append(" select ifnull(sum(a.num),0) from ( ");
        sb.append(
                " select ifnull(sum(amount+amamount),0) as num from inventoryproduct WHERE categories = 'A1' AND indicatorno = 'B05' ");
        sb.append(" AND trtype = 'ZC' AND facno = '${facno}' ");
        if (!genre.equals("")) {
            sb.append(" AND genre ").append(genre);
        }
        if (!itclscode.equals("")) {
            sb.append(" AND itclscode ").append(itclscode);
        }
        sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        // 借厂商部分
        sb.append(" UNION ALL ");
        sb.append(" select ifnull(sum(amount+amamount),0) as num from inventoryproduct where facno = '${facno}' AND whdsc = '借厂商' ");
        if (!genre.equals("")) {
            sb.append(" AND genre ").append(genre);
        }
        if (!itclscode.equals("")) {
            sb.append(" AND itclscode ").append(itclscode);
        }
        sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        //柯茂的在制需要拧出来分开算，所以这里需要加公司别判断
        if (facno.equals("C")) {
            sb.append(" UNION ALL ");
            // 如果中类编号是B40 生产在制品
            sb.append(" SELECT ifnull(sum(amount+amamount),0) AS num FROM inventoryproduct WHERE facno = '${facno}' and trtype = 'ZZ'   ");
            if (!genre.equals("")) {
                sb.append(" AND genre ").append(genre);
            }
            sb.append(" AND wareh = 'SCZZ' ");
            sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        }
        sb.append(" )a ");
        // 如果中类编号是B50（加工刀片库存（含刀柄））就直接选择库号
        sb.setLength(0);
        if (indicatorno.equals("B50")) {
            sb.append(" SELECT ifnull(sum(amount+amamount),0) FROM inventoryproduct ");
            sb.append(" WHERE facno = '${facno}' ");
            sb.append(" AND categories = 'A1' ");
            sb.append(" AND indicatorno = 'B50' ");
            sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        }
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
