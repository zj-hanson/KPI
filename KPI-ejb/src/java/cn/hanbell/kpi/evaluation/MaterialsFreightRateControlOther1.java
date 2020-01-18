/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
public class MaterialsFreightRateControlOther1 extends FreeServiceERP {

    public MaterialsFreightRateControlOther1() {
        super();
        queryParams.put("facno", "C");
    }

    //运费金额
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";

        //CDRN20
        BigDecimal cdrlnhad = BigDecimal.ZERO;
        //CDRX643、CDRX645
        BigDecimal cdrhad = BigDecimal.ZERO;
        //INV310、INV325
        BigDecimal invhad = BigDecimal.ZERO;
        
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT isnull(sum(f.freight),0) from cdrlnhad d ,cdrfre f  LEFT JOIN secuser s ON  f.userno=s.userno  where d.trno=f.shpno and d.status<>'W'  ");
        sb.append("  and d.facno='${facno}' and s.pdepno LIKE '1N%' ");
        sb.append(" and year(d.indate) = ${y} and month(d.indate)= ${m} ");
        String cdrn20 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        sb.setLength(0);
        sb.append(" SELECT isnull(sum(f.freight),0) FROM cdrhad d,cdrfre f LEFT JOIN secuser s ON  f.userno=s.userno  WHERE d.shpno=f.shpno and d.houtsta<>'W'  ");
        sb.append("  and d.facno='${facno}' and s.pdepno LIKE '1N%' ");
        sb.append(" and year(d.shpdate) = ${y} and month(d.shpdate)= ${m} ");
        String cdrx64 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)) .replace("${facno}", facno);

        sb.setLength(0);
        sb.append(" SELECT isnull(sum(f.freight),0) FROM invhadh d ,cdrfre f  LEFT JOIN secuser s ON  f.userno=s.userno  WHERE  d.trno=f.shpno and d.status<>'W'  ");
        sb.append("  and d.facno='${facno}' and s.pdepno LIKE '1N%' ");
        sb.append(" and year(d.trdate) = ${y} and month(d.trdate)= ${m} ");
        String inv3 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(cdrn20);
        Query query2 = superEJB.getEntityManager().createNativeQuery(cdrx64);
        Query query3 = superEJB.getEntityManager().createNativeQuery(inv3);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            Object o3 = query3.getSingleResult();
            cdrlnhad = (BigDecimal) o1;
            cdrhad = (BigDecimal) o2;
            invhad = (BigDecimal) o3;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        //不含税 并固定除以1.09 减税款
        return cdrlnhad.add(cdrhad).add(invhad).divide(BigDecimal.valueOf(1.09), 4, RoundingMode.HALF_UP);
    }

}
