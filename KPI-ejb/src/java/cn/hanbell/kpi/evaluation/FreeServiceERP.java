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

    protected BigDecimal inv310ks = BigDecimal.ZERO;
    protected BigDecimal man410and510 = BigDecimal.ZERO;

    public FreeServiceERP() {
        queryParams = new LinkedHashMap<>();
    }

    // 服务成本（厂外）——质量扣款折让金额,关联4个分类
    public BigDecimal getARM423Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String ogdkid = map.get("ogdkid") != null ? map.get("ogdkid").toString() : "";
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
//        switch (type) {
//            case 2:
//                sb.append(" AND h.recdate<= '${d}' ");
//                break;
//            case 5:
//                sb.append(" AND h.recdate= '${d}' ");
//                break;
//            default:
//                sb.append(" AND h.recdate<= '${d}' ");
//        }
        String sqlstr = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno).replace("${ogdkid}", ogdkid);
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

    // 服务成本（厂外）——服务领退料
    public BigDecimal getINV310outer(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        //区域别
        String hmark1 = map.get("hmark1") != null ? map.get("hmark1").toString() : "";
        //产品别
        String hmark2 = map.get("hmark2") != null ? map.get("hmark2").toString() : "";

        //领料
        BigDecimal iaf = BigDecimal.ZERO;
        //退料
        BigDecimal iag = BigDecimal.ZERO;
        //借出无法归还部分
        BigDecimal z08 = BigDecimal.ZERO;

        //领料SQL
        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(t.tramt),0) FROM invtrnh t WHERE   facno ='${facno}' and  t.trtype='IAF' AND t.rescode IN ('1001','1013') ");
        if (!"".equals(hmark1)) {
            sb.append(" and t.hmark1 ").append(hmark1);
        }
        if (!"".equals(hmark2)) {
            sb.append(" and t.hmark2 ").append(hmark2);
        }
        sb.append(" AND year(t.trdate) = ${y} and month(t.trdate)= ${m} ");
//        switch (type) {
//            case 2:
//                sb.append(" AND t.trdate<= '${d}' ");
//                break;
//            case 5:
//                sb.append(" AND t.trdate= '${d}' ");
//                break;
//            default:
//                sb.append(" AND t.trdate<= '${d}' ");
//        }
        String iafsql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        sb.setLength(0);
        //退料SQL
        sb.append(" select isnull(-sum(t.tramt),0) FROM invtrnh t WHERE   facno ='${facno}' and  t.trtype='IAG' AND t.rescode IN ('1002','1014') ");
        if (!"".equals(hmark1)) {
            sb.append(" and t.hmark1 ").append(hmark1);
        }
        if (!"".equals(hmark2)) {
            sb.append(" and t.hmark2 ").append(hmark2);
        }
        sb.append(" AND year(t.trdate) = ${y} and month(t.trdate)= ${m} ");
//        switch (type) {
//            case 2:
//                sb.append(" AND t.trdate<= '${d}' ");
//                break;
//            case 5:
//                sb.append(" AND t.trdate= '${d}' ");
//                break;
//            default:
//                sb.append(" AND t.trdate<= '${d}' ");
//        }
        String iagsql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        sb.setLength(0);
        //借出无法归还部分SQL
        sb.append(" select isnull(sum(t.tramt),0) FROM invtrnh t WHERE   facno ='${facno}' and  t.trtype='IAB'  AND t.rescode IN ('Z08') ");
        if (!"".equals(hmark1)) {
            sb.append(" and t.hmark1 ").append(hmark1);
        }
        if (!"".equals(hmark2)) {
            sb.append(" and t.hmark2 ").append(hmark2);
        }
        sb.append(" AND year(t.trdate) = ${y} and month(t.trdate)= ${m} ");
