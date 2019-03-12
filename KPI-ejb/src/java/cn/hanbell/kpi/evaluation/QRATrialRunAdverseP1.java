/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.SuperEJBForMES;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;

/**
 *
 * @author C1749 真是试车合格率
 */
public class QRATrialRunAdverseP1 extends QRAConnMES {

    SuperEJBForMES superMES = lookupSuperEJBForMESBean();

    private SuperEJBForMES lookupSuperEJBForMESBean() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForMES) c.lookup("java:global/KPI/KPI-ejb/SuperEJBForMES!cn.hanbell.kpi.comm.SuperEJBForMES");
        } catch (NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public QRATrialRunAdverseP1() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String STEPID = map.get("STEPID") != null ? map.get("STEPID").toString() : "";
        StringBuilder sb = new StringBuilder();
        Double totalNum = null;
        Double quaNum = null;
        BigDecimal result = BigDecimal.ZERO;
        //MES总的试车数
        sb.append(" select count(1) as num  from ( ");
        sb.append(" select DISTINCT PRODUCTCOMPID from ( ");
        sb.append(" SELECT DISTINCT A.PRODUCTORDERID,A.PRODUCTCOMPID, A.STEPID,B.PRODUCTMODEL,A.PRODUCTID,C.PRODUCTORDERTYPE, MIN(A.STEPSEQ) as STEPSEQ,D.TRRESULT,A.MODIFYTIME ");
        sb.append(" FROM PROCESS_STEP A INNER JOIN MPRODUCT B ON A.PRODUCTID=B.PRODUCTID ");
        sb.append(" INNER JOIN PROCESS_PRE C ON A.PRODUCTORDERID=C.PRODUCTORDERID ");
        sb.append(" INNER JOIN  PROCESS_TR D on A.PRODUCTORDERID=D.PRODUCTORDERID ");
        sb.append(" WHERE 1=1 ");
        if (!"".equals(STEPID)) {
            if ("P".equals(STEPID)) {
                sb.append(" AND (A.STEPID like  '%P%') ");
            }
            if ("干泵".equals(STEPID)) {
                sb.append(" AND (A.STEPID like  '%P机体试车站%') ");
            }
            if ("油泵".equals(STEPID)) {
                sb.append(" AND (A.STEPID like  '%P机组试车站%') ");
            }
            if ("服务机".equals(STEPID)) {
                sb.append(" AND (A.STEPID like  '%P机组试车站%') ");
            }
        }

        sb.append(" AND C.PRODUCTORDERTYPE='一般制令' and D.TR_TIMES = '1' ");
        sb.append(" AND D.PRODUCTID not like '%GB%' ");
        sb.append(" AND year(A.MODIFYTIME) = ${y} and month(dateadd(HOUR,-8,A.MODIFYTIME))=${m} ");
        sb.append(" GROUP BY A.PRODUCTORDERID, A.PRODUCTCOMPID, A.STEPID,B.PRODUCTMODEL,A.PRODUCTID,C.PRODUCTORDERTYPE,D.TRRESULT,A.MODIFYTIME,A.MODIFYTIME ");
        sb.append(" ) as a ");
        sb.append(" )as b ");
        String totalSql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        sb.setLength(0);
        //MES不良试车数
        sb.append(" SELECT count(1)  FROM PROCESS_TR A  LEFT JOIN MPRODUCT B on A.PRODUCTID=B.PRODUCTID ");
        sb.append(" where 1=1  ");
        if (!"".equals(STEPID)) {
            if ("P".equals(STEPID)) {
                sb.append(" AND (A.STEPID like  '%P%') ");
            }
            if ("干泵".equals(STEPID)) {
                sb.append(" AND (A.STEPID like  '%P机体试车站%') ");
            }
            if ("油泵".equals(STEPID)) {
                sb.append(" AND (A.STEPID like  '%P机组试车站%') ");
            }
            if ("服务机".equals(STEPID)) {
                sb.append(" AND (A.STEPID like  '%P机组试车站%') ");
            }
        }
        sb.append(" AND A.PRODUCTCOMPID not like '%GB%' AND A.TR_TIMES = '1'  AND A.TRRESULT='不合格' ");
        sb.append(" AND year(A.MODIFYTIME)=${y} AND month(dateadd(HOUR,-8,A.MODIFYTIME))=${m} ");
        String quaSql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query1 = superMES.getEntityManager().createNativeQuery(totalSql);
        Query query2 = superMES.getEntityManager().createNativeQuery(quaSql);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            totalNum = Double.valueOf(o1.toString());
            quaNum = Double.valueOf(o2.toString());
            if (totalNum != 0.0) {
                Double quaResult = quaNum / totalNum;
                result = BigDecimal.valueOf(quaResult);
            }
        } catch (Exception ex) {
            log4j.error("QRATrialRunAdverseR1", ex);
        }
        result = BigDecimal.ONE.subtract(result).setScale(5, BigDecimal.ROUND_HALF_UP);
        return result;
    }

}
