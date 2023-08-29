/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.ShoppingMenuWeight;
import cn.hanbell.kpi.entity.ShoppingTable;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class ShoppingTableBean extends SuperEJBForKPI<ShoppingTable> {

    public ShoppingTableBean() {
        super(ShoppingTable.class);
    }

    public List<ShoppingTable> findByYearmon(String yearmon) {
        Query q = this.getEntityManager().createNamedQuery("ShoppingTable.findByYearmon");
        q.setParameter("yearmon", yearmon);
        try {
            List<ShoppingTable> result = q.getResultList();
            return result;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<ShoppingTable> findByFacnoAndYearmon(String facno, String yearmon) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select head.*");
        sql.append(" from shoppingtable head left join shoppingmanufacturer detail on head.facno=detail.facno and head.vdrno=detail.vdrno");
        sql.append(" where detail.facno is not null");
        sql.append(" and");
        sql.append(" head.facno='").append(facno).append("'");
        sql.append(" and");
        sql.append(" head.yearmon='").append(yearmon).append("'");
        Query q = this.getEntityManager().createNativeQuery(sql.toString(),ShoppingTable.class);
        try {
            List<ShoppingTable> result = q.getResultList();
            return result;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void deleteByYearmon(String yearmon) {
        StringBuffer sql = new StringBuffer();
        sql.append(" delete from shoppingtable where yearmon='").append(yearmon).append("'");
        try {
            this.getEntityManager().createNativeQuery(sql.toString()).executeUpdate();
        } catch (Exception e) {
        }
    }
}
