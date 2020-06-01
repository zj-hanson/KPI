/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
/**
 *
 * @author C1879 服务维修
 */
public class ServiceMaintainQuantity extends ServiceMaintain {

    public ServiceMaintainQuantity() {
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String CProductType = map.get("CProductType") != null ? map.get("CProductType").toString() : "";

        BigDecimal quantity = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT count(*)  FROM ServerRepairTB where 1=1 and ");
        if (!"CProductType".equals(CProductType)) {
            sb.append(" CProductType ").append(CProductType);
        }
        sb.append(" and year(FinishDate) = ${y} and month(FinishDate)= ${m} ");

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));

        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            quantity = BigDecimal.valueOf((int) o);
        } catch (Exception e) {
            Logger.getLogger(ProcessRatio.class.getName()).log(Level.SEVERE, null, e);
        }
        return quantity;
    }

}
