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
 * @author C1879 服务费用金额
 */
public abstract class ServiceAmount extends Service {

    public ServiceAmount() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String status = map.get("status") != null ? map.get("status").toString() : "";
        BigDecimal amount = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();

        sb.append(" select isnull(sum(a.MY033),0) from ( select distinct PORMY.MY033,MZ005,MZ006,TC054,MY024,TC017 ");
        sb.append(" FROM  PORMY,PORMZ LEFT JOIN REPLC ON LC001= MZ005 AND LC002=MZ006 AND LC003=MZ007 LEFT JOIN REPTC ON TC001=LC001 AND TC002=LC002");
        sb.append(" LEFT JOIN SERBQ ON BQ001=TC054  and TC054<>'' WHERE MY001=MZ001 AND MY002=MZ002 AND MZ005 like '%FW%' ");
        sb.append(" AND TC017 like '").append(deptno).append("%'");
        //status N免费 Y收费
        sb.append(" AND SERBQ.BQ501 ='").append(status).append("'");
        sb.append(" AND year(MY024) =${y} and month(MY024)=${m} ) as a ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));

        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            amount = new BigDecimal(o1.toString());
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return amount;
    }

}
