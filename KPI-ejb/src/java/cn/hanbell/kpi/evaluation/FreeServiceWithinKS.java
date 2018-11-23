/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1879 
 * 维修成本（厂内） 客诉维修领退料成本、整机销账领退料成本
 */
public class FreeServiceWithinKS extends FreeServiceERP {

    public FreeServiceWithinKS() {
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
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

}
