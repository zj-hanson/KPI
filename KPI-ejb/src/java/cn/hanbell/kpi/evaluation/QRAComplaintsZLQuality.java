/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1749 得到mis制令台数总和
 */
public class QRAComplaintsZLQuality extends QRAComplaints {

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        StringBuilder varnr = new StringBuilder();
        String mis = map.get("mis") != null ? map.get("mis").toString() : ""; //MJS（3/6/12）
        int count = Integer.parseInt(mis);
        QRAComplaintsQualityOrder mKS = new QRAComplaintsQualityOrder();
        varnr.append(" in(");
        List<String> list = mKS.getValue(y, m, d, type, map);
        List<String> newlist = new ArrayList<>();
        //去掉null和是%的制造号码
        for (int i = 0; i < list.size(); i++) {
            if (!"".equals(list.get(i).trim()) && !"%".equals(list.get(i).trim())) {
                newlist.add(list.get(i));
            }
        }
        //循环换成SQL能识别的类型
        for (int i = 0; i < newlist.size(); i++) {
            if (newlist.size() != (i + 1)) {
                varnr.append("'").append(newlist.get(i)).append("',");
            } else {
                varnr.append("'").append(newlist.get(i)).append("') ");
            }
        }
        //循环mis次数得到制令
        int sum = 0;
        int year, month;
        for (int i = m; i > (m - count); i--) {
            if (i <= 0) {
                year = y - 1;
                month = 12 + i;
                sum += getZLnum(varnr.toString(), month, year);
            } else {
                sum += getZLnum(varnr.toString(), i, y);
            }
        }

        return BigDecimal.valueOf(sum);
    }

    public int getZLnum(String varnr, int m, int y) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(d.manno) from sfcwad d ,manmas m where d.manno = m.manno and d.facno = m.facno and d.prono = m.prono ");
        if (!"".equals(varnr.trim())) {
            sb.append(" and d.varnr ").append(varnr);
        }
        sb.append(" AND year(m.mandate) = ${y} and month(m.mandate)= ${m} ");
        String sql = sb.toString().replace("${m}", String.valueOf(m)).replace("${y}", String.valueOf(y));

        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            return Integer.valueOf(o.toString());
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}
