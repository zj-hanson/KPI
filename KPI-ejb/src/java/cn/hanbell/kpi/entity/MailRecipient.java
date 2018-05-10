/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.FormDetailEntity;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C0160
 */
@Entity
@Table(name = "mailrecipient")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MailRecipient.findAll", query = "SELECT m FROM MailRecipient m"),
    @NamedQuery(name = "MailRecipient.findById", query = "SELECT m FROM MailRecipient m WHERE m.id = :id"),
    @NamedQuery(name = "MailRecipient.findByPId", query = "SELECT m FROM MailRecipient m WHERE m.pid = :pid ORDER BY m.seq"),
    @NamedQuery(name = "MailRecipient.findByPIdAndKind", query = "SELECT m FROM MailRecipient m WHERE  m.pid = :pid AND m.kind = :kind ORDER BY m.seq"),
    @NamedQuery(name = "MailRecipient.findByMailadd", query = "SELECT m FROM MailRecipient m WHERE m.mailadd = :mailadd")})
public class MailRecipient extends FormDetailEntity {

    @Size(max = 2)
    @Column(name = "kind")
    private String kind;
    @Size(max = 20)
    @Column(name = "userid")
    private String userid;
    @Size(max = 45)
    @Column(name = "username")
    private String username;
    @Size(max = 45)
    @Column(name = "mailadd")
    private String mailadd;

    public MailRecipient() {
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMailadd() {
        return mailadd;
    }

    public void setMailadd(String mailadd) {
        this.mailadd = mailadd;
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
        if (!(object instanceof MailRecipient)) {
            return false;
        }
        MailRecipient other = (MailRecipient) object;
        if (this.id != null && other.id != null) {
            return Objects.equals(this.id, other.id);
        }
        return Objects.equals(this.userid, other.userid);
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.MailRecipient[ id=" + id + " ]";
    }

}
