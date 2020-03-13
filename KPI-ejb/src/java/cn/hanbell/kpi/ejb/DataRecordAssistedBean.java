/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.DataRecordAssisted;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class DataRecordAssistedBean extends SuperEJBForKPI<DataRecordAssisted> {

    public DataRecordAssistedBean() {
        super(DataRecordAssisted.class);
    }

    public DataRecordAssisted findByFacnoAndTypeAndItemname(String facno, String type, String itemname) {
        Query query = getEntityManager().createNamedQuery("DataRecordAssisted.findByFacnoAndTypeAndItemname");
        query.setParameter("facno", facno);
        query.setParameter("type", type);
        query.setParameter("itemname", itemname);
        try {
            Object o = query.getSingleResult();
            return (DataRecordAssisted) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public Integer findByShownoMax(String facno) {
        int showno = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(" select max(showno) FROM dataRecordAssisted where facno='${facno}'");
        String sql = sb.toString().replace("${facno}", facno);
        try {
            Query query = getEntityManager().createNativeQuery(sql);
            Object o1 = query.getSingleResult();
            if (o1 != null) {
                showno = (Integer) o1;
            }
            return showno + 1;
        } catch (Exception e) {
            return null;
        }
    }

}
