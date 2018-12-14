/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.entity.ClientRanking;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
public class ClientNowAndPastBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;
    private final DecimalFormat df;
    @EJB
    protected ClientTableBean clientTableBean;

    public ClientNowAndPastBean() {
        this.df = new DecimalFormat("#,##0.00");
    }

    //出货台数SQL
    protected String getQuantitySql(int y, int m, String facno, LinkedHashMap<String, String> map) {
        String decode = map.get("decode") != null ? map.get("decode") : "";
        String depno = map.get("depno") != null ? map.get("depno") : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD") : "";
        String style = map.get("style") != null ? map.get("style") : "";
        StringBuilder sb = new StringBuilder();
        sb.append(" select  z.cusno as 'cusno' ,c.cusna as 'cusna' ,z.num  as 'shpqy1' from ( ");
        sb.append(" select x.cusno,sum(num) as num  from ( ");
        sb.append(" select h.cusno,sum(shpqy1) as num  from cdrdta d left join cdrhad h on d.shpno=h.shpno  ");
        sb.append(" where h.facno='${facno}' and h.houtsta <> 'W' ");
        if (n_code_DA.equals("= 'AA'")) {
            sb.append(" and left(d.itnbr,1)='3' ");
        } else {
            sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N'  ");
        }
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        // 当无产品别时，则按大类查询
        //当查询年限超过2017年则按产品别查询，否则按部门区分
        if (!"".equals(n_code_DC)) {
            if (y > 2017) {
                sb.append(" and d.n_code_DC ").append(n_code_DC);
            } else {
                if (!"".equals(depno)) {
                    sb.append(" and h.depno ").append(depno);
                }
            }
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ='00' ");
        }
        sb.append(" and year(h.shpdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.shpdate) = ${m} ");
        } else {
            sb.append(" and month(h.shpdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by  h.cusno ");
        sb.append(" union all ");
        sb.append(" select  h.cusno,-sum(bshpqy1) as num  from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno ");
        sb.append(" where h.baksta <> 'W'  and  h.facno='${facno}' ");
        if (n_code_DA.equals("= 'AA'")) {
            sb.append(" and left(d.itnbr,1)='3' ");
        } else {
            sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N'  ");
        }
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        //当查询年限超过2017年则按产品别查询，否则按部门区分
        if (!"".equals(n_code_DC)) {
            if (y > 2017) {
                sb.append(" and d.n_code_DC ").append(n_code_DC);
            } else {
                if (!"".equals(depno)) {
                    sb.append(" and h.depno ").append(depno);
                }
            }
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ='00' ");
        }
        sb.append(" and year(h.bakdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.bakdate)= ${m} ");
        } else {
            sb.append(" and month(h.bakdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by  h.cusno ) ");
        sb.append(" x  group by x.cusno ) ");
        sb.append(" z,cdrcus c  where z.cusno=c.cusno ");

        return sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
    }

    //出货金额SQL
    protected String getAmountSql(int y, int m, String facno, LinkedHashMap<String, String> map) {
        String decode = map.get("decode") != null ? map.get("decode") : "";
        String depno = map.get("depno") != null ? map.get("depno") : "";
        String ogdkid = map.get("ogdkid") != null ? map.get("ogdkid") : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String style = map.get("style") != null ? map.get("style") : "";
        StringBuilder sb = new StringBuilder();
        sb.append(" select  z.cusno as 'cusno' ,c.cusna as 'cusna' ,z.num  as 'shpamts' from ( ");
        sb.append(" select x.cusno,sum(num) as num  from ( ");
        sb.append(" select  h.cusno,isnull(convert(decimal(16,2),sum((d.shpamts * h.ratio)/(h.taxrate + 1))),0) as num ");
        sb.append(" from cdrdta d left join cdrhad h on d.shpno=h.shpno ");
        sb.append(" where h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' and h.facno='${facno}' ");
        sb.append(" and h.houtsta <> 'W' ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        //当查询年限超过2017年则按产品别查询，否则按部门区分
        if (!"".equals(n_code_DC)) {
            if (y > 2017) {
                sb.append(" and d.n_code_DC ").append(n_code_DC);
            } else {
                if (!"".equals(depno)) {
                    sb.append(" and h.depno ").append(depno);
                }
            }
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.shpdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.shpdate) = ${m} ");
        } else {
            sb.append(" and month(h.shpdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by  h.cusno ");
        sb.append(" union all ");
        sb.append(" select  h.cusno,isnull(convert(decimal(16,2),-sum((d.bakamts * h.ratio)/(h.taxrate + 1))),0) as num ");
        sb.append(" from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno ");
        sb.append(" where h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' and h.facno='${facno}' ");
        sb.append(" and h.baksta <> 'W' ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        //当查询年限超过2017年则按产品别查询，否则按部门区分
        if (!"".equals(n_code_DC)) {
            if (y > 2017) {
                sb.append(" and d.n_code_DC ").append(n_code_DC);
            } else {
                if (!"".equals(depno)) {
                    sb.append(" and h.depno ").append(depno);
                }
            }
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.bakdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.bakdate)= ${m} ");
        } else {
            sb.append(" and month(h.bakdate) BETWEEN 1 AND ${m} ");
        }
        //机体机组算入折让
        sb.append(" group by  h.cusno ");
        sb.append(" union all ");
        //加扣款
        sb.append(" SELECT h.cusno AS 'cusno', ISNULL(SUM(CASE h.amtco WHEN 'P' THEN d.psamt WHEN 'M' THEN d.psamt *(-1) ELSE 0 END),0) AS num FROM armpmm h,armacq d,cdrdta s ");
        sb.append(" WHERE h.facno=d.facno AND h.trno = d.trno AND d.facno = s.facno AND d.shpno=s.shpno AND d.shpseq = s.trseq  AND h.facno='${facno}' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and s.n_code_DA ").append(n_code_DA);
        }
        //当查询年限超过2017年则按产品别查询，否则按部门区分
        if (!"".equals(n_code_DC)) {
            if (y > 2017) {
                sb.append(" and s.n_code_DC ").append(n_code_DC);
            } else {
                if (!"".equals(depno)) {
                    sb.append(" and d.depno ").append(depno);
                }
            }
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and s.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.trdat) = ${y}  ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.trdat) = ${m} ");
        } else {
            sb.append(" and month(h.trdat) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by  h.cusno ");
        sb.append(" union all ");
        //--折让
        sb.append(" SELECT d.ivocus AS 'cusno',ISNULL(sum(d.recamt),0) AS num FROM armrec d,armrech h where d.facno=h.facno AND d.recno=h.recno ");
        sb.append("  AND h.prgno='ARM423' AND h.recstat='1' AND d.raccno='6001' AND h.facno='${facno}' ");
        if (!"".equals(ogdkid)) {
            sb.append(" AND h.ogdkid ").append(ogdkid);
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and h.n_code_DA ").append(n_code_DA);
        }
        //当查询年限超过2017年则按产品别查询，否则按部门区分
        if (!"".equals(n_code_DC)) {
            if (y > 2017) {
                sb.append(" and h.n_code_DC ").append(n_code_DC);
            } else {
                if (!"".equals(depno)) {
                    sb.append(" and h.depno ").append(depno);
                }
            }
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" AND h.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.recdate) = ${y}  ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.recdate) = ${m} ");
        } else {
            sb.append(" and month(h.recdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" GROUP BY d.ivocus ");
        sb.append(" union all ");
        //它项金额,关联部门 发票
        sb.append(" SELECT h.cusno AS 'cusno', ISNULL(sum(h.shpamt),0) AS  num FROM armbil h WHERE h.rkd='RQ11' AND h.facno='${facno}' ");
        sb.append("  AND h.depno ").append(depno);
        sb.append(" and year(h.bildat) = ${y}  ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.bildat) = ${m} ");
        } else {
            sb.append(" and month(h.bildat) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by  h.cusno ) ");
        sb.append(" x  group by x.cusno ) ");
        sb.append(" z,cdrcus c where z.cusno=c.cusno ");
        return sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
    }

    // 返回ClientRanking集合
    protected List getClient(int y, int m, String arr1, LinkedHashMap<String, String> map) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(" select  a.cusno as 'cusno',a.cusna as 'cusna',a.shpqy1 as 'shpqy1',b.shpamts as 'shpamts' from ( ");
            sb.append(getQuantitySql((y), m, arr1, map));
            sb.append(" ) a,( ");
            sb.append(getAmountSql((y), m, arr1, map));
            sb.append(" ) b where a.cusno=b.cusno  and a.cusna  =b.cusna ORDER BY shpamts DESC ");
            erpEJB.setCompany(arr1);
            Query query = erpEJB.getEntityManager().createNativeQuery(sb.toString());
            List result = query.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ClientNowAndPastBean.getClient()" + e);
        }
        return null;
    }

    //得到总金额
    protected Double getSumAmount(int y, int m, String arr1, LinkedHashMap<String, String> map) {
        String decode = map.get("decode") != null ? map.get("decode") : "";
        String depno = map.get("depno") != null ? map.get("depno") : "";
        String ogdkid = map.get("ogdkid") != null ? map.get("ogdkid") : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String style = map.get("style") != null ? map.get("style") : "";
        StringBuilder sb = new StringBuilder();
        sb.append(" select  isnull(SUM(z.num ),0) from ( ");
        sb.append(" select x.cusno,sum(num) as num  from ( ");
        sb.append(" select  h.cusno,isnull(convert(decimal(16,2),sum((d.shpamts * h.ratio)/(h.taxrate + 1))),0) as num ");
        sb.append(" from cdrdta d left join cdrhad h on d.shpno=h.shpno ");
        sb.append(" where h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' and h.facno='${facno}' ");
        sb.append(" and h.houtsta <> 'W' ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        //当查询年限超过2017年则按产品别查询，否则按部门区分
        if (!"".equals(n_code_DC)) {
            if (y > 2017) {
                sb.append(" and d.n_code_DC ").append(n_code_DC);
            } else {
                if (!"".equals(depno)) {
                    sb.append(" and h.depno ").append(depno);
                }
            }
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.shpdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.shpdate) = ${m} ");
        } else {
            sb.append(" and month(h.shpdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by  h.cusno ");
        sb.append(" union all ");
        sb.append(" select  h.cusno,isnull(convert(decimal(16,2),-sum((d.bakamts * h.ratio)/(h.taxrate + 1))),0) as num ");
        sb.append(" from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno ");
        sb.append(" where h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' and h.facno='${facno}' ");
        sb.append(" and h.baksta <> 'W' ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        //当查询年限超过2017年则按产品别查询，否则按部门区分
        if (!"".equals(n_code_DC)) {
            if (y > 2017) {
                sb.append(" and d.n_code_DC ").append(n_code_DC);
            } else {
                if (!"".equals(depno)) {
                    sb.append(" and h.depno ").append(depno);
                }
            }
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.bakdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.bakdate)= ${m} ");
        } else {
            sb.append(" and month(h.bakdate) BETWEEN 1 AND ${m} ");
        }
        //机体机组算入折让
        sb.append(" group by  h.cusno ");
        sb.append(" union all ");
        //加扣款
        sb.append(" SELECT h.cusno AS 'cusno', ISNULL(SUM(CASE h.amtco WHEN 'P' THEN d.psamt WHEN 'M' THEN d.psamt *(-1) ELSE 0 END),0) AS num FROM armpmm h,armacq d,cdrdta s ");
        sb.append(" WHERE h.facno=d.facno AND h.trno = d.trno AND d.facno = s.facno AND d.shpno=s.shpno AND d.shpseq = s.trseq  AND h.facno='${facno}' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and s.n_code_DA ").append(n_code_DA);
        }
        //当查询年限超过2017年则按产品别查询，否则按部门区分
        if (!"".equals(n_code_DC)) {
            if (y > 2017) {
                sb.append(" and s.n_code_DC ").append(n_code_DC);
            } else {
                if (!"".equals(depno)) {
                    sb.append(" and d.depno ").append(depno);
                }
            }
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and s.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.trdat) = ${y}  ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.trdat) = ${m} ");
        } else {
            sb.append(" and month(h.trdat) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by  h.cusno ");
        sb.append(" union all ");
        //--折让
        sb.append(" SELECT d.ivocus AS 'cusno',ISNULL(sum(d.recamt),0) AS num FROM armrec d,armrech h where d.facno=h.facno AND d.recno=h.recno ");
        sb.append("  AND h.prgno='ARM423' AND h.recstat='1' AND d.raccno='6001' AND h.facno='${facno}' ");
        if (!"".equals(ogdkid)) {
            sb.append(" AND h.ogdkid ").append(ogdkid);
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and h.n_code_DA ").append(n_code_DA);
        }
        //当查询年限超过2017年则按产品别查询，否则按部门区分
        if (!"".equals(n_code_DC)) {
            if (y > 2017) {
                sb.append(" and h.n_code_DC ").append(n_code_DC);
            } else {
                if (!"".equals(depno)) {
                    sb.append(" and h.depno ").append(depno);
                }
            }
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" AND h.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.recdate) = ${y}  ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.recdate) = ${m} ");
        } else {
            sb.append(" and month(h.recdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" GROUP BY d.ivocus ");
        sb.append(" union all ");
        //它项金额,关联部门 发票
        sb.append(" SELECT h.cusno AS 'cusno', ISNULL(sum(h.shpamt),0) AS  num FROM armbil h WHERE h.rkd='RQ11' AND h.facno='${facno}' ");
        sb.append("  AND h.depno ").append(depno);
        sb.append(" and year(h.bildat) = ${y}  ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.bildat) = ${m} ");
        } else {
            sb.append(" and month(h.bildat) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by  h.cusno ) ");
        sb.append(" x  group by x.cusno ) ");
        sb.append(" z,cdrcus c where z.cusno=c.cusno ");
        String sql = sb.toString().replace("${facno}", arr1).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        erpEJB.setCompany(arr1);
        try {
            Query query = erpEJB.getEntityManager().createNativeQuery(sql);
            Object o1 = query.getSingleResult();
            return Double.parseDouble(o1.toString());
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ClientNowAndPastBean.getSumAmount()" + e);
        }
        return 0.0;
    }

    //当前
    public List<ClientRanking> getNowClient(int y, int m, LinkedHashMap<String, String> map) {
        String facno = map.get("facno") != null ? map.get("facno") : "";
        //查询当前值并赋予排名
        String[] arr = facno.split(",");
        //过渡list
        List<ClientRanking> list;
        //汇总返回list
        List<ClientRanking> returnlist = new ArrayList<>();
        ClientRanking ct;
        boolean aa;
        //总金额
        Double sumshpamts = 0.0;
        try {
            for (String arr1 : arr) {
                list = new ArrayList<>();
                sumshpamts += getSumAmount(y, m, arr1, map);
                List result = getClient(y, m, arr1, map);
                if (result != null && !result.isEmpty()) {
                    for (int i = 0; i < result.size(); i++) {
                        ct = new ClientRanking();
                        Object[] row = (Object[]) result.get(i);
                        ct.setCusno(row[0].toString());
                        ct.setCusna(row[1].toString());
                        ct.setNowshpqy1(RTshpqy1(row[2].toString()));
                        ct.setNowshpamts(row[3].toString());
                        list.add(ct);
                    }
                    //汇总
                    //循环list数据往returnlist合并数据
                    if (!list.isEmpty() && returnlist.isEmpty()) {
                        returnlist = list;
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            aa = true;
                            for (int j = 0; j < returnlist.size(); j++) {
                                //list客户与returnlist客户相同 则台数与金额 returnlist = returnlist + list
                                if (list.get(i).getCusna().equals(returnlist.get(j).getCusna())) {
                                    returnlist.get(j).setNowshpqy1(RTshpqy1(returnlist.get(j).getNowshpqy1(), list.get(i).getNowshpqy1()));
                                    returnlist.get(j).setNowshpamts(RTshpamts(returnlist.get(j).getNowshpamts(), list.get(i).getNowshpamts()));
                                    aa = false;
                                }
                            }
                            if (aa) {
                                returnlist.add(list.get(i));
                            }
                        }
                    }
                }
            }
            //客户排名
            int min;
            for (int i = 0; i < returnlist.size() - 1; i++) {
                min = i;
                ct = new ClientRanking();
                for (int j = i + 1; j < returnlist.size(); j++) {
                    if (Double.valueOf(returnlist.get(j).getNowshpamts()) > Double.valueOf(returnlist.get(min).getNowshpamts())) {
                        min = j;
                    }
                }
                if (min != i) {
                    ct = returnlist.get(i);
                    returnlist.set(i, returnlist.get(min));
                    returnlist.set(min, ct);
                }

            }
            if (returnlist != null && !returnlist.isEmpty()) {
                int sumshpqy1 = 0;
                ct = new ClientRanking();
                for (int i = 0; i < returnlist.size(); i++) {
                    returnlist.get(i).setNowrank(String.valueOf(i + 1));
                    sumshpqy1 += Integer.parseInt(returnlist.get(i).getNowshpqy1());
                }
                ct.setCusna("总计");
                ct.setNowshpqy1(String.valueOf(sumshpqy1));
                ct.setNowshpamts(String.valueOf(sumshpamts));
                returnlist.add(ct);
            }
            return returnlist;
        } catch (Exception e) {
            return null;
        }
    }

    //去年同期
    public List<ClientRanking> getPastClient(int y, int m, LinkedHashMap<String, String> map) {
        String facno = map.get("facno") != null ? map.get("facno") : "";
        //查询去年同期值并赋予排名
        String[] arr = facno.split(",");
        //过渡list
        List<ClientRanking> list;
        //汇总返回list
        List<ClientRanking> returnlist = new ArrayList<>();
        ClientRanking ct;
        boolean aa;
        //总金额
        Double sumshpamts = 0.0;
        try {
            for (String arr1 : arr) {
                list = new ArrayList<>();
                sumshpamts += getSumAmount(y - 1, m, arr1, map);
               List result = getClient(y - 1, m, arr1, map);
                if (result != null && !result.isEmpty()) {
                    for (int i = 0; i < result.size(); i++) {
                        ct = new ClientRanking();
                        Object[] row = (Object[]) result.get(i);
                        ct.setCusno(row[0].toString());
                        ct.setCusna(row[1].toString());
                        ct.setPastshpqy1(RTshpqy1(row[2].toString()));
                        ct.setPastshpamts(row[3].toString());
                        list.add(ct);
                    }
                    //汇总
                    //循环list数据往returnlist合并数据
                    if (!list.isEmpty() && returnlist.isEmpty()) {
                        returnlist = list;
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            aa = true;
                            for (int j = 0; j < returnlist.size(); j++) {
                                //list客户与returnlist客户相同 则台数与金额 returnlist = returnlist + list
                                if (list.get(i).getCusno().equals(returnlist.get(j).getCusno())) {
                                    returnlist.get(j).setPastshpqy1(RTshpqy1(returnlist.get(j).getPastshpqy1(), list.get(i).getPastshpqy1()));
                                    returnlist.get(j).setPastshpamts(RTshpamts(returnlist.get(j).getPastshpamts(), list.get(i).getPastshpamts()));
                                    aa = false;
                                }
                            }
                            if (aa) {
                                returnlist.add(list.get(i));
                            }
                        }
                    }
                }
            }
            //客户排名
            int min;
            for (int i = 0; i < returnlist.size() - 1; i++) {
                min = i;
                ct = new ClientRanking();
                for (int j = i + 1; j < returnlist.size(); j++) {
                    if (Double.valueOf(returnlist.get(j).getPastshpamts()) > Double.valueOf(returnlist.get(min).getPastshpamts())) {
                        min = j;
                    }
                }
                if (min != i) {
                    ct = returnlist.get(i);
                    returnlist.set(i, returnlist.get(min));
                    returnlist.set(min, ct);
                }
            }
            if (returnlist != null && !returnlist.isEmpty()) {
                int sumshpqy1 = 0;
                ct = new ClientRanking();
                for (int i = 0; i < returnlist.size(); i++) {
                    returnlist.get(i).setPastrank(String.valueOf(i + 1));
                    sumshpqy1 += Integer.parseInt(returnlist.get(i).getPastshpqy1());
                }
                ct.setCusna("总计");
                ct.setPastshpqy1(String.valueOf(sumshpqy1));
                ct.setPastshpamts(String.valueOf(sumshpamts));
                returnlist.add(ct);
            }
            return returnlist;
        } catch (Exception e) {
            return null;
        }
    }

    //得到查询结果
    public List<ClientRanking> getClientList(int y, int m, LinkedHashMap<String, String> map) {
        List<ClientRanking> list = new LinkedList<>();
        //得到已经有排名的list
        //NowClient
        List<ClientRanking> nowList = getNowClient(y, m, map);
        //PastClient
        List<ClientRanking> pastList = getPastClient(y, m, map);
        //循环nowList 并与 pastList 合并客户
        //其他值
        int nowothershpqy1, pastothershpqy1;
        Double nowothershpamts, pastothershpamts;
        //TOP20总计
        int top20nowshpqy1 = 0;
        Double top20nowshpamts = 0.0;
        int top20pastshpqy1 = 0;
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
                                    top20nowshpqy1 += Integer.parseInt(now.getNowshpqy1());
                                    top20nowshpamts += Double.parseDouble(now.getNowshpamts());
                                    top20pastshpqy1 += Integer.parseInt(past.getPastshpqy1());
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
                            top20nowshpqy1 += Integer.parseInt(now.getNowshpqy1());
                            top20nowshpamts += Double.parseDouble(now.getNowshpamts());
                            ct.setCusno(now.getCusno());
                            ct.setCusna(now.getCusna());
                            ct.setNowrank(now.getNowrank());
                            ct.setNowshpqy1(now.getNowshpqy1());
                            ct.setNowshpamts(df.format(Double.valueOf(now.getNowshpamts())));
                            ct.setPastshpqy1("0");
                            ct.setPastshpamts("0");
                            ct.setDifferencevalue(df.format(Double.valueOf(now.getNowshpamts())));
                            ct.setGrowthrate("100");
                            list.add(ct);
                        }
                    }
                    //如果排名大于20 则有其他项
                    if (i >= 20 && now.getCusna().equals("总计")) {
                        nowothershpqy1 = Integer.parseInt(now.getNowshpqy1()) - top20nowshpqy1;
                        nowothershpamts = Double.parseDouble(now.getNowshpamts()) - top20nowshpamts;
                        //去年同期有值时进行其他项计算
                        if (pastList != null && !pastList.isEmpty()) {
                            for (int j = 0; j < pastList.size(); j++) {
                                if (pastList.get(j).getCusna().equals("总计")) {
                                    pastothershpqy1 = Integer.parseInt(pastList.get(j).getPastshpqy1()) - top20pastshpqy1;
                                    pastothershpamts = Double.parseDouble(pastList.get(j).getPastshpamts()) - top20pastshpamts;
                                    ClientRanking ct = new ClientRanking();
                                    ct.setCusna("其他");
                                    ct.setNowshpqy1(String.valueOf(nowothershpqy1));
                                    ct.setNowshpamts(df.format(nowothershpamts < 0 ? 0 : nowothershpamts));
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

    // 得到台数无小数点
    protected String RTshpqy1(String a) {
        return String.valueOf(Double.valueOf(a).intValue());
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

    //制冷冷冻大于2018年SQL
    protected String getNowSqlRL(int y, int m, String facno, LinkedHashMap<String, String> map) {
        String decode = map.get("decode") != null ? map.get("decode") : "";
        String depno = map.get("depno") != null ? map.get("depno") : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String style = map.get("style") != null ? map.get("style") : "";

        StringBuilder sb = new StringBuilder();
        sb.append(" select  a.cusno as 'cusno',a.cusna as 'cusna',a.shpqy1 as 'shpqy1',b.shpamts as 'shpamts' from ( ");
        //台数
        sb.append(" select  z.cusno as 'cusno' ,c.cusna as 'cusna' ,z.num  as 'shpqy1' from ( ");
        sb.append(" select x.cusno,sum(num) as num  from ( ");
        sb.append(" select h.cusno,sum(shpqy1) as num  from cdrdta d left join cdrhad h on d.shpno=h.shpno  ");
        sb.append(" where h.facno='${facno}' and h.houtsta <> 'W' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N'  ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ='00' ");
        }
        sb.append(" and year(h.shpdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.shpdate) = ${m} ");
        } else {
            sb.append(" and month(h.shpdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by  h.cusno ");
        sb.append(" union all ");
        sb.append(" select  h.cusno,-sum(bshpqy1) as num  from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno ");
        sb.append(" where h.baksta <> 'W'  and  h.facno='${facno}' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N'  ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ='00' ");
        }
        sb.append(" and year(h.bakdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.bakdate)= ${m} ");
        } else {
            sb.append(" and month(h.bakdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by  h.cusno ) ");
        sb.append(" x  group by x.cusno ) ");
        sb.append(" z,cdrcus c  where z.cusno=c.cusno ");
        sb.append(" ) a,( ");
        //金额
        sb.append(" select  z.cusno as 'cusno' ,c.cusna as 'cusna' ,z.num  as 'shpamts' from ( ");
        sb.append(" select x.cusno,sum(num) as num  from ( ");
        sb.append(" select  h.cusno,isnull(convert(decimal(16,2),sum((d.shpamts * h.ratio)/(h.taxrate + 1))),0) as num ");
        sb.append(" from cdrdta d left join cdrhad h on d.shpno=h.shpno ");
        sb.append(" where h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' and h.facno='${facno}' ");
        sb.append(" and h.houtsta <> 'W' ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.shpdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.shpdate) = ${m} ");
        } else {
            sb.append(" and month(h.shpdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by  h.cusno ");
        sb.append(" union all ");
        sb.append(" select  h.cusno,isnull(convert(decimal(16,2),-sum((d.bakamts * h.ratio)/(h.taxrate + 1))),0) as num ");
        sb.append(" from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno ");
        sb.append(" where h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' and h.facno='${facno}' ");
        sb.append(" and h.baksta <> 'W' ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.bakdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.bakdate)= ${m} ");
        } else {
            sb.append(" and month(h.bakdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by  h.cusno ) ");
        sb.append(" x  group by x.cusno ) ");
        sb.append(" z,cdrcus c where z.cusno=c.cusno ");
        sb.append(" ) b where a.cusno=b.cusno  and a.cusna  =b.cusna ORDER BY shpamts DESC ");

        return sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
    }

    //制冷冷冻小于2018年SQL
    protected String getPastSqlRL(int y, int m, String facno, LinkedHashMap<String, String> map) {
        String decode = map.get("decode") != null ? map.get("decode") : "";
        String depno = map.get("depno") != null ? map.get("depno") : "";
        String style = map.get("style") != null ? map.get("style") : "";
        String type = map.get("type") != null ? map.get("type") : "";
        
        StringBuilder sb = new StringBuilder();
        sb.append(" select  a.cusno as 'cusno',a.cusna as 'cusna',a.shpqy1 as 'shpqy1',b.shpamts as 'shpamts' from ( ");
        //台数
        sb.append(" select  z.cusno as 'cusno' ,c.cusna as 'cusna' ,z.num  as 'shpqy1' from (");
        sb.append(" select x.cusno,sum(num) as num  from ( ");
        sb.append(" select h.cusno,sum(d.shpqy1) as num from cdrdta d inner join cdrhad h on d.shpno=h.shpno ");
        sb.append(" inner join cdrdmas t on t.cdrno =d.cdrno and t.itnbr = d.itnbr  and d.ctrseq = t.trseq ");
        sb.append(" where h.cusno not in ('SSD00107','SGD00088','SJS00254','SCQ00146')  ");
        sb.append(" and t.dmark1 ${type} in ('APZ','AVIP','AVP','BPZ','BVP','EXAP','EXBP','EXZP','LB','LBPL','LBPV','LBV','LT','LTG','LTL','LTS','LTVL','LTVS','RG','ZP','L') ");
        sb.append(" and h.depno  in ( '1D000','1B100','1E100','1D100','1C100','1C000','1E000','1B000','1F100','1F000','1F500' ,'1V000','1T100')  ");
        sb.append(" and d.itnbr  in ( select itnbr from invmas where itcls in('3176','3177','3179','3180','3276','3279','3280','3083','4079','3676','3679','3680','3015')) ");
        sb.append(" and h.houtsta <> 'W' and d.issevdta='N' and h.facno = '${facno}' ");
        //sb.append(" and d.n_code_DD in ('00') ");
        sb.append(" and year(h.shpdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.shpdate) = ${m} ");
        } else {
            sb.append(" and month(h.shpdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by h.cusno ");
        sb.append(" union all  ");
        sb.append(" select bh.cusno,-sum(bd.bshpqy1) as num from cdrbhad bh right join cdrbdta bd on bh.bakno=bd.bakno  ");
        sb.append(" inner join cdrdmas t on t.cdrno =bd.cdrno  and t.itnbr = bd.itnbr and bd.ctrseq = t.trseq  ");
        sb.append(" where bh.cusno not in ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        sb.append(" and t.dmark1 ${type} in ('APZ','AVIP','AVP','BPZ','BVP','EXAP','EXBP','EXZP','LB','LBPL','LBPV','LBV','LT','LTG','LTL','LTS','LTVL','LTVS','RG','ZP','L') ");
        sb.append(" and bh.depno in ( '1D000','1B100','1E100','1D100','1C100','1C000','1E000','1B000','1F100','1F000','1F500' ,'1V000','1T100') ");
        sb.append(" and bd.itnbr in ( select itnbr from invmas where itcls in('3176','3177','3179','3180','3276','3279','3280','3083','4079','3676','3679','3680','3015')) ");
        sb.append(" and bh.baksta <>'W' and bd.issevdta='N' and  bh.facno = '${facno}' ");
        //sb.append(" and bd.n_code_DD in ('00') ");
        sb.append(" and year(bh.bakdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(bh.bakdate)= ${m} ");
        } else {
            sb.append(" and month(bh.bakdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by bh.cusno) ");
        sb.append(" x  group by x.cusno) ");
        sb.append(" z,cdrcus c  where z.cusno=c.cusno ");
        sb.append(" ) a,( ");
        sb.append(" select  z.cusno as 'cusno' ,c.cusna as 'cusna' ,z.num  as 'shpamts' from ( ");
        sb.append(" select x.cusno,sum(num) as num  from ( ");
        sb.append(" select h.cusno,isnull(convert(decimal(16,2),sum((d.shpamts * h.ratio)/(h.taxrate + 1))),0) as num from cdrdta d  ");
        sb.append(" inner join cdrhad h on d.shpno=h.shpno inner join cdrdmas t on t.cdrno =d.cdrno and t.itnbr = d.itnbr  and d.ctrseq = t.trseq ");
        sb.append(" where h.cusno not in ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        sb.append(" and t.dmark1 ${type} in ('APZ','AVIP','AVP','BPZ','BVP','EXAP','EXBP','EXZP','LB','LBPL','LBPV','LBV','LT','LTG','LTL','LTS','LTVL','LTVS','RG','ZP','L') ");
        sb.append(" and h.depno  in ( '1D000','1B100','1E100','1D100','1C100','1C000','1E000','1B000','1F100','1F000','1F500' ,'1V000','1T100') ");
        sb.append(" and d.itnbr  in ( select itnbr from invmas where itcls in('3176','3177','3179','3180','3276','3279','3280','3083','4079','3676','3679','3680','3015')) ");
        sb.append(" and h.houtsta <> 'W' and d.issevdta='N' and h.facno = '${facno}' ");
        //sb.append(" and d.n_code_DD in ('00') ");
        sb.append(" and year(h.shpdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.shpdate) = ${m} ");
        } else {
            sb.append(" and month(h.shpdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by h.cusno ");
        sb.append(" union all  ");
        sb.append(" select bh.cusno,isnull(convert(decimal(16,2),-sum((bd.bakamts * bh.ratio)/(bh.taxrate + 1))),0) as num from cdrbhad bh  ");
        sb.append(" right join cdrbdta bd on bh.bakno=bd.bakno inner join cdrdmas t on t.cdrno =bd.cdrno  and t.itnbr = bd.itnbr and bd.ctrseq = t.trseq ");
        sb.append(" where bh.cusno not in ('SSD00107','SGD00088','SJS00254','SCQ00146')  ");
        sb.append(" and t.dmark1 ${type} in ('APZ','AVIP','AVP','BPZ','BVP','EXAP','EXBP','EXZP','LB','LBPL','LBPV','LBV','LT','LTG','LTL','LTS','LTVL','LTVS','RG','ZP','L') ");
        sb.append(" and bh.depno in ( '1D000','1B100','1E100','1D100','1C100','1C000','1E000','1B000','1F100','1F000','1F500' ,'1V000','1T100') ");
        sb.append(" and bd.itnbr in ( select itnbr from invmas where itcls in('3176','3177','3179','3180','3276','3279','3280','3083','4079','3676','3679','3680','3015')) ");
        sb.append(" and bh.baksta <>'W' and bd.issevdta='N' and  bh.facno = '${facno}' ");
        //sb.append(" and bd.n_code_DD in ('00') ");
        sb.append(" and year(bh.bakdate) = ${y} ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(bh.bakdate)= ${m} ");
        } else {
            sb.append(" and month(bh.bakdate) BETWEEN 1 AND ${m} ");
        }
        sb.append(" group by bh.cusno) ");
        sb.append(" x  group by x.cusno) ");
        sb.append(" z,cdrcus c where z.cusno=c.cusno) ");
        sb.append(" b where a.cusno=b.cusno  and a.cusna  =b.cusna ORDER BY shpamts DESC ");

        return sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", type);
    }

    // 返回ClientRanking集合
    protected List getClientRT(int y, int m, String arr1, LinkedHashMap<String, String> map) {
        try {
            erpEJB.setCompany(arr1);
            Query query;
            if (y > 2017) {
                query = erpEJB.getEntityManager().createNativeQuery(getNowSqlRL(y, m, arr1, map));
            } else {
                query = erpEJB.getEntityManager().createNativeQuery(getPastSqlRL(y, m, arr1, map));
            }
            List result = query.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ClientNowAndPastBean.getClient()" + e);
        }
        return null;
    }

    //当前
    public List<ClientRanking> getNowClientRL(int y, int m, LinkedHashMap<String, String> map) {
        String facno = map.get("facno") != null ? map.get("facno") : "";
        //查询当前值并赋予排名
        String[] arr = facno.split(",");
        //过渡list
        List<ClientRanking> list;
        //汇总返回list
        List<ClientRanking> returnlist = new ArrayList<>();
        ClientRanking ct;
        boolean aa;
        try {
            for (String arr1 : arr) {
                list = new ArrayList<>();
                List result = getClientRT(y, m, arr1, map);
                if (result != null && !result.isEmpty()) {
                    for (int i = 0; i < result.size(); i++) {
                        ct = new ClientRanking();
                        Object[] row = (Object[]) result.get(i);
                        ct.setCusno(row[0].toString());
                        ct.setCusna(row[1].toString());
                        ct.setNowshpqy1(RTshpqy1(row[2].toString()));
                        ct.setNowshpamts(row[3].toString());
                        list.add(ct);
                    }
                    //汇总
                    //循环list数据往returnlist合并数据
                    if (!list.isEmpty() && returnlist.isEmpty()) {
                        returnlist = list;
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            aa = true;
                            for (int j = 0; j < returnlist.size(); j++) {
                                //list客户与returnlist客户相同 则台数与金额 returnlist = returnlist + list
                                if (list.get(i).getCusna().equals(returnlist.get(j).getCusna())) {
                                    returnlist.get(j).setNowshpqy1(RTshpqy1(returnlist.get(j).getNowshpqy1(), list.get(i).getNowshpqy1()));
                                    returnlist.get(j).setNowshpamts(RTshpamts(returnlist.get(j).getNowshpamts(), list.get(i).getNowshpamts()));
                                    aa = false;
                                }
                            }
                            if (aa) {
                                returnlist.add(list.get(i));
                            }
                        }
                    }
                }
            }
            //客户排名
            int min;
            for (int i = 0; i < returnlist.size() - 1; i++) {
                min = i;
                ct = new ClientRanking();
                for (int j = i + 1; j < returnlist.size(); j++) {
                    if (Double.valueOf(returnlist.get(j).getNowshpamts()) > Double.valueOf(returnlist.get(min).getNowshpamts())) {
                        min = j;
                    }
                }
                if (min != i) {
                    ct = returnlist.get(i);
                    returnlist.set(i, returnlist.get(min));
                    returnlist.set(min, ct);
                }

            }
            if (returnlist != null && !returnlist.isEmpty()) {
                int sumshpqy1 = 0;
                Double sumshpamts = 0.0;
                ct = new ClientRanking();
                for (int i = 0; i < returnlist.size(); i++) {
                    returnlist.get(i).setNowrank(String.valueOf(i + 1));
                    sumshpqy1 += Integer.parseInt(returnlist.get(i).getNowshpqy1());
                    sumshpamts += Double.parseDouble(returnlist.get(i).getNowshpamts());
                }
                ct.setCusna("总计");
                ct.setNowshpqy1(String.valueOf(sumshpqy1));
                ct.setNowshpamts(String.valueOf(sumshpamts));
                returnlist.add(ct);
            }
            return returnlist;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ClientNowAndPastBean.getNowClientRL():" + e);
            return null;
        }
    }

    //去年同期
    public List<ClientRanking> getPastClientRL(int y, int m, LinkedHashMap<String, String> map) {
        String facno = map.get("facno") != null ? map.get("facno") : "";
        //查询去年同期值并赋予排名
        String[] arr = facno.split(",");
        //过渡list
        List<ClientRanking> list;
        //汇总返回list
        List<ClientRanking> returnlist = new ArrayList<>();
        ClientRanking ct;
        boolean aa;
        try {
            for (String arr1 : arr) {
                list = new ArrayList<>();
                List result = getClientRT(y - 1, m, arr1, map);
                if (result != null && !result.isEmpty()) {
                    for (int i = 0; i < result.size(); i++) {
                        ct = new ClientRanking();
                        Object[] row = (Object[]) result.get(i);
                        ct.setCusno(row[0].toString());
                        ct.setCusna(row[1].toString());
                        ct.setPastshpqy1(RTshpqy1(row[2].toString()));
                        ct.setPastshpamts(row[3].toString());
                        list.add(ct);
                    }
                    //汇总
                    //循环list数据往returnlist合并数据
                    if (!list.isEmpty() && returnlist.isEmpty()) {
                        returnlist = list;
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            aa = true;
                            for (int j = 0; j < returnlist.size(); j++) {
                                //list客户与returnlist客户相同 则台数与金额 returnlist = returnlist + list
                                if (list.get(i).getCusno().equals(returnlist.get(j).getCusno())) {
                                    returnlist.get(j).setPastshpqy1(RTshpqy1(returnlist.get(j).getPastshpqy1(), list.get(i).getPastshpqy1()));
                                    returnlist.get(j).setPastshpamts(RTshpamts(returnlist.get(j).getPastshpamts(), list.get(i).getPastshpamts()));
                                    aa = false;
                                }
                            }
                            if (aa) {
                                returnlist.add(list.get(i));
                            }
                        }
                    }

                }
            }

            //客户排名
            int min;
            for (int i = 0; i < returnlist.size() - 1; i++) {
                min = i;
                ct = new ClientRanking();
                for (int j = i + 1; j < returnlist.size(); j++) {
                    if (Double.valueOf(returnlist.get(j).getPastshpamts()) > Double.valueOf(returnlist.get(min).getPastshpamts())) {
                        min = j;
                    }
                }
                if (min != i) {
                    ct = returnlist.get(i);
                    returnlist.set(i, returnlist.get(min));
                    returnlist.set(min, ct);
                }
            }
            if (returnlist != null && !returnlist.isEmpty()) {
                int sumshpqy1 = 0;
                Double sumshpamts = 0.0;
                ct = new ClientRanking();
                for (int i = 0; i < returnlist.size(); i++) {
                    returnlist.get(i).setPastrank(String.valueOf(i + 1));
                    sumshpqy1 += Integer.parseInt(returnlist.get(i).getPastshpqy1());
                    sumshpamts += Double.parseDouble(returnlist.get(i).getPastshpamts());
                }
                ct.setCusna("总计");
                ct.setPastshpqy1(String.valueOf(sumshpqy1));
                ct.setPastshpamts(String.valueOf(sumshpamts));
                returnlist.add(ct);
            }
            return returnlist;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ClientNowAndPastBean.getPastClientRL():" + e);
            return null;
        }
    }

    //得到查询结果
    public List<ClientRanking> getClientListRL(int y, int m, LinkedHashMap<String, String> map) {
        List<ClientRanking> list = new LinkedList<>();
        //得到已经有排名的list
        //NowClient
        List<ClientRanking> nowList = getNowClientRL(y, m, map);
        //PastClient
        List<ClientRanking> pastList = getPastClientRL(y, m, map);
        //循环nowList 并与 pastList 合并客户
        //其他值
        int nowothershpqy1, pastothershpqy1;
        Double nowothershpamts, pastothershpamts;
        //TOP20总计
        int top20nowshpqy1 = 0;
        Double top20nowshpamts = 0.0;
        int top20pastshpqy1 = 0;
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
                                    top20nowshpqy1 += Integer.parseInt(now.getNowshpqy1());
                                    top20nowshpamts += Double.parseDouble(now.getNowshpamts());
                                    top20pastshpqy1 += Integer.parseInt(past.getPastshpqy1());
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
                            top20nowshpqy1 += Integer.parseInt(now.getNowshpqy1());
                            top20nowshpamts += Double.parseDouble(now.getNowshpamts());
                            ct.setCusno(now.getCusno());
                            ct.setCusna(now.getCusna());
                            ct.setNowrank(now.getNowrank());
                            ct.setNowshpqy1(now.getNowshpqy1());
                            ct.setNowshpamts(df.format(Double.valueOf(now.getNowshpamts())));
                            ct.setPastshpqy1("0");
                            ct.setPastshpamts("0");
                            ct.setDifferencevalue(df.format(Double.valueOf(now.getNowshpamts())));
                            ct.setGrowthrate("100");
                            list.add(ct);
                        }
                    }
                    //如果排名大于20 则有其他项
                    if (i >= 20 && now.getCusna().equals("总计")) {
                        nowothershpqy1 = Integer.parseInt(now.getNowshpqy1()) - top20nowshpqy1;
                        nowothershpamts = Double.parseDouble(now.getNowshpamts()) - top20nowshpamts;
                        //去年同期有值时进行其他项计算
                        if (pastList != null && !pastList.isEmpty()) {
                            for (int j = 0; j < pastList.size(); j++) {
                                if (pastList.get(j).getCusna().equals("总计")) {
                                    pastothershpqy1 = Integer.parseInt(pastList.get(j).getPastshpqy1()) - top20pastshpqy1;
                                    pastothershpamts = Double.parseDouble(pastList.get(j).getPastshpamts()) - top20pastshpamts;
                                    ClientRanking ct = new ClientRanking();
                                    ct.setCusna("其他");
                                    ct.setNowshpqy1(String.valueOf(nowothershpqy1));
                                    ct.setNowshpamts(df.format(nowothershpamts < 0 ? 0 : nowothershpamts));
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
            System.out.println("cn.hanbell.kpi.ejb.ClientNowAndPastBean.getClientListRL():" + e);
            return list;
        }
    }
}
