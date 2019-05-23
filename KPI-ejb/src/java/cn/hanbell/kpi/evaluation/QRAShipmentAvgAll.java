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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
public class QRAShipmentAvgAll extends SuperEJBForKPI<SalesTable> implements Actual {

    protected LinkedHashMap<String, Object> queryParams;

    public QRAShipmentAvgAll() {
        super(SalesTable.class);
        queryParams = new LinkedHashMap<>();
    }

    //获得当月的移动平均出货台数
    public List getAvgShip(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        BigDecimal result = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        List list = new ArrayList();
        try {
            for (int i = 0; i < 12; i++) {
                sb.setLength(0);
                sb.append(" SELECT  sum(quantity) as num FROM salestable WHERE 1=1 and  type='Shipment' ");
                if (!"".equals(n_code_DA)) {
                    sb.append(" and n_code_DA = '").append(n_code_DA).append("' ");
                }
                if (!"".equals(n_code_DC)) {
                    sb.append(" and n_code_DC = '").append(n_code_DC).append("' ");
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
                sb.append(" and DATE_FORMAT(cdrdate,'%Y%m') BETWEEN  '");
                sb.append(BaseLib.formatDate("yyyyMM", pastYear));
                sb.append("'  and  '");
                sb.append(BaseLib.formatDate("yyyyMM", nowYear));
                sb.append("'  ");
                String sql = sb.toString();
                Query query = this.getEntityManager().createNativeQuery(sql);
                //先通过循环得到12个月中每个月的移动平均值
                Object o1 = query.getSingleResult();
                result = (BigDecimal) o1;
                result = result.divide(BigDecimal.valueOf(12), 2, BigDecimal.ROUND_HALF_UP);
                //把每个月的值都加到集合中取
                list.add(result);
            }
            return list;
        } catch (NumberFormatException ex) {
            log4j.error("QRAComplaintAvg-getAvgShip()", ex);
        }
        return null;
    }

    //获得3/6/12的移动平均出货台数的总和
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mis = map.get("mis") != null ? map.get("mis").toString() : ""; //MJS（3/6/12）
        int count = Integer.parseInt(mis);
        BigDecimal sumAvgNum = BigDecimal.ZERO;
        Double avgNum = 0.0;
        List avglist = getAvgShip(y, m, d, type, map);
        try {
            if (!avglist.isEmpty()) {
                //当集合不为空时 循环得到 3 6 12 的MIS的移动平均总数
                for (int i = 0; i < count; i++) {
                    avgNum += Double.valueOf(avglist.get(i).toString());
                }
                sumAvgNum = BigDecimal.valueOf(avgNum);
            }
            return sumAvgNum;
        } catch (NumberFormatException ex) {
            log4j.error("QRAComplaintAvg-getValue()", ex);
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
