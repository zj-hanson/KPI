/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.MailRecipient;
import cn.hanbell.kpi.entity.MailSetting;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class MailSettingBean extends SuperEJBForKPI<MailSetting> {

    @EJB
    private MailRecipientBean mailRecipientBean;

    public MailSettingBean() {
        super(MailSetting.class);
    }

    public MailSetting findByMailClazz(String mailClazz) {
        Query query = getEntityManager().createNamedQuery("MailSetting.findByMailClazz");
        query.setParameter("mailClazz", mailClazz);
        try {
            Object o = query.getSingleResult();
            return (MailSetting) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<String> findRecipientTo(Object pid) {
        List<String> to = new ArrayList<>();
        List<MailRecipient> detail = mailRecipientBean.findByPIdAndKind(pid, "to");
        if (detail != null && !detail.isEmpty()) {
            detail.forEach((r) -> {
                to.add(r.getMailadd());
            });
        }
        return to;
    }

    public List<String> findRecipientCc(Object pid) {
        List<String> cc = new ArrayList<>();
        List<MailRecipient> detail = mailRecipientBean.findByPIdAndKind(pid, "cc");
        if (detail != null && !detail.isEmpty()) {
            detail.forEach((r) -> {
                cc.add(r.getMailadd());
            });
        }
        return cc;
    }

    public List<String> findRecipientBcc(Object pid) {
        List<String> bcc = new ArrayList<>();
        List<MailRecipient> detail = mailRecipientBean.findByPIdAndKind(pid, "bcc");
        if (detail != null && !detail.isEmpty()) {
            detail.forEach((r) -> {
                bcc.add(r.getMailadd());
            });
        }
        return bcc;
    }

}
