/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.Category;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class CategoryBean extends SuperEJBForKPI<Category> {

    public CategoryBean() {
        super(Category.class);
    }

    public List<Category> findRootByCompany(String value) {
        Query query = getEntityManager().createNamedQuery("Category.findRootByCompany");
        query.setParameter("company", value);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

}
