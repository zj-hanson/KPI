/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
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
 * 取到ERP出货的制造号码
 */
public class TrialRunAdverseAHGetERPShipment extends TrialRun {
    SuperEJBForERP superEJB = lookupSuperEJBForERPBean();
    
    private SuperEJBForERP lookupSuperEJBForERPBean() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForERP) c.lookup("java:global/KPI/KPI-ejb/SuperEJBForERP!cn.hanbell.kpi.comm.SuperEJBForERP");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    public TrialRunAdverseAHGetERPShipment() {
        
    }

    public List getVarnrList(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String typecode = map.get("typecode") != null ? map.get("typecode").toString() : "";//机型
        StringBuilder sb = new StringBuilder();
        List varnrList = new ArrayList();
        sb.append(" SELECT  cdrlot.varnr FROM cdrhad ");
        sb.append(" LEFT JOIN cdrdta ON cdrdta.shpno=cdrhad.shpno ");
        sb.append(" LEFT JOIN cdrlot ON cdrlot.shpno=cdrdta.shpno  AND cdrdta.trseq=cdrlot.trseq ");
        sb.append(" LEFT JOIN cdrcus ON cdrcus.cusno=cdrhad.cusno ");
        sb.append(" WHERE  cdrhad.houtsta<>'W' and cdrhad.facno = ${facno} ");
        sb.append(" AND  cdrdta.n_code_DA='AH' ");
        sb.append(" AND cdrdta.n_code_DD='00' and  cdrdta.n_code_DC like 'AJ%' ");
        if(!"".equals(typecode)){
            if(typecode.contains("480")){
                sb.append(" and (cdrdta.itnbrcus like 'AB-077%' or cdrdta.itnbrcus like 'AB-130%' or cdrdta.itnbrcus like 'AB-240%' or cdrdta.itnbrcus like 'AB-420%' or cdrdta.itnbrcus like 'AB-480R%') ");
            }
            if (typecode.contains("600")) {
                sb.append(" and (cdrdta.itnbrcus like 'AB-600R%' or cdrdta.itnbrcus like 'AAB-780R%') ");
            }
            if (typecode.contains("1030")) {
                sb.append(" and (cdrdta.itnbrcus like 'AB-1030R%' or cdrdta.itnbrcus like 'AB-1200R%') ");
            }
            if (typecode.contains("1320")) {
                sb.append(" and (cdrdta.itnbrcus like 'AB-1320%' or cdrdta.itnbrcus like 'AB-1560%' or cdrdta.itnbrcus like 'AB-1900R%' or cdrdta.itnbrcus like 'AB-2600%') ");
            }
        }
        sb.append(" and year(cdrhad.shpdate) =${y} and month(cdrhad.shpdate)=${m} ");
        String varnrSql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);
        superEJB.setCompany(facno);
        Query varnrQuery = superEJB.getEntityManager().createNativeQuery(varnrSql);
        try {
            varnrList = varnrQuery.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(TrialRunAdverseAHGetERPShipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return varnrList;
    }

}
