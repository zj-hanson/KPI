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
 * @author C1749
 */
public class TrialRunAdverseAA extends TrialRun {

    public TrialRunAdverseAA() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String typecode = map.get("typecode") != null ? map.get("typecode").toString() : "";//机型
        StringBuilder sb = new StringBuilder();
        BigDecimal reslut = BigDecimal.ZERO;
        sb.append(" select count(*) from ( ");
        sb.append(" SELECT DISTINCT PRODUCTCOMPID FROM PROCESS_TR_JZ A WHERE 1=1 AND  TRRESULT = '不合格' AND TR_TIMES='1' AND A.STEPID LIKE '%机组试车站%' ");
        sb.append(" AND year(A.MODIFYTIME)=${y} and month(A.MODIFYTIME)=${m} ");
        sb.append(" ) as a ");
        String badnum = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        sb.setLength(0);

        sb.append(" select count(*) from ( ");
        sb.append(" SELECT A.PRODUCTORDERID, A.PRODUCTCOMPID, A.STEPID,B.PRODUCTMODEL,C.PRODUCTORDERTYPE, MIN(A.STEPSEQ),TRACKOUTTIME ");
        sb.append(" FROM PROCESS_STEP A INNER JOIN MPRODUCT B ON A.PRODUCTID=B.PRODUCTID ");
        sb.append(" INNER JOIN PROCESS_PRE C ON A.PRODUCTORDERID=C.PRODUCTORDERID ");
        sb.append(" WHERE A.STEPID='机组试车站'  ");
        sb.append(" AND year(A.TRACKOUTTIME)=${y} and month(A.TRACKOUTTIME)=${m} ");
        sb.append(" GROUP BY A.PRODUCTORDERID, A.PRODUCTCOMPID, A.STEPID,B.PRODUCTMODEL,C.PRODUCTORDERTYPE,TRACKOUTTIME ");
        sb.append(" ) as a ");
        String allnum = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query1 = superEJB.getEntityManager().createNativeQuery(badnum);//不良数
        Query query2 = superEJB.getEntityManager().createNativeQuery(allnum);//总试车数
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            BigDecimal value1 = BigDecimal.valueOf(Double.valueOf(o1.toString()));
            BigDecimal value2 = BigDecimal.valueOf(Double.valueOf(o2.toString()));
            if(value2.compareTo(BigDecimal.ZERO)>0){
               reslut = value1.divide(value2, 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
               return reslut;
            }
        } catch (Exception ex) {
            Logger.getLogger(TrialRunAdverseAA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

}
