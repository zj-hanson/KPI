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
public class ProductionQuantity extends Production {

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String prono = map.get("prono") != null ? map.get("prono").toString() : "";
        String linecode = map.get("linecode") != null ? map.get("linecode").toString() : "";
        String mankind = map.get("mankind") != null ? map.get("mankind").toString() : "";
        String typecode = map.get("typecode") != null ? map.get("typecode").toString() : "";
        String itcls = map.get("itcls") != null ? map.get("itcls").toString() : "";
        String itnbrf = map.get("itnbrf") != null ? map.get("itnbrf").toString() : "";    
        
        BigDecimal manqty = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(manqty),0) from manmas where manstatus in ('B','C','D','E','H','I') ");
        sb.append(" and  facno = '${facno}' and prono = '${prono}' ");
        if (!"".equals(linecode)) {
            sb.append(" and linecode ").append(linecode);
        }
        if (!"".equals(mankind)) {
            sb.append(" and  mankind ").append(mankind);
        }
        if (!"".equals(typecode)) {
            sb.append(" and typecode ").append(typecode);
        }
        if (!"".equals(itcls)) {
            sb.append(" and itcls ").append(itcls);
        }
        if (!"".equals(itnbrf)) {
            sb.append(itnbrf);
        }
        sb.append(" and year(issdate) = ${y} and month(issdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and issdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and issdate= '${d}' ");
                break;
            default:
                sb.append(" and issdate<= '${d}' ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno).replace("${prono}", prono);
        
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            manqty = (BigDecimal) o;
        } catch (Exception e) {
             Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, e);
        }
        return manqty;
    }

}
