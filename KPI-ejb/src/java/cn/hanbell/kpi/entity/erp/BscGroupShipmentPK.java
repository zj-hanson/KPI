/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity.erp;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Embeddable
public class BscGroupShipmentPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "facno")
    private String facno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "soday")
    @Temporal(TemporalType.TIMESTAMP)
    private Date soday;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "protype")
    private String protype;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "protypeno")
    private String protypeno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "shptype")
    private String shptype;

    public BscGroupShipmentPK() {
    }

    public BscGroupShipmentPK(String facno, Date soday, String type, String protype, String protypeno, String shptype) {
        this.facno = facno;
        this.soday = soday;
        this.type = type;
        this.protype = protype;
        this.protypeno = protypeno;
        this.shptype = shptype;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

    public Date getSoday() {
        return soday;
    }

    public void setSoday(Date soday) {
        this.soday = soday;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProtype() {
        return protype;
    }

    public void setProtype(String protype) {
        this.protype = protype;
    }

    public String getProtypeno() {
        return protypeno;
    }

    public void setProtypeno(String protypeno) {
        this.protypeno = protypeno;
    }

    public String getShptype() {
        return shptype;
    }

    public void setShptype(String shptype) {
        this.shptype = shptype;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facno != null ? facno.hashCode() : 0);
        hash += (soday != null ? soday.hashCode() : 0);
        hash += (type != null ? type.hashCode() : 0);
        hash += (protype != null ? protype.hashCode() : 0);
        hash += (protypeno != null ? protypeno.hashCode() : 0);
        hash += (shptype != null ? shptype.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BscGroupShipmentPK)) {
            return false;
        }
        BscGroupShipmentPK other = (BscGroupShipmentPK) object;
        if ((this.facno == null && other.facno != null) || (this.facno != null && !this.facno.equals(other.facno))) {
            return false;
        }
        if ((this.soday == null && other.soday != null) || (this.soday != null && !this.soday.equals(other.soday))) {
            return false;
        }
        if ((this.type == null && other.type != null) || (this.type != null && !this.type.equals(other.type))) {
            return false;
        }
        if ((this.protype == null && other.protype != null) || (this.protype != null && !this.protype.equals(other.protype))) {
            return false;
        }
        if ((this.protypeno == null && other.protypeno != null) || (this.protypeno != null && !this.protypeno.equals(other.protypeno))) {
            return false;
        }
        if ((this.shptype == null && other.shptype != null) || (this.shptype != null && !this.shptype.equals(other.shptype))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.erp.BscGroupShipmentPK[ facno=" + facno + ", soday=" + soday + ", type=" + type + ", protype=" + protype + ", protypeno=" + protypeno + ", shptype=" + shptype + " ]";
    }
    
}
