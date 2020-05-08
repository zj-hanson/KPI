/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author C1749
 */
@Entity
public class MaterialQCUpload implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String SUPPLYID;
    private String SUPPLYNAME;
    private String ITDSCFIELDS;
    private String ITNBRFIELDS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSUPPLYID() {
        return SUPPLYID;
    }

    public void setSUPPLYID(String SUPPLYID) {
        this.SUPPLYID = SUPPLYID;
    }

    public String getSUPPLYNAME() {
        return SUPPLYNAME;
    }

    public void setSUPPLYNAME(String SUPPLYNAME) {
        this.SUPPLYNAME = SUPPLYNAME;
    }

    public String getITDSCFIELDS() {
        return ITDSCFIELDS;
    }

    public void setITDSCFIELDS(String ITDSCFIELDS) {
        this.ITDSCFIELDS = ITDSCFIELDS;
    }

    public String getITNBRFIELDS() {
        return ITNBRFIELDS;
    }

    public void setITNBRFIELDS(String ITNBRFIELDS) {
        this.ITNBRFIELDS = ITNBRFIELDS;
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
        if (!(object instanceof MaterialQCUpload)) {
            return false;
        }
        MaterialQCUpload other = (MaterialQCUpload) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.MaterialQCUpload[ id=" + id + " ]";
    }

}
