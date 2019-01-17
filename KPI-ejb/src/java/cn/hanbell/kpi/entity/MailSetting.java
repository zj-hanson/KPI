/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.FormEntity;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C0160
 */
@Entity
@Table(name = "mailsetting")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MailSetting.findAll", query = "SELECT m FROM MailSetting m")
    ,
    @NamedQuery(name = "MailSetting.findById", query = "SELECT m FROM MailSetting m WHERE m.id = :id")
    ,
    @NamedQuery(name = "MailSetting.findByCompany", query = "SELECT m FROM MailSetting m WHERE m.company = :company")
    ,
    @NamedQuery(name = "MailSetting.findByFormid", query = "SELECT m FROM MailSetting m WHERE m.formid = :formid")
    ,
    @NamedQuery(name = "MailSetting.findByFormtype", query = "SELECT m FROM MailSetting m WHERE m.formtype = :formtype")
    ,
    @NamedQuery(name = "MailSetting.findByFormkind", query = "SELECT m FROM MailSetting m WHERE m.formkind = :formkind")
    ,
    @NamedQuery(name = "MailSetting.findByName", query = "SELECT m FROM MailSetting m WHERE m.name = :name")
    ,
    @NamedQuery(name = "MailSetting.findByMailClazz", query = "SELECT m FROM MailSetting m WHERE m.mailClazz = :mailClazz")
    ,
    @NamedQuery(name = "MailSetting.findByStatus", query = "SELECT m FROM MailSetting m WHERE m.status = :status")
    ,
    @NamedQuery(name = "MailSetting.findByJobScheduleAndStatus", query = "SELECT m FROM MailSetting m WHERE m.jobSchedule = :jobschedule AND m.status = :status")
})
public class MailSetting extends FormEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "company")
    private String company;
    @Size(max = 45)
    @Column(name = "formtype")
    private String formtype;
    @Size(max = 10)
    @Column(name = "formkind")
    private String formkind;
    @Size(max = 200)
    @Column(name = "name")
    private String name;
    @Size(max = 400)
    @Column(name = "descript")
    private String descript;
    @Size(max = 100)
    @Column(name = "mailClazz")
    private String mailClazz;
    @Size(max = 200)
    @Column(name = "mailEJB")
    private String mailEJB;
    @Size(max = 20)
    @Column(name = "jobSchedule")
    private String jobSchedule;
    @Size(max = 200)
    @Column(name = "remark")
    private String remark;

    public MailSetting() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFormtype() {
        return formtype;
    }

    public void setFormtype(String formtype) {
        this.formtype = formtype;
    }

    public String getFormkind() {
        return formkind;
    }

    public void setFormkind(String formkind) {
        this.formkind = formkind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getMailClazz() {
        return mailClazz;
    }

    public void setMailClazz(String mailClazz) {
        this.mailClazz = mailClazz;
    }

    /**
     * @return the mailEJB
     */
    public String getMailEJB() {
        return mailEJB;
    }

    /**
     * @param mailEJB the mailEJB to set
     */
    public void setMailEJB(String mailEJB) {
        this.mailEJB = mailEJB;
    }

    /**
     * @return the jobSchedule
     */
    public String getJobSchedule() {
        return jobSchedule;
    }

    /**
     * @param jobSchedule the jobSchedule to set
     */
    public void setJobSchedule(String jobSchedule) {
        this.jobSchedule = jobSchedule;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MailSetting)) {
            return false;
        }
        MailSetting other = (MailSetting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.MailSetting[ id=" + id + " ]";
    }

}
