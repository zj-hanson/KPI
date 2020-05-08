/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity.erp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "bsc_groupshipment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BscGroupShipment.findAll", query = "SELECT b FROM BscGroupShipment b"),
    @NamedQuery(name = "BscGroupShipment.findByFacno", query = "SELECT b FROM BscGroupShipment b WHERE b.bscGroupShipmentPK.facno = :facno"),
    @NamedQuery(name = "BscGroupShipment.findBySoday", query = "SELECT b FROM BscGroupShipment b WHERE b.bscGroupShipmentPK.soday = :soday"),
    @NamedQuery(name = "BscGroupShipment.findByType", query = "SELECT b FROM BscGroupShipment b WHERE b.bscGroupShipmentPK.type = :type"),
    @NamedQuery(name = "BscGroupShipment.findByProtype", query = "SELECT b FROM BscGroupShipment b WHERE b.bscGroupShipmentPK.protype = :protype"),
    @NamedQuery(name = "BscGroupShipment.findByProtypeno", query = "SELECT b FROM BscGroupShipment b WHERE b.bscGroupShipmentPK.protypeno = :protypeno"),
    @NamedQuery(name = "BscGroupShipment.findByShptype", query = "SELECT b FROM BscGroupShipment b WHERE b.bscGroupShipmentPK.shptype = :shptype"),
    @NamedQuery(name = "BscGroupShipment.findByCusno", query = "SELECT b FROM BscGroupShipment b WHERE b.cusno = :cusno"),
    @NamedQuery(name = "BscGroupShipment.findByQuantity", query = "SELECT b FROM BscGroupShipment b WHERE b.quantity = :quantity"),
    @NamedQuery(name = "BscGroupShipment.findByAmount", query = "SELECT b FROM BscGroupShipment b WHERE b.amount = :amount")})
public class BscGroupShipment implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BscGroupShipmentPK bscGroupShipmentPK;
    @Size(max = 20)
    @Column(name = "cusno")
    private String cusno;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "amount")
    private BigDecimal amount;

    public BscGroupShipment() {
    }

    public BscGroupShipment(BscGroupShipmentPK bscGroupShipmentPK) {
        this.bscGroupShipmentPK = bscGroupShipmentPK;
    }

    public BscGroupShipment(String facno, Date soday, String type, String protype, String protypeno, String shptype) {
        this.bscGroupShipmentPK = new BscGroupShipmentPK(facno, soday, type, protype, protypeno, shptype);
    }

    public BscGroupShipmentPK getBscGroupShipmentPK() {
        return bscGroupShipmentPK;
    }

    public void setBscGroupShipmentPK(BscGroupShipmentPK bscGroupShipmentPK) {
        this.bscGroupShipmentPK = bscGroupShipmentPK;
    }

    public String getCusno() {
        return cusno;
    }

    public void setCusno(String cusno) {
        this.cusno = cusno;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bscGroupShipmentPK != null ? bscGroupShipmentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BscGroupShipment)) {
            return false;
        }
        BscGroupShipment other = (BscGroupShipment) object;
        if ((this.bscGroupShipmentPK == null && other.bscGroupShipmentPK != null) || (this.bscGroupShipmentPK != null && !this.bscGroupShipmentPK.equals(other.bscGroupShipmentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.erp.BscGroupShipment[ bscGroupShipmentPK=" + bscGroupShipmentPK + " ]";
    }

}
