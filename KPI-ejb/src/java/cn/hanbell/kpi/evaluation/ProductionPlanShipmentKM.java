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
public class ProductionPlanShipmentKM extends ProductionPlanShipment {

    public ProductionPlanShipmentKM() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("itcls", " in ('3H80','3W76','3W80')");
        queryParams.put("itnbrf", " in ('3176','3179','3180') ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String itcls = map.get("itcls") != null ? map.get("itcls").toString() : "";
        String itnbrf = map.get("itnbrf") != null ? map.get("itnbrf").toString() : "";

        BigDecimal value = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(d.shpqy1),0) as totshpqy from cdrhad h, cdrdta d ");
        sb.append(" where  h.houtsta<>'W' and h.shpno=d.shpno and  h.facno=d.facno  and h.facno='${facno}' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(itcls)) {
            sb.append(" and (d.itnbr in(select itnbr from invmas where itcls ").append(itcls).append(") ");
            sb.append(" and d.itnbr NOT in(select itnbr from invmas where itcls ").append(itnbrf).append(")) ");
        }
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and h.shpdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and h.shpdate= '${d}' ");
                break;
            default:
                sb.append(" and h.shpdate<= '${d}' ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

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
