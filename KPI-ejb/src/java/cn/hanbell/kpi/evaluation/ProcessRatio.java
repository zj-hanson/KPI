/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.SuperEJBForMES;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
public class ProcessRatio extends Process {

    private SuperEJBForMES superMES;

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String stats = map.get("stats") != null ? map.get("stats").toString() : "";
        String RESPONSIBILITYDP = map.get("RESPONSIBILITYDP") != null ? map.get("RESPONSIBILITYDP").toString() : "";
        String ANALYSISJUDGEMENTRESULT = map.get("ANALYSISJUDGEMENTRESULT") != null ? map.get("ANALYSISJUDGEMENTRESULT").toString() : "";

        BigDecimal godqty = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT isnull(count(s.PROJECTID),0) FROM FLOW_FORM_UQF_S_NOW s INNER JOIN FLOW_FORM_UQF_COMP_NOW c ON s.PROJECTID=c.PROJECTID  WHERE 0=0 ");
        if (!"Scrap".equals(stats)) {
            sb.append(" and s.PROJECTID IN (SELECT DISTINCT PROJECTID FROM FLOW_FORM_UQF_COMP_NOW WHERE PRODUCTID NOT LIKE '%-GB%' ) ");
        }
        if (!"Scrap".equals(stats)) {
            sb.append(" and s.PROJECTCREATEUSERID in (select USERID FROM MPROCESSUSERGROUP_USER where GROUPROLE<>'普通人员' AND GROUPID='GU001') ");
        }
        if ("YX".equals(stats)) {
            //圆型不良件数特有条件
            sb.append(" AND (s.DEFECTDESCRIPTION LIKE '%齿形%' OR  s.DEFECTDESCRIPTION LIKE '%导程%') ");
        }
        if (!"".equals(RESPONSIBILITYDP)) {
            sb.append(" and s.RESPONSIBILITYDP ").append(RESPONSIBILITYDP);
        }
        if (!"".equals(ANALYSISJUDGEMENTRESULT)) {
            sb.append(" and  s.ANALYSISJUDGEMENTRESULT ").append(ANALYSISJUDGEMENTRESULT);
        }

        sb.append(" and year(s.PROJECTCREATETIME) = ${y} and month(s.PROJECTCREATETIME)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and s.PROJECTCREATETIME<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and s.PROJECTCREATETIME= '${d}' ");
                break;
            default:
                sb.append(" and s.PROJECTCREATETIME<= '${d}' ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));

        Query query = superMES.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            godqty = BigDecimal.valueOf((int)o) ;
        } catch (Exception e) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, e);
        }
        return godqty;
    }

    public SuperEJBForMES getSuperMES() {
        return superMES;
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup(JNDIName);
        superMES = (SuperEJBForMES) objRef;
    }

}
