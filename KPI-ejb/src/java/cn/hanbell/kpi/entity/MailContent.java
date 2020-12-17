/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.FormDetailEntity;
import java.util.Objects;
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
@Table(name = "mailcontent")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MailContent.findAll", query = "SELECT m FROM MailContent m"),
    @NamedQuery(name = "MailContent.findById", query = "SELECT m FROM MailContent m WHERE m.id = :id"),
    @NamedQuery(name = "MailContent.findByPId", query = "SELECT m FROM MailContent m WHERE m.pid = :pid"),
    @NamedQuery(name = "MailContent.findByIndicator", query = "SELECT m FROM MailContent m WHERE m.indicator = :indicator"),
    @NamedQuery(name = "MailContent.findBySortid", query = "SELECT m FROM MailContent m WHERE m.sortid = :sortid")})
public class MailContent extends FormDetailEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "indicator")
    private String indicator;
    @Column(name = "sortid")
    private Integer sortid;

    public MailContent() {
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public Integer getSortid() {
        return sortid;
    }

    public void setSortid(Integer sortid) {
        this.sortid = sortid;
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
        if (!(object instanceof MailContent)) {
            return false;
        }
        MailContent other = (MailContent) object;
        if (this.id != null && other.id != null) {
            return Objects.equals(this.id, other.id);
        }
        if (this.id == null && other.id == null) {
            return Objects.equals(this.indicator, other.indicator);
        }
        return this.seq == other.seq;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.MailContent[ id=" + id + " ]";
    }

}