//        switch (type) {
//            case 2:
//                sb.append(" AND t.trdate<= '${d}' ");
//                break;
//            case 5:
//                sb.append(" AND t.trdate= '${d}' ");
//                break;
//            default:
//                sb.append(" AND t.trdate<= '${d}' ");
//        }
        String z08sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(iafsql);
        Query query2 = superEJB.getEntityManager().createNativeQuery(iagsql);
        Query query3 = superEJB.getEntityManager().createNativeQuery(z08sql);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            Object o3 = query3.getSingleResult();
            iaf = (BigDecimal) o1;
            iag = (BigDecimal) o2;
            z08 = (BigDecimal) o3;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return iaf.add(iag).add(z08);
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
//        switch (type) {
//            case 2:
//                sb.append(" AND h.cfmdate<= '${d}' ");
//                break;
//            case 5:
//                sb.append(" AND h.cfmdate= '${d}' ");
//                break;
//            default:
//                sb.append(" AND h.cfmdate<= '${d}' ");
//        }
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

    // 维修成本（厂内） 客诉维修领退料成本、整机销账领退料成本
    public BigDecimal getINV310KS(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        //区域别
        String hmark1 = map.get("hmark1") != null ? map.get("hmark1").toString() : "";
        //产品别
        String hmark2 = map.get("hmark2") != null ? map.get("hmark2").toString() : "";

        //领料
        BigDecimal iaf = BigDecimal.ZERO;
        //退料
        BigDecimal iag = BigDecimal.ZERO;

        //领料SQL
        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(t.tramt),0) FROM invtrnh t WHERE  t.trtype='IAF' AND t.rescode IN ('1003','0003') and  facno ='${facno}' ");
        if (!"".equals(hmark1)) {
            sb.append(" and t.hmark1 ").append(hmark1);
        }
        if (!"".equals(hmark2)) {
            sb.append(" and t.hmark2 ").append(hmark2);
        }
        sb.append(" AND year(t.trdate) = ${y} and month(t.trdate)= ${m} ");
//        switch (type) {
//            case 2:
//                sb.append(" AND t.trdate<= '${d}' ");
//                break;
//            case 5:
//                sb.append(" AND t.trdate= '${d}' ");
//                break;
//            default:
//                sb.append(" AND t.trdate<= '${d}' ");
//        }
        String iafsql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        sb.setLength(0);
        //退料SQL
        sb.append(" select isnull(-sum(t.tramt),0) FROM invtrnh t WHERE t.trtype='IAG' AND t.rescode IN ('1004','0003')  and facno ='${facno}'  ");
        if (!"".equals(hmark1)) {
            sb.append(" and t.hmark1 ").append(hmark1);
        }
        if (!"".equals(hmark2)) {
            sb.append(" and t.hmark2 ").append(hmark2);
        }
        sb.append(" AND year(t.trdate) = ${y} and month(t.trdate)= ${m} ");
