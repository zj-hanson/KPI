/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.persistence.Query;

/**
 *
 * @author C1749 空压机体试车MES数据集合不良数
 */
public class QRATrialRunAdverseAH1 extends QRA {

    public QRATrialRunAdverseAH1() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        Double totalNum = null;
        Double quaNum = null;
        BigDecimal result = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        //MES总的试车数
        sb.append(" select count(1) as num  from ( ");
        sb.append(" select DISTINCT PRODUCTCOMPID from ( ");
        sb.append(" SELECT DISTINCT A.PRODUCTORDERID,A.PRODUCTCOMPID, A.STEPID,B.PRODUCTMODEL,A.PRODUCTID,C.PRODUCTORDERTYPE, MIN(A.STEPSEQ) as STEPSEQ,D.TRRESULT,A.MODIFYTIME ");
        sb.append(" FROM PROCESS_STEP A INNER JOIN MPRODUCT B ON A.PRODUCTID=B.PRODUCTID ");
        sb.append(" INNER JOIN PROCESS_PRE C ON A.PRODUCTORDERID=C.PRODUCTORDERID ");
        sb.append(" INNER JOIN  PROCESS_TR D on A.PRODUCTORDERID=D.PRODUCTORDERID ");
        sb.append(" WHERE 1=1 AND A.STEPID='机体试车站'  ");
        sb.append(" AND C.PRODUCTORDERTYPE='一般制令' and D.TR_TIMES = '1' ");
        sb.append(" AND D.PRODUCTID not like '%GB%' ");
        sb.append(" AND year(A.MODIFYTIME) = ${y} and month(dateadd(HOUR,-8,A.MODIFYTIME))=${m} ");
        sb.append(" GROUP BY A.PRODUCTORDERID, A.PRODUCTCOMPID, A.STEPID,B.PRODUCTMODEL,A.PRODUCTID,C.PRODUCTORDERTYPE,D.TRRESULT,A.MODIFYTIME,A.MODIFYTIME ");
        sb.append(" ) as a ");
        sb.append(" )as b ");
        String totalSql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        sb.setLength(0);
        //MES不良试车数
        sb.append(" SELECT  count(1)  FROM  PROCESS_TR A INNER JOIN MPRODUCT B ON A.PRODUCTID=B.PRODUCTID WHERE 1=1 ");
        sb.append(" and A.PRODUCTID not like '%GB%' and A.TR_TIMES='1' AND A.STEPID LIKE '%机体试车站%'  AND  A.TRRESULT='不合格' ");
        sb.append(" AND year(A.MODIFYTIME)=${y} and month(dateadd(hour,-8,A.MODIFYTIME))=${m} ");
        String quaSql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query1 = superEJBForMES.getEntityManager().createNativeQuery(totalSql);
        Query query2 = superEJBForMES.getEntityManager().createNativeQuery(quaSql);
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
