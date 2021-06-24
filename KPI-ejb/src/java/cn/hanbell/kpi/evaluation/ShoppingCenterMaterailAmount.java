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
import javax.naming.InitialContext;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C2082
 */
public class ShoppingCenterMaterailAmount implements Actual {

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
        String material = map.get("material") != null ? map.get("prono").toString() : "";
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT sum(acpamt) as cp_acpamt");
        sql.append(" FROM apmpyh left join KpiPurPcm  on apmpyh.facno = KpiPurPcm.Facno and apmpyh.vdrno=KpiPurPcm.Vdrno ,purvdr ,purhad");
        sql.append(" WHERE apmpyh.vdrno = purvdr.vdrno  and  purhad.facno = apmpyh.facno");
        sql.append(" and purhad.prono = apmpyh.prono and purhad.pono = apmpyh.pono");
        sql.append(" and  apmpyh.pyhkind = '1'");
        sql.append(" AND apmpyh.facno = ${facno}  and apmpyh.prono =${prono}");
        sql.append(" and KpiPurPcm.MaterialTypeName ='").append(material).append("'");
        sql.append(" and year(apmpyh.trdat) = ${year} and month(apmpyh.trdat)= ${month}");
        String sqlString = sql.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno).replace("${prono}", prono);
        try {
            superEJB.setCompany(facno);
            Query query = superEJB.getEntityManager().createNativeQuery(sqlString);
            Object o1 = query.getSingleResult();
            BigDecimal result = (BigDecimal) o1;
            if (result == null) {
                return BigDecimal.ZERO;
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

}
