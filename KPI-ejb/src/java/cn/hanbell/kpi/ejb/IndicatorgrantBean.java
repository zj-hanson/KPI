/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.Indicatorgrant;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class IndicatorgrantBean extends SuperEJBForKPI<Indicatorgrant> {

    public IndicatorgrantBean() {
        super(Indicatorgrant.class);
    }

    public Indicatorgrant findByUserid(String userid) {
        Query query = getEntityManager().createNamedQuery("Indicatorgrant.findByUserid");
        query.setParameter("userid", userid.trim());
        try {
            Object o = query.getSingleResult();
            return (Indicatorgrant) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public Indicatorgrant findByUseridAndFormid(String userid, String formid) {
        Query query = getEntityManager().createNamedQuery("Indicatorgrant.findByUseridAndFormid");
        query.setParameter("userid", userid.trim());
        query.setParameter("formid", formid.trim());
        try {
            Object o = query.getSingleResult();
            return (Indicatorgrant) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public Indicatorgrant findByUseridAndFormidNotId(String userid, String formid, int id) {
        Query query = getEntityManager().createNamedQuery("Indicatorgrant.findByUseridAndFormidNotId");
        query.setParameter("userid", userid.trim());
        query.setParameter("formid", formid.trim());
        query.setParameter("id", id);
        try {
            Object o = query.getSingleResult();
            return (Indicatorgrant) o;
        } catch (Exception ex) {
            return null;
        }
    }
}
