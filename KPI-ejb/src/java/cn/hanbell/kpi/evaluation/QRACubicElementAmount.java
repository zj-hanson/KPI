/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1749 三次元完工入库总数
 */
public class QRACubicElementAmount implements Actual {

    protected SuperEJBForERP superEJB = lookupSuperEJBForERP();
    protected LinkedHashMap<String, Object> queryParams;
    protected final Logger log4j = LogManager.getLogger();

    public QRACubicElementAmount() {
        queryParams = new LinkedHashMap<>();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String genre1 = map.get("genre1") != null ? map.get("genre1").toString() : "";//产品别
        BigDecimal result = BigDecimal.ZERO;//总加工数
        StringBuilder sb = new StringBuilder();
        //圆型件精加工和方形件精加工总数量
        sb.append(" select isnull(sum(a.al),0) from ( ");
        sb.append(" SELECT manmas.itnbrf as '件号',invmas.itdsc as '品名',invmas.spdsc as '规格',invmas.genre1,sum(sfcwad.attqty1) as  al ");
        sb.append(" FROM invmas,manmas,sfcwad,sfcwah ");
        sb.append(" WHERE ( sfcwah.facno = sfcwad.facno ) and ");
        sb.append(" ( sfcwah.prono = sfcwad.prono ) and ");
        sb.append(" ( sfcwah.inpno = sfcwad.inpno ) and ");
        sb.append(" ( sfcwad.manno = manmas.manno ) and ");
        sb.append(" ( sfcwad.facno = manmas.facno ) and ");
        sb.append(" ( sfcwad.prono = manmas.prono ) and ");
        sb.append(" ( invmas.itnbr = manmas.itnbrf ) ");
        sb.append(" and sfcwah.facno = 'C' and sfcwah.prono = '1' and sfcwah.stats <>'3' ");
        sb.append(" and manmas.typecode in ('01','02','05') and manmas.linecode in ('FX','YX') and manmas.itnbrf not like '%-GB%' ");
        if (!"".equals(genre1)) {
            if (genre1.equals("R")) {//取ERPinvmas表里的genre1（产品别）判断
                sb.append(" and (invmas.genre1 = 'R'  or invmas.genre1 = 'L' or invmas.genre1 = 'RG' or invmas.genre1 = 'RT' ) ");
            }
            if (genre1.equals("AH")) {
                sb.append(" and (invmas.genre1 = 'A'  or invmas.genre1 = 'AA' or invmas.genre1 = 'AH' or invmas.genre1 = 'AJ' ) ");
            }
            if (genre1.equals("P")) {
                sb.append(" and (invmas.genre1 = 'P'  or invmas.genre1 = 'PA' or invmas.genre1 = 'PH' ) ");
            }
        }
        sb.append(" and year(sfcwah.indat) = ${y}  and  month(dateadd(hour,-8,sfcwah.indat))=${m} ");
        sb.append(" GROUP BY manmas.itnbrf,invmas.itdsc,invmas.spdsc,invmas.genre1 ");
        sb.append(" ) as a ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = BigDecimal.valueOf(Double.valueOf(o1.toString()));
            return result;
        } catch (Exception ex) {
            log4j.error("QRACubicElementAmount异常", ex);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }

    protected SuperEJBForERP lookupSuperEJBForERP() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForERP) c.lookup("java:global/KPI/KPI-ejb/SuperEJBForERP!cn.hanbell.kpi.comm.SuperEJBForERP");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup("java:global/KPI/KPI-ejb/SuperEJBForERP!cn.hanbell.kpi.comm.SuperEJBForERP");
        superEJB = (SuperEJBForERP) objRef;
    }

    @Override
    public int getUpdateMonth(int y, int m) {
        int month;
        if (m == 1) {
            month = 12;
        } else {
            month = m - 1;
        }
        return month;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        int year;
        if (m == 1) {
            year = y - 1;
        } else {
            year = y;
        }
        return year;
    }

}
