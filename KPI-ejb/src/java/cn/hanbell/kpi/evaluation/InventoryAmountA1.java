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
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author C1749 生产目标计算接口
 */
public class InventoryAmountA1 extends Inventory {

    public InventoryAmountA1() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String genre = map.get("genre") != null ? map.get("genre").toString() : "";
        try {
            StringBuilder sb = new StringBuilder();
            List<String> warehs = this.getWarehs(map);
            if (warehs == null || genre == null || "".equals(genre)) {
                throw new Exception();
            }
            sb.append(" select sum(a.sum)");
            sb.append(" from (select sum(amount)+sum(amamount) as 'sum'");
            sb.append(" from inventoryproduct where genre ").append(genre);
            sb.append(" and wareh in ('").append(StringUtils.join(warehs.toArray(), "\',\'")).append("')");
            sb.append(" and yearmon =  '").append(y).append(getMon(m)).append("'");
            sb.append(" union all");
            //生产库存需要加上生产在制和借厂商部分
            sb.append(" select sum(amount)+sum(amamount) as 'sum'");
            sb.append(" from inventoryproduct where genre ").append(genre);
            sb.append(" and wareh in ('SCZZ')");
            sb.append(" and yearmon =  '").append(y).append(getMon(m)).append("'");
            sb.append(" union all");
            sb.append(" select sum(amount)+sum(amamount) as 'sum'");
            sb.append(" from inventoryproduct where genre ").append(genre);
            sb.append(" whdsc in ('借厂商')");
            sb.append(" and yearmon =  '").append(y).append(getMon(m)).append("'");
            sb.append(" )a;");
            Query q = superEJBForKPI.getEntityManager().createNativeQuery(sb.toString());
            return (BigDecimal) q.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    @Override
    public List<String> getWarehs(LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String genreno = map.get("genreno") != null ? map.get("genreno").toString() : "";
        String genrena = map.get("genrena") != null ? map.get("genrena").toString() : "";
        StringBuilder sb = new StringBuilder();
        sb.append(" select detail.wareh");
        sb.append(" from invindexdta detail left join   invindexhad head");
        sb.append(" on detail.facno=head.facno and  detail.prono=head.prono");
        sb.append(" AND detail.indno=head.indno and  detail.genreno=head.genreno where head.genreno ").append(genreno);
        sb.append(" and genrena").append(genrena);
        try {
            Query q = superEJBForERP.getEntityManager().createNativeQuery(sb.toString());
            List<String> warens = q.getResultList();
            return warens;
        } catch (Exception e) {
            return null;
        }
    }

}
