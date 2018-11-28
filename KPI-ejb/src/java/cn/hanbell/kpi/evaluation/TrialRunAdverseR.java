/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.SuperEJBForMES;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
 * @author C1749
 */
public class TrialRunAdverseR extends TrialRun {
    
    SuperEJBForMES superEJBForMES = lookupSuperEJBForMESBean();
    
    SuperEJBForMES lookupSuperEJBForMESBean() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForMES) c.lookup("java:global/KPI/KPI-ejb/SuperEJBForMES!cn.hanbell.kpi.comm.SuperEJBForMES");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String STEPID = map.get("STEPID") != null ? map.get("STEPID").toString() : "";//试车站
        String typecode = map.get("typecode") != null ? map.get("typecode").toString() : "";//机型
        BigDecimal divisor1 = BigDecimal.ZERO;
        BigDecimal divisor2 = BigDecimal.ZERO;
        BigDecimal result = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT sum(convert(int,A.TR_TIMES)) FROM  PROCESS_TR A INNER JOIN MPRODUCT B ON A.PRODUCTID=B.PRODUCTID WHERE 1=1 ");
        sb.append(" AND A.TRRESULT='不合格' ");
        if (!"".equals(STEPID)) {
            sb.append(" AND A.STEPID LIKE '%").append(STEPID).append("%'");
        }
        if (!"".equals(typecode) && "RC2".equals(typecode)) {
            sb.append(" AND (A.PRODUCTID not like '31342-%' or A.PRODUCTID  not like '31343-%' or A.PRODUCTID not like '31344-%' ");
            sb.append(" or A.PRODUCTID not like '31363-%' or A.PRODUCTID not like '31366-%' or A.PRODUCTID not like '31367-%' ) ");
            sb.append(" AND (B.PRODUCTMODEL not like 'LB%' or  B.PRODUCTMODEL not like 'LT%')");
        } else if (!"".equals(typecode) && "LB".equals(typecode)) {
            sb.append(" AND (B.PRODUCTMODEL  like 'LB%' or  B.PRODUCTMODEL  like 'LT%')");
        } else if (!"".equals(typecode) && "RCD".equals(typecode)) {
            sb.append(" AND (A.PRODUCTID  like '31342-%' or A.PRODUCTID   like '31343-%' or A.PRODUCTID  like '31344-%' ");
            sb.append(" or A.PRODUCTID  like '31363-%' or A.PRODUCTID  like '31366-%' or A.PRODUCTID  like '31367-%' ) ");
            sb.append(" AND (B.PRODUCTMODEL not like 'LB%' or  B.PRODUCTMODEL not like 'LT%')");
        }
        sb.append(" AND year(A.MODIFYTIME) = ${y} AND month(A.MODIFYTIME)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and A.MODIFYTIME<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and A.MODIFYTIME= '${d}' ");
                break;
            default:
                sb.append(" and A.MODIFYTIME<= '${d}' ");
        }
        String sqlDivisor1 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        sb.setLength(0);
        
        sb.append(" SELECT sum(convert(int,A.TR_TIMES)) FROM  PROCESS_TR A INNER JOIN MPRODUCT B ON A.PRODUCTID=B.PRODUCTID WHERE 1=1 ");
        //sb.append(" AND A.TRRESULT='不合格' ");
        if (!"".equals(STEPID)) {
            sb.append(" AND A.STEPID LIKE '%").append(STEPID).append("%'");
        }
        if (!"".equals(typecode) && "RC2".equals(typecode)) {
            sb.append(" AND (A.PRODUCTID not like '31342-%' or A.PRODUCTID  not like '31343-%' or A.PRODUCTID not like '31344-%' ");
            sb.append(" or A.PRODUCTID not like '31363-%' or A.PRODUCTID not like '31366-%' or A.PRODUCTID not like '31367-%' ) ");
            sb.append(" AND (B.PRODUCTMODEL not like 'LB%' or  B.PRODUCTMODEL not like 'LT%')");
        } else if (!"".equals(typecode) && "LB".equals(typecode)) {
            sb.append(" AND (B.PRODUCTMODEL  like 'LB%' or  B.PRODUCTMODEL  like 'LT%')");
        } else if (!"".equals(typecode) && "RCD".equals(typecode)) {
            sb.append(" AND (A.PRODUCTID  like '31342-%' or A.PRODUCTID   like '31343-%' or A.PRODUCTID  like '31344-%' ");
            sb.append(" or A.PRODUCTID  like '31363-%' or A.PRODUCTID  like '31366-%' or A.PRODUCTID  like '31367-%' ) ");
            sb.append(" AND (B.PRODUCTMODEL not like 'LB%' or  B.PRODUCTMODEL not like 'LT%')");
        }
        sb.append(" AND year(A.MODIFYTIME) = ${y} AND month(A.MODIFYTIME)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and A.MODIFYTIME<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and A.MODIFYTIME= '${d}' ");
                break;
            default:
                sb.append(" and A.MODIFYTIME<= '${d}' ");
        }
        String sqlDivisor2 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        
        Query query1 = superEJBForMES.getEntityManager().createNativeQuery(sqlDivisor1);
        Query query2 = superEJBForMES.getEntityManager().createNativeQuery(sqlDivisor2);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            Double value1 = Double.valueOf(o1.toString());
            Double value2 = Double.valueOf(o2.toString());
            result = BigDecimal.valueOf(value1 / value2).divide(BigDecimal.ONE, 4, RoundingMode.HALF_UP);
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
}
