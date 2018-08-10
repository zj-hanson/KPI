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
 */

public class ProcessQuantity extends  Process{

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String prono = map.get("prono") != null ? map.get("prono").toString() : "";
        String linecode = map.get("linecode") != null ? map.get("linecode").toString() : "";
        String stats = map.get("stats") != null ? map.get("stats").toString() : ""; 
        
        BigDecimal attqty1 = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(sfcwad.attqty1),0) FROM invmas,manmas,sfcwad,sfcwah ");
        sb.append(" WHERE ( sfcwah.facno = sfcwad.facno ) and ( sfcwah.prono = sfcwad.prono ) and ( sfcwah.inpno = sfcwad.inpno ) ");
        sb.append(" AND ( sfcwad.manno = manmas.manno ) and ( sfcwad.facno = manmas.facno ) and ( sfcwad.prono = manmas.prono ) ");
        sb.append(" AND ( invmas.itnbr = manmas.itnbrf ) AND ( sfcwah.facno = '${facno}' and sfcwah.prono ='${prono}' ");
        if (!"".equals(linecode)) {
            sb.append(" and manmas.linecode ").append(linecode);
        }
        if (!"".equals(stats)) {
            sb.append(" and sfcwad.stats ").append(stats);
        }

        sb.append(" AND year(sfcwah.indat) = ${y} and month(sfcwah.indat)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and sfcwah.indat<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and sfcwah.indat= '${d}' ");
                break;
            default:
                sb.append(" and sfcwah.indat<= '${d}' ");
        }
        sb.append(" ) ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno).replace("${prono}", prono);
        
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            attqty1 = (BigDecimal) o;
        } catch (Exception e) {
             Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, e);
        }
        return attqty1;
    }
    
}
