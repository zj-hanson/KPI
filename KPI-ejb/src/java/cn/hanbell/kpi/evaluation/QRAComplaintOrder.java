/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;

/**
 *
 * @author C1749 得到mis制令台数总和
 */
public class QRAComplaintOrder implements Actual {

    protected SuperEJBForERP superEJB = lookupSuperEJBForERP();
    protected LinkedHashMap<String, Object> queryParams;

    public QRAComplaintOrder() {
        queryParams = new LinkedHashMap<>();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mis = map.get("mis") != null ? map.get("mis").toString() : ""; //MJS（3/6/12）
        StringBuilder sb = new StringBuilder();
        int count = 0;
        QRAComplaintCountAll mKS = new QRAComplaintCountAll();
        List<String> list = mKS.getValue(y, m, d, type, map);
        try {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null) {
                    sb.append(" select isnull(count(1),0) from ( ");
                    sb.append(" select m.mandate  from sfcwad d LEFT JOIN manmas m on d.manno = m.manno and d.facno = m.facno and d.prono = m.prono where ");
                    sb.append(" d.varnr <> '%' ");
                    sb.append(" and d.varnr like  '%").append(list.get(i)).append("%' ");
                    sb.append("  and datediff(mm,m.mandate,getdate())<= ").append(mis);
                    sb.append(" ) as a ");
                    String sql = sb.toString();
                    superEJB.setCompany("C");
                    Query query = superEJB.getEntityManager().createNativeQuery(sql);
                    Object o1 = query.getSingleResult();
                    String str = o1.toString();
                    if (!"0".equals(str)) {
                        count++;
                    }
                }
            }
            return BigDecimal.valueOf(count);
        } catch (Exception ex) {
            Logger.getLogger(QRAHansonRotor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }

    protected SuperEJBForERP lookupSuperEJBForERP() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForERP) c.lookup("java:global/KPI/KPI-ejb/SuperEJBForERP!cn.hanbell.kpi.comm.SuperEJBForERP");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup("java:global/KPI/KPI-ejb/SuperEJBForERP!cn.hanbell.kpi.comm.SuperEJBForERP");
        superEJB = (SuperEJBForERP) objRef;
    }

    @Override
    public int getUpdateMonth(int y, int m) {
        return m;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        return y;
    }

}
