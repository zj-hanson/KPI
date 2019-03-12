/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.SuperEJBForHFTYS;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;

/**
 *
 * @author C1749 冷媒试车合肥通用所数据集
 */
public class QRATrialRunAdverseR1V1 extends QRAConnHFTYS {

    public QRATrialRunAdverseR1V1() {

    }

    SuperEJBForHFTYS superEJBForHFTYS = lookupSuperEJBForHFTYSBean();

    SuperEJBForHFTYS lookupSuperEJBForHFTYSBean() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForHFTYS) c.lookup("java:global/KPI/KPI-ejb/SuperEJBForHFTYS!cn.hanbell.kpi.comm.SuperEJBForHFTYS");
        } catch (NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return super.getValue(y, m, d, type, map); //To change body of generated methods, choose Tools | Templates.
    }

    //取到冷媒合肥通用所的试车数据集
    public List getHftysList(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String itnbrType = map.get("itnbrType") != null ? map.get("itnbrType").toString() : "";//机型
        StringBuilder sb = new StringBuilder();
        sb.append(" select a.varnr from (  ");
        sb.append(" select  T.*,N.value from testdescription T ");
        sb.append(" LEFT JOIN nameplatedata N  on t.testid=n.testid ");
        sb.append(" where testnum=1 and N.nameplateparameter like  '机器型号%' ");
        if (!"".equals(itnbrType)) {
            if ("RC1".equals(itnbrType)) {
                sb.append(" and (N.value not like '%1020%' and N.value not like '%1130%' and N.value not like '%1270%' and  N.value not like '%1520%' and N.value not like '%1530%') ");
                sb.append(" and (N.value not like '%LB%' and N.value not like '%LT%') ");
            }
            if ("LB".equals(itnbrType)) {
                sb.append(" and (N.value like '%LB%' or N.value like '%LT%') ");
            }
            if ("RC2".equals(itnbrType)) {
                sb.append(" and (N.value like '%1020%' or N.value like '%1130%' or N.value like '%1270%' or  N.value like '%1520%' or N.value like '%1530%') ");
                sb.append(" and (N.value not like '%LB%' and N.value not like '%LT%') ");
            }
        }

        sb.append(" and  year(endtime)=${y} and month(dateadd(hour,-8,endtime))=${m} ");
        sb.append(" ) as a ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        superEJBForHFTYS.setConn("H");//H表示冷媒的数据库
        Query query1 = superEJBForHFTYS.getEntityManager().createNativeQuery(sql);
        try {

            List o1 = query1.getResultList();
            List o2 = o1;
            return o2;
        } catch (Exception ex) {
            log4j.error("QRATrialRunAdverseR1V1", ex);
        }
        return null;
    }

}
