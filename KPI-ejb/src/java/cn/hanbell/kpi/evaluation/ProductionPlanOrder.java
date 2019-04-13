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
public class ProductionPlanOrder extends Production {

    public ProductionPlanOrder() {
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String itcls = map.get("itcls") != null ? map.get("itcls").toString() : "";
        String itnbrf = map.get("itnbrf") != null ? map.get("itnbrf").toString() : "";

        BigDecimal value = BigDecimal.ZERO;
        BigDecimal value98 = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(d.cdrqy1),0) as totcdrqy from cdrhmas h, cdrdmas d ");
        sb.append(" where  h.hrecsta<>'W' and h.cdrno=d.cdrno and  h.facno=d.facno  and h.facno='${facno}' and d.drecsta not in ('99','98')");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(itcls)) {
            sb.append(" and d.itnbr in(select itnbr from invmas where itcls ").append(itcls).append(") ");
        }
        sb.append(" and year(h.recdate) = ${y} and month(h.recdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and h.recdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and h.recdate= '${d}' ");
                break;
            default:
                sb.append(" and h.recdate<= '${d}' ");
        }
        String sql1 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        sb.setLength(0);
        //本月当天人工结案订单
        sb.append(" select isnull(sum(d.cdrqy1),0) as totcdrqy from cdrhmas h, cdrdmas d ");
        sb.append(" where  h.hrecsta<>'W' and h.cdrno=d.cdrno and  h.facno=d.facno  and h.facno='${facno}' and d.drecsta IN ('99','98') ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(itcls)) {
            sb.append(" and d.itnbr in(select itnbr from invmas where itcls ").append(itcls).append(") ");
        }
        sb.append(" and year(h.recdate) = ${y} and month(h.recdate)= ${m} and h.recdate < '${d}' and left(convert(char(12),d.enddate,112),8)= '${d}'");

        String sql2 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(sql1);
        Query query2 = superEJB.getEntityManager().createNativeQuery(sql2);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            value = (BigDecimal) o1;
            value98 = (BigDecimal) o2;
            //减去本月当天发生的人工结案订单
            return value.subtract(value98);
        } catch (Exception e) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, e);
        }
        return BigDecimal.ZERO;
    }

}
