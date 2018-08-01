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
        String prosscode = map.get("prosscode") != null ? map.get("prosscode").toString() : "";
        String wrcode = map.get("wrcode") != null ? map.get("wrcode").toString() : "";
        String stats = map.get("stats") != null ? map.get("stats").toString() : ""; 
        
        BigDecimal godqty = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(godqty),0) from sfcfsd d,sfcfsh h where h.facno = d.facno and h.prono = d.prono and h.fshno = d.fshno");
        sb.append(" and h.facno = '${facno}' and h.prono= '${prono}' ");
        if (!"".equals(prosscode)) {
            sb.append(" and h.prosscode ").append(prosscode);
        }
        if (!"".equals(wrcode)) {
            sb.append(" and  h.wrcode ").append(wrcode);
        }
        if (!"".equals(stats)) {
            sb.append(" and d.stats ").append(stats);
        }

        sb.append(" and year(h.fshdat) = ${y} and month(h.fshdat)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and h.fshdat<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and h.fshdat= '${d}' ");
                break;
            default:
                sb.append(" and h.fshdat<= '${d}' ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno).replace("${prono}", prono);
        
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            godqty = (BigDecimal) o;
        } catch (Exception e) {
             Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, e);
        }
        return godqty;
    }
    
}
