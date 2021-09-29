/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
public class ShoppingCenterAmountTHB extends ShoppingCenterAmount {

    public ShoppingCenterAmountTHB() {
        super();
        queryParams.put("facno", "A");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {

        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String prono = map.get("prono") != null ? map.get("prono").toString() : "";
        String vdrnoSql = map.get("vdrno") != null ? map.get("vdrno").toString() : "";
        //获取采购中心所有厂商
        try {
            StringBuffer vdrno = null;
            if (vdrnoSql != null && !"".equals(vdrnoSql)) {
                vdrno = new StringBuffer();
                Query query1 = superEJBForKPI.getEntityManager().createNativeQuery(vdrnoSql);
                List<String> vdrnos = query1.getResultList();
                for (String o : vdrnos) {
                    vdrno.append("'").append(o).append("',");
                }
            }
            StringBuffer sb = new StringBuffer();
            sb.append(" select   sum(apmpyh.acpamt)/4.3   as 'cp_acpamt' ");
            sb.append(" from apmpyh ,purvdr ");
            sb.append(" where purvdr.vdrno = apmpyh.vdrno ");
            sb.append(" and purkind not in ('9999') ");
            sb.append(" and apmpyh.pyhkind='1' and itnbr <> '9' ");
            if (vdrno != null && !"".equals(vdrno)) {
                sb.append(" and apmpyh.vdrno in (").append(vdrno.substring(0, vdrno.length() - 1)).append(")");
            }
            sb.append(" and year(apmpyh.trdat) = ${y} and month(apmpyh.trdat)= ${m} ");
            String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
            superEJB.setCompany(facno);
            Query query = superEJB.getEntityManager().createNativeQuery(sql);
            Object o1 = query.getSingleResult();
            BigDecimal result = (BigDecimal) o1;
            if (result == null) {
                return BigDecimal.ZERO;
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
}
