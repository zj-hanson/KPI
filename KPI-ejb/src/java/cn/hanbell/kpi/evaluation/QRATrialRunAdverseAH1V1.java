/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.SuperEJBForHFTYS;
import cn.hanbell.kpi.comm.SuperEJBForHXY;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1749 空压机体海学友试车数据集
 */
public class QRATrialRunAdverseAH1V1 extends QRAConnHXY {
    public QRATrialRunAdverseAH1V1(){
        super();
    }
    protected final Logger log4j = LogManager.getLogger();
    
    SuperEJBForHXY superEJBForHXY = lookupSuperEJBForHXYBean();
    SuperEJBForHXY lookupSuperEJBForHXYBean() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForHXY) c.lookup("java:global/KPI/KPI-ejb/SuperEJBForHXY!cn.hanbell.kpi.comm.SuperEJBForHXY");
        } catch (NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    //取到机体海学友的试车数据集
    public List getHxyList(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        List o1 = new ArrayList();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT T.varnr FROM hxy_testhad T LEFT JOIN hxy_testdta Y on T.hid = Y.did  and  testcount='1' ");
        sb.append(" WHERE   year(testdate)=${y} and   month(dateadd(hour,-8,testdate))=${m} ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
        Query query1 = superEJBForHXY.getEntityManager().createNativeQuery(sql);
        try {
            o1 = query1.getResultList();
            return o1;
        } catch (Exception ex) {
            log4j.error("QRATrialRunAdverseAH1V1", ex);
        }
        return null;
    }

}
