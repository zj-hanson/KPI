/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.MailRecipient;
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
public class MailRecipientBean extends SuperEJBForKPI<MailRecipient> {

    public MailRecipientBean() {
        super(MailRecipient.class);
    }

    public List<MailRecipient> findByPIdAndKind(Object pid, String kind) {
        Query query = getEntityManager().createNamedQuery("MailRecipient.findByPIdAndKind");
        query.setParameter("pid", pid);
        query.setParameter("kind", kind);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

}
