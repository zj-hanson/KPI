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
 * @author C1749
 */
public class TrialRunAdverseAHShipA2 extends TrialRun {

    public TrialRunAdverseAHShipA2() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String typecode = map.get("typecode") != null ? map.get("typecode").toString() : "";//机型
        StringBuilder sb = new StringBuilder();
        BigDecimal reslut1 = BigDecimal.ZERO;//不合格的
        sb.append(" select sum(a.NUM) from ( ");
        sb.append(" SELECT SUBSTRING( convert(varchar(10),dateadd(hour,-8,A.MODIFYTIME),111),6,5)  DAY , B.PRODUCTMODEL, COUNT(*) NUM ");
        sb.append(" FROM  PROCESS_TR A INNER JOIN MPRODUCT B ON A.PRODUCTID=B.PRODUCTID WHERE 1=1 ");
        sb.append(" AND year(A.MODIFYTIME) = ${y} AND month(A.MODIFYTIME)= ${m} ");
        sb.append(" AND A.STEPID LIKE '%机体试车站%' ");
        if (!typecode.equals("") && typecode.equals("480")) {
            sb.append(" and  (B.PRODUCTMODEL like 'AB-077%' or B.PRODUCTMODEL like 'AB-130%' or B.PRODUCTMODEL like 'AB-240%' or B.PRODUCTMODEL like 'AB-420%' or B.PRODUCTMODEL like 'AB-480R%') ");
        } else if (!typecode.equals("") && typecode.equals("600")) {
            sb.append(" and (B.PRODUCTMODEL like 'AB-600R%' or B.PRODUCTMODEL like 'AAB-780R%') ");
        } else if (!typecode.equals("") && typecode.equals("1030")) {
            sb.append(" and (B.PRODUCTMODEL like 'AB-1030R%' or B.PRODUCTMODEL like 'AB-1200R%') ");
        } else {
            sb.append(" and (B.PRODUCTMODEL like 'AB-1320%' or B.PRODUCTMODEL like 'AB-1560%' or B.PRODUCTMODEL like 'AB-1900R%' or B.PRODUCTMODEL like 'AB-2600%') ");
        }
        sb.append(" GROUP BY SUBSTRING( convert(varchar(10),dateadd(hour,-8,A.MODIFYTIME),111),6,5)  ,B.PRODUCTMODEL ");
        sb.append(" ) as a ");
        String num = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        Query query1 = superEJB.getEntityManager().createNativeQuery(num);
        try {
            Object o1 = query1.getSingleResult();
            Double num1 = Double.valueOf(o1.toString());
            reslut1 = (BigDecimal.valueOf(num1));
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reslut1;
    }

}
