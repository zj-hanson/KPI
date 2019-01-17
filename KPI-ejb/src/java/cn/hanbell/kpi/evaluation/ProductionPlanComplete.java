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
public class ProductionPlanComplete extends Production {

    public ProductionPlanComplete() {
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String prono = map.get("prono") != null ? map.get("prono").toString() : "";
        String stats = map.get("stats") != null ? map.get("stats").toString() : "";
        String typecode = map.get("typecode") != null ? map.get("typecode").toString() : "";
        String linecode = map.get("linecode") != null ? map.get("linecode").toString() : "";
        String prosscode = map.get("prosscode") != null ? map.get("prosscode").toString() : "";
        String wrcode = map.get("wrcode") != null ? map.get("wrcode").toString() : "";
        String itcls = map.get("itcls") != null ? map.get("itcls").toString() : "";
        String itnbrf = map.get("itnbrf") != null ? map.get("itnbrf").toString() : "";

        BigDecimal value = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT isnull(sum(d.godqty),0) FROM  sfcfsd d, sfcfsh h LEFT JOIN manmas m ON d.manno=m.manno ");
        sb.append(" where  h.facno=d.facno and  h.prono=d.prono  and h.fshno=d.fshno ");
        sb.append(" AND d.facno=m.facno AND d.prono=m.prono AND d.manno=m.manno and h.facno='${facno}' and h.prono='${prono}' and h.stats='${stats}' ");
        sb.append(" AND m.facno='${facno}' and m.prono='${prono}'   ");
        if (!"".equals(linecode)) {
            sb.append(" and m.linecode ").append(linecode);
        }
        if (!"".equals(typecode)) {
            sb.append(" and m.typecode ").append(typecode);
        }
        if (!"".equals(prosscode)) {
            sb.append(" and h.prosscode ").append(prosscode);
        }
        if (!"".equals(wrcode)) {
            sb.append(" and h.wrcode ").append(wrcode);
        }
        if (!"".equals(itcls)) {
            sb.append(" and m.itcls ").append(itcls);
        }
        if (!"".equals(itnbrf)) {
            sb.append(" and m.itnbrf  ").append(itnbrf);
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
                .replace("${facno}", facno).replace("${prono}", prono).replace("${stats}", stats);

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            value = (BigDecimal) o;
        } catch (Exception e) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, e);
        }
        return value;
    }
}
