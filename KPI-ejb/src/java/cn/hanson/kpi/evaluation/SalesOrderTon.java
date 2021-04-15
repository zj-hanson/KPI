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
 * 预估订单数量
 *
 * @author C0160
 */
public class SalesOrderTon extends SalesOrder {

    public SalesOrderTon() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String cusno = map.get("cusno") != null ? map.get("cusno").toString() : "";//客户
        String protype = map.get("protype") != null ? map.get("protype").toString() : "";//种类
        String variety = map.get("variety") != null ? map.get("variety").toString() : "";//细类

        BigDecimal ton = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(cast((case substring(s.judco,1,1)+s.fvco ");
        sb.append(" when '4F' then d.cdrqy1*s.rate2 else d.cdrqy1 end) as decimal(17,2))),0) ");
        sb.append(" from cdrhmas h,cdrdmas d,invmas s ");
        sb.append(" where h.facno=d.facno and h.cdrno=d.cdrno and h.hrecsta not in ('N','W') and d.drecsta<'95' and s.itnbr=d.itnbr ");
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

        String cdrdmas = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(cdrdmas);
        try {
            Object o1 = query.getSingleResult();
            ton = (BigDecimal) o1;
        } catch (Exception ex) {
            log4j.error("ShipmentPredictTonHY.getValue()异常", ex);
        }
        return ton;
    }

    @Override
    public BigDecimal getNotDelivery(Date d, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String cusno = map.get("cusno") != null ? map.get("cusno").toString() : "";//客户
        String protype = map.get("protype") != null ? map.get("protype").toString() : "";//种类
        String variety = map.get("variety") != null ? map.get("variety").toString() : "";//细类

        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(cast((case substring(s.judco,1,1)+s.fvco ");
        sb.append(" when '4F' then (d.cdrqy1-d.shpqy1)*s.rate2 else (d.cdrqy1-d.shpqy1) end) as decimal(17,2))),0) ");
        sb.append(" from cdrhmas h,cdrdmas d,invmas s where h.facno=d.facno and h.cdrno=d.cdrno and h.hrecsta<>'W' ");
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
