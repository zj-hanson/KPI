/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1879 服务单结案单空压机体体现每月完成率
 */
public abstract class ServiceClosingSpecial extends Service {
  
    public ServiceClosingSpecial() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String status = map.get("status") != null ? map.get("status").toString() : "";
        String id = map.get("id") != null ? map.get("id").toString() : "";
        BigDecimal closing = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();

        //JA 结案 LJ 累计concat
        if ("JA".equals(status)) {
            sb.append(" select isnull(count(distinct a.TC001),0) from ( Select  TC004, (TC001+TC002) as TC001 from REPTC ");
            sb.append(" LEFT JOIN PORMZ LEFT JOIN PORMY on PORMZ.MZ001 = PORMY.MY001 and PORMZ.MZ002 = PORMY.MY002 on REPTC.TC001 = PORMZ.MZ005 and REPTC.TC002 = PORMZ.MZ006 ");
            sb.append(" where TC001 like '%FW%'  AND TC015<>'' and TC018='3' AND TC034='Y' ");
            sb.append(" and TC017 like '").append(deptno).append("%'");
            sb.append(" and year(TC004) =${y} AND  month(TC004)=${m} ");
        } else {
            sb.append(" Select isnull(count(distinct a.TC001),0) from (  Select (TC001+TC002) as TC001,TC003 from REPTC where TC001 like '%FW%' ");
            sb.append(" and TC017 like '").append(deptno).append("%'");
            sb.append(" and year(TC003) =${y} AND  month(TC003)=${m} ");
        }
        sb.append(" )as a ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));

        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            closing = new BigDecimal(o1.toString());
            List list = getLastValue(y, m, map);
            if (list != null && !list.isEmpty()) {
                if (!"".equals(id)) {
                    Indicator entity = indicatorBean.findById(Integer.valueOf(id));                    
                    IndicatorDetail order;                   
                    if ("JA".equals(status)) {
                        order = entity.getOther2Indicator();
                    } else {
                        order = entity.getOther1Indicator();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        Object[] row = (Object[]) list.get(i);
                        updateValue(Integer.valueOf(row[0].toString()), BigDecimal.valueOf(Double.valueOf(row[1].toString())), order);
                    }
                    indicatorDetailBean.update(order);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return closing;
    }

    public List getLastValue(int y, int m, LinkedHashMap<String, Object> map) {
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String status = map.get("status") != null ? map.get("status").toString() : "";

        StringBuilder sb = new StringBuilder();

        //JA 结案 LJ 累计concat
        if ("JA".equals(status)) {
            sb.append(" select month(TC004),isnull(count(distinct a.TC001),0) from ( Select  TC004, (TC001+TC002) as TC001 from REPTC ");
            sb.append(" LEFT JOIN PORMZ LEFT JOIN PORMY on PORMZ.MZ001 = PORMY.MY001 and PORMZ.MZ002 = PORMY.MY002 on REPTC.TC001 = PORMZ.MZ005 and REPTC.TC002 = PORMZ.MZ006 ");
            sb.append(" where TC001 like '%FW%'  AND TC015<>'' and TC018='3' AND TC034='Y' ");
            sb.append(" and TC017 like '").append(deptno).append("%'");
            sb.append(" and year(TC004) =${y} AND  month(TC004)< ${m} ");
            sb.append(" )as a GROUP BY month(TC004)");
        } else {
            sb.append(" Select month(TC003),isnull(count(distinct a.TC001),0) from (  Select (TC001+TC002) as TC001,TC003 from REPTC where TC001 like '%FW%' ");
            sb.append(" and TC017 like '").append(deptno).append("%'");
            sb.append(" and year(TC003) =${y} AND  month(TC003)<${m} ");
            sb.append(" )as a GROUP BY month(TC003)");
        }
        
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));

        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            List list = query.getResultList();
            return list;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


}
