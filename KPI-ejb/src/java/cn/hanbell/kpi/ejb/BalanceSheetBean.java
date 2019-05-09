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
public class BalanceSheetBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;
    private final DecimalFormat df;
    private final DecimalFormat dfpercent;

    public BalanceSheetBean() {
        this.df = new DecimalFormat("#,##0.00");
        this.dfpercent = new DecimalFormat("0.00％");
    }

    private int findyear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    private int findmonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    private List assetlist(Date date,String facno) {
        StringBuilder sb = new StringBuilder();

        sb.append(" select 998 AS seq,sum(end1/10000) as now,sum(begin1/10000 ) as past,sum((end1-begin1)/10000 ) as difference ");
        sb.append(" from accbalmon where facno='${facno}'AND  seq in(3,9,10,13,15) and accyear=${y}  and accmon=${m} ");
        sb.append(" union all ");
        sb.append(" select 999 AS seq,sum(end1/10000 ) as now,sum(begin1/10000 ) as past,sum((end1-begin1)/10000 ) as difference ");
        sb.append("  from accbalmon where facno='${facno}' AND seq in(19,20,21,40,41,42,43,44) and accyear=${y} and accmon=${m} ");

        String sql = sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(findyear(date))).replace("${m}", String.valueOf(findmonth(date)));

        erpEJB.setCompany(facno.equals("CK")?"C":facno);
        try {
            Query query = erpEJB.getEntityManager().createNativeQuery(sql);
            return query.getResultList();

        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.BalanceSheetBean.assetMAP()" + e.toString());
        }
        return null;
    }

    public LinkedHashMap<String, String[]> assetMap(Date date,String facno) {
        LinkedHashMap<String, String[]> map = new LinkedHashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select seq,(end1/10000 ) as now,(begin1/10000 ) as past,((end1-begin1)/10000 ) as difference ");
        sb.append(" from accbalmon  where facno='${facno}' and accyear=${y}  and accmon=${m} ");

        String sql = sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(findyear(date))).replace("${m}", String.valueOf(findmonth(date)));

        erpEJB.setCompany(facno.equals("CK")?"C":facno);
        try {
            Query query = erpEJB.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    Object[] row = (Object[]) result.get(i);
                    String[] arr = new String[4];
                    arr[0] = df.format(Double.parseDouble(row[2].toString()));
                    arr[1] = df.format(Double.parseDouble(row[1].toString()));
                    arr[2] = df.format(Double.parseDouble(row[3].toString()));
                    if (Double.parseDouble(row[2].toString()) != 0) {
                        arr[3] = dfpercent.format(Double.parseDouble(row[3].toString()) / Double.parseDouble(row[2].toString()) * 100);
                    } else {
                        if (Double.parseDouble(row[1].toString()) == 0) {
                            arr[3] = dfpercent.format(0);
                        } else {
                            arr[3] = dfpercent.format(100);
                        }
                    }
                    map.put(row[0].toString(), arr);
                }
                List list = assetlist(date,facno);
                for (int i = 0; i < list.size(); i++) {
                    Object[] row = (Object[]) list.get(i);
                    String[] arr = new String[4];
                    arr[0] = df.format(Double.parseDouble(row[2].toString()));
                    arr[1] = df.format(Double.parseDouble(row[1].toString()));
                    arr[2] = df.format(Double.parseDouble(row[3].toString()));
                    if (Double.parseDouble(row[2].toString()) != 0) {
                        arr[3] = dfpercent.format(Double.parseDouble(row[3].toString()) / Double.parseDouble(row[2].toString()) * 100);
                    } else {
                        if (Double.parseDouble(row[1].toString()) == 0) {
                            arr[3] = dfpercent.format(0);
                        } else {
                            arr[3] = dfpercent.format(100);
                        }
                    }
                    map.put(row[0].toString(), arr);
                }
                return map;
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.BalanceSheetBean.assetMAP()" + e.toString());
        }
        return null;
    }

    public List liabilitiesList(Date date,String facno) {
        StringBuilder sb = new StringBuilder();

        sb.append(" select 998 AS seq,sum(end2/10000 ) as now,sum(begin2/10000 ) as past,sum((end2-begin2)/10000 ) as difference ");
        sb.append(" from accbalmon where facno='${facno}' AND  seq in(3,9,11,12) and accyear=${y} and accmon=${m} ");
        sb.append(" union all ");
        sb.append(" select 999 AS seq,sum(end2/10000 ) as now,sum(begin2/10000 ) as past,sum((end2-begin2)/10000 ) as difference ");
        sb.append(" from accbalmon where facno='${facno}' AND  seq in(20,21,22,23,24,25,26) and accyear=${y} and accmon=${m}");
        sb.append(" union all ");
        sb.append(" select 1000 AS seq,sum(end2/10000 ) as now,sum(begin2/10000 ) as past,sum((end2-begin2)/10000 ) as difference ");
        sb.append(" from accbalmon where facno='${facno}' AND  seq in(43,44) and accyear=${y} and accmon=${m}");

        String sql = sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(findyear(date))).replace("${m}", String.valueOf(findmonth(date)));

        erpEJB.setCompany(facno.equals("CK")?"C":facno);
        try {
            Query query = erpEJB.getEntityManager().createNativeQuery(sql);
            return query.getResultList();

        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.BalanceSheetBean.assetMAP()" + e.toString());
        }
        return null;
    }

    public LinkedHashMap<String, String[]> liabilitiesMap(Date date,String facno) {
        LinkedHashMap<String, String[]> map = new LinkedHashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select seq,end2/10000 as now,begin2/10000 as past,(end2-begin2)/10000 as difference ");
        sb.append(" from accbalmon where facno='${facno}' and accyear=${y} and accmon=${m} ");

        String sql = sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(findyear(date))).replace("${m}", String.valueOf(findmonth(date)));

        erpEJB.setCompany(facno.equals("CK")?"C":facno);
        try {
            Query query = erpEJB.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    Object[] row = (Object[]) result.get(i);
                    String[] arr = new String[4];
                    arr[0] = df.format(Double.parseDouble(row[2].toString()));
                    arr[1] = df.format(Double.parseDouble(row[1].toString()));
                    arr[2] = df.format(Double.parseDouble(row[3].toString()));
                    if (Double.parseDouble(row[2].toString()) != 0) {
                        arr[3] = dfpercent.format(Double.parseDouble(row[3].toString()) / Double.parseDouble(row[2].toString()) * 100);
                    } else {
                        if (Double.parseDouble(row[1].toString()) == 0) {
                            arr[3] = dfpercent.format(0);
                        } else {
                            arr[3] = dfpercent.format(100);
                        }
                    }
                    map.put(row[0].toString(), arr);
                }
                List list = liabilitiesList(date,facno);
                for (int i = 0; i < list.size(); i++) {
                    Object[] row = (Object[]) list.get(i);
                    String[] arr = new String[4];
                    arr[0] = df.format(Double.parseDouble(row[2].toString()));
                    arr[1] = df.format(Double.parseDouble(row[1].toString()));
                    arr[2] = df.format(Double.parseDouble(row[3].toString()));
                    if (Double.parseDouble(row[2].toString()) != 0) {
                        arr[3] = dfpercent.format(Double.parseDouble(row[3].toString()) / Double.parseDouble(row[2].toString()) * 100);
                    } else {
                        if (Double.parseDouble(row[1].toString()) == 0) {
                            arr[3] = dfpercent.format(0);
                        } else {
                            arr[3] = dfpercent.format(100);
                        }
                    }
                    map.put(row[0].toString(), arr);
                }
                return map;
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.BalanceSheetBean.assetMAP()" + e.toString());
        }
        return null;
    }

    //现金流量表
    public LinkedHashMap<String, String[]> getCashFlowMap(Date date) {
        LinkedHashMap<String, String[]> map = new LinkedHashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT itemname,seq, ");
        sb.append(" isnull(sum(case when yea=${y} AND mon BETWEEN 1 and ${m} then adjamt ELSE 0 END),0), ");
        sb.append(" isnull(sum(case when yea=").append(findyear(date) - 1);
        sb.append(" AND mon BETWEEN 1 and ${m} then adjamt ELSE 0 END),0), ");
        sb.append(" isnull(sum(case when yea=${y} AND mon BETWEEN 1 and ${m} then adjamt ELSE 0 END),0)- ");
        sb.append(" isnull(sum(case when yea=").append(findyear(date) - 1);
        sb.append(" AND mon BETWEEN 1 and ${m} then adjamt ELSE 0 END),0) ");
        sb.append(" FROM acccashmas m ,acccash h WHERE  m.itemno=h.itemno AND m.report=h.report AND facno='C' AND coin='RMB' and h.report='2' ");
        sb.append(" GROUP BY itemname,facno,seq,h.report,m.itemno,coin ORDER BY seq ASC ");

        String sql = sb.toString().replace("${y}", String.valueOf(findyear(date))).replace("${m}", String.valueOf(findmonth(date)));
        erpEJB.setCompany("C");
        try {
            Query query = erpEJB.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    Object[] row = (Object[]) result.get(i);
                    String[] arr = new String[5];
                    arr[0] = row[0].toString();
                    arr[1] = df.format(Double.parseDouble(row[2].toString())/10000);
                    arr[2] = df.format(Double.parseDouble(row[3].toString())/10000);
                    arr[3] = df.format(Double.parseDouble(row[4].toString())/10000);
                    if (Double.parseDouble(row[3].toString()) != 0.00) {
                        arr[4] = dfpercent.format(Double.parseDouble(row[4].toString()) / Double.parseDouble(row[3].toString()) * 100);
                    } else {
                        if (Double.parseDouble(row[4].toString()) == 0.00) {
                            arr[4] = dfpercent.format(0);
                        } else {
                            arr[4] = dfpercent.format(100);
                        }
                    }
                    map.put(row[1].toString(), arr);
                }
                return map;
            }
        } catch (Exception e) {
        }
        return null;
    }
}
