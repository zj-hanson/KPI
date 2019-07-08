/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1879 客服单结案单
 */
public abstract class ServiceCustomerSpacial extends Service {

    public ServiceCustomerSpacial() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String status = map.get("status") != null ? map.get("status").toString() : "";
        String id = map.get("id") != null ? map.get("id").toString() : "";
        BigDecimal closing = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select ISNULL(count(distinct BQ001),0) from SERBQ where 1=1 ");
        //JA 结案
        if ("JA".equals(status)) {
            sb.append(" AND BQ035='Y' ");
        }
        sb.append(" and BQ017 LIKE '").append(deptno).append("%'");
        sb.append(" and year(BQ021) =${y} ");
        sb.append(" AND  month(BQ021) =${m}  ");

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));

        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            closing = new BigDecimal(o1.toString());
            List list = getLastValue(y, m, map);
            if (list != null && !list.isEmpty()) {
                if (!"".equals(id)) {
                    Indicator entity = indicatorBean.findById(Integer.valueOf(id));                    
                    IndicatorDetail order;                   
                    if ("JA".equals(status)) {
                        order = entity.getOther4Indicator();
                    } else {
                        order = entity.getOther3Indicator();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        Object[] row = (Object[]) list.get(i);
                        updateValue(Integer.valueOf(row[0].toString()), BigDecimal.valueOf(Double.valueOf(row[1].toString())), order);
                    }
                    indicatorDetailBean.update(order);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return closing;
    }

    public List getLastValue(int y, int m, LinkedHashMap<String, Object> map) {
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String status = map.get("status") != null ? map.get("status").toString() : "";

        StringBuilder sb = new StringBuilder();
        sb.append(" select  month(BQ021),ISNULL(count(distinct BQ001),0) from SERBQ where 1=1 ");
        //JA 结案
        if ("JA".equals(status)) {
            sb.append(" AND BQ035='Y' ");
        }
        sb.append(" and BQ017 LIKE '").append(deptno).append("%'");
        sb.append(" and year(BQ021) =${y} ");
        sb.append(" AND  month(BQ021) <${m} GROUP BY month(BQ021) ");

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            List list = query.getResultList();
            return list;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
