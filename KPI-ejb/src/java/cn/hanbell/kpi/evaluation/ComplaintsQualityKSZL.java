/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.SuperEJBForCRM;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
public class ComplaintsQualityKSZL {

    SuperEJBForCRM superEJBForCRM = lookupSuperEJBForCRMBean();

    public ComplaintsQualityKSZL() {
    }

    public List<String> getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //定义变量属性 获得查询参数
        String BQ197 = map.get("BQ197") != null ? map.get("BQ197").toString() : ""; //产品别
        String BQ130 = map.get("BQ130") != null ? map.get("BQ130").toString() : ""; //收费否
        String MI017 = map.get("MI017") != null ? map.get("MI017").toString() : ""; //是否在原厂保固期
        String BQ035 = map.get("BQ035") != null ? map.get("BQ035").toString() : ""; //结案码
        String BQ110 = map.get("BQ110") != null ? map.get("BQ110").toString() : ""; //是否客诉
        List<String> reslut = new LinkedList();
        StringBuilder sb = new StringBuilder();
        sb.append("select  CA009  from SERBQ,SERBR,(SELECT SERCA.CA001,CA009,");
        sb.append(" CASE WHEN datediff(DAY,REPMI.MI009,CONVERT(VARCHAR(8),GETDATE(),112))>0 THEN 'N' ELSE 'Y' END AS MI017");
        sb.append(" FROM SERCA AS SERCA");
        sb.append(" LEFT JOIN WARMB WARMB ON MB001=CA003");
        sb.append(" LEFT JOIN REPMQ REPMQ ON MQ001=CA010 AND MQ003='a1'");
        sb.append(" LEFT JOIN SERAC SERAC ON AC001=CA019");
        sb.append(" LEFT JOIN REPTA REPTA ON REPTA.TA001 = SERCA.CA010 AND REPTA.TA002 = SERCA.CA011");
        sb.append(" LEFT JOIN REPMI REPMI ON MI002=CA009 ) AS x");
        sb.append(" where SERBQ.BQ001=SERBR.BR001 and x.CA001=SERBQ.BQ001");
        sb.append(" and BQ197 = '${BQ197}' AND BQ130 ='${BQ130}' and MI017 = '${MI017}' and BQ035 = '${BQ035}' and BQ110 = '${BQ110}'");
        String sql = sb.toString().replace("${BQ197}", BQ197).replace("${BQ130}", BQ130).replace("${MI017}", MI017).replace("${BQ035}", BQ035).replace("${BQ110}", BQ110);
        Query query = superEJBForCRM.getEntityManager().createNativeQuery(sql);
        try {
            reslut = query.getResultList();
            return reslut;
        } catch (Exception e) {
        }
        return reslut;
    }

    private SuperEJBForCRM lookupSuperEJBForCRMBean() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForCRM) c.lookup("java:global/KPI/KPI-ejb/SuperEJBForCRM!cn.hanbell.kpi.comm.SuperEJBForCRM");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
