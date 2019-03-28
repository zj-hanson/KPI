/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import com.lightshell.comm.BaseLib;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
public abstract class QRAComplaintRatio extends QRAAConnERP {

    //获得当月的移动平均出货台数
    public List getAvgShip(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        //String decode = map.get("decode") != null ? map.get("decode").toString() : "";移动平均不区分内外销
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";
        String[] arr = facno.split(",");//公司别
        Double result = 0.0;
        Double avgShip = 0.0;
        List list = new ArrayList();
        Double num1, num2;
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < 12; i++) {
                for (String arr1 : arr) {
                    sb.append("select isnull(sum(d.shpqy1),0) from cdrhad h,cdrdta d where h.facno=d.facno and h.shpno=d.shpno  and h.houtsta<>'W'  ");
                    sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
                    sb.append(" and d.issevdta='N' and h.facno='${facno}' ");
                    if (n_code_DA.contains("AA")) {
                        sb.append(" and left(d.itnbr,1)='3' ");
                    }
                    if (!"".equals(n_code_DA)) {
                        sb.append(" and d.n_code_DA ").append(n_code_DA);
                    }
                    if (!"".equals(n_code_DD)) {
                        sb.append(" and d.n_code_DD ").append(n_code_DD);
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(d);
                    calendar.add(Calendar.MONTH, -i - 12);
                    Date pastYear = calendar.getTime();//取到12个月前的日期
                    calendar.clear();
                    calendar.setTime(d);
                    calendar.add(Calendar.YEAR, -1);
                    calendar.add(Calendar.YEAR, 1);
                    calendar.add(Calendar.MONTH, -1 - i);
                    Date nowYear = calendar.getTime();
                    sb.append(" and h.shpdate BETWEEN  '");
                    sb.append(BaseLib.formatDate("yyyyMM", pastYear));
                    sb.append("'  and  '");
                    sb.append(BaseLib.formatDate("yyyyMM", nowYear));
                    sb.append("'  ");
                    String cdrdta = sb.toString().replace("${facno}", arr1);
                    sb.setLength(0);
                    sb.append("select isnull(sum(d.bshpqy1),0) from cdrbhad h,cdrbdta d where h.facno=d.facno and h.bakno=d.bakno and h.baksta<>'W'  ");
                    sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
                    sb.append(" and d.issevdta='N' and h.facno='${facno}' ");
                    if (n_code_DA.contains("AA")) {
                        sb.append(" and left(d.itnbr,1)='3' ");
                    }
                    if (!"".equals(n_code_DA)) {
                        sb.append(" and d.n_code_DA ").append(n_code_DA);
                    }
                    if (!"".equals(n_code_DD)) {
                        sb.append(" and d.n_code_DD ").append(n_code_DD);
                    }
                    sb.append(" and h.bakdate BETWEEN  '");
                    sb.append(BaseLib.formatDate("yyyyMM", pastYear));
                    sb.append("'  and  '");
                    sb.append(BaseLib.formatDate("yyyyMM", nowYear));
                    sb.append("'  ");
                    String cdrbdta = sb.toString().replace("${facno}", arr1);
                    superEJB.setCompany(facno);
                    Query query1 = superEJB.getEntityManager().createNativeQuery(cdrdta);
                    Query query2 = superEJB.getEntityManager().createNativeQuery(cdrbdta);

                    Object o1 = query1.getSingleResult();
                    Object o2 = query2.getSingleResult();
                    DecimalFormat df = new DecimalFormat("#.00");
                    num1 = Double.parseDouble(o1.toString());
                    num2 = Double.parseDouble(o2.toString());
                    avgShip = avgShip = (num1 - num2) / 12;
                    result += Double.valueOf(df.format(avgShip));
                }
                list.add(result);
                result = null;
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    //获得3/6/12的移动平均出货台数的总和
    public Double getsumAvgShip(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mis = map.get("mis") != null ? map.get("mis").toString() : ""; //MJS（3/6/12）
        int count = Integer.parseInt(mis);
        Double sumDouble = 0.0;
        List avglist = getAvgShip(y, m, d, type, map);
        for (int i = 0; i < count; i++) {
          sumDouble += Double.parseDouble(avglist.get(i).toString());
        }
        
//        for (int i = m; i > (m - count); i--) {
//            if (i <= 0) {
//                year = y - 1;
//                month = 12 + i;
//                sumDouble += getAvgShip(year, month, d, type, map);
//            } else {
//                sumDouble += getAvgShip(y, i, d, type, map);
//            }
//        }
        return sumDouble;
    }
}
