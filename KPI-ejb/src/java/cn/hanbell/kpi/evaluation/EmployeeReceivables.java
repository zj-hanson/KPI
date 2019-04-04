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
import javax.persistence.Query;

/**
 *
 * @author C1879
 * 应收款项
 */
public class EmployeeReceivables extends Shipment {

    public EmployeeReceivables() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String userid = map.get("userid") != null ? map.get("userid").toString() : "";

        StringBuilder sb = new StringBuilder();
        sb.append("select ISNULL(sum(booamt - recamt),0) from armhad  where facno='${facno}'");
        if (!"".equals(userid)) {
            sb.append(" and mancode ='").append(userid).append("' ");
        }
        sb.append(" AND year(bildat) = ${y} and month(bildat)=${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" AND bildat<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" AND bildat= '${d}' ");
                break;
            default:
                sb.append(" bildat<= '${d}' ");
        }
        String sqlstr = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${facno}", facno);
        superEJB.setCompany(facno);
        
        Query query = superEJB.getEntityManager().createNativeQuery(sqlstr);
        try {
            Object o = query.getSingleResult();
            return (BigDecimal) o;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

}
