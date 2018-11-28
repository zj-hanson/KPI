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
 * 查询所有制令明细
 */
public class ComplaintsQualityOrder {

    SuperEJBForCRM superEJBForCRM = lookupSuperEJBForCRMBean();

    public ComplaintsQualityOrder() {
    }

    public List<String> getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //定义变量属性 获得查询参数
        String BQ197 = map.get("BQ197") != null ? map.get("BQ197").toString() : ""; //产品别
        String BQ003 = map.get("BQ003") != null ? map.get("BQ003").toString() : ""; //品號類別代號
        String BQ134 = map.get("BQ134") != null ? map.get("BQ134").toString() : ""; //冷媒特殊处理 不算BQ134为-1的情况
        String BQ110 = map.get("BQ110") != null ? map.get("BQ110").toString() : ""; //是否客诉
        List<String> reslut = new LinkedList();
        StringBuilder sb = new StringBuilder();
        sb.append(" select a.CA009 as num from ( ");
        sb.append(" select  DISTINCT CA009,BQ001 from SERBQ,SERCA where BQ001= CA001  ");
        sb.append(" and  rtrim(BQ197) like '${BQ197}' ");
         if (!"".equals(BQ003)) {
            sb.append(" and BQ003 ").append(BQ003);
        }
        if (!"".equals(BQ134)) {
            sb.append(" and BQ505 ").append(BQ134);
        }
        if (!"".equals(BQ110)) {
            sb.append(" and BQ110 ").append(BQ110);
        }
        sb.append(") as a ");
        String sql = sb.toString().replace("${BQ197}", BQ197);
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
