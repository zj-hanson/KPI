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
public class InventoryAmountA3 extends Inventory {

    public InventoryAmountA3() {
        super();
    }

    // 华东本部 D50
    // 南京分公司 D20
    // 广州分公司 D30
    // 重庆分公司 D60
    // 济南分公司 D40
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String indicatorno = map.get("indicatorno") != null ? map.get("indicatorno").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" SELECT ifnull(sum(amount+amamount),0) FROM inventoryproduct  ");
        sb.append(" WHERE facno = '${facno}' ");
        sb.append(" AND categories = 'A3'  ");
        if (!"".equals(indicatorno)) {
            sb.append(" AND indicatorno = '").append(indicatorno).append("'");
        }
        sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}",
                facno);
        Query query = superEJBForKPI.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            // 确认当前计算指标是否属于服务部 如果是 加上服务在制部分数据
            if (!"".equals(indicatorno)) {
                switch (indicatorno) {
                    case "D10":
                        BigDecimal fwzzResult = getFWZZValue(y, m, d, type, map);
                        result = BigDecimal.valueOf(Double.parseDouble(o1.toString())).add(fwzzResult);
                        break;
                    case "D20":
                    case "D30":
                    case "D40":
                    case "D60":
                        BigDecimal partsResult = getPartsValue(y, m, d, type, map);
                        result = BigDecimal.valueOf(Double.parseDouble(o1.toString())).add(partsResult);
                        break;
                    case "D50":
                        result = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
                    default:
                        break;
                }
            }
            return result;
        } catch (Exception ex) {
            log4j.error("InventoryAmountA3--getValue()异常", ex.toString());
        }
        return result;
    }

    // 服务在制数据 仅服务部需要加上
    public BigDecimal getFWZZValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String prono = map.get("prono") != null ? map.get("prono").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" SELECT ifnull(sum(amount+amamount),0) FROM inventoryproduct  WHERE  ");
        sb.append(" facno = '${facno}' ");
        sb.append(" AND trtype = 'ZZ' AND wareh = 'FWZZ' ");
        sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m))
                .replace("${facno}", facno).replace("${prono}", prono);
        Query query = superEJBForKPI.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
        } catch (Exception ex) {
            log4j.error("InventoryAmountA3--getFWZZValue()异常", ex.toString());
        }
        return result;

    }

    // 各分公司零件部分
    public BigDecimal getPartsValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String indicatorno = map.get("indicatorno") != null ? map.get("indicatorno").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" SELECT ifnull(sum(amount+amamount),0) FROM inventoryproduct WHERE 1=1  ");
        if (!"".equals(indicatorno)) {
            switch (indicatorno) {
                case "D20":
                    sb.append(" AND facno = 'N'  ");
                    break;
                case "D30":
                    sb.append(" AND facno = 'G'  ");
                    break;
                case "D60":
                    sb.append(" AND facno = 'C4'  ");
                    break;
                case "D40":
                    sb.append(" AND facno = 'J'  ");
                    break;
                default:
                    break;
            }
        }
        sb.append(" AND  itclscode <> '1' AND trtype = 'ZC'  ");
        sb.append(" ");
        sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query = superEJBForKPI.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
        } catch (Exception ex) {
            log4j.error("InventoryAmountA3--getFWZZValue()异常", ex.toString());
        }
        return result;
    }

}
