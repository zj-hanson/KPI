/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@Entity
@Table(name = "shoppingmenuweight")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ShoppingMenuWeight.findAll", query = "SELECT s FROM ShoppingMenuWeight s"),
    @NamedQuery(name = "ShoppingMenuWeight.findById", query = "SELECT s FROM ShoppingMenuWeight s WHERE s.id = :id"),
    @NamedQuery(name = "ShoppingMenuWeight.findByFacno", query = "SELECT s FROM ShoppingMenuWeight s WHERE s.facno = :facno"),
    @NamedQuery(name = "ShoppingMenuWeight.findByItnbr", query = "SELECT s FROM ShoppingMenuWeight s WHERE s.itnbr = :itnbr"),
    @NamedQuery(name = "ShoppingMenuWeight.findByWeight", query = "SELECT s FROM ShoppingMenuWeight s WHERE s.weight = :weight"),
    @NamedQuery(name = "ShoppingMenuWeight.findByStatus", query = "SELECT s FROM ShoppingMenuWeight s WHERE s.status = :status"),
    @NamedQuery(name = "ShoppingMenuWeight.findByCreator", query = "SELECT s FROM ShoppingMenuWeight s WHERE s.creator = :creator"),
    @NamedQuery(name = "ShoppingMenuWeight.findByCredate", query = "SELECT s FROM ShoppingMenuWeight s WHERE s.credate = :credate"),
    @NamedQuery(name = "ShoppingMenuWeight.findByOptuser", query = "SELECT s FROM ShoppingMenuWeight s WHERE s.optuser = :optuser"),
    @NamedQuery(name = "ShoppingMenuWeight.findByOptdate", query = "SELECT s FROM ShoppingMenuWeight s WHERE s.optdate = :optdate"),
    @NamedQuery(name = "ShoppingMenuWeight.findByCfmuser", query = "SELECT s FROM ShoppingMenuWeight s WHERE s.cfmuser = :cfmuser"),
    @NamedQuery(name = "ShoppingMenuWeight.findByCfmdate", query = "SELECT s FROM ShoppingMenuWeight s WHERE s.cfmdate = :cfmdate"),
    @NamedQuery(name = "ShoppingMenuWeight.findByItnbrAndFacno", query = "SELECT s FROM ShoppingMenuWeight s WHERE s.itnbr = :itnbr and s.facno = :facno")})
public class ShoppingMenuWeight extends SuperEntity {

    private static final long serialVersionUID = 1L;
    @Size(max = 20)
    @Column(name = "facno")
    private String facno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "itnbr")
    private String itnbr;
    @Column(name = "itdsc")
    private String itdsc;
    @NotNull
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "isIn")
    private Boolean isIn;
    public ShoppingMenuWeight() {
    }

    public ShoppingMenuWeight(Integer id) {
        this.id = id;
    }

    public ShoppingMenuWeight(Integer id, String itnbr, BigDecimal weight) {
        this.id = id;
        this.itnbr = itnbr;
        this.weight = weight;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

    public String getItnbr() {
        return itnbr;
    }

    public void setItnbr(String itnbr) {
        this.itnbr = itnbr;
    }

    public String getItdsc() {
        return itdsc;
    }

    public void setItdsc(String itdsc) {
        this.itdsc = itdsc;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCredate() {
        return credate;
    }

    public void setCredate(Date credate) {
        this.credate = credate;
    }

    public String getOptuser() {
        return optuser;
    }

    public void setOptuser(String optuser) {
        this.optuser = optuser;
    }

    public Date getOptdate() {
        return optdate;
    }

    public void setOptdate(Date optdate) {
        this.optdate = optdate;
    }

    public String getCfmuser() {
        return cfmuser;
    }

    public void setCfmuser(String cfmuser) {
        this.cfmuser = cfmuser;
    }

    public Date getCfmdate() {
        return cfmdate;
    }

    public void setCfmdate(Date cfmdate) {
        this.cfmdate = cfmdate;
    }

    public Boolean getIsIn() {
        return isIn;
    }

    public void setIsIn(Boolean isIn) {
        this.isIn = isIn;
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
        if (!(object instanceof ShoppingMenuWeight)) {
            return false;
        }
        ShoppingMenuWeight other = (ShoppingMenuWeight) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.ShoppingMenuWeight[ id=" + id + " ]";
    }

}
