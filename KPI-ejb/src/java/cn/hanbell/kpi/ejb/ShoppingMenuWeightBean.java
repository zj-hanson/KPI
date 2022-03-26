/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.ShoppingManufacturer;
import cn.hanbell.kpi.entity.ShoppingMenuWeight;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class ShoppingMenuWeightBean extends SuperEJBForKPI<ShoppingMenuWeight> {

    public final String shbToHsZj = "";

    public ShoppingMenuWeightBean() {
        super(ShoppingMenuWeight.class);
    }

    public ShoppingMenuWeight findByItnbrAndFacno(String itnbr, String facno) {
        Query q = this.getEntityManager().createNamedQuery("ShoppingMenuWeight.findByItnbrAndFacno");
        q.setParameter("itnbr", itnbr);
        q.setParameter("facno", facno);
        try {
            ShoppingMenuWeight result = (ShoppingMenuWeight) q.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public List<ShoppingMenuWeight> findByFacno(String facno) {
        Query q = this.getEntityManager().createNamedQuery("ShoppingMenuWeight.findByFacno");
        q.setParameter("facno", facno);
        try {
            List<ShoppingMenuWeight> result = q.getResultList();
            return result;
        } catch (Exception ex) {
            return null;
        }
    }
}
