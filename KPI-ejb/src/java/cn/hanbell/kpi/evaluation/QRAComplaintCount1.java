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
 * 查询所有制令明细的制造号码
 */
public class QRAComplaintCount1 {

    SuperEJBForCRM superEJBForCRM = lookupSuperEJBForCRMBean();

    public QRAComplaintCount1() {
    }

    public List<String> getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //定义变量属性 获得查询参数
        String BQ197 = map.get("BQ197") != null ? map.get("BQ197").toString() : ""; //产品别
        String BQ003 = map.get("BQ003") != null ? map.get("BQ003").toString() : ""; //品號類別代號
        String BQ505 = map.get("BQ505") != null ? map.get("BQ505").toString() : ""; //冷媒特殊处理 不算BQ134为-1的情况
        String BQ110 = map.get("BQ110") != null ? map.get("BQ110").toString() : ""; //是否客诉
        List<String> reslut = new LinkedList();
        StringBuilder sb = new StringBuilder();
        sb.append(" select a.CA009 as num from ( ");
        sb.append(" select  DISTINCT CA009,BQ001 from SERBQ,SERCA where BQ001= CA001  ");
        if(!"".equals(BQ197)){
            sb.append(" and  rtrim(BQ197) ").append(BQ197);
        }
        if (!"".equals(BQ003)) {
            sb.append(" and BQ003 ").append(BQ003);
        }
        if (!"".equals(BQ505)) {
            sb.append(" and BQ505 ").append(BQ505);
        }
        if (!"".equals(BQ110)) {
            sb.append(" and BQ110 ").append(BQ110);
        }
        sb.append(") as a ");
        String sql = sb.toString();
        Query query = superEJBForCRM.getEntityManager().createNativeQuery(sql);
        try {
            reslut = query.getResultList();
            return reslut;
        } catch (Exception ex) {
            Logger.getLogger(QRAComplaintCount1.class.getName()).log(Level.SEVERE, null, ex);
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
