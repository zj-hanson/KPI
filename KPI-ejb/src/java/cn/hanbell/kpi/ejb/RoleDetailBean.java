/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.entity.RoleDetail;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
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
public class RoleDetailBean extends SuperEJBForKPI<RoleDetail> {

    public RoleDetailBean() {
        super(RoleDetail.class);
    }

    public List<RoleDetail> findByUserId(String id) {
        Query query = getEntityManager().createNamedQuery("RoleDetail.findByUserId");
        query.setParameter("userid", id);
        return query.getResultList();
    }

}
