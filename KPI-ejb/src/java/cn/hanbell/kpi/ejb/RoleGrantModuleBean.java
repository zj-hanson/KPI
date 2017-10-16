/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
@Stateless
public class RoleGrantModuleBean extends SuperEJBForKPI<RoleGrantModule> {

    public RoleGrantModuleBean() {
        super(RoleGrantModule.class);
    }

    public int getRowCountByUserId(int id) {
        Query query = getEntityManager().createNamedQuery("RoleGrantModule.getRowCountByUserId");
        query.setParameter("userid", id);
        try {
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public List<RoleGrantModule> findByUserId(int id) {
        Query query = this.getEntityManager().createNamedQuery("RoleGrantModule.findByUserId");
        query.setParameter("userid", id);
        return query.getResultList();
    }

    public List<RoleGrantModule> findByRoleId(int id) {
        Query query = this.getEntityManager().createNamedQuery("RoleGrantModule.findByRoleId");
        query.setParameter("roleid", id);
        return query.getResultList();
    }

}
