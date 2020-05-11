/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.BaseEntity;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author C1879
 */
@Entity
@Table(name = "datarecordassisted")
@NamedQueries({
    @NamedQuery(name = "DataRecordAssisted.findAll", query = "SELECT d FROM DataRecordAssisted d"),
    @NamedQuery(name = "DataRecordAssisted.findByFacnoAndTypeAndItemname", query = "SELECT d FROM DataRecordAssisted d WHERE d.facno = :facno AND d.type = :type AND d.itemname = :itemname ")
})
public class DataRecordAssisted extends BaseEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "facno")
    private String facno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "itemname")
    private String itemname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "adjitemname")
    private String adjitemname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "whethershow")
    private boolean whethershow;
    @Basic(optional = false)
    @NotNull()
    @Column(name = "highlight")
    private boolean highlight;
    @Size(max = 45)
    @Column(name = "remark1")
    private String remark1;
    @Size(max = 45)
    @Column(name = "remark2")
    private String remark2;
    @Column(name = "showno")
    private Integer showno;

    public DataRecordAssisted() {
    }

    public DataRecordAssisted(Integer id) {
        this.id = id;
    }

    public DataRecordAssisted(Integer id, String facno, String type, String adjitemname, String itemname, boolean whethershow, boolean highlight) {
        this.id = id;
        this.facno = facno;
        this.type = type;
        this.adjitemname = adjitemname;
        this.itemname = itemname;
        this.whethershow = whethershow;
        this.highlight = highlight;
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
        if (!(object instanceof DataRecordAssisted)) {
            return false;
        }
        DataRecordAssisted other = (DataRecordAssisted) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.DataRecordAssisted[ id=" + id + " ]";
    }

    public Integer getShowno() {
        return showno;
    }

    public void setShowno(Integer showno) {
        this.showno = showno;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getAdjitemname() {
        return adjitemname;
    }

    public void setAdjitemname(String adjitemname) {
        this.adjitemname = adjitemname;
    }

    public boolean getWhethershow() {
        return whethershow;
    }

    public void setWhethershow(boolean whethershow) {
        this.whethershow = whethershow;
    }

    public boolean getHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

}
