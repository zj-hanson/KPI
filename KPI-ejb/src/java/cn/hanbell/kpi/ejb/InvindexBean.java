/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.Invindex;
import cn.hanbell.kpi.entity.InvindexDetail;
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
public class InvindexBean extends SuperEJBForKPI<Invindex> {

    public InvindexBean() {
        super(Invindex.class);
    }

    public Invindex findByGenernoAndFormid(String generno, String formid) {
        Query query = getEntityManager().createNamedQuery("Invindex.findByGenernoAndFormid");
        query.setParameter("generno", generno.trim());
        query.setParameter("formid", formid.trim());
        try {
            return (Invindex)query.getSingleResult();
        } catch (Exception ex) {
            throw ex;
        }
    }

}
