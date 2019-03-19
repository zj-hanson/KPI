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
 * @author C1749 汉声实际催货量（吨）
 */
public abstract class ShipmentDelyAmts extends Shipment {

    public ShipmentDelyAmts() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String cdrcus = map.get("cdrcus") != null ? map.get("cdrcus").toString() : "";//客户
        String material = map.get("material") != null ? map.get("material").toString() : "";//种类
        //实际催货量：cdrdta 本月 cdrhad的houtsta状态不为 W 的单据
        //未来几天催货量：cdrdta 本月 cdrhad状态为 N 的单据
        String houtsta = map.get("houtsta") != null ? map.get("houtsta").toString() : "";//状态
        BigDecimal amts = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(a.shpamts),0) from ( ");
        sb.append(" select d.shpno,h.indate,left(convert(char(12),h.indate,112),6) as yearmon,d.itnbr,d.trseq,h.shpdate, ");
        sb.append(" cast( (case substring(s.judco,1,1)+s.fvco when '4F' then d.shpqy1*s.rate2  else d.shpqy1 end)  as decimal(17,2)) as shpqy, ");
        sb.append(" d.unpris,s.rate2,h.ratio, ");
        sb.append(" cast((case when h.coin<>'RMB' then d.shpamts*h.ratio else d.shpamts*h.ratio/(h.taxrate+1) end) as decimal(17,2)) as shpamts, ");
        sb.append(" (case when h.cusno in ('HSH00003') then 'SHB' when h.cusno in ('HTW00001') then 'THB' else 'OTHER' end) as cdrcus, ");
        sb.append(" (case when left(s.spdsc,2)='HT' then 'HT' when left(s.spdsc,2)='QT'then 'QT' else 'OTH' end) as material ");
        sb.append(" from cdrdta d left join invmas s on s.itnbr=d.itnbr ,cdrhad h   ");
        sb.append(" where h.shpno=d.shpno  ");
        if (!"".equals(houtsta)) {
            sb.append(" and h.houtsta ").append(houtsta);
        }
        sb.append(" and year(h.indate) = ${y} and month(h.indate)= ${m} and h.indate<= '${d}' ");
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
            amts = (BigDecimal) o1;
        } catch (Exception ex) {
            log4j.error("HSShipmentExpeditingAmts getValue()异常", ex);
        }
        return amts;
    }

}
