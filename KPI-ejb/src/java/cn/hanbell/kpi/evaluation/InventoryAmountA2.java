/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.ejb.IndicatorBean;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;

/**
 *
 * @author C1749 营业性质
 */
public class InventoryAmountA2 extends Inventory {

    IndicatorBean indicatorBean = lookupIndicatorBeanBean();
    protected BigDecimal fgsValue = BigDecimal.ZERO;// 汉钟分公司库 的数据
    protected BigDecimal fgsZjValue = BigDecimal.ZERO;// 分公司整机部分的数据

    public InventoryAmountA2() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String categories = map.get("categories") != null ? map.get("categories").toString() : "";// 大类编号
        String indicatorno = map.get("indicatorno") != null ? map.get("indicatorno").toString() : "";// 指标编号
        String genre = map.get("genre") != null ? map.get("genre").toString() : "";// 产品别
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        // 公用物料部分
        sb.append(" SELECT ifnull(sum(amount+amamount),0) FROM inventoryproduct ");
        sb.append(" WHERE facno = '${facno}' ");
        sb.append(" AND trtype = 'ZC' ");
        sb.append(" AND indicatorno = 'C10' ");// 待指标数据都归档完成之前用这个逻辑（ERP需维护每个指标对应的库别）
        // if (!"".equals(indicatorno)) {
        // sb.append(" AND indicatorno = '").append(indicatorno).append("'");
        // }
        if (!"".equals(categories)) {
            sb.append(" AND categories = '").append(categories).append("'");
        }
        if (!"".equals(genre)) {
            sb.append(" AND genre ").append(genre);
        }
        sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}",
                facno);
        Query query = superEJBForKPI.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
        } catch (Exception ex) {
            log4j.error("InventoryAmountA2--getValue()异常", ex.toString());
        }
        return result;
    }

    // 汉钟分公司库的数据
    public BigDecimal getFgsValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String categories = map.get("categories") != null ? map.get("categories").toString() : "";
        String indicatorno = map.get("indicatorno") != null ? map.get("indicatorno").toString() : "";
        String genre = map.get("genre") != null ? map.get("genre").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" SELECT ifnull(sum(amount+amamount),0) FROM inventoryproduct  ");
        sb.append(" WHERE facno = '${facno}' ");
        sb.append(" AND trtype = 'ZC' ");
        if (!"".equals(categories)) {
            sb.append(" AND categories = '").append(categories).append("'");
        }
        if (!"".equals(indicatorno)) {
            sb.append(" AND indicatorno ='").append(indicatorno).append("'");
        }
        if (!"".equals(genre)) {
            sb.append(" AND genre ").append(genre);
        }
        sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}",
                facno);
        Query query = superEJBForKPI.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
        } catch (Exception ex) {
            log4j.error("InventoryAmountA2--getFgsValue()异常", ex.toString());
        }
        return result;
    }

    // 分公司整机数据 只有分公司数据才有自己本身整机的数据
    public BigDecimal getFgsZjValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String categories = map.get("categories") != null ? map.get("categories").toString() : "";
        String indicatorno = map.get("indicatorno") != null ? map.get("indicatorno").toString() : "";
        String genre = map.get("genre") != null ? map.get("genre").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" SELECT ifnull(sum(amount+amamount),0) FROM inventoryproduct ");
        sb.append(" where 1=1  ");
        if (!"".equals(indicatorno)) {
            switch (indicatorno) {
                case "C12":
                    sb.append(" AND facno = 'N' ");
                    break;
                case "C13":
                    sb.append(" AND facno = 'G' ");
                    break;
                case "C14":
                    sb.append(" AND facno = 'C4' ");
                    break;
                case "C15":
                    sb.append(" AND facno = 'J' ");
                    break;
                default:
                    sb.append(" ");
            }
        }
        sb.append(" AND trtype = 'ZC' ");
        sb.append(" AND itclscode = '1' ");// itclscode='1'为整机物料
        sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}",
                facno);
        Query query = superEJBForKPI.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
        } catch (Exception ex) {
            log4j.error("InventoryAmountA2--getFgsZjValue()异常", ex.toString());
        }
        return result;
    }

    // 获取生产性物料的库存金额
    public BigDecimal getProductValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String categories = map.get("categories") != null ? map.get("categories").toString() : "";
        String indicatorno = map.get("indicatorno") != null ? map.get("indicatorno").toString() : "";
        String genre = map.get("genre") != null ? map.get("genre").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        // 生产性物料 RT、AD和P部分的数据
        sb.append(" select ifnull(sum(a.num),0) from ( ");
        // indicatorno暂时写成B20
        sb.append(
                " select sum(amount+amamount) as num from inventoryproduct WHERE categories = 'A1' AND indicatorno = 'B20' ");
        sb.append(" AND trtype = 'ZC' AND facno = '${facno}' ");
        if (!"".equals(genre)) {
            sb.append(" AND genre ").append(genre);
        }
        sb.append(" AND genre NOT IN ('RT','P','AD') ");
        sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        // 借厂商部分
        sb.append(" UNION ALL ");
        sb.append(" select sum(amount+amamount) as num from inventoryproduct where facno = '${facno}' AND whdsc = '借厂商' ");
        if (!"".equals(genre)) {
            sb.append(" AND genre ").append(genre);
        }
        sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        sb.append(" )a ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}",
                facno);
        Query query = superEJBForKPI.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
        } catch (Exception ex) {
            log4j.error("InventoryAmountA2--getProductValue()异常", ex.toString());
        }
        return result;
    }

    // 获取生产在制的库存金额
    public BigDecimal getgetProductZZValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String categories = map.get("categories") != null ? map.get("categories").toString() : "";
        String indicatorno = map.get("indicatorno") != null ? map.get("indicatorno").toString() : "";
        String genre = map.get("genre") != null ? map.get("genre").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(
                " SELECT ifnull(sum(amount+amamount),0) AS num FROM inventoryproduct WHERE facno = '${facno}' and trtype = 'ZZ'   ");
        sb.append(" AND wareh = 'SCZZ' ");
        if (!"".equals(genre)) {
            sb.append(" AND genre ").append(genre);
        }
        sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}",
                facno);
        Query query = superEJBForKPI.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
            return result;
        } catch (Exception ex) {
            log4j.error("InventoryAmountA2--getgetProductZZValue()异常", ex.toString());
        }
        return result;
    }

    private IndicatorBean lookupIndicatorBeanBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
