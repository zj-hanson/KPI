/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */

package cn.hanson.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForMES;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author C0160
 */
public abstract class CastingProduction implements Actual {

    SuperEJBForMES superEJBForMES = lookupSuperEJBForMESBean();

    protected LinkedHashMap<String, Object> queryParams;

    protected final org.apache.logging.log4j.Logger log4j = LogManager.getLogger("cn.hanbell.kpi");

    public CastingProduction() {
        queryParams = new LinkedHashMap<>();
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        // 指定MES连接此处不处理
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

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // 公司
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        // 产线
        String line = map.get("line") != null ? map.get("line").toString() : "";
        // 工序
        String step = map.get("step") != null ? map.get("step").toString() : "";

        BigDecimal ton = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ISNULL(SUM(CAST(A.CASTINGWEIGHT AS FLOAT)*CAST(A.TRACKOUTQTY AS FLOAT)),0) ");
        sb.append(" FROM CAST_PROCESS_STEP A LEFT JOIN CAST_PROCESS B ON A.PRODUCTORDERID = B.PRODUCTORDERID ");
        sb.append(" WHERE B.PROCESSSTATUS != '已结案' ");
        if (!"".equals(line)) {
            sb.append(" AND A.PROCESSLINE ").append(line);
        }
        if (!"".equals(step)) {
            sb.append(" AND A.STEPID ").append(step);
        }
        sb.append(" AND year(B.PRODUCTTIME) = ${y} AND month(B.PRODUCTTIME)= ${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" AND datepart(DAY ,B.PRODUCTTIME) <= ${d} ");
                break;
            case 5:
                // 日
                sb.append(" AND datepart(DAY ,B.PRODUCTTIME) = ${d} ");
                break;
            default:
                sb.append(" AND datepart(DAY ,B.PRODUCTTIME) = ${d} ");
        }

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}",
            String.valueOf(day));

        superEJBForMES.setCompany(facno);
        Query query = superEJBForMES.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            ton = BigDecimal.valueOf(Double.valueOf(o1.toString()));
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return ton;

    }

    protected BigDecimal getBoxQuantity(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return BigDecimal.ZERO;
    }

    protected BigDecimal getQuantity(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return BigDecimal.ZERO;
    }

    private SuperEJBForMES lookupSuperEJBForMESBean() {
        try {
            Context context = new InitialContext();
            return (SuperEJBForMES)context
                .lookup("java:global/KPI/KPI-ejb/SuperEJBForMES!cn.hanbell.kpi.comm.SuperEJBForMES");
        } catch (NamingException ne) {
            log4j.error(ne);
            throw new RuntimeException(ne);
        }
    }

}
