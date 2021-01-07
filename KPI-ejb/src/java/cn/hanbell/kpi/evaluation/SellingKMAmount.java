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
public class SellingKMAmount extends Shipment {

    public SellingKMAmount() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {

        BigDecimal shp1 = BigDecimal.ZERO;
        BigDecimal bshp1 = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append("select isnull(sum(case h.tax when '1' then (d.shpamts * h.ratio) else (d.shpamts * h.ratio)/(h.taxrate + 1) end),0) from cdrdta d left join cdrhad h on d.shpno=h.shpno LEFT JOIN cdrdmas s on s.cdrno=d.cdrno AND s.itnbr=d.itnbr where ");
        sb.append(" h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and h.houtsta <> 'W' and d.issevdta='N' and h.facno='C' ");
        sb.append(" and d.n_code_DA='R' and d.n_code_DD ='00' AND h.cusno='SSH00451' AND (s.dmark1 LIKE '%LT%' or s.dmark1 like '%RT%') ");
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
        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));

        sb.setLength(0);
        sb.append("select isnull(sum(case h.tax when '1' then (d.bakamts * h.ratio) else (d.bakamts * h.ratio)/(h.taxrate + 1) end),0) from cdrbdta d left join cdrbhad h on d.bakno=h.bakno LEFT JOIN cdrdmas s on s.cdrno=d.cdrno AND s.itnbr=d.itnbr where ");
        sb.append(" h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and h.facno='C' and h.baksta<>'W'And d.issevdta='N' ");
        sb.append(" and d.n_code_DA='R' and d.n_code_DD ='00' AND (s.dmark1 LIKE '%LT%' or s.dmark1 like '%RT%') AND h.cusno='SSH00451' ");
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
        String cdrbdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));

        superEJB.setCompany("C");
        Query query1 = superEJB.getEntityManager().createNativeQuery(cdrdta);
        Query query2 = superEJB.getEntityManager().createNativeQuery(cdrbdta);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            shp1 = (BigDecimal) o1;
            bshp1 = (BigDecimal) o2;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shp1.subtract(bshp1);
    }

}
