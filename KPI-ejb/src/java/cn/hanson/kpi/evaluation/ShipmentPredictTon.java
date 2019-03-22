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
 * @author C1749 汉声预估订单量（吨）
 */
public abstract class ShipmentPredictTon extends Shipment {

    public ShipmentPredictTon() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String cdrcus = map.get("cdrcus") != null ? map.get("cdrcus").toString() : "";//客户
        String material = map.get("material") != null ? map.get("material").toString() : "";//总分种类
        String variety = map.get("variety") != null ? map.get("variety").toString() : "";//细分种类别
        BigDecimal ton = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(a.cdrqy),0) from ( ");
        sb.append(" select h.cdrno,h.decode,h.recdate,left(convert(char(12),h.recdate,112),6) as yearmon,h.ratio,d.itnbr,s.itcls, ");
        sb.append(" cast( (case substring(s.judco,1,1)+s.fvco when '4F' then d.cdrqy1*s.rate2  else d.cdrqy1 end)  as decimal(17,2)) as cdrqy , ");
        sb.append(" cast( (case when  h.coin<>'RMB' then  d.tramts*h.ratio else d.tramts*h.ratio/(h.taxrate+1) end )as decimal(17,2)) as shpamts, ");
        sb.append(" (case when h.cusno in ('HSH00003') then 'SHB' when h.cusno in ('HTW00001') then 'THB' else 'OTHER' end) as cdrcus, ");
        sb.append(" (case when left(s.spdsc,2)='HT' then 'HT' when left(s.spdsc,2)='QT'then 'QT' else 'OTH' end) as material ");
        sb.append(" from cdrdmas d, cdrhmas h ,invmas s ");
        sb.append(" where h.cdrno=d.cdrno and h.hrecsta not in ('N','W') and s.itnbr=d.itnbr ");
        if (!"".equals(variety)) {
            if (!"OTH".equals(variety)) {
                sb.append(" and s.itcls in (select itcls from bsc_zlitcls where salitcls = '").append(variety).append("')");
            } else {
                sb.append(" and s.itcls not in (select itcls from bsc_zlitcls ) ");
            }
        }
        sb.append(" and year(h.recdate) = ${y} and month(h.recdate)= ${m} and h.recdate<= '${d}' ");
        sb.append(" ) as a  where 1=1 ");
        if (!"".equals(cdrcus)) {
            sb.append(" and a.cdrcus = '").append(cdrcus).append("'");
        }
        if (!"".equals(material)) {
            sb.append(" and a.material = '").append(material).append("'");
        }
        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(cdrdta);
        try {
            Object o1 = query.getSingleResult();
            ton = (BigDecimal) o1;
        } catch (Exception ex) {
            log4j.error("HSShipmentExpeditingTonne getValue()异常", ex);
        }
        return ton;
    }

}
