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
        sb.append(" select isnull(sum(a.shpqy1),0) from ( ");
        //出货
        sb.append(" select d.itnbr,d.shpno,d.trseq,s.itcls,s.itdsc,h.shpdate,isnull(d.shpqy1,0),isnull(d.shpqy2,0) as shpqy2,d.unpris,s.rate2, ");
        sb.append(" cast( (case substring(s.judco,1,1)+s.fvco when '4F' then d.shpqy1*s.rate2  else d.shpqy1 end)  as decimal(17,2)) as shpqy1, ");
        sb.append(" cast((case when h.coin<>'RMB' then d.shpamts*h.ratio else d.shpamts*h.ratio/(h.taxrate+1) end) as decimal(10,4)) as shpamts,'2' , ");
        sb.append(" left(convert(char(12),h.shpdate,112),6),h.moveno ");
        sb.append(" from cdrdta d,cdrhad h,invmas s ");
        sb.append(" where h.shpno=d.shpno and d.itnbr=s.itnbr ");
        sb.append(" and h.houtsta not in ('W','N') ");
        if (!"".equals(protype)) {
            sb.append(" and left(s.spdsc,2) ").append(protype);
        }
        if (!"".equals(cusno)) {
            sb.append(" and h.cusno ").append(cusno);
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
        sb.append(" union all ");
        //销退
        sb.append(" select d.itnbr,d.bakno,d.trseq,s.itcls,s.itdsc,h.bakdate,-1*isnull(d.bshpqy1,0),-1*isnull(d.bshpqy2,0) as shpqy2,d.unpris,s.rate2, ");
        sb.append(" -1*cast( (case substring(s.judco,1,1)+s.fvco when '4F' then d.bshpqy1*s.rate2  else d.bshpqy1 end)  as decimal(17,2)) as shpqy1, ");
        sb.append(" -1*cast((case when h.coin<>'RMB' then d.bakamts*h.ratio else d.bakamts*h.ratio/(h.taxrate+1) end) as decimal(10,4)) as shpamts,'1' , ");
        sb.append(" left(convert(char(12),h.bakdate,112),6),h.bakno ");
        sb.append(" from cdrbdta d,cdrbhad h,invmas s ");
        sb.append(" where h.bakno=d.bakno and d.itnbr=s.itnbr and h.baksta not in ('W','N') and h.owarehyn='Y' ");
        if (!"".equals(protype)) {
            sb.append(" and left(s.spdsc,2) ").append(protype);
        }
        if (!"".equals(cusno)) {
            sb.append(" and h.cusno ").append(cusno);
        }
        sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and h.bakdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and h.bakdate= '${d}' ");
                break;
            default:
                sb.append(" and h.bakdate<= '${d}' ");
        }
        sb.append(" union all ");
        //折让
        sb.append(" select  d.itnbr,d.bakno,d.trseq,s.itcls,s.itdsc,a.bildat,-1*isnull(d.bshpqy1,0),-1*isnull(d.bshpqy2,0) as shpqy2,d.unpris,s.rate2, ");
        sb.append(" -1*cast( (case substring(s.judco,1,1)+s.fvco when '4F' then d.bshpqy1*s.rate2  else d.bshpqy1 end)  as decimal(17,2)) as shpqy1, ");
        sb.append(" -1*cast((  a.losamts/(1+a.taxrate) ) as decimal(10,4)) as shpamts,'1' , ");
        sb.append(" left(convert(char(12),a.bildat,112),6),a.recno ");
        sb.append(" from armblos a,cdrbdta d,invmas s ");
        sb.append(" where a.facno=d.facno ");
        sb.append(" and a.bakno=d.bakno and a.trseq=d.trseq and s.itnbr=d.itnbr ");
        sb.append(" and a.facno='${facno}' ");
        if (!"".equals(protype)) {
            sb.append(" and left(s.spdsc,2) ").append(protype);
        }
        if (!"".equals(cusno)) {
            sb.append(" and a.ivocus ").append(cusno);
        }
        sb.append(" and year(a.bildat) = ${y} and month(a.bildat)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and a.bildat<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and a.bildat= '${d}' ");
                break;
            default:
                sb.append(" and a.bildat<= '${d}' ");
        }
        sb.append(" )as a ");
        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);
        superEJB.setCompany(facno);
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
