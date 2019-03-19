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
 *
 * @author C1749 汉声出货重量
 */
public abstract class ShipmentTon extends Shipment {

    public ShipmentTon() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String protype = map.get("protype") != null ? map.get("protype").toString() : "";//种类别
        String cusno = map.get("cusno") != null ? map.get("cusno").toString() : "";//客户
        BigDecimal ton = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append("select isnull(sum(shpnum),0) from bsc_groupshipment ");
        sb.append(" where facno='${facno}' ");
        if (!"".equals(protype)) {
            sb.append(" and protype ='").append(protype).append("' ");
        }
        if (!"".equals(cusno)) {
            sb.append(" and cusno ='").append(cusno).append("' ");
        }
        sb.append(" and year(soday) = ${y} and month(soday)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and soday<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and soday= '${d}' ");
                break;
            default:
                sb.append(" and soday<= '${d}' ");
        }
        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);
        superEJB.setCompany("C");
        Query query = superEJB.getEntityManager().createNativeQuery(cdrdta);
        try {
            Object o1 = query.getSingleResult();
            ton = (BigDecimal) o1;
            return ton;
        } catch (Exception ex) {
            log4j.error("ShipmentTon getValue()异常", ex);
        }
        return BigDecimal.ZERO;
    }

}
