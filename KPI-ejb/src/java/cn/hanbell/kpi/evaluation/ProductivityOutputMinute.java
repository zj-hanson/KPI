/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import com.lightshell.comm.BaseLib;
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
public class ProductivityOutputMinute extends Productivity {

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String prono = map.get("prono") != null ? map.get("prono").toString() : "";
        String prosscode = map.get("prosscode") != null ? map.get("prosscode").toString() : "";
        String linecode = map.get("linecode") != null ? map.get("linecode").toString() : "";
        String stats = map.get("stats") != null ? map.get("stats").toString() : "";
        BigDecimal OutputMinute = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT isnull(sum(sfcwad.attqty1*isnull(manbor.mchstdtm,0)),0) FROM invmas,manmas,sfcwad,sfcwah,manbor ");
        sb.append(" WHERE  sfcwah.facno = sfcwad.facno and  sfcwah.prono = sfcwad.prono  and  sfcwah.inpno = sfcwad.inpno ");
        sb.append(" AND  sfcwad.manno = manmas.manno  and  sfcwad.facno = manmas.facno  and  sfcwad.prono = manmas.prono ");
        sb.append(" AND  invmas.itnbr = manmas.itnbrf AND manbor.facno=manmas.facno  and manbor.prono=manmas.prono AND manbor.manno = manmas.manno ");
        sb.append(" AND  sfcwah.facno = '${facno}' and sfcwah.prono ='${prono}'  ");
        if (!"".equals(prosscode)) {
            sb.append(" and manbor.prosscode ").append(prosscode);
        }
        if (!"".equals(linecode)) {
            sb.append(" and manmas.linecode ").append(linecode);
        }
        if (!"".equals(stats)) {
            sb.append(" and sfcwad.stats ").append(stats);
        }

        sb.append(" AND year(sfcwah.indat) = ${y} and month(sfcwah.indat)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and sfcwah.indat<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and sfcwah.indat= '${d}' ");
                break;
            default:
                sb.append(" and sfcwah.indat<= '${d}' ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno).replace("${prono}", prono);

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            OutputMinute = (BigDecimal) o;
        } catch (Exception e) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, e);
        }
        return OutputMinute;
    }

}
