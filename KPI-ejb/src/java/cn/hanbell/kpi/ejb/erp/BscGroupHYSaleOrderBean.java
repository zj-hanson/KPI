/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.erp;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.entity.erp.BscGroupShipment;
import com.lightshell.comm.BaseLib;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class BscGroupHYSaleOrderBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    protected LinkedHashMap<String, Object> queryParams = new LinkedHashMap<>();

    public BscGroupHYSaleOrderBean() {

    }

    public LinkedHashMap<String, Object> getQueryParams() {
        return this.queryParams;
    }

    public void updataSaleOrderActualValue(int y, int m, Date d) {

        queryParams.clear();
        queryParams.put("facno", "Y");
        queryParams.put("spdsc", " in ('HT')");
        queryParams.put("cusno", " not in ('YZJ00001') ");
        List<BscGroupShipment> resultData = getSalesOrder(y, m, d, y, getQueryParams());

        List<BscGroupShipment> tempData;
        queryParams.clear();
        queryParams.put("facno", "Y");
        queryParams.put("spdsc", " ='QT' ");
        queryParams.put("cusno", " not in ('YZJ00001') ");
        tempData = getSalesOrder(y, m, d, y, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(a.getQuantity().add(b.getQuantity()));
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "Y");
        queryParams.put("spdsc", " not in ('HT','QT') ");
        queryParams.put("cusno", " not in ('YZJ00001') ");
        tempData = getSalesOrder(y, m, d, y, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(a.getQuantity().add(b.getQuantity()));
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        if (resultData != null) {
            erpEJB.setCompany("C");
            erpEJB.getEntityManager().createNativeQuery("delete from bsc_groupshipment where  facno='Y' and year(soday)=" + y + " and month(soday) = " + m + " and type = 'SalesOrder'").executeUpdate();
            for (BscGroupShipment e : resultData) {
                erpEJB.getEntityManager().persist(e);
            }
        }
    }

    //订单台数金额
    protected List<BscGroupShipment> getSalesOrder(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String spdsc = map.get("spdsc") != null ? map.get("spdsc").toString() : "";
        String cusno = map.get("cusno") != null ? map.get("cusno").toString() : "";
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
        if (!"".equals(cusno)) {
            sb.append(" AND h.cusno ").append(cusno);
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
            if ("='QT'".equals(spdsc.trim())) {
                protype = "球铁";
                protypeno = "BI";
                shptype = "";
            } else if ("='HT'".equals(spdsc.trim())) {
                protype = "灰铁";
                protypeno = "GI";
                shptype = "";
            } else if (spdsc.contains("not")) {
                protype = "其他";
                protypeno = "OTH";
                shptype = "";
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
                BscGroupShipment e = new BscGroupShipment("Y", recdate, "SalesOrder", protype, protypeno, shptype);
                e.setQuantity(num);
                e.setAmount(amts);
                e.setCusno(shptype);
                data.add(e);
            }
        } catch (Exception ex) {
            Logger.getLogger(BscGroupHSSaleOrderBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

}
