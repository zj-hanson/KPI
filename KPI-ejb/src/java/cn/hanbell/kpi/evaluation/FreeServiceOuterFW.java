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
 * @author C1879
 * 厂外服务成本服务领退料
 */
public class FreeServiceOuterFW extends FreeServiceERP{

    public FreeServiceOuterFW() {
        super();
    }
    
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String hmark1 = map.get("hmark1") != null ? map.get("hmark1").toString() : "";
        String hmark2 = map.get("hmark2") != null ? map.get("hmark2").toString() : "";

        //领料INV310
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
    
       
}
