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
public class ConsumptionCutter extends Process {

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String iocode = map.get("iocode") != null ? map.get("iocode").toString() : "";
        String trtype = map.get("trtype") != null ? map.get("trtype").toString() : "";
        String depno = map.get("depno") != null ? map.get("depno").toString() : "";
        String qualification = map.get("qualification") != null ? map.get("qualification").toString() : "";
        BigDecimal amount = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT isnull(sum(p.unittotcst/p.unittotqy*t.trnqy1),0) FROM invtrnh t,invpri p WHERE t.facno=p.facno AND  t.itnbr=p.itnbr ");
        sb.append(" AND t.facno='${facno}' AND t.iocode='${iocode}' AND t.trtype='${trtype}' ");
        if (!"".equals(depno)) {
            sb.append(" AND t.depno ").append(depno);
        }
        if (!"".equals(qualification)) {
            sb.append(" and ").append(qualification);
        }
        sb.append(" AND p.yearmon=(select max(yearmon) from invpri y where y.itnbr=p.itnbr and y.facno=p.facno and y.unittotcst <> 0 ) ");
        sb.append(" AND year(t.trdate) = ${y} and month(t.trdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and t.trdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and t.trdate= '${d}' ");
                break;
            default:
                sb.append(" and t.trdate<= '${d}' ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno).replace("${iocode}", iocode).replace("${trtype}", trtype);;

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            amount = (BigDecimal) o;
        } catch (Exception e) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, e);
        }
        return amount;
    }

}
