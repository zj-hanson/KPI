/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.persistence.Query;

/**
 * 预估订单金额
 *
 * @author C0160
 */
public class SalesOrderAmount extends SalesOrder {

    public SalesOrderAmount() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String cusno = map.get("cusno") != null ? map.get("cusno").toString() : "";//客户
        String protype = map.get("protype") != null ? map.get("protype").toString() : "";//种类
        String variety = map.get("variety") != null ? map.get("variety").toString() : "";//细类

        BigDecimal amts = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(cast((case when h.tax <> '4' then d.tramts*h.ratio ");
        sb.append(" else d.tramts*h.ratio/(h.taxrate + 1) end) as decimal(17,2))),0) ");
        sb.append(" from cdrdmas d,cdrhmas h,invmas s ");
        sb.append(" where h.facno=d.facno and h.cdrno=d.cdrno and h.hrecsta not in ('N','W') and s.itnbr=d.itnbr ");
        if (!"".equals(cusno)) {
            sb.append(" and h.cusno ").append(cusno);
        }
        if (!"".equals(protype)) {
            sb.append(" and left(s.spdsc,2) ").append(protype);
        }
        if (!"".equals(variety)) {
            if (!"OTH".equals(variety)) {
                sb.append(" and s.itcls in (select itcls from bsc_zlitcls where salitcls = '").append(variety).append("')");
            } else {
                sb.append(" and s.itcls not in (select itcls from bsc_zlitcls ) ");
            }
        }
        sb.append(" and year(h.recdate) = ${y} and month(h.recdate)= ${m} and h.recdate<= '${d}' ");

        String cdrdmas = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(cdrdmas);
        try {
            Object o1 = query.getSingleResult();
            amts = (BigDecimal) o1;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return amts;
    }

    @Override
    public BigDecimal getNotDelivery(Date d, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String cusno = map.get("cusno") != null ? map.get("cusno").toString() : "";//客户
        String protype = map.get("protype") != null ? map.get("protype").toString() : "";//种类
        String variety = map.get("variety") != null ? map.get("variety").toString() : "";//细类

        StringBuilder sb = new StringBuilder();
        sb.append("select isnull(sum((d.cdrqy1 - d.shpqy1) * d.unpris * h.ratio/(case h.tax when '4' then (h.taxrate + 1) else 1 end)),0) ");
        sb.append(" from cdrhmas h,cdrdmas d,invmas s where h.facno=d.facno and h.cdrno=d.cdrno and h.hrecsta not in ('N','W') ");
        sb.append(" and d.itnbr=s.itnbr ");
        sb.append(" and ((d.cdrqy1-d.shpqy1)>0 or (d.cdrqy2-d.shpqy2)>0) and d.drecsta<'95' and h.facno='${facno}' ");
        if (!"".equals(cusno)) {
            sb.append(" and h.cusno ").append(cusno);
        }
        if (!"".equals(protype)) {
            sb.append(" and left(s.spdsc,2) ").append(protype);
        }
        if (!"".equals(variety)) {
            if (!"OTH".equals(variety)) {
                sb.append(" and s.itcls in (select itcls from bsc_zlitcls where salitcls = '").append(variety).append("')");
            } else {
                sb.append(" and s.itcls not in (select itcls from bsc_zlitcls ) ");
            }
        }
        sb.append(" and h.recdate<= '${d}' ");

        String cdrdmas = sb.toString().replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(cdrdmas);
        try {
            Object o1 = query.getSingleResult();
            return (BigDecimal) o1;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

}
