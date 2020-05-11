/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class IncomeStatementBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;
    private final DecimalFormat df;
    private final DecimalFormat dfpercent;

    public IncomeStatementBean() {
        this.df = new DecimalFormat("#,##0.00");
        this.dfpercent = new DecimalFormat("0.00％");
    }

    private int year(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    private int month(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    private String DFormat(String a) {
        return df.format(Double.parseDouble(a));
    }

    public LinkedHashMap<String, String[]> yearMap(Date date, String facno) {
        LinkedHashMap<String, String[]> map = new LinkedHashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select a.seq,a.tyear/10000 as tyear,a.tyrate/100 as tyrate,a.lyear/10000 as lyear,a.lyrate/100 as lyrate,");
        sb.append(" (a.tyear-a.lyear)/10000 as changeyear,b.dradj/10000 as dradj,b.cradj/100 as cradj from accinmon a, ");
        sb.append(" (select seq,dradj,cradj from accinmon where facno='CK' and accyear = ${y}  and accmon = 1 ) b ");
        sb.append("  where a.facno='CK' and a.seq = b.seq and a.accyear = ${y}  and a.accmon = ${m} ");
        sb.append(" union all ");
        sb.append(" select 999 as seq,(a.tyear-b.tyear)/10000 as tyear,(a.tyrate-b.tyrate)  as tyrate,(a.lyear-b.lyear)/10000 as lyear,(a.lyrate-b.lyrate) as lyrate, ");
        sb.append(" (a.changeyear-b.changeyear)/10000 as changeyear,(a.dradj-b.dradj)/10000 as dradj,(a.cradj-b.cradj) as cradj  from ");
        sb.append(" (select a.seq,a.tyear,a.tyrate/100 as tyrate,a.lyear,a.lyrate/100 as lyrate,(a.tyear-a.lyear) as changeyear,b.dradj,b.cradj/100 as cradj from accinmon a, ");
        sb.append(" (select seq,dradj,cradj from accinmon where facno='CK' and accyear = ${y}  and accmon = 1) b ");
        sb.append(" where a.facno='CK' and a.seq = b.seq and a.seq = 1 and a.accyear = ${y}  and a.accmon = ${m} ) a, ");
        sb.append(" ( select a.seq,a.tyear,a.tyrate/100 as tyrate,a.lyear,a.lyrate/100 as lyrate,(a.tyear-a.lyear) as changeyear,b.dradj,b.cradj/100 as cradj from accinmon a, ");
        sb.append(" (select seq,dradj,cradj from accinmon where facno='CK' and accyear = ${y}  and accmon = 1) b ");
        sb.append(" where a.facno='CK' and a.seq = b.seq and a.seq = 2 and a.accyear = ${y}  and a.accmon = ${m} ) b ");
        String sql = sb.toString().replace("${y}", String.valueOf(year(date))).replace("${m}", String.valueOf(month(date)));

        erpEJB.setCompany(facno);
        Query query = erpEJB.getEntityManager().createNativeQuery(sql);
        try {
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    Object[] row = (Object[]) result.get(i);
                    String[] arr = new String[9];
                    arr[0] = df.format(Double.parseDouble(row[1].toString()));
                    arr[1] = dfpercent.format(Double.parseDouble(row[2].toString()) * 100);
                    arr[2] = DFormat(row[3].toString());
                    arr[3] = dfpercent.format(Double.parseDouble(row[4].toString()) * 100);
                    arr[4] = DFormat(row[5].toString());
                    if (Double.parseDouble(row[3].toString()) != 0) {
                        arr[5] = dfpercent.format(Double.parseDouble(row[5].toString()) / Double.parseDouble(row[3].toString()) * 100);
                    } else {
                        if (Double.parseDouble(row[5].toString()) != 0) {
                            arr[5] = dfpercent.format(100);
                        } else {
                            arr[5] = dfpercent.format(0);
                        }
                    }
                    arr[6] = DFormat(row[6].toString());
                    arr[7] = dfpercent.format(Double.parseDouble(row[7].toString()) * 100);
                    if (Double.parseDouble(row[6].toString()) != 0) {
                        arr[8] = dfpercent.format(Double.parseDouble(row[1].toString()) / Double.parseDouble(row[6].toString()) * 100);
                    } else {
                        if (Double.parseDouble(row[1].toString()) != 0) {
                            arr[8] = dfpercent.format(100);
                        } else {
                            arr[8] = dfpercent.format(0);
                        }
                    }
                    map.put(row[0].toString(), arr);
                }
                return map;
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.IncomeStatementBean.yearMap()" + e.toString());
        }
        return null;
    }

    public LinkedHashMap<String, String[]> monthMap(Date date, String facno) {
        LinkedHashMap<String, String[]> map = new LinkedHashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select seq,tmon/10000  as tmon,tmrate/100 as tmrate,lmon/10000 as lmon,lmrate/100 as lmrate ,(tmon-lmon)/10000 as changemon from accinmon ");
        sb.append(" where facno='CK' and accyear = ${y}  and accmon = ${m} ");
        sb.append(" union all ");
        sb.append(" select 999 as seq,(a.tmon-b.tmon)/10000 as tmon,(a.tmrate-b.tmrate)/100 as tmrate, ");
        sb.append(" (a.lmon-b.lmon)/10000 as lmon,(a.lmrate-b.lmrate)/100 as lmrate,(a.changemon -b.changemon)/10000 as changemon from ");
        sb.append(" (select seq,tmon,tmrate,lmon,lmrate ,(tmon-lmon) as changemon  from accinmon ");
        sb.append(" where facno='CK'  and accyear = ${y}  and accmon = ${m} and seq = 1 )  a , ");
        sb.append(" (select seq,tmon,tmrate,lmon,lmrate ,(tmon-lmon) as changemon from accinmon where facno='CK' ");
        sb.append(" and accyear = ${y}  and accmon = ${m} and seq = 2 )  b ");
        String sql = sb.toString().replace("${y}", String.valueOf(year(date))).replace("${m}", String.valueOf(month(date)));

        erpEJB.setCompany(facno);
        Query query = erpEJB.getEntityManager().createNativeQuery(sql);
        try {
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    Object[] row = (Object[]) result.get(i);
                    String[] arr = new String[6];
                    arr[0] = DFormat(row[1].toString());
                    arr[1] = dfpercent.format(Double.parseDouble(row[2].toString()) * 100);
                    arr[2] = DFormat(row[3].toString());
                    arr[3] = dfpercent.format(Double.parseDouble(row[4].toString()) * 100);
                    arr[4] = DFormat(row[5].toString());
                    if (Double.parseDouble(row[3].toString()) != 0) {
                        arr[5] = dfpercent.format(Double.parseDouble(row[5].toString()) / Double.parseDouble(row[3].toString()) * 100);
                    } else {
                        if (Double.parseDouble(row[5].toString()) != 0) {
                            arr[5] = dfpercent.format(100);
                        } else {
                            arr[5] = dfpercent.format(0);
                        }
                    }
                    map.put(row[0].toString(), arr);
                }
                return map;
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.IncomeStatementBean.yearMap()" + e.toString());
        }
        return null;
    }

}
