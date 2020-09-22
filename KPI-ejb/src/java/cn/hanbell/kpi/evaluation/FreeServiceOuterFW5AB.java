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
 */
public class FreeServiceOuterFW5AB extends FreeServiceOuterFW {

    public FreeServiceOuterFW5AB() {
        super();
        queryParams.put("facno", "K");
//        queryParams.put("hmark1", "='HD' ");
//        queryParams.put("hmark2", " IN('RTZ','RZ','ZSNY') ");
    }

    //2019年6月5日免费服务离心机服务领退料并入柯茂
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal rt = super.getValue(y, m, d, type, map);
        BigDecimal fj = getfjValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.put("facno", "C");
        queryParams.put("hmark1", " <> 'CK' ");
        queryParams.put("hmark2", " ='CM' ");
        BigDecimal cm = super.getValue(y, m, d, type, map);
        //@decription 2020年9月21号顺应陈海英需求，增加浙江柯茂的数据，有效期本年年底。
        queryParams.clear();
        queryParams.put("facno", "E");
        //ZJComer
        BigDecimal jzComer = super.getValue(y, m, d, type, queryParams);
        return rt.add(cm).add(fj).add(jzComer); //To change body of generated methods, choose Tools | Templates.
    }

    public BigDecimal getfjValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        BigDecimal o2 = BigDecimal.ZERO;
        BigDecimal o3 = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        //领料
        sb.append(" select isnull(sum(t.tramt),0) FROM invtrnh t WHERE  facno ='K' AND t.prono='1' and  t.trtype='IAP' AND depno<>'K0000009' AND t.iocode='2' ");
        sb.append(" AND year(t.trdate) = ${y} and month(t.trdate)= ${m} ");

        String iap2sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        sb.setLength(0);
        //退料
        sb.append(" select isnull(-sum(t.tramt),0) FROM invtrnh t WHERE facno ='K' AND t.prono='1' and  t.trtype='IAP' AND depno<>'K0000009' AND t.iocode='1' ");
        sb.append(" AND year(t.trdate) = ${y} and month(t.trdate)= ${m} ");

        String iap1sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(iap2sql);
        Query query2 = superEJB.getEntityManager().createNativeQuery(iap1sql);
        try {
            Object p1 = query1.getSingleResult();
            Object p2 = query2.getSingleResult();

            o2 = (BigDecimal) p1;
            o3 = (BigDecimal) p2;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return o2.subtract(o3);
    }

}
