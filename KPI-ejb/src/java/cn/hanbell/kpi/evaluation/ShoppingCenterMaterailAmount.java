/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C2082
 */
public class ShoppingCenterMaterailAmount implements Actual {

    protected SuperEJBForKPI superEJBForKPI = lookupSuperEJBForKPI();
    protected SuperEJBForERP superEJB;

    protected LinkedHashMap<String, Object> queryParams;
    protected final Logger log4j = LogManager.getLogger();

    public ShoppingCenterMaterailAmount() {
        queryParams = new LinkedHashMap<>();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String prono = map.get("prono") != null ? map.get("prono").toString() : "";
        String materialSql = map.get("material") != null ? map.get("material").toString() : "";
        try {
            StringBuffer materials = new StringBuffer();
            EntityManager em = superEJBForKPI.getEntityManager();
            Query query1 = em.createNativeQuery(materialSql);
            List<String> vdrnos = query1.getResultList();
            for (String o : vdrnos) {
                materials.append("'").append(o).append("',");
            }

            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT sum(acpamt) as cp_acpamt");
            sql.append(" FROM apmpyh ,purvdr ,purhad");
            sql.append(" WHERE apmpyh.vdrno = purvdr.vdrno  and  purhad.facno = apmpyh.facno");
            sql.append(" and purhad.prono = apmpyh.prono and purhad.pono = apmpyh.pono");
            sql.append(" and  apmpyh.pyhkind = '1'");
            //台湾汉钟的公司别和生产地与大陆的ERP规范不一样
            if (!"A".equals(facno)) {
                sql.append(" AND apmpyh.facno = '${facno}'  and apmpyh.prono ='${prono}'");
            }
            if (materials != null && !"".equals(materials.toString())) {
                sql.append(" and apmpyh.vdrno in(").append(materials.substring(0, materials.length() - 1)).append(")");
            } else {
                sql.append(" and apmpyh.vdrno in ('')");
            }
            sql.append(" and year(apmpyh.trdat) = ${year} and month(apmpyh.trdat)= ${month}");
            String sqlString = sql.toString().replace("${year}", String.valueOf(y)).replace("${month}", String.valueOf(m)).replace("${facno}", facno).replace("${prono}", prono);

            superEJB.setCompany(facno);
            Query query = superEJB.getEntityManager().createNativeQuery(sqlString);
            Object o1 = query.getSingleResult();
            BigDecimal result = (BigDecimal) o1;
            if (result == null) {
                result = BigDecimal.ZERO;
            }
            //台湾的币别除以汇率
            if ("A".equals(facno)) {
                result = result.divide(new BigDecimal("4.3"), 2);
            }
            return result;
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup(JNDIName);
        superEJB = (SuperEJBForERP) objRef;
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

    private SuperEJBForKPI lookupSuperEJBForKPI() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForKPI) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
