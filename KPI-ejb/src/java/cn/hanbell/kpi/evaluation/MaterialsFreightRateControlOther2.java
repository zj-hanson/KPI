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
 * @author C1879
 */
public class MaterialsFreightRateControlOther2 extends FreeServiceERP {

    public MaterialsFreightRateControlOther2() {
        super();
        queryParams.put("facno", "C");
    }

    //营业额
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";

        //取资产损益表销售收入
        BigDecimal accinmon = BigDecimal.ZERO;
        
        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(tmon),0)  from accinmon  where facno='CK' and seq = 1   ");
        sb.append(" and accyear = ${y} and accmon = ${m} ");
        String cdrn20 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(cdrn20);
        try {
            Object o1 = query1.getSingleResult();
            if(o1!= null){
                accinmon = (BigDecimal) o1;
            }           
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return accinmon;
    }
}
