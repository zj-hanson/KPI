/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
public class TrialRunAdverseAH extends TrialRun {

    public TrialRunAdverseAH() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String typecode = map.get("typecode") != null ? map.get("typecode").toString() : "";//机型
        StringBuilder sb = new StringBuilder();
        BigDecimal reslut1 = BigDecimal.ZERO;
        sb.append(" select sum(a.NUM) from ( ");
        sb.append(" SELECT SUBSTRING( convert(varchar(10),dateadd(hour,-8,A.MODIFYTIME),111),6,5)  DAY , B.PRODUCTMODEL, COUNT(*) NUM ");
        sb.append(" FROM  PROCESS_TR A INNER JOIN MPRODUCT B ON A.PRODUCTID=B.PRODUCTID WHERE 1=1 ");
        sb.append(" AND year(A.MODIFYTIME) = ${y} AND month(A.MODIFYTIME)= ${m} ");
        sb.append(" AND A.STEPID LIKE '%机体试车站%' ");
        sb.append(" GROUP BY SUBSTRING( convert(varchar(10),dateadd(hour,-8,A.MODIFYTIME),111),6,5)  ,B.PRODUCTMODEL ");
        sb.append(" ) as a ");
        String num = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        sb.setLength(0);

        sb.append(" select sum(a.DEFECTNUM) from ( ");
        sb.append(" SELECT SUBSTRING( convert(varchar(10),dateadd(hour,-8,A.MODIFYTIME),111),6,5)  DAY , B.PRODUCTMODEL,COUNT(*) DEFECTNUM ");
        sb.append(" FROM  PROCESS_TR A INNER JOIN MPRODUCT B ON A.PRODUCTID=B.PRODUCTID WHERE 1=1 ");
        sb.append(" AND year(A.MODIFYTIME) = ${y} AND month(A.MODIFYTIME)= ${m} ");
        sb.append(" AND A.STEPID LIKE '%机体试车站%' ");
        sb.append(" AND A.TRRESULT='不合格' ");
        sb.append(" GROUP BY SUBSTRING( convert(varchar(10),dateadd(hour,-8,A.MODIFYTIME),111),6,5)  ,B.PRODUCTMODEL ");
        sb.append(" )as a ");
        String DEFECTNUM = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        Query query1 = superEJB.getEntityManager().createNativeQuery(DEFECTNUM);
        Query query2 = superEJB.getEntityManager().createNativeQuery(num);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            Double value1 = Double.valueOf(o1.toString());
            Double value2 = Double.valueOf(o2.toString());
            reslut1 = BigDecimal.valueOf(value1 / value2).divide(BigDecimal.ONE, 4, RoundingMode.HALF_UP);
            
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reslut1;
    }

}
