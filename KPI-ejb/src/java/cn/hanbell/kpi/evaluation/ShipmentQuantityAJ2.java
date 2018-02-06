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
 * @author C0160
 */
public class ShipmentQuantityAJ2 extends ShipmentAJ {

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal shpqy1 = BigDecimal.ZERO;
        BigDecimal bshpqy1 = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append("select isnull(sum(d.shpqy1),0) from cdrhad h,cdrdta d where h.facno=d.facno and h.shpno=d.shpno and h.facno='C' and h.houtsta<>'W' ");
        sb.append(" and d.n_code_DA='AH' and d.n_code_CD='WX' and d.n_code_DC like 'AJ%' and d.n_code_DD='00' ");
        sb.append(" and year(h.shpdate) = ${y} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and month(h.shpdate)= ${m} and h.shpdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and month(h.shpdate)= ${m} and h.shpdate= '${d}' ");
                break;
            default:
                sb.append(" and month(h.shpdate)= ${m} and h.shpdate<= '${d}' ");
        }
        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));

        sb.setLength(0);
        sb.append("select isnull(sum(d.bshpqy1),0) from cdrbhad h,cdrbdta d where h.facno=d.facno and h.bakno=d.bakno and h.facno='C' and h.baksta<>'W' ");
        sb.append(" and d.n_code_DA='AH' and d.n_code_CD='WX' and d.n_code_DC like 'AJ%' and d.n_code_DD='00' ");
        sb.append(" and year(h.bakdate) = ${y} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and month(h.bakdate)= ${m} and h.bakdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and month(h.bakdate)= ${m} and h.bakdate= '${d}' ");
                break;
            default:
                sb.append(" and month(h.bakdate)= ${m} and h.bakdate<= '${d}' ");
        }
        String cdrbdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));

        superEJB.setCompany("C");
        Query query1 = superEJB.getEntityManager().createNativeQuery(cdrdta);
        Query query2 = superEJB.getEntityManager().createNativeQuery(cdrbdta);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            shpqy1 = (BigDecimal) o1;
            bshpqy1 = (BigDecimal) o2;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shpqy1.subtract(bshpqy1);
    }

}
