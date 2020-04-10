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
 * @author C1879 厂外服务成本差旅费
 */
public class FreeServiceOuterCL extends FreeServiceOuterOA {

    public FreeServiceOuterCL() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        //配合部门
        String appDept = map.get("depno") != null ? map.get("depno").toString() : "";

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ISNULL(sum(a.totaltaxInclusiveRMB),0) FROM  ");
        sb.append(" ( select DISTINCT pi.serialNumber,h.appDept,h.totaltaxInclusiveRMB from HZ_CW028 h ");
        sb.append(" INNER JOIN  ProcessInstance pi on h.processSerialNumber=pi.serialNumber INNER JOIN WorkItem wi on pi.contextOID=wi.contextOID ");
        sb.append(" where h.cost in ('0','1') and h.reimbursement ='1'  and pi.currentState = '3' ");
        if (!"".equals(appDept)) {
            sb.append("  AND h.appDept ").append(appDept);
        }
        sb.append(" AND year(wi.completedTime) = ${y} and month(wi.completedTime)= ${m} ");
        sb.append(" and wi.completedTime=( select max(d.completedTime) from WorkItem d where wi.contextOID=d.contextOID ");
        sb.append("  AND year(d.completedTime) = ${y} and month(d.completedTime)>=${m} ) ");
        sb.append(" ) as a ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);
        Query query = superEFGP.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            return BigDecimal.valueOf(Double.valueOf(o.toString()));
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

}
