/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.ShoppingManufacturer;
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
public class ShoppingManufacturerBean extends SuperEJBForKPI<ShoppingManufacturer> {

    public ShoppingManufacturerBean() {
        super(ShoppingManufacturer.class);
    }

    public List<ShoppingManufacturer> findByUsernaAndVdrna(String facno, String userna, String vdrna) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from shoppingmanufacturer where 1=1");
        if (facno != null && !"".equals(facno)) {
            sql.append(" and facno='").append(facno).append("'");
        }
        if (userna != null && !"".equals(userna)) {
            sql.append(" and userna='").append(userna).append("'");
        }
        if (vdrna != null && !"".equals(vdrna)) {
            sql.append(" and vdrna='").append(vdrna).append("'");
        }
        try {
            Query query = this.getEntityManager().createNativeQuery(sql.toString(),ShoppingManufacturer.class);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public ShoppingManufacturer findByVdrno(String vdrno) {
        Query q = this.getEntityManager().createNamedQuery("ShoppingManufacturer.findByVdrno");
        q.setParameter("vdrno", vdrno);
        try {
            ShoppingManufacturer result = (ShoppingManufacturer) q.getSingleResult();
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

}
