/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1879
 * 厂外免费服务金额运费单CDRN20  与FreeServiceOuterYSoa合计一起为运输费用
 */
public class FreeServiceOuterYSerp extends FreeServiceERP{

    public FreeServiceOuterYSerp() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        //部门
        String depno = map.get("depno") != null ? map.get("depno").toString() : "";

        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(c.freight),0) from cdrlnhad h LEFT JOIN cdrfre c on c.shpno = h.trno and c.facno = h.facno ");
        sb.append(" where  h.facno='${facno}' AND h.status ='Y' and  h.resno = '03' and ( h.fwno <> ''and h.fwno <> '-') and (h.kfno <> '' and h.kfno <> '-') and c.freight> 0  ");
        if (!"".equals(depno)) {
            sb.append(" AND h.depno ").append(depno);
        }
        sb.append(" AND year(h.cfmdate) = ${y} and month(h.cfmdate)= ${m} ");

        String cdrn20sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(cdrn20sql);
        try {
            Object o = query.getSingleResult();
            return (BigDecimal) o;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }
    
    
}
