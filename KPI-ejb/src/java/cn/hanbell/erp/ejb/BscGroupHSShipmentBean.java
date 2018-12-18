/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.erp.ejb;

import cn.hanbell.erp.entity.BscGroupShipment;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import com.lightshell.comm.BaseLib;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class BscGroupHSShipmentBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    protected LinkedHashMap<String, Object> queryParams = new LinkedHashMap<>();

    public BscGroupHSShipmentBean() {
    }

    public LinkedHashMap<String, Object> getQueryParams() {
        return this.queryParams;
    }

    public void updataActualValue(int y, int m, Date d) {

        queryParams.clear();
        queryParams.put("facno", "H");
        queryParams.put("spdsc", " ='QT' ");
        List<BscGroupShipment> resultData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());

        List<BscGroupShipment> tempData;
        queryParams.clear();
        queryParams.put("facno", "H");
        queryParams.put("spdsc", " ='HT' ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        if (resultData != null) {
            erpEJB.setCompany("C");
            erpEJB.getEntityManager().createNativeQuery("delete from bsc_groupshipment where  facno='C' and year(soday)=" + y + " and month(soday) = " + m).executeUpdate();
            for (BscGroupShipment e : resultData) {
                erpEJB.getEntityManager().persist(e);
            }
        }
    }

    //出货
    protected List<BscGroupShipment> getShipment(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String spdsc = map.get("spdsc") != null ? map.get("spdsc").toString() : "";
        List<BscGroupShipment> data = new ArrayList<>();
        List<BscGroupShipment> temp;
        BigDecimal num = BigDecimal.ZERO;
        BigDecimal amts = BigDecimal.ZERO;
        StringBuilder shpSql = new StringBuilder();
        shpSql.append(" select a.soday,isnull(sum(a.num),0),isnull(sum(a.shpamts),0) from ( ");
        shpSql.append(" select h.shpdate as soday, ");
        shpSql.append(" cast(isnull(case substring(s.judco,1,1)+s.fvco when '4F' then d.shpqy1*s.rate2  else d.shpqy1 end,0) as decimal(12,2)) as num,");
        shpSql.append(" cast((case when h.coin<>'RMB' then d.shpamts*h.ratio else d.shpamts*h.ratio/(h.taxrate+1) end) as decimal(12,2)) as shpamts ");
        shpSql.append(" from cdrdta d,cdrhad h,invmas s ");
        shpSql.append(" where h.shpno=d.shpno and d.itnbr=s.itnbr  and h.houtsta not in ('W','N') ");
        if (!"".equals(spdsc)) {
            shpSql.append(" and substring(s.spdsc,1,2) ").append(spdsc);
        }
        shpSql.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} and h.shpdate<='${d}' ");
        shpSql.append(" ) as a GROUP BY a.soday ");
        String cdrdta = shpSql.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        
        StringBuilder bakSql = new StringBuilder();
        bakSql.append(" select a.soday,isnull(sum(a.num),0),isnull(sum(a.shpamts),0) from ( ");
        bakSql.append(" SELECT  h.bakdate as soday, ");
        bakSql.append(" cast(isnull(case substring(s.judco,1,1)+s.fvco when '4F' then d.bshpqy1*s.rate2  else d.bshpqy1 end,0) as decimal(12,2)) as num, ");
        bakSql.append(" cast((case when h.coin<>'RMB' then d.bakamts*h.ratio else d.bakamts*h.ratio/(h.taxrate+1) end) as decimal(12,2)) as shpamts ");
        bakSql.append(" from cdrbdta d,cdrbhad h,invmas s,bsc_zlitcls z ");
        bakSql.append(" where h.bakno=d.bakno and d.itnbr=s.itnbr and s.itcls=z.itcls and h.baksta not in ('W','N') ");
        if (!"".equals(spdsc)) {
            bakSql.append(" and substring(s.spdsc,1,2) ").append(spdsc);
        }
        bakSql.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} and h.bakdate<='${d}' ");
        bakSql.append(" ) as a GROUP BY a.soday ");
        String cdrbdta = bakSql.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        erpEJB.setCompany(facno);
        Query shpQuery = erpEJB.getEntityManager().createNativeQuery(cdrdta);
        Query bakQuery = erpEJB.getEntityManager().createNativeQuery(cdrbdta);
        try {
            List shpResult = shpQuery.getResultList();
            List bakResult = bakQuery.getResultList();
            Date shpdate;
            String protype, protypeno, shptype;
            if (spdsc.contains("QT")) {
                protype = "球铁";
                protypeno = "BI";
                shptype = "5";
            } else if (spdsc.contains("HT")) {
                protype = "灰铁";
                protypeno = "GI";
                shptype = "5";
            } else {
                protype = "";
                protypeno = "";
                shptype = "";
            }
            for (int i = 0; i < shpResult.size(); i++) {
                Object o[] = (Object[]) shpResult.get(i);
                shpdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
                num = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
                amts = BigDecimal.valueOf(Double.valueOf(o[2].toString()));
                BscGroupShipment e = new BscGroupShipment("C", shpdate, protype, protypeno, shptype);
                e.setShpnum(num);
                e.setShpamts(amts);
                data.add(e);
            }
            for (int i = 0; i < bakResult.size(); i++) {
                Object o[] = (Object[]) bakResult.get(i);
                shpdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
                num = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
                amts = BigDecimal.valueOf(Double.valueOf(o[2].toString()));
                BscGroupShipment e = new BscGroupShipment("C", shpdate, protype, protypeno, shptype);
                e.setShpnum(num);
                e.setShpamts(amts);
                if (data.contains(e)) {
                    BscGroupShipment entity = data.get(data.indexOf(e));
                    entity.setShpnum(entity.getShpnum().add(num));
                    entity.setShpamts(entity.getShpamts().add(amts));
                } else {
                    data.add(e);
                }
            }
            //订单
            temp = getSalesOrder(y, m, d, Calendar.MONTH, getQueryParams());
            if (temp != null && !temp.isEmpty()) {
                for (BscGroupShipment c : temp) {
                    if (data.contains(c)) {
                        BscGroupShipment a = data.get(data.indexOf(c));
                        a.setOrdnum(a.getOrdnum().add(c.getOrdnum()));
                        a.setOrdamts(a.getOrdamts().add(c.getOrdamts()));
                    } else {
                        data.add(c);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(BscGroupHSShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    //订单台数
    protected List<BscGroupShipment> getSalesOrder(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String spdsc = map.get("spdsc") != null ? map.get("spdsc").toString() : "";
        List<BscGroupShipment> data = new ArrayList<>();
        BigDecimal num = BigDecimal.ZERO;
        BigDecimal amts = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select a.soday,isnull(sum(ordnum),0),isnull(sum(tramts),0) from ( ");
        sb.append(" select h.recdate as soday, ");
        sb.append(" cast(isnull(case substring(s.judco,1,1)+s.fvco when '4F' then d.cdrqy1*s.rate2  else d.cdrqy1 end,0) as decimal(12,2)) as ordnum,  ");
        sb.append(" cast((case when h.coin<>'RMB' then d.tramts*h.ratio else d.tramts*h.ratio/(h.taxrate+1) end) as decimal(12,2)) as tramts ");
        sb.append(" from cdrdmas d, cdrhmas h ,invmas s ");
        sb.append(" where s.itnbr=d.itnbr and h.cdrno=d.cdrno and h.hrecsta not in ('N','W') and d.drecsta <>'98'  ");
        if (!"".equals(spdsc)) {
            sb.append(" and substring(s.spdsc,1,2) ").append(spdsc);
        }
        sb.append(" AND year(h.recdate) = ${y} and month(h.recdate)=${m} AND h.recdate<='${d}' ");
        sb.append(" ) as a GROUP BY a.soday ");
        String cdrSql = sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        erpEJB.setCompany(facno);
        Query cdrQuery = erpEJB.getEntityManager().createNativeQuery(cdrSql);
        try {
            List cdrResult = cdrQuery.getResultList();
            Date recdate;
            String protype, protypeno, shptype;
            if (spdsc.contains("QT")) {
                protype = "球铁";
                protypeno = "BI";
                shptype = "5";
            } else if (spdsc.contains("HT")) {
                protype = "灰铁";
                protypeno = "GI";
                shptype = "5";
            } else {
                protype = "";
                protypeno = "";
                shptype = "";
            }
            for (int i = 0; i < cdrResult.size(); i++) {
                Object o[] = (Object[]) cdrResult.get(i);
                recdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
                num = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
                amts = BigDecimal.valueOf(Double.valueOf(o[2].toString()));
                BscGroupShipment e = new BscGroupShipment("C", recdate, protype, protypeno, shptype);
                e.setOrdnum(num);
                e.setOrdamts(amts);
                data.add(e);
            }
        } catch (Exception ex) {
            Logger.getLogger(BscGroupHSShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

}
