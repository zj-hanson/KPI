/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.BaseEntity;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@Entity
@Table(name = "balancerecordassisted")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BalanceRecordAssisted.findAll", query = "SELECT b FROM BalanceRecordAssisted b"),
    @NamedQuery(name = "BalanceRecordAssisted.findById", query = "SELECT b FROM BalanceRecordAssisted b WHERE b.id = :id"),
    @NamedQuery(name = "BalanceRecordAssisted.findByFacno", query = "SELECT b FROM BalanceRecordAssisted b WHERE b.facno = :facno"),
    @NamedQuery(name = "BalanceRecordAssisted.findByType", query = "SELECT b FROM BalanceRecordAssisted b WHERE b.type = :type"),
    @NamedQuery(name = "BalanceRecordAssisted.findByItemname", query = "SELECT b FROM BalanceRecordAssisted b WHERE b.itemname = :itemname"),
    @NamedQuery(name = "BalanceRecordAssisted.findByWhethershow", query = "SELECT b FROM BalanceRecordAssisted b WHERE b.whethershow = :whethershow"),
    @NamedQuery(name = "BalanceRecordAssisted.findByLeftshow", query = "SELECT b FROM BalanceRecordAssisted b WHERE b.leftshow = :leftshow"),
    @NamedQuery(name = "BalanceRecordAssisted.findByHighlight", query = "SELECT b FROM BalanceRecordAssisted b WHERE b.highlight = :highlight"),
    @NamedQuery(name = "BalanceRecordAssisted.findByRemark1", query = "SELECT b FROM BalanceRecordAssisted b WHERE b.remark1 = :remark1"),
    @NamedQuery(name = "BalanceRecordAssisted.findByRemark2", query = "SELECT b FROM BalanceRecordAssisted b WHERE b.remark2 = :remark2")})
public class BalanceRecordAssisted extends BaseEntity {

    private static final long serialVersionUID = 1L;
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
    @Column(name = "whethershow")
    private boolean whethershow;
    @Basic(optional = false)
    @NotNull
    @Column(name = "leftshow")
    private boolean leftshow;
    @Basic(optional = false)
    @NotNull
    @Column(name = "highlight")
    private boolean highlight;
    @Size(max = 45)
    @Column(name = "remark1")
    private String remark1;
    @Size(max = 45)
    @Column(name = "remark2")
    private String remark2;

    public BalanceRecordAssisted() {
    }

    public BalanceRecordAssisted(Integer id) {
        this.id = id;
    }

    public BalanceRecordAssisted(Integer id, String facno, String type, String itemname, boolean whethershow, boolean leftshow, boolean highlight) {
        this.id = id;
        this.facno = facno;
        this.type = type;
        this.itemname = itemname;
        this.whethershow = whethershow;
        this.leftshow = leftshow;
        this.highlight = highlight;
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

    public boolean getWhethershow() {
        return whethershow;
    }

    public void setWhethershow(boolean whethershow) {
        this.whethershow = whethershow;
    }

    public boolean getLeftshow() {
        return leftshow;
    }

    public void setLeftshow(boolean leftshow) {
        this.leftshow = leftshow;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BalanceRecordAssisted)) {
            return false;
        }
        BalanceRecordAssisted other = (BalanceRecordAssisted) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.BalanceRecordAssisted[ id=" + id + " ]";
    }
    
}
