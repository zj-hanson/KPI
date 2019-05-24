/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tw.hanbell.kpi.entity.erp;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author C1749
 */
@Embeddable
public class GrpsdailytmpPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "hangrp")
    private String hangrp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "trdate")
    private String trdate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "series")
    private String series;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "intertra_grp")
    private String intertraGrp;

    public GrpsdailytmpPK() {
    }

    public GrpsdailytmpPK(String hangrp, String trdate, String series, String type, String intertraGrp) {
        this.hangrp = hangrp;
        this.trdate = trdate;
        this.series = series;
        this.type = type;
        this.intertraGrp = intertraGrp;
    }

    public String getHangrp() {
        return hangrp;
    }

    public void setHangrp(String hangrp) {
        this.hangrp = hangrp;
    }

    public String getTrdate() {
        return trdate;
    }

    public void setTrdate(String trdate) {
        this.trdate = trdate;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntertraGrp() {
        return intertraGrp;
    }

    public void setIntertraGrp(String intertraGrp) {
        this.intertraGrp = intertraGrp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hangrp != null ? hangrp.hashCode() : 0);
        hash += (trdate != null ? trdate.hashCode() : 0);
        hash += (series != null ? series.hashCode() : 0);
        hash += (type != null ? type.hashCode() : 0);
        hash += (intertraGrp != null ? intertraGrp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GrpsdailytmpPK)) {
            return false;
        }
        GrpsdailytmpPK other = (GrpsdailytmpPK) object;
        if ((this.hangrp == null && other.hangrp != null) || (this.hangrp != null && !this.hangrp.equals(other.hangrp))) {
            return false;
        }
        if ((this.trdate == null && other.trdate != null) || (this.trdate != null && !this.trdate.equals(other.trdate))) {
            return false;
        }
        if ((this.series == null && other.series != null) || (this.series != null && !this.series.equals(other.series))) {
            return false;
        }
        if ((this.type == null && other.type != null) || (this.type != null && !this.type.equals(other.type))) {
            return false;
        }
        if ((this.intertraGrp == null && other.intertraGrp != null) || (this.intertraGrp != null && !this.intertraGrp.equals(other.intertraGrp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.erp.GrpsdailytmpPK[ hangrp=" + hangrp + ", trdate=" + trdate + ", series=" + series + ", type=" + type + ", intertraGrp=" + intertraGrp + " ]";
    }
    
}
