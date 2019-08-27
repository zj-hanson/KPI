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

}
