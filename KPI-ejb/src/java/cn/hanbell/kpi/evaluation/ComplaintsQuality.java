/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
public class ComplaintsQuality extends Complaints {

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";
        BigDecimal avgShip = null;
        BigDecimal num1 = null;
        BigDecimal num2 = null;
        StringBuilder sb = new StringBuilder();
        sb.append("select isnull(sum(d.shpqy1),0) from cdrhad h,cdrdta d where h.facno=d.facno and h.shpno=d.shpno  and h.houtsta<>'W' and left(d.itnbr,1)='3' ");
        sb.append(" and h.facno='${facno}' ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.YEAR, -1);
        Date pastYear = calendar.getTime();
        sb.append(" and h.shpdate BETWEEN  '");
        sb.append(BaseLib.formatDate("yyyyMMdd", pastYear));
        sb.append("'  and  '");
        sb.append( BaseLib.formatDate("yyyyMMdd", d));
        sb.append("'  ");
        String cdrdta = sb.toString().replace("${facno}", facno);

        sb.setLength(0);
        sb.append("select isnull(sum(d.bshpqy1),0) from cdrbhad h,cdrbdta d where h.facno=d.facno and h.bakno=d.bakno and h.baksta<>'W'  and left(d.itnbr,1)='3' ");
        sb.append(" and h.facno='${facno}' ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
          sb.append(" and h.bakdate BETWEEN  '");
        sb.append(BaseLib.formatDate("yyyyMMdd", pastYear));
        sb.append("'  and  '");
        sb.append( BaseLib.formatDate("yyyyMMdd", d));
        sb.append("'  ");
        String cdrbdta = sb.toString().replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(cdrdta);
        Query query2 = superEJB.getEntityManager().createNativeQuery(cdrbdta);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            
            num1 = (BigDecimal) o1;
            num2 = (BigDecimal) o2;
            avgShip = num1.subtract(num2);
            return avgShip.divide(BigDecimal.valueOf(12),1, RoundingMode.HALF_UP);
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return avgShip.ZERO;
        
    }

}
