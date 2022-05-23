/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.ShoppingManufacturer;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
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
public class ShoppingManufacturerBean extends SuperEJBForKPI<ShoppingManufacturer> {

    public ShoppingManufacturerBean() {
        super(ShoppingManufacturer.class);
    }

    public List<ShoppingManufacturer> findByFacno(String facno) {
        Query q = this.getEntityManager().createNamedQuery("ShoppingManufacturer.findByFacno");
        q.setParameter("facno", facno);
        try {
            List<ShoppingManufacturer> result = q.getResultList();
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

    
    public List<ShoppingManufacturer> findByMaterialTypeName(String facno,String materialTypeName) {
        StringBuffer sql=new StringBuffer();
        sql.append(" SELECT * FROM ShoppingManufacturer s");
        sql.append(" where s.facno = '").append(facno).append("' and s.materialTypeName in(").append(materialTypeName).append(")");
        Query q = this.getEntityManager().createNativeQuery(sql.toString(),ShoppingManufacturer.class);
        try {
            List<ShoppingManufacturer> result = q.getResultList();
            return result;
        } catch (Exception ex) {
            return null;
        }
    }
    
    public List<ShoppingManufacturer> findByUsernaAndVdrna(String facno, String userna, String vdrna) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from shoppingmanufacturer where 1=1");
        if (facno != null && !"".equals(facno)) {
            sql.append(" and facno='").append(facno).append("'");
        }
        if (userna != null && !"".equals(userna)) {
            sql.append(" and userna like'%").append(userna).append("%'");
        }
        if (vdrna != null && !"".equals(vdrna)) {
            sql.append(" and vdrna like'%").append(vdrna).append("%'");
        }
        try {
            Query query = this.getEntityManager().createNativeQuery(sql.toString(), ShoppingManufacturer.class);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public ShoppingManufacturer findByVdrnoAndFacno(String vdrno, String facno) {
        Query q = this.getEntityManager().createNamedQuery("ShoppingManufacturer.findByVdrnoAndFacno");
        q.setParameter("vdrno", vdrno);
        q.setParameter("facno", facno);
        try {
            Object o = q.getSingleResult();
            return (ShoppingManufacturer) o;
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

}
