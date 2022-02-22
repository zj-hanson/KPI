/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.entity.ppm;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
 * @author FredJie
 */
@Entity
@Table(name = "quality")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Quality.findAll", query = "SELECT q FROM Quality q"),
    @NamedQuery(name = "Quality.findById", query = "SELECT q FROM Quality q WHERE q.id = :id"),
    @NamedQuery(name = "Quality.findByUid", query = "SELECT q FROM Quality q WHERE q.uid = :uid"),
    @NamedQuery(name = "Quality.findByCustomer", query = "SELECT q FROM Quality q WHERE q.customer = :customer"),
    @NamedQuery(name = "Quality.findByStartDate", query = "SELECT q FROM Quality q WHERE q.startDate = :startDate"),
    @NamedQuery(name = "Quality.findByEndDate", query = "SELECT q FROM Quality q WHERE q.endDate = :endDate"),
    @NamedQuery(name = "Quality.findByPrincipalId", query = "SELECT q FROM Quality q WHERE q.principalId = :principalId"),
    @NamedQuery(name = "Quality.findByPrincipalName", query = "SELECT q FROM Quality q WHERE q.principalName = :principalName"),
    @NamedQuery(name = "Quality.findByImplementDate", query = "SELECT q FROM Quality q WHERE q.implementDate = :implementDate"),
    @NamedQuery(name = "Quality.findByStatus", query = "SELECT q FROM Quality q WHERE q.status = :status"),
    @NamedQuery(name = "Quality.findByCreator", query = "SELECT q FROM Quality q WHERE q.creator = :creator"),
    @NamedQuery(name = "Quality.findByCredate", query = "SELECT q FROM Quality q WHERE q.credate = :credate"),
    @NamedQuery(name = "Quality.findByOptuser", query = "SELECT q FROM Quality q WHERE q.optuser = :optuser"),
    @NamedQuery(name = "Quality.findByOptdate", query = "SELECT q FROM Quality q WHERE q.optdate = :optdate"),
    @NamedQuery(name = "Quality.findByCompany", query = "SELECT q FROM Quality q WHERE q.company = :company")})
public class Quality implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "uid")
    private String uid;
    @Size(max = 20)
    @Column(name = "customer")
    private String customer;
    @Lob
    @Size(max = 65535)
    @Column(name = "productName")
    private String productName;
    @Lob
    @Size(max = 65535)
    @Column(name = "characteristic")
    private String characteristic;
    @Column(name = "startDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Lob
    @Size(max = 65535)
    @Column(name = "controlItem")
    private String controlItem;
    @Size(max = 100)
    @Column(name = "principalId")
    private String principalId;
    @Size(max = 100)
    @Column(name = "principalName")
    private String principalName;
    @Column(name = "implementDate")
    @Temporal(TemporalType.DATE)
    private Date implementDate;
    @Size(max = 2)
    @Column(name = "status")
    private String status;
    @Size(max = 20)
    @Column(name = "creator")
    private String creator;
    @Column(name = "credate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date credate;
    @Size(max = 100)
    @Column(name = "optuser")
    private String optuser;
    @Column(name = "optdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date optdate;
    @Size(max = 2)
    @Column(name = "company")
    private String company;

    public Quality() {
    }

    public Quality(Integer id) {
        this.id = id;
    }

    public Quality(Integer id, String uid) {
        this.id = id;
        this.uid = uid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getControlItem() {
        return controlItem;
    }

    public void setControlItem(String controlItem) {
        this.controlItem = controlItem;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public Date getImplementDate() {
        return implementDate;
    }

    public void setImplementDate(Date implementDate) {
        this.implementDate = implementDate;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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
        if (!(object instanceof Quality)) {
            return false;
        }
        Quality other = (Quality) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanson.kpi.entity.ppm.Quality[ id=" + id + " ]";
    }
    
}
