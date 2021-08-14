/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1879 服务各区支援明细
 */
public abstract class ServiceSupportQuantity extends Service {

    public ServiceSupportQuantity() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String status = map.get("status") != null ? map.get("status").toString() : "";

        BigDecimal quantity = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append("select isnull(count(distinct TC002),0) from REPTC where  TC001<>'CXWX' AND TC001 like '%FW%' ");
        //other支援其他地区
        if ("other".equals(status)) {
            sb.append(" AND left(TC012,2)<>left(TC017,2) ");
        }
        sb.append(" AND TC017 ").append(deptno);
        sb.append(" AND year(REPTC.CREATE_DATE) = ${y} and month(REPTC.CREATE_DATE)= ${m} ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));

        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            quantity = new BigDecimal(o1.toString());
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return quantity;
    }

}
