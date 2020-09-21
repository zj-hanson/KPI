/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.AccountsReceivables;
import cn.hanbell.kpi.entity.Invtrtrday;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author Administrator
 */
@Stateless
@LocalBean
public class InvtrtrdayBean extends SuperEJBForKPI<Invtrtrday> {

    public InvtrtrdayBean() {
        super(Invtrtrday.class);
    }

    public List<Invtrtrday> findByCompanyAndSeqAndMon(Invtrtrday i) {
        Query query = getEntityManager().createNamedQuery("Invtrtrday.findByCompanyAndSeqAndMon");
        query.setParameter("company", i.getCompany());
        query.setParameter("seq", i.getSeq());
        query.setParameter("mon", i.getMon());
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Invtrtrday> findByCompanyAndSeqAndMon(String company, int y, int m) {
        Query query = getEntityManager().createNamedQuery("Invtrtrday.findByCompanyAndSeqAndMon");
        query.setParameter("company", company);
        query.setParameter("seq", y);
        query.setParameter("mon", m);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

}
