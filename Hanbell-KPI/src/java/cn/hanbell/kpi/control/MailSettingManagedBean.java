/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.comm.MailNotify;
import cn.hanbell.kpi.ejb.MailRecipientBean;
import cn.hanbell.kpi.ejb.MailSettingBean;
import cn.hanbell.kpi.entity.MailRecipient;
import cn.hanbell.kpi.entity.MailSetting;
import cn.hanbell.kpi.lazy.MailSettingModel;
import cn.hanbell.kpi.web.FormMultiBean;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.naming.InitialContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "mailSettingManagedBean")
@SessionScoped
public class MailSettingManagedBean extends FormMultiBean<MailSetting, MailRecipient> {

    @EJB
    private MailSettingBean mailSettingBean;

    @EJB
    private MailRecipientBean mailRecipientBean;

    protected MailNotification mailBean;

    private String recipient;

    public MailSettingManagedBean() {
        super(MailSetting.class, MailRecipient.class);
    }

    @Override
    public void create() {
        super.create();
        newEntity.setCompany(userManagedBean.getCompany());
        newEntity.setFormdate(getDate());
        setCurrentEntity(newEntity);
    }

    @Override
    public void createDetail() {
        super.createDetail();
        newDetail.setKind("to");
    }

    @Override
    public void handleDialogReturnWhenDetailEdit(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            currentDetail.setUserid(user.getUserid());
            currentDetail.setUsername(user.getUsername());
            currentDetail.setMailadd(user.getEmail());
            if (currentDetail.getMailadd() == null) {
                currentDetail.setMailadd(user.getUserid() + "@hanbell.com.cn");
            }
        }
    }

    @Override
    public void init() {
        superEJB = mailSettingBean;
        detailEJB = mailRecipientBean;
        model = new MailSettingModel(mailSettingBean);
        super.init();
    }

    public void moveDown() {
        if (currentDetail != null) {
            int i, n, m;
            n = currentDetail.getSeq();
            i = detailList.indexOf(currentDetail);
            if (i < detailList.size() - 1) {
                MailRecipient mr = detailList.get(i + 1);
                m = mr.getSeq();
                mr.setSeq(-1);
                currentDetail.setSeq(m);
                this.doConfirmDetail();
                mr.setSeq(n);
                currentDetail = mr;
                this.doConfirmDetail();
                //重新排序
                detailList.sort((MailRecipient o1, MailRecipient o2) -> {
                    if (o1.getSeq() > o2.getSeq()) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
                showInfoMsg("Info", "MoveDown");
            } else {
                showWarnMsg("Warn", "已是最后");
            }
        }
    }

    public void moveUp() {
        if (currentDetail != null) {
            int i, n, m;
            n = currentDetail.getSeq();
            i = detailList.indexOf(currentDetail);
            if (i > 0) {
                MailRecipient mr = detailList.get(i - 1);
                m = mr.getSeq();
                mr.setSeq(-1);
                currentDetail.setSeq(m);
                this.doConfirmDetail();
                mr.setSeq(n);
                currentDetail = mr;
                this.doConfirmDetail();
                //重新排序
                detailList.sort((MailRecipient o1, MailRecipient o2) -> {
                    if (o1.getSeq() > o2.getSeq()) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
                showInfoMsg("Info", "MoveUp");
            } else {
                showWarnMsg("Warn", "已是最前");
            }
        }
    }

    public void sendMail() {
        if (queryDateBegin != null && currentEntity != null) {
            try {
                setMailBean(currentEntity.getMailEJB());
                if (mailBean != null) {
                    mailBean.init();
                    mailBean.setD(queryDateBegin);
                    mailBean.setMailContent();
                    mailBean.setMailSubject();
                    mailBean.notify(new MailNotify());
                    showInfoMsg("Info", "报表邮件发送成功");
                } else {
                    showErrorMsg("Error", "MailEJB设置错误");
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                showErrorMsg("Error", ex.toString());
            } catch (Exception ex) {
                showErrorMsg("Error", ex.toString());
            }
        }
    }

    public void setMailBean(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup(JNDIName);
        mailBean = (MailNotification) objRef;
    }

    public void testMail() {
        if (queryDateBegin != null && currentEntity != null) {
            try {
                setMailBean(currentEntity.getMailEJB());
                if (mailBean != null && recipient != null && !"".equals(recipient)) {
                    mailBean.init();
                    //清除设定的收件人,重设为测试人员
                    if (!mailBean.getTo().isEmpty()) {
                        mailBean.getTo().clear();
                    }
                    if (!mailBean.getCc().isEmpty()) {
                        mailBean.getCc().clear();
                    }
                    if (!mailBean.getBcc().isEmpty()) {
                        mailBean.getBcc().clear();
                    }
                    if (recipient.contains("@")) {
                        mailBean.getTo().add(recipient);
                    } else {
                        mailBean.getTo().add(recipient + "@hanbell.com.cn");
                    }
                    mailBean.setD(queryDateBegin);
                    mailBean.setMailContent();
                    mailBean.setMailSubject();
                    mailBean.notify(new MailNotify());
                    showInfoMsg("Info", "报表邮件发送成功");
                } else {
                    showErrorMsg("Error", "MailEJB或测试人员设置错误");
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                showErrorMsg("Error", ex.toString());
            } catch (Exception ex) {
                showErrorMsg("Error", ex.toString());
            }
        }
    }

    /**
     * @return the recipient
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * @param recipient the recipient to set
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

}
