/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.SalesTable;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author C1749 移动平均出货台数
 */
public class QRAShipmentAvg extends SuperEJBForKPI<SalesTable> implements Actual {

    protected LinkedHashMap<String, Object> queryParams;

    public QRAShipmentAvg() {
        super(SalesTable.class);
        queryParams = new LinkedHashMap<>();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        BigDecimal result = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  sum(quantity) as num from salestable WHERE 1=1 and  type='Shipment' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and n_code_DA = '").append(n_code_DA).append("' ");
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and n_code_DC = '").append(n_code_DC).append("' ");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.YEAR, -1);
        Date pastDay = calendar.getTime();
        sb.append(" and cdrdate BETWEEN  '");
        sb.append(BaseLib.formatDate("yyyyMMdd", pastDay));
        sb.append("'  and  '");
        calendar.clear();
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.MONTH, -1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date nowDay = calendar.getTime();
        sb.append(BaseLib.formatDate("yyyyMMdd", d));
        sb.append("'  ");
        String sql = sb.toString();
        Query query = this.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = (BigDecimal) o1;
            if (result != null) {
                result = result.divide(BigDecimal.valueOf(12), 0, BigDecimal.ROUND_HALF_UP);
            }
            return result;
        } catch (Exception ex) {
            log4j.error("QRAComplaintActual2", ex);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {

    }

    @Override
    public EntityManager getEntityManager() {
        try {
            Context c = new InitialContext();
            IndicatorBean indicatorBean = (IndicatorBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
            return indicatorBean.getEntityManager();
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }

    @Override
    public int getUpdateMonth(int y, int m) {
        return m;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        return y;
    }
}
