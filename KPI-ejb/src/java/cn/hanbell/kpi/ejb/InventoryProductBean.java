/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.InventoryProduct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class InventoryProductBean extends SuperEJBForKPI<InventoryProduct> {

    @EJB
    SuperEJBForERP superEJBForERP;

    protected LinkedHashMap<String, String> queryStringParams;

    protected Logger log4j = LogManager.getLogger();

    String[] strArr;

    public InventoryProductBean() {
        super(InventoryProduct.class);
        queryStringParams = new LinkedHashMap<>();
    }

    public boolean queryInventoryProductIsExist(InventoryProduct ip) {
        Query query = this.getEntityManager().createNamedQuery("InventoryProduct.findByUnique");
        try {
            query.setParameter("yearmon", ip.getYearmon());
            query.setParameter("whdsc", ip.getWhdsc());
            query.setParameter("genre", ip.getGenre());
            query.setParameter("trtype", ip.getTrtype());
            query.setParameter("itclscode", ip.getItclscode());
            query.setParameter("categories", ip.getCategories());
            if (query.getResultList().isEmpty()) {
                return false;
            }
        } catch (Exception ex) {
            System.out.println("cn.hanbell.kpi.ejb.InventoryProductBean.queryInventoryProductIsExist()" + ex.toString());
        }
        return true;
    }

    public List getEditList(InventoryProduct ip) {
        Query query = this.getEntityManager().createNamedQuery("InventoryProduct.findByEditRow");
        try {
            query.setParameter("facno", ip.getFacno());
            query.setParameter("yearmon", ip.getYearmon());
            query.setParameter("wareh", ip.getWareh());
            query.setParameter("itclscode", ip.getItclscode());
            List result = query.getResultList();
            return result;
        } catch (Exception ex) {
            System.out.println("cn.hanbell.kpi.ejb.InventoryProductBean.getEditList()" + ex.toString());
        }
        return null;
    }

    // 获取ERP的invamount的数据 存到KPI的inventoryproduct表中去
    private List getDataForERPList(int y, int m, LinkedHashMap<String, String> map) {
        String facno = map.get("facno") != null ? map.get("facno") : "";
        String prono = map.get("prono") != null ? map.get("prono") : "";
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT a.facno,a.yearmon,a.trtype,a.deptno,a.wareh,a.whdsc,");
        sb.append(" (case when  d.genre <> '' then  d.genre else  a.genre end ), ");
        sb.append(" a.itclscode,d.genreno,h.genzls,sum(a.amount) AS amount,'' AS amamount  ");
        sb.append(" FROM invamount a LEFT OUTER JOIN invwh w on w.facno = a.facno and w.prono = a.prono and w.wareh = a.wareh ");
        sb.append(" LEFT JOIN invindexdta d ON a.facno = d.facno AND a.prono = d.prono AND a.wareh = d.wareh  ");
        sb.append(" LEFT JOIN invindexhad h ON h.facno = d.facno AND h.prono = d.prono AND h.indno = d.indno  ");
        sb.append(" where a.facno='${facno}' and a.prono = '${prono}'  ");
        sb.append(" AND a.genre NOT LIKE '%,%' AND a.genre NOT LIKE 'QT' ");
        sb.append(" and a.yearmon='${y}${m}'  ");
        sb.append(" GROUP BY a.facno,a.trtype,a.deptno,a.yearmon,a.wareh,a.whdsc,(case when  d.genre <> '' then  d.genre else  a.genre end ), ");
        sb.append(" a.itclscode,d.genreno,h.genzls ");
        sb.append(" UNION ALL ");
        sb.append(" SELECT a.facno,a.yearmon,a.trtype,a.deptno,a.wareh,a.whdsc, ");
        sb.append(" (case when  a.genre <> '' then  a.genre else  'R' end ), ");
        sb.append(" a.itclscode,'' AS genreno,'' AS genzls,sum(a.amount) AS amount,'' AS amamount ");
        sb.append(" FROM invamount a LEFT OUTER JOIN invwh w on w.facno = a.facno and w.prono = a.prono and w.wareh = a.wareh ");
        sb.append(" where a.facno<>'${facno}' and a.prono = '${prono}' ");
        sb.append(" and a.yearmon='${y}${m}'  ");
        sb.append(" GROUP BY a.facno,a.trtype,a.deptno,a.yearmon,a.wareh,a.whdsc,a.genre,a.itclscode ");
        String sql = sb.toString().replace("${facno}", String.valueOf(facno)).replace("${prono}", String.valueOf(prono))
                .replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(getMon(m)));
        try {
            superEJBForERP.setCompany(facno);
            Query query = superEJBForERP.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            return result;
        } catch (Exception ex) {
            log4j.error("InventoryProductBean-getDataForERPList()异常！！！", ex.toString());
        }
        return null;
    }

    // 更新到KPI的inventoryproduct表中的方法 在mailBean里设置自动排程更新数据
    public boolean updateInventoryProduct(int y, int m) {
        queryStringParams.clear();
        queryStringParams.put("facno", "C");
        queryStringParams.put("prono", "1");
        // 取到数据源
        List dataList = getDataForERPList(y, m, queryStringParams);
        List<InventoryProduct> ipResultList = new ArrayList<>();
        InventoryProduct ip;
        try {
            // 判读当前list 是否有数据 有就更新 没有就返回空
            if (!dataList.isEmpty()) {
                for (int i = 0; i < dataList.size(); i++) {
                    ip = new InventoryProduct();
                    Object[] row = (Object[]) dataList.get(i);
                    ip.setFacno(row[0].toString());
                    ip.setYearmon(row[1].toString());
                    ip.setTrtype(row[2].toString());
                    ip.setDeptno(row[3] != null ? row[3].toString() : "");
                    ip.setWareh(row[4].toString());
                    ip.setWhdsc(row[5].toString());
                    //兴塔的空压机体整机归为空压机组库存(兴塔成品仓库）
                    String genre = row[6] != null ? row[6].toString() : "";
                    String itclscode = row[7].toString();
                    if (row[4].toString().equals("EW01") && genre.equals("AJ") && itclscode.equals("2")) {
                        ip.setGenre("A");
                    } else {
                        ip.setGenre(genre);
                    }
                    ip.setItclscode(itclscode);
                    ip.setCategories(row[8] != null ? row[8].toString() : "");
                    ip.setIndicatorno(row[9] != null ? row[9].toString() : "");
                    ip.setAmount(BigDecimal.valueOf(Double.valueOf(row[10].toString())));
                    ip.setAmamount(BigDecimal.ZERO);
                    ipResultList.add(ip);
                }
                if (!ipResultList.isEmpty()) {
                    this.getEntityManager()
                            .createNativeQuery("delete from inventoryproduct where yearmon ='" + y + getMon(m) + "'").executeUpdate();
                    for (InventoryProduct e : ipResultList) {
                        this.persist(e);
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            log4j.error("InventoryProductBean-updateInventoryProduct()方法异常！！", ex.toString());
        }
        return false;
    }

    // 各产品别之库存统计表
    // 取到KPI的invamountproduct表中的数据做呈现
    private List getDataforKPIInvamountProductList(int y, int m, LinkedHashMap<String, String> map) {
        String facno = map.get("facno") != null ? map.get("facno") : "";
        StringBuilder sb = new StringBuilder();
        sb.append(" select whdsc, ");
        sb.append(" ifnull(sum(CASE when genre =  'A'  then amount + ifnull(amamount,0) end),0) as 'AA', ");
        sb.append(" ifnull(sum(CASE when genre =  'AJ'  then amount + ifnull(amamount,0) end),0) as 'AH', ");
        sb.append(" ifnull(sum(CASE when genre =  'AD'  then amount + ifnull(amamount,0) end),0) as 'SDS', ");
        sb.append(" ifnull(sum(CASE when (genre = 'R' or genre = 'RG') then amount + ifnull(amamount,0) end),0) as 'R', ");
        sb.append(" ifnull(sum(CASE when genre =  'RT'  then amount + ifnull(amamount,0) end),0) as 'RT', ");
        sb.append(" ifnull(sum(CASE when genre = 'L'  then amount + ifnull(amamount,0) end),0) as 'L', ");
        sb.append(" ifnull(sum(CASE when genre = 'P'  then amount + ifnull(amamount,0) end),0) as 'P', ");
        sb.append(" ifnull(sum(CASE when genre = 'S'  then amount + ifnull(amamount,0) end),0) as 'S' ");
        sb.append(" from inventoryproduct where facno = '${facno}' and yearmon = '${y}${m}' ");
        sb.append(" GROUP BY whdsc ");
        String sql = sb.toString().replace("${facno}", String.valueOf(facno)).replace("${y}", String.valueOf(y))
                .replace("${m}", String.valueOf(getMon(m)));
        try {
            Query query = this.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            return result;
        } catch (Exception ex) {
            log4j.error("InventoryProductBean--getDataforKPIInvamountProductList()异常！！！", ex.toString());
        }
        return null;
    }

    public List<String[]> getDisplayInvamountProductList(int y, int m) {
        queryStringParams.clear();
        queryStringParams.put("facno", "C");
        List<String[]> dataResultList = new ArrayList<>();
        // 取到集合
        List dataDisplayList = getDataforKPIInvamountProductList(y, m, queryStringParams);
        try {
            if (!dataDisplayList.isEmpty()) {
                // 定义最后一列的数据类型
                BigDecimal cos1 = BigDecimal.ZERO;
                BigDecimal cos2 = BigDecimal.ZERO;
                BigDecimal cos3 = BigDecimal.ZERO;
                BigDecimal cos4 = BigDecimal.ZERO;
                BigDecimal cos5 = BigDecimal.ZERO;
                BigDecimal cos6 = BigDecimal.ZERO;
                BigDecimal cos7 = BigDecimal.ZERO;
                BigDecimal cos8 = BigDecimal.ZERO;
                BigDecimal cos9 = BigDecimal.ZERO;
                // 循环添加合计行、合计列
                for (int i = 0; i < dataDisplayList.size(); i++) {
                    strArr = new String[10];
                    Object[] row = (Object[]) dataDisplayList.get(i);
                    strArr[0] = row[0].toString();
                    strArr[1] = row[1].toString();
                    cos1 = cos1.add(BigDecimal.valueOf(Double.parseDouble(row[1].toString())));
                    strArr[2] = row[2].toString();
                    cos2 = cos2.add(BigDecimal.valueOf(Double.parseDouble(row[2].toString())));
                    strArr[3] = row[3].toString();
                    cos3 = cos3.add(BigDecimal.valueOf(Double.parseDouble(row[3].toString())));
                    strArr[4] = row[4].toString();
                    cos4 = cos4.add(BigDecimal.valueOf(Double.parseDouble(row[4].toString())));
                    strArr[5] = row[5].toString();
                    cos5 = cos5.add(BigDecimal.valueOf(Double.parseDouble(row[5].toString())));
                    strArr[6] = row[6].toString();
                    cos6 = cos6.add(BigDecimal.valueOf(Double.parseDouble(row[6].toString())));
                    strArr[7] = row[7].toString();
                    cos7 = cos7.add(BigDecimal.valueOf(Double.parseDouble(row[7].toString())));
                    strArr[8] = row[8].toString();
                    cos8 = cos8.add(BigDecimal.valueOf(Double.parseDouble(row[8].toString())));
                    // 添加最后一列的合计列
                    BigDecimal total = ((BigDecimal) row[1]).add((BigDecimal) row[2]).add((BigDecimal) row[3])
                            .add((BigDecimal) row[4]).add((BigDecimal) row[5]).add((BigDecimal) row[6]).add((BigDecimal) row[7])
                            .add((BigDecimal) row[8]);
                    strArr[9] = total.toString();
                    cos9 = cos9.add(total);
                    dataResultList.add(strArr);
                }
                // 添加最后一行的合计行
                String[] strArrTatal = new String[10];
                strArrTatal[0] = "合计";
                strArrTatal[1] = cos1.toString();
                strArrTatal[2] = cos2.toString();
                strArrTatal[3] = cos3.toString();
                strArrTatal[4] = cos4.toString();
                strArrTatal[5] = cos5.toString();
                strArrTatal[6] = cos6.toString();
                strArrTatal[7] = cos7.toString();
                strArrTatal[8] = cos8.toString();
                strArrTatal[9] = cos9.toString();
                dataResultList.add(strArrTatal);
            }
            return dataResultList;
        } catch (Exception ex) {
            log4j.error("InventoryProductBean--setDisplayInvamountProductList()异常！！！", ex.toString());
        }
        return null;
    }

    // 当前月份 m
    private String getMon(int m) {
        if (m < 10) {
            return "0" + m;
        }
        return String.valueOf(m);
    }

}