//        switch (type) {
//            case 2:
//                sb.append(" AND t.trdate<= '${d}' ");
//                break;
//            case 5:
//                sb.append(" AND t.trdate= '${d}' ");
//                break;
//            default:
//                sb.append(" AND t.trdate<= '${d}' ");
//        }
        String iagsql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(iafsql);
        Query query2 = superEJB.getEntityManager().createNativeQuery(iagsql);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            iaf = (BigDecimal) o1;
            iag = (BigDecimal) o2;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return iaf.add(iag);
    }

    //维修成本（厂内）制令领退料 每次更新为每月4号更新上月数据
    public BigDecimal getMAN410and510(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        //产品类别
        String remark1 = map.get("remark1") != null ? map.get("remark1").toString() : "";
        //领料
        BigDecimal man430 = BigDecimal.ZERO;
        //退料
        BigDecimal man530 = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT isnull(sum(cost),0) FROM ( ");
        sb.append(" SELECT  a.facno,a.prono,a.pisno,a.typecode,a.kfdh,sum(ttmatm) as cost,a.remark1 FROM ");
        sb.append(" (SELECT manpih.facno,manpih.prono,manpih.pisno,manpih.trtype,manpih.iocode,manpih.reascode,manpih.issdepno,manpih.itnbrf ");
        sb.append(" ,manpid.seqnr,manmas.typecode,manmas.kfdh,manmas.remark1 FROM manpid left outer join invmas s on s.itnbr=manpid.altitnbr, ");
        sb.append(" manpih left outer join invmas t on t.itnbr=manpih.itnbrf left outer join manmas on manmas.facno=manpih.facno and  manmas.prono=manpih.prono and manmas.manno=manpih.manno ");
        sb.append(" where  (manpih.facno= manpid.facno) and (manpih.prono = manpid.prono) and (manpih.pisno = manpid.pisno)   AND (manpih.issstatus = 'C') ");
        sb.append(" and manpid.altitnbr <> '3188-GBR6254-FW' and manpih.facno = '${facno}' and manpih.prono='1'  and manpih.issdepno = '9900' ");
        sb.append(" and manpid.wareh not in ('EKF03','EKF01','EZK01','EZK03','EAKF03') and manmas.typecode='02' ");
        sb.append("");
        sb.append("");
        sb.append(" AND manpih.issstatus = 'C' and manpih.facno = '${facno}' and manpih.prono='1'  and manpih.issdepno = '9900' ");
        sb.append(" and manpid.altitnbr<>'3188-GBR6254-FW' and manpid.wareh not in ('EKF03','EKF01','EZK01','EZK03','EAKF03') and manmas.typecode='02' ");
        if (!"".equals(remark1)) {
            sb.append(" AND  manmas.remark1 ").append(remark1);
        }
        sb.append(" and year(manpih.issdate) = ${y} and month(manpih.issdate)= ${m} ");
        sb.append(" ) as a LEFT JOIN invtrnh b on a.facno=b.facno and a.trtype=b.trtype and a.pisno=b.trno and a.seqnr=b.trseq ");
        sb.append(" WHERE b.facno='${facno}' AND b.prono='1' GROUP BY  a.facno,a.prono,a.pisno,a.typecode,a.kfdh,a.remark1) as c ");
        String sqlman430 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        sb.setLength(0);
        sb.append(" SELECT isnull(-sum(cost),0) FROM ( ");
        sb.append(" SELECT a.facno,a.prono,a.manno,a.typecode,a.kfdh,sum(ttmatm) as cost,a.remark1 FROM ");
        sb.append(" (SELECT manreh.facno, manreh.prono,manreh.manno,manreh.itnbrf,manreh.retdepno, manreh.trtype,manreh.retno,manreh.iocode,manred.seqnr, ");
        sb.append(" manmas.typecode,manmas.kfdh ,manmas.remark1 FROM  manred left outer join invmas s on s.itnbr=manred.altitnbr, ");
        sb.append(" manreh left outer join invmas t on t.itnbr=manreh.itnbrf ");
        sb.append(" left outer join manmas on manmas.facno=manreh.facno and  manmas.prono=manreh.prono and manmas.manno=manreh.manno ");
        sb.append(" WHERE ( manreh.facno = manred.facno ) and( manreh.prono = manred.prono ) and( manreh.retno = manred.retno ) and  ( manreh.issstatus = 'C') ");
        sb.append(" and manred.altitnbr <>'3188-GBR6254-FW' and manreh.facno='${facno}' and manreh.prono='1' and manreh.retdepno='9900' ");
        sb.append(" and manred.wareh not in ('EKF03','EKF01','EZK01','EZK03','EAKF03') and manmas.typecode='02' ");
        if (!"".equals(remark1)) {
            sb.append(" AND  manmas.remark1 ").append(remark1);
        }
        sb.append(" and year(manreh.retdate) = ${y} and month(manreh.retdate)= ${m} ");
        sb.append(" )as a left join invtrnh b on a.facno=b.facno and a.trtype=b.trtype and a.retno=b.trno and a.seqnr=b.trseq ");
        sb.append("  WHERE b.facno='${facno}' AND b.prono='1' group by a.facno,a.prono,a.manno,a.typecode,a.kfdh,a.remark1) as c ");
        String sqlman530 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(sqlman430);
        Query query2 = superEJB.getEntityManager().createNativeQuery(sqlman530);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            man430 = (BigDecimal) o1;
            man530 = (BigDecimal) o2;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return man430.add(man530);
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

}
