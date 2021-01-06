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
public abstract class DeliveryTon extends Shipment {

    public DeliveryTon() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String cusno = map.get("cusno") != null ? map.get("cusno").toString() : "";//客户
        String protype = map.get("protype") != null ? map.get("protype").toString() : "";//种类
        String variety = map.get("variety") != null ? map.get("variety").toString() : "";//细分种类别
        //实际催货量：cdrdta 本月 cdrhad的houtsta状态不为 W 的单据
        //未来几天催货量：cdrdta 本月 cdrhad状态为 N 的单据
        String houtsta = map.get("houtsta") != null ? map.get("houtsta").toString() : "";//状态

        BigDecimal ton = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        //出货
        sb.append(" select isnull(sum(cast((case substring(s.judco,1,1)+s.fvco ");
        sb.append(" when '4F' then d.shpqy1*s.rate2 else d.shpqy1 end) as decimal(17,2))),0) ");
        sb.append(" from cdrdta d,cdrhad h,invmas s ");
        sb.append(" where h.facno=d.facno and h.shpno=d.shpno and d.itnbr=s.itnbr ");
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
        if (!"".equals(houtsta)) {
            sb.append(" and h.houtsta ").append(houtsta).append(" ");
        }
        sb.append(" and year(h.indate) = ${y} and month(h.indate)= ${m} and h.indate<= '${d}' ");

        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(cdrdta);
        try {
            Object o1 = query.getSingleResult();
            ton = (BigDecimal) o1;
        } catch (Exception ex) {
            log4j.error("DeliveryTon.getValue()异常", ex);
        }
        return ton;
    }

}
