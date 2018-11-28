/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.erp.entity;

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
 * @author C1749
 */
@Entity
@Table(name = "bsc_groupshipment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BscGroupShipment.findAll", query = "SELECT b FROM BscGroupShipment b")
    , @NamedQuery(name = "BscGroupShipment.findByFacno", query = "SELECT b FROM BscGroupShipment b WHERE b.bscGroupShipmentPK.facno = :facno")
    , @NamedQuery(name = "BscGroupShipment.findBySoday", query = "SELECT b FROM BscGroupShipment b WHERE b.bscGroupShipmentPK.soday = :soday")
    , @NamedQuery(name = "BscGroupShipment.findByProtype", query = "SELECT b FROM BscGroupShipment b WHERE b.bscGroupShipmentPK.protype = :protype")
    , @NamedQuery(name = "BscGroupShipment.findByProtypeno", query = "SELECT b FROM BscGroupShipment b WHERE b.bscGroupShipmentPK.protypeno = :protypeno")
    , @NamedQuery(name = "BscGroupShipment.findByShptype", query = "SELECT b FROM BscGroupShipment b WHERE b.bscGroupShipmentPK.shptype = :shptype")
    , @NamedQuery(name = "BscGroupShipment.findByCusno", query = "SELECT b FROM BscGroupShipment b WHERE b.cusno = :cusno")
    , @NamedQuery(name = "BscGroupShipment.findByShpnum", query = "SELECT b FROM BscGroupShipment b WHERE b.shpnum = :shpnum")
    , @NamedQuery(name = "BscGroupShipment.findByShpamts", query = "SELECT b FROM BscGroupShipment b WHERE b.shpamts = :shpamts")
    , @NamedQuery(name = "BscGroupShipment.findByOrdnum", query = "SELECT b FROM BscGroupShipment b WHERE b.ordnum = :ordnum")
    , @NamedQuery(name = "BscGroupShipment.findByOrdamts", query = "SELECT b FROM BscGroupShipment b WHERE b.ordamts = :ordamts")})
public class BscGroupShipment implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BscGroupShipmentPK bscGroupShipmentPK;
    @Size(max = 20)
    @Column(name = "cusno")
    private String cusno;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "shpnum")
    private BigDecimal shpnum;
    @Column(name = "shpamts")
    private BigDecimal shpamts;
    @Column(name = "ordnum")
    private BigDecimal ordnum;
    @Column(name = "ordamts")
    private BigDecimal ordamts;

    public BscGroupShipment() {
        shpnum = BigDecimal.ZERO;
        shpamts = BigDecimal.ZERO;
        ordnum = BigDecimal.ZERO;
        ordamts = BigDecimal.ZERO;
    }

    public BscGroupShipment(String facno, Date soday, String protype, String protypeno, String shptype) {
        this.bscGroupShipmentPK = new BscGroupShipmentPK(facno, soday, protype, protypeno, shptype);
        shpnum = BigDecimal.ZERO;
        shpamts = BigDecimal.ZERO;
        ordnum = BigDecimal.ZERO;
        ordamts = BigDecimal.ZERO;
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

    public BigDecimal getShpnum() {
        return shpnum;
    }

    public void setShpnum(BigDecimal shpnum) {
        this.shpnum = shpnum;
    }

    public BigDecimal getShpamts() {
        return shpamts;
    }

    public void setShpamts(BigDecimal shpamts) {
        this.shpamts = shpamts;
    }

    public BigDecimal getOrdnum() {
        return ordnum;
    }

    public void setOrdnum(BigDecimal ordnum) {
        this.ordnum = ordnum;
    }

    public BigDecimal getOrdamts() {
        return ordamts;
    }

    public void setOrdamts(BigDecimal ordamts) {
        this.ordamts = ordamts;
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
        if (this.bscGroupShipmentPK.equals(other.bscGroupShipmentPK)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "cn.hanbell.erp.entity.BscGroupShipment[ bscGroupShipmentPK=" + bscGroupShipmentPK + " ]";
    }

}
