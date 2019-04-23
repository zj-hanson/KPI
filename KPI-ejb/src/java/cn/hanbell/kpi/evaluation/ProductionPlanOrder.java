/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorDailyBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDaily;
import cn.hanbell.kpi.entity.IndicatorDetail;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
public class ProductionPlanOrder extends Production {

    IndicatorDailyBean indicatorDailyBean = lookupIndicatorDailyBeanBean();

    IndicatorBean indicatorBean = lookupIndicatorBeanBean();

    public ProductionPlanOrder() {
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String itcls = map.get("itcls") != null ? map.get("itcls").toString() : "";
        String itnbrf = map.get("itnbrf") != null ? map.get("itnbrf").toString() : "";
        String id = map.get("id") != null ? map.get("id").toString() : "";
        String noUpdate = map.get("noUpdate") != null ? map.get("noUpdate").toString() : "";

        BigDecimal value = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(d.cdrqy1),0) as totcdrqy from cdrhmas h, cdrdmas d ");
        sb.append(" where  h.hrecsta<>'W' and h.cdrno=d.cdrno and  h.facno=d.facno  and h.facno='${facno}' and d.drecsta not in ('99','98') and d.n_code_DD <> 'ZZ' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(itcls)) {
            sb.append(" and d.itnbr in(select itnbr from invmas where itcls ").append(itcls).append(") ");
        }
        sb.append(" and year(h.recdate) = ${y} and month(h.recdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and h.recdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and h.recdate= '${d}' ");
                break;
            default:
                sb.append(" and h.recdate<= '${d}' ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            value = (BigDecimal) o;
            //更新本月以往天数订单
            List list = getLastValue(y, m, d, map);
            if (list != null && !list.isEmpty()) {
                if (!"".equals(id) && !"true".equals(noUpdate)) {
                    Indicator entity = indicatorBean.findById(Integer.valueOf(id));
                    if (entity != null && entity.getOther4Indicator() != null) {
                        IndicatorDetail salesOrder = entity.getOther4Indicator();
                        IndicatorDaily daily = indicatorDailyBean.findByPIdDateAndType(salesOrder.getId(), salesOrder.getSeq(), m, salesOrder.getType());
                        daily.clearDate();
                        for (int i = 0; i < list.size(); i++) {
                            Object[] row = (Object[]) list.get(i);
                            updateValue(Integer.valueOf(row[0].toString()), BigDecimal.valueOf(Double.valueOf(row[1].toString())), daily);
                        }
                        indicatorDailyBean.update(daily);
                    }
                }
            }
            return value;
        } catch (Exception e) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, e);
        }
        return value;
    }

    public void updateValue(int day, BigDecimal na, IndicatorDaily daily) {
        String col = "setD" + String.format("%02d", day);
        try {
            Method setMethod = daily.getClass().getDeclaredMethod(col, BigDecimal.class);
            setMethod.invoke(daily, na);
        } catch (Exception ex) {
            Logger.getLogger(ProductionPlanOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List getLastValue(int y, int m, Date d, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String itcls = map.get("itcls") != null ? map.get("itcls").toString() : "";
        String itnbrf = map.get("itnbrf") != null ? map.get("itnbrf").toString() : "";

        BigDecimal value = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select day(h.recdate),isnull(sum(d.cdrqy1),0) as totcdrqy from cdrhmas h, cdrdmas d ");
        sb.append(" where  h.hrecsta<>'W' and h.cdrno=d.cdrno and  h.facno=d.facno  and h.facno='${facno}' and d.drecsta not in ('99','98') and d.n_code_DD <> 'ZZ'");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(itcls)) {
            sb.append(" and d.itnbr in(select itnbr from invmas where itcls ").append(itcls).append(") ");
        }
        sb.append(" and year(h.recdate) = ${y} and month(h.recdate)= ${m} and h.recdate< '${d}' GROUP BY day(h.recdate) ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            List list = query.getResultList();
            return list;
        } catch (Exception e) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    private IndicatorBean lookupIndicatorBeanBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private IndicatorDailyBean lookupIndicatorDailyBeanBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorDailyBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorDailyBean!cn.hanbell.kpi.ejb.IndicatorDailyBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
