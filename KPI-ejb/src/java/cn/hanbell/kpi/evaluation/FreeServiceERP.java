/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import com.lightshell.comm.BaseLib;
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
 * @author C1879
 */
public abstract class FreeServiceERP implements Actual {

    protected SuperEJBForERP superEJB = lookupSuperEJBForERP();

    protected LinkedHashMap<String, Object> queryParams;

    public FreeServiceERP() {
        queryParams = new LinkedHashMap<>();
    }

    // 质量扣款——质量扣款金额,关联4个分类
    public BigDecimal getARM423Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String ogdkid = map.get("ogdkid") != null ? map.get("ogdkid").toString() : "";
        String hmark1 = map.get("hmark1") != null ? map.get("hmark1").toString() : "";
        String hmark2 = map.get("hmark2") != null ? map.get("hmark2").toString() : "";

        //质量扣款
        BigDecimal arm423 = BigDecimal.ZERO;
        //折让
        BigDecimal armpmm = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ISNULL(-SUM(d.recamt),0) FROM armrec d,armrech h where d.facno=h.facno AND d.recno=h.recno AND h.prgno='ARM423' AND h.recstat='1' AND d.raccno='6001' ");
        sb.append(" AND h.facno='${facno}' AND h.ogdkid='${ogdkid}' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" AND h.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" AND h.n_code_CD ").append(n_code_CD);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" AND h.n_code_DC ").append(n_code_DC);
        }
        sb.append(" AND year(h.recdate) = ${y} and month(h.recdate)= ${m} ");
        String sqlARM423 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno).replace("${ogdkid}", ogdkid);

        sb.setLength(0);

        sb.append(" SELECT SUM(ISNULL(armpmm.pmamt,0))  FROM armpmm WHERE zlk='Y' AND h.facno='${facno}' ");
        if (!"".equals(hmark1)) {
            sb.append(" and hmark1 ").append(hmark1);
        }
        if (!"".equals(hmark2)) {
            sb.append(" and hmark2 ").append(hmark2);
        }
        sb.append(" AND year(trdat) = ${y} and month(trdat)= ${m} ");
        String sqlARMPMM = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(sqlARM423);
        Query query2 = superEJB.getEntityManager().createNativeQuery(sqlARMPMM);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            arm423 = (BigDecimal) o1;
            armpmm = (BigDecimal) o2;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arm423.add(armpmm);
    }

    // 服务成本（厂外）借出单运费
    public BigDecimal getCDRN20Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        //部门
        String depno = map.get("depno") != null ? map.get("depno").toString() : "";

        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(c.freight),0) from cdrlnhad h LEFT JOIN cdrfre c on c.shpno = h.trno and c.facno = h.facno ");
        sb.append(" where  h.facno='${facno}' AND h.status ='Y' and  h.resno = '03' and ( h.fwno <> ''and h.fwno <> '-') and (h.kfno <> '' and h.kfno <> '-') and c.freight> 0  ");
        if (!"".equals(depno)) {
            sb.append(" AND h.depno ").append(depno);
        }
        sb.append(" AND year(h.cfmdate) = ${y} and month(h.cfmdate)= ${m} ");

        String cdrn20sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(cdrn20sql);
        try {
            Object o = query.getSingleResult();
            return (BigDecimal) o;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
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
        //每月3-5号自动更新上个月的数据
        int month;
        if (m == 1) {
            month = 12;
        } else {
            month = m - 1;
        }
        return month;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        //每月3-5号自动更新上个月的数据
        int year;
        if (m == 1) {
            year = y - 1;
        } else {
            year = y;
        }
        return year;
    }

}
