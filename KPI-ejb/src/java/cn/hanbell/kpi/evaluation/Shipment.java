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
import javax.naming.InitialContext;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
public abstract class Shipment implements Actual {

    protected SuperEJBForERP superEJB;

    protected LinkedHashMap<String, Object> queryParams;

    protected BigDecimal arm232 = BigDecimal.ZERO;
    protected BigDecimal arm235 = BigDecimal.ZERO;
    protected BigDecimal arm270 = BigDecimal.ZERO;
    protected BigDecimal arm423 = BigDecimal.ZERO;

    public Shipment() {
        queryParams = new LinkedHashMap<>();
    }

    public SuperEJBForERP getEJB() {
        return superEJB;
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup(JNDIName);
        superEJB = (SuperEJBForERP) objRef;
    }

    public BigDecimal getARM232Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno").toString();
        String n_code_DA = map.get("n_code_DA").toString();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ISNULL(SUM(CASE h.amtco WHEN 'P' THEN d.psamt WHEN 'M' THEN d.psamt *(-1) ELSE 0 END),0) FROM armpmm h,armacq d ");
        sb.append(" WHERE h.facno=d.facno AND h.trno = d.trno AND h.facno='${facno}' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" AND h.hmark1 ").append(n_code_DA);
        }
        sb.append(" AND year(h.trdat) = ${y} AND month(h.trdat) = ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" AND h.trdat<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" AND h.trdat= '${d}' ");
                break;
            default:
                sb.append(" AND h.trdat<= '${d}' ");
        }
        String sqlstr = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${facno}", facno);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sqlstr);
        try {
            Object o = query.getSingleResult();
            return (BigDecimal) o;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getARM235Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno").toString();
        String decode = map.get("decode").toString();
        String n_code_DA = map.get("n_code_DA").toString();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ISNULL(SUM(apmamt),0) FROM armicdh h WHERE h.shpno IN ");
        sb.append(" (SELECT DISTINCT b.shpno FROM cdrhad a,cdrdta b WHERE a.facno=b.facno AND a.shpno=b.shpno AND a.houtsta<>'W' AND a.facno='${facno}' AND a.decode='${decode}' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" AND b.n_code_DA ").append(n_code_DA);
        }
        sb.append(" AND year(a.shpdate) = ${y}  AND month(a.shpdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" AND a.shpdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" AND a.shpdate= '${d}' ");
                break;
            default:
                sb.append(" AND a.shpdate<= '${d}' ");
        }
        sb.append(" ) ");
        sb.append(" AND year(h.shpdate) = ${y} AND month(h.shpdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" AND h.shpdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" AND h.shpdate= '${d}' ");
                break;
            default:
                sb.append(" AND h.shpdate<= '${d}' ");
        }
        String sqlstr = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${facno}", facno).replace("${decode}", decode);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sqlstr);
        try {
            Object o = query.getSingleResult();
            return (BigDecimal) o;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getARM270Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno").toString();
        String deptno = map.get("deptno").toString();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ISNULL(SUM(h.shpamt),0) FROM armbil h WHERE h.rkd='RQ11' AND h.facno='${facno}' AND h.depno IN (${deptno}) ");
        sb.append(" AND year(h.bildat) = ${y} AND month(h.bildat)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" AND h.bildat<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" AND h.bildat= '${d}' ");
                break;
            default:
                sb.append(" AND h.bildat<= '${d}' ");
        }
        String sqlstr = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${facno}", facno).replace("${deptno}", deptno);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sqlstr);
        try {
            Object o = query.getSingleResult();
            return (BigDecimal) o;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getARM423Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno").toString();
        String n_code_DA = map.get("n_code_DA").toString();
        String ogdkid = map.get("ogdkid").toString();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ISNULL(SUM(d.recamt),0) FROM armrec d,armrech h where d.facno=h.facno AND d.recno=h.recno AND h.prgno='ARM423' AND h.recstat='1' AND d.raccno='6001' ");
        sb.append(" AND h.facno='${facno}' AND h.ogdkid='${ogdkid}' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" AND h.hmark1 ").append(n_code_DA);
        }
        sb.append(" AND year(h.recdate) = ${y} and month(h.recdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" AND h.recdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" AND h.recdate= '${d}' ");
                break;
            default:
                sb.append(" AND h.recdate<= '${d}' ");
        }
        String sqlstr = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${facno}", facno).replace("${ogdkid}", ogdkid);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sqlstr);
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

}
