/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.AccountsReceivables;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class AccountsReceivablesBean extends SuperEJBForKPI<AccountsReceivables> {

    public AccountsReceivablesBean() {
        super(AccountsReceivables.class);
    }

    public List<AccountsReceivables> findByCompanyAndSeqAndMon(AccountsReceivables r) {
        Query query = getEntityManager().createNamedQuery("AccountsReceivables.findByCompanyAndSeqAndMon");
        query.setParameter("company", r.getCompany());
        query.setParameter("seq", r.getSeq());
        query.setParameter("mon", r.getMon());
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<AccountsReceivables> findByCompanyAndSeqAndMon(String company, int y, int m) {
        Query query = getEntityManager().createNamedQuery("AccountsReceivables.findByCompanyAndSeqAndMon");
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
