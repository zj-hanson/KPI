/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.DataRecorDassisted;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class DataRecorDassistedBean extends SuperEJBForKPI<DataRecorDassisted> {

    public DataRecorDassistedBean() {
        super(DataRecorDassisted.class);
    }

    public DataRecorDassisted findByFacnoAndTypeAndItemname(String facno, String type, String itemname) {
        Query query = getEntityManager().createNamedQuery("DataRecorDassisted.findByFacnoAndTypeAndItemname");
        query.setParameter("facno", facno);
        query.setParameter("type", type);
        query.setParameter("itemname", itemname);
        try {
            Object o = query.getSingleResult();
            return (DataRecorDassisted) o;
        } catch (Exception ex) {
            return null;
        }
    }

}
