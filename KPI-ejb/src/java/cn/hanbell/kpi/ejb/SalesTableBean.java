/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.ClientRanking;
import cn.hanbell.kpi.entity.SalesTable;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
public class SalesTableBean extends SuperEJBForKPI<SalesTable> {

    @EJB
    private SalesTableUpdateBean salesTableUpdateBean;

    private final DecimalFormat df;

    public SalesTableBean() {
        super(SalesTable.class);
        this.df = new DecimalFormat("#,##0.00");
    }

    public boolean querySalesTableIsExist(int y, int m, String daString, String typeString) {
        String da = daString == null ? "" : daString;
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT * FROM SalesTable  WHERE type='${type}' and year(cdrdate)=${y} and month(cdrdate)=${m} ");
        if (!"".equals(da) && !"外销零件".equals(da)) {
            sb.append(" and n_code_DA = '").append(da).append("'");
            sb.append(" AND hmark1<>'WXLJ' ");
        } else if ("外销零件".equals(da)) {
            sb.append(" and n_code_CD LIKE 'WX%' AND hmark1='WXLJ' ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", typeString);
        Query query = getEntityManager().createNativeQuery(sql);
        try {
            if (!query.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.SalesTableBean.querySalesTableIsExist()" + e);
        }
        return false;
    }

    private void deleteSales(int y, int m, String daString, String typeString) {
        String da = daString == null ? "" : daString;
        StringBuilder sb = new StringBuilder();
        sb.append(" delete from SalesTable  WHERE type='${type}' and year(cdrdate)=${y} and month(cdrdate)=${m}  ");
        if (!"".equals(da) && !"外销零件".equals(da)) {
            sb.append(" and n_code_DA = '").append(da).append("'");
            sb.append(" AND hmark1<>'WXLJ' ");
        } else if ("外销零件".equals(da)) {
            sb.append(" and n_code_CD LIKE 'WX%' AND hmark1='WXLJ'");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", typeString);
        try {
            Query query = getEntityManager().createNativeQuery(sql);
            int count = query.executeUpdate();
            System.out.println("cn.hanbell.kpi.ejb.SalesTableBean.deleteSales()受影响行数：" + count);
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.SalesTableBean.deleteSales()" + e);
        }

    }

    //更新到KPI
    public boolean updateSalesTable(int y, int m, String daString, String typeString) {
        String da = daString == null ? "" : daString;
        List<SalesTable> SalesTableList = new ArrayList<>();
        if ("Shipment".equals(typeString)) {
            SalesTableList = salesTableUpdateBean.getShipmentListSum(y, m, da, typeString);
        }
        if ("SalesOrder".equals(typeString)) {
            SalesTableList = salesTableUpdateBean.getSalesOrderListSum(y, m, da, typeString);
        }
        if ("ServiceAmount".equals(typeString)) {
            SalesTableList = salesTableUpdateBean.getServiceAmountListSum(y, m, da, typeString);
        }
        try {
            if (SalesTableList != null && !SalesTableList.isEmpty()) {
                if (querySalesTableIsExist(y, m, da, typeString)) {
                    deleteSales(y, m, da, typeString);
                }
                for (SalesTable salesTable : SalesTableList) {
                    salesTable.setStatus("N");
                    super.persist(salesTable);
                }
                return true;
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.SalesTableBean.updateSalesTable()" + e.toString());
        }
        return false;
    }

    //获得总台数
    protected Double getSumQuantity(int y, int m, LinkedHashMap<String, String> map, String type) {
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String style = map.get("style") != null ? map.get("style") : "";

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT sum(quantity) FROM SalesTable where type='${type}' ");
        sb.append(" AND n_code_DA ").append(n_code_DA);
        if (!"".equals(n_code_DC)) {
            sb.append(" AND n_code_DC ").append(n_code_DC);
        }
        sb.append(" AND year(cdrdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(cdrdate) = ${m} ");
        } else {
            sb.append(" and month(cdrdate) BETWEEN 1 AND ${m} ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", type);
        try {
            Query query = getEntityManager().createNativeQuery(sql);
            Object o1 = query.getSingleResult();
            return Double.parseDouble(o1.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }

    //获得总金额
    protected Double getSumAmount(int y, int m, LinkedHashMap<String, String> map, String type) {
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String style = map.get("style") != null ? map.get("style") : "";

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT sum(amount) FROM SalesTable where type='${type}' ");
        sb.append(" AND n_code_DA ").append(n_code_DA);
        if (!"".equals(n_code_DC)) {
            sb.append(" AND n_code_DC ").append(n_code_DC);
        }
        sb.append(" AND year(cdrdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(cdrdate) = ${m} ");
        } else {
            sb.append(" and month(cdrdate) BETWEEN 1 AND ${m} ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", type);
        try {
            Query query = getEntityManager().createNativeQuery(sql);
            Object o1 = query.getSingleResult();
            return Double.parseDouble(o1.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }

    // 返回当前ClientRanking集合getNowClient
    protected List<ClientRanking> getNowClient(int y, int m, LinkedHashMap<String, String> map, String type) {
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String style = map.get("style") != null ? map.get("style") : "";
        String status = map.get("status") != null ? map.get("status") : "";

        StringBuilder sb = new StringBuilder();
        if (status.equals("true")) {
            sb.append(" Select parentcusno,parentcusna,sum(quantity),sum(amount) FROM  SalesTable where type='${type}' ");
        } else {
            sb.append(" Select cusno,cusna,sum(quantity),sum(amount) FROM  SalesTable where type='${type}' ");
        }
        sb.append(" AND n_code_DA ").append(n_code_DA);
        if (!"".equals(n_code_DC)) {
            sb.append(" AND n_code_DC ").append(n_code_DC);
        }
        sb.append(" AND year(cdrdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(cdrdate) = ${m} ");
        } else {
            sb.append(" and month(cdrdate) BETWEEN 1 AND ${m} ");
        }
        if (status.equals("true")) {
            sb.append(" GROUP BY parentcusno ORDER BY sum(amount) desc LIMIT 20 ");
        } else {
            sb.append(" GROUP BY cusno ORDER BY sum(amount) desc LIMIT 20 ");
        }

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", type);
        try {
            ClientRanking ct;
            List<ClientRanking> list = new ArrayList<>();
            Query query = getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    ct = new ClientRanking();
                    Object[] row = (Object[]) result.get(i);
                    ct.setCusno(row[0].toString());
                    ct.setCusna(row[1].toString());
                    ct.setNowrank(String.valueOf(i + 1));
                    ct.setNowshpqy1(row[2].toString());
                    ct.setNowshpamts(row[3].toString());
                    list.add(ct);
                }
                ct = new ClientRanking();
                ct.setCusna("总计");
                ct.setNowshpqy1(String.valueOf(getSumQuantity(y, m, map, type)));
                ct.setNowshpamts(String.valueOf(getSumAmount(y, m, map, type)));
                list.add(ct);
            }
            return list;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ClientNowAndPastBean.getClient()" + e);
        }
        return null;
    }

    // 返回同期ClientRanking集合
    protected List<ClientRanking> getPastClient(int y, int m, LinkedHashMap<String, String> map, String type) {
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String style = map.get("style") != null ? map.get("style") : "";
        String status = map.get("status") != null ? map.get("status") : "";

        StringBuilder sb = new StringBuilder();
        if (status.equals("true")) {
            sb.append(" Select parentcusno,parentcusna,sum(quantity),sum(amount) FROM  SalesTable where type='${type}' ");
        } else {
            sb.append(" Select cusno,cusna,sum(quantity),sum(amount) FROM  SalesTable where type='${type}' ");
        }
        sb.append(" AND n_code_DA ").append(n_code_DA);
        if (!"".equals(n_code_DC)) {
            sb.append(" AND n_code_DC ").append(n_code_DC);
        }
        sb.append(" AND year(cdrdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(cdrdate) = ${m} ");
        } else {
            sb.append(" and month(cdrdate) BETWEEN 1 AND ${m} ");
        }
        if (status.equals("true")) {
            sb.append(" GROUP BY parentcusno ORDER BY sum(amount) desc ");
        } else {
            sb.append(" GROUP BY cusno ORDER BY sum(amount) desc");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", type);
        try {
            ClientRanking ct;
            List<ClientRanking> list = new ArrayList<>();
            Query query = getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    ct = new ClientRanking();
                    Object[] row = (Object[]) result.get(i);
                    ct.setCusno(row[0].toString());
                    ct.setCusna(row[1].toString());
                    ct.setPastrank(String.valueOf(i + 1));
                    ct.setPastshpqy1(row[2].toString());
                    ct.setPastshpamts(row[3].toString());
                    list.add(ct);
                }
                ct = new ClientRanking();
                ct.setCusna("总计");
                ct.setPastshpqy1(String.valueOf(getSumQuantity(y, m, map, type)));
                ct.setPastshpamts(String.valueOf(getSumAmount(y, m, map, type)));
                list.add(ct);
            }
            return list;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ClientNowAndPastBean.getClient()" + e);
        }
        return null;
    }

    public List<ClientRanking> getClientList(int y, int m, LinkedHashMap<String, String> map) {
        String type = "Shipment";
        List<ClientRanking> list = new ArrayList<>();
        //得到已经有排名的list
        //NowClient
        List<ClientRanking> nowList = getNowClient(y, m, map, type);
        //PastClient
        List<ClientRanking> pastList = getPastClient(y - 1, m, map, type);
        //循环nowList 并与 pastList 合并客户
        //其他值
        Double nowothershpqy1, pastothershpqy1;
        Double nowothershpamts, pastothershpamts;
        //TOP20总计
        Double top20nowshpqy1 = 0.0;
        Double top20nowshpamts = 0.0;
        Double top20pastshpqy1 = 0.0;
        Double top20pastshpamts = 0.0;
        try {
            if (nowList != null && !nowList.isEmpty()) {
                ClientRanking now;
                ClientRanking past;
                boolean aa, bb;
                for (int i = 0; i < nowList.size(); i++) {
                    now = nowList.get(i);
                    aa = true;
                    bb = true;
                    //前20项台数金额累加 以计算其他值
                    if (i < 20) {
                        ClientRanking ct = new ClientRanking();
                        if (pastList != null && !pastList.isEmpty()) {
                            for (int j = 0; j < pastList.size(); j++) {
                                past = pastList.get(j);
                                if (now.getCusna().equals(past.getCusna())) {
                                    ct.setCusno(now.getCusno());
                                    ct.setCusna(now.getCusna());
                                    ct.setNowrank(now.getNowrank());
                                    ct.setNowshpqy1(now.getNowshpqy1());
                                    ct.setNowshpamts(df.format(Double.valueOf(now.getNowshpamts())));
                                    ct.setPastrank(past.getPastrank());
                                    ct.setPastshpqy1(past.getPastshpqy1());
                                    ct.setPastshpamts(df.format(Double.valueOf(past.getPastshpamts())));
                                    ct.setDifferencevalue(RTdifferencevalue(now.getNowshpamts(), past.getPastshpamts()));
                                    ct.setGrowthrate(RTgrowthrate(now.getNowshpamts(), past.getPastshpamts()));
                                    //合计TOP20
                                    top20nowshpqy1 += Double.parseDouble(now.getNowshpqy1());
                                    top20nowshpamts += Double.parseDouble(now.getNowshpamts());
                                    top20pastshpqy1 += Double.parseDouble(past.getPastshpqy1());
                                    top20pastshpamts += Double.parseDouble(past.getPastshpamts());
                                    if ((Double.parseDouble(now.getNowshpamts()) - Double.parseDouble(past.getPastshpamts())) < 0) {
                                        ct.setStyle("red");
                                    }
                                    aa = false;
                                    list.add(ct);
                                }
                            }
                        }
                        if (aa) {
                            top20nowshpqy1 += Double.parseDouble(now.getNowshpqy1());
                            top20nowshpamts += Double.parseDouble(now.getNowshpamts());
                            ct.setCusno(now.getCusno());
                            ct.setCusna(now.getCusna());
                            ct.setNowrank(now.getNowrank());
                            ct.setNowshpqy1(now.getNowshpqy1());
                            ct.setNowshpamts(df.format(Double.valueOf(now.getNowshpamts())));
                            ct.setPastshpqy1("0");
                            ct.setPastshpamts("0");
                            ct.setDifferencevalue(df.format(Double.valueOf(now.getNowshpamts())));
                            if (Double.valueOf(now.getNowshpamts()) < 0.0) {
                                ct.setGrowthrate("-100");
                            } else {
                                ct.setGrowthrate("100");
                            }
                            list.add(ct);
                        }
                    }
                    //如果排名大于20 则有其他项
                    if (i >= 20 && now.getCusna().equals("总计")) {
                        nowothershpqy1 = Double.parseDouble(now.getNowshpqy1()) - top20nowshpqy1;
                        nowothershpamts = Double.parseDouble(now.getNowshpamts()) - top20nowshpamts;
                        //去年同期有值时进行其他项计算
                        if (pastList != null && !pastList.isEmpty()) {
                            for (int j = 0; j < pastList.size(); j++) {
                                if (pastList.get(j).getCusna().equals("总计")) {
                                    pastothershpqy1 = Double.parseDouble(pastList.get(j).getPastshpqy1()) - top20pastshpqy1;
                                    pastothershpamts = Double.parseDouble(pastList.get(j).getPastshpamts()) - top20pastshpamts;
                                    ClientRanking ct = new ClientRanking();
                                    ct.setCusna("其他");
                                    ct.setNowshpqy1(String.valueOf(nowothershpqy1));
                                    ct.setNowshpamts(df.format(Math.ceil(nowothershpamts)==0 ? 0 : nowothershpamts));
                                    ct.setPastshpqy1(String.valueOf(pastothershpqy1));
                                    if (new DecimalFormat("#").format(pastothershpamts).equals("0")) {
                                        ct.setPastshpamts("0");
                                        ct.setDifferencevalue(RTdifferencevalue(String.valueOf(nowothershpamts), String.valueOf(pastothershpamts)));
                                        ct.setGrowthrate("100");
                                    } else {
                                        ct.setPastshpamts(df.format(pastothershpamts));
                                        ct.setDifferencevalue(RTdifferencevalue(String.valueOf(nowothershpamts), String.valueOf(pastothershpamts)));
                                        ct.setGrowthrate(RTgrowthrate(String.valueOf(nowothershpamts), String.valueOf(pastothershpamts)));
                                    }
                                    if ((nowothershpamts - pastothershpamts) < 0) {
                                        ct.setStyle("red");
                                    }
                                    if (Double.parseDouble(pastList.get(j).getPastshpamts()) == 0) {
                                        ct.setDifferencevalue("0");
                                        ct.setGrowthrate("100");
                                    }
                                    list.add(ct);
                                    ClientRanking ctsum = new ClientRanking();
                                    ctsum.setCusna(now.getCusna());
                                    ctsum.setNowshpqy1(now.getNowshpqy1());
                                    ctsum.setNowshpamts(df.format(Double.valueOf(now.getNowshpamts())));
                                    ctsum.setPastshpqy1(pastList.get(j).getPastshpqy1());
                                    ctsum.setPastshpamts(df.format(Double.valueOf(pastList.get(j).getPastshpamts())));
                                    ctsum.setDifferencevalue(RTdifferencevalue(now.getNowshpamts(), pastList.get(j).getPastshpamts()));
                                    ctsum.setGrowthrate(RTgrowthrate(now.getNowshpamts(), pastList.get(j).getPastshpamts()));
                                    if ((Double.parseDouble(now.getNowshpamts()) - Double.parseDouble(pastList.get(j).getPastshpamts())) < 0) {
                                        ctsum.setStyle("red");
                                    }
                                    if (Double.parseDouble(pastList.get(j).getPastshpamts()) == 0) {
                                        ctsum.setDifferencevalue("0");
                                        ctsum.setGrowthrate("100");
                                    } else {
                                        ctsum.setDifferencevalue(RTdifferencevalue(now.getNowshpamts(), pastList.get(j).getPastshpamts()));
                                        ctsum.setGrowthrate(RTgrowthrate(now.getNowshpamts(), pastList.get(j).getPastshpamts()));
                                    }
                                    list.add(ctsum);

                                }
                            }
                            //去年同期无值则给固定值
                        } else {
                            ClientRanking ct = new ClientRanking();
                            ct.setCusna("其他");
                            ct.setNowshpqy1(String.valueOf(nowothershpqy1));
                            ct.setNowshpamts(df.format(nowothershpamts));
                            ct.setPastshpqy1("0");
                            ct.setPastshpamts("0");
                            ct.setDifferencevalue(df.format(nowothershpamts));
                            if ((nowothershpamts - 0) < 0) {
                                ct.setStyle("red");
                                ct.setGrowthrate("-100");
                            } else {
                                ct.setGrowthrate("100");
                            }
                            list.add(ct);
                            ClientRanking ctsum = new ClientRanking();
                            ctsum.setCusna(now.getCusna());
                            ctsum.setNowshpqy1(now.getNowshpqy1());
                            ctsum.setNowshpamts(df.format(Double.valueOf(now.getNowshpamts())));
                            ctsum.setPastshpqy1("0");
                            ctsum.setPastshpamts("0");
                            ctsum.setDifferencevalue(df.format(Double.valueOf(now.getNowshpamts())));
                            ctsum.setGrowthrate("100");
                            list.add(ctsum);
                        }
                    }
                }

            }
            return list;
        } catch (Exception e) {
            return list;
        }
    }

    //得到台数无小数点
    protected String RTshpqy1(Double a) {
        return String.valueOf(a.intValue());
    }

    //得到合计台数
    protected String RTshpqy1(String a, String b) {
        return String.valueOf(Double.valueOf(a).intValue() + Double.valueOf(b).intValue());
    }

    //得到合计金额
    protected String RTshpamts(String a, String b) {
        return String.valueOf(Double.valueOf(a) + Double.valueOf(b));
    }

    // 同比差异值 = 本年累计金额 - 去年同期累计金额
    protected String RTdifferencevalue(String a, String b) {
        return df.format(Double.parseDouble(a) - Double.parseDouble(b));
    }

    // 同比成长率 = (本年累计金额 - 去年同期累计金额)/去年同期累计金额*100
    protected String RTgrowthrate(String a, String b) {
        return df.format((Double.parseDouble(a) - Double.parseDouble(b)) / Double.parseDouble(b) * 100);
    }

}
