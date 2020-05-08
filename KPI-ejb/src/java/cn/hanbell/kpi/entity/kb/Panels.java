/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity.kb;

import com.lightshell.comm.SuperEntity;
import java.math.BigDecimal;
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
@Table(name = "panels")
@NamedQueries({
    @NamedQuery(name = "Panels.findAll", query = "SELECT p FROM Panels p WHERE p.status='V' "),
    @NamedQuery(name = "Panels.findById", query = "SELECT p FROM Panels p WHERE p.id = :id ")
})
public class Panels extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "facno")
    private String facno;
    @Size(max = 45)
    @Column(name = "formid")
    private String formid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "pdname")
    private String pdname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "api")
    private String api;
    @Size(max = 20)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "deptno")
    private String deptno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "deptname")
    private String deptname;
    @Column(name = "sortid")
    private Integer sortid;
    @Column(name = "hasOther")
    private Integer hasOther;
    @Size(max = 40)
    @Column(name = "other1Label")
    private String other1Label;
    @Size(max = 40)
    @Column(name = "other2Label")
    private String other2Label;
    @Size(max = 40)
    @Column(name = "other3Label")
    private String other3Label;
    @Size(max = 40)
    @Column(name = "other4Label")
    private String other4Label;
    @Size(max = 40)
    @Column(name = "other5Label")
    private String other5Label;
    @Size(max = 40)
    @Column(name = "other6Label")
    private String other6Label;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "other1")
    private BigDecimal other1;
    @Column(name = "other2")
    private BigDecimal other2;
    @Column(name = "other3")
    private BigDecimal other3;
    @Column(name = "other4")
    private BigDecimal other4;
    @Column(name = "other5")
    private BigDecimal other5;
    @Column(name = "other6")
    private BigDecimal other6;
    @Size(max = 10)
    @Column(name = "unit")
    private String unit;
    @Column(name = "rate")
    private BigDecimal rate;
    @Size(max = 100)
    @Column(name = "other1Interface")
    private String other1Interface;
    @Size(max = 100)
    @Column(name = "other1EJB")
    private String other1EJB;
    @Size(max = 100)
    @Column(name = "other2Interface")
    private String other2Interface;
    @Size(max = 100)
    @Column(name = "other2EJB")
    private String other2EJB;
    @Size(max = 100)
    @Column(name = "other3Interface")
    private String other3Interface;
    @Size(max = 100)
    @Column(name = "other3EJB")
    private String other3EJB;
    @Size(max = 100)
    @Column(name = "other4Interface")
    private String other4Interface;
    @Size(max = 100)
    @Column(name = "other4EJB")
    private String other4EJB;
    @Size(max = 100)
    @Column(name = "other5Interface")
    private String other5Interface;
    @Size(max = 100)
    @Column(name = "other5EJB")
    private String other5EJB;
    @Size(max = 100)
    @Column(name = "other6Interface")
    private String other6Interface;
    @Size(max = 100)
    @Column(name = "other6EJB")
    private String other6EJB;
    @Size(max = 200)
    @Column(name = "remark")
    private String remark;

    public Panels() {
        this.other1 = BigDecimal.ZERO;
        this.other2 = BigDecimal.ZERO;
        this.other3 = BigDecimal.ZERO;
        this.other4 = BigDecimal.ZERO;
        this.other5 = BigDecimal.ZERO;
        this.other6 = BigDecimal.ZERO;
    }

    public Panels(Integer id) {
        this.id = id;
    }

    public Panels(Integer id, String facno, String pdname, String api, String deptno, String deptname, String status) {
        this.id = id;
        this.facno = facno;
        this.pdname = pdname;
        this.api = api;
        this.deptno = deptno;
        this.deptname = deptname;
        this.status = status;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    public String getPdname() {
        return pdname;
    }

    public void setPdname(String pdname) {
        this.pdname = pdname;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public Integer getSortid() {
        return sortid;
    }

    public void setSortid(Integer sortid) {
        this.sortid = sortid;
    }

    public Integer getHasOther() {
        return hasOther;
    }

    public void setHasOther(Integer hasOther) {
        this.hasOther = hasOther;
    }

    public String getOther1Label() {
        return other1Label;
    }

    public void setOther1Label(String other1Label) {
        this.other1Label = other1Label;
    }

    public String getOther2Label() {
        return other2Label;
    }

    public void setOther2Label(String other2Label) {
        this.other2Label = other2Label;
    }

    public String getOther3Label() {
        return other3Label;
    }

    public void setOther3Label(String other3Label) {
        this.other3Label = other3Label;
    }

    public String getOther4Label() {
        return other4Label;
    }

    public void setOther4Label(String other4Label) {
        this.other4Label = other4Label;
    }

    public String getOther5Label() {
        return other5Label;
    }

    public void setOther5Label(String other5Label) {
        this.other5Label = other5Label;
    }

    public String getOther6Label() {
        return other6Label;
    }

    public void setOther6Label(String other6Label) {
        this.other6Label = other6Label;
    }

    public BigDecimal getOther1() {
        return other1;
    }

    public void setOther1(BigDecimal other1) {
        this.other1 = other1;
    }

    public BigDecimal getOther2() {
        return other2;
    }

    public void setOther2(BigDecimal other2) {
        this.other2 = other2;
    }

    public BigDecimal getOther3() {
        return other3;
    }

    public void setOther3(BigDecimal other3) {
        this.other3 = other3;
    }

    public BigDecimal getOther4() {
        return other4;
    }

    public void setOther4(BigDecimal other4) {
        this.other4 = other4;
    }

    public BigDecimal getOther5() {
        return other5;
    }

    public void setOther5(BigDecimal other5) {
        this.other5 = other5;
    }

    public BigDecimal getOther6() {
        return other6;
    }

    public void setOther6(BigDecimal other6) {
        this.other6 = other6;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getOther1Interface() {
        return other1Interface;
    }

    public void setOther1Interface(String other1Interface) {
        this.other1Interface = other1Interface;
    }

    public String getOther1EJB() {
        return other1EJB;
    }

    public void setOther1EJB(String other1EJB) {
        this.other1EJB = other1EJB;
    }

    public String getOther2Interface() {
        return other2Interface;
    }

    public void setOther2Interface(String other2Interface) {
        this.other2Interface = other2Interface;
    }

    public String getOther2EJB() {
        return other2EJB;
    }

    public void setOther2EJB(String other2EJB) {
        this.other2EJB = other2EJB;
    }

    public String getOther3Interface() {
        return other3Interface;
    }

    public void setOther3Interface(String other3Interface) {
        this.other3Interface = other3Interface;
    }

    public String getOther3EJB() {
        return other3EJB;
    }

    public void setOther3EJB(String other3EJB) {
        this.other3EJB = other3EJB;
    }

    public String getOther4Interface() {
        return other4Interface;
    }

    public void setOther4Interface(String other4Interface) {
        this.other4Interface = other4Interface;
    }

    public String getOther4EJB() {
        return other4EJB;
    }

    public void setOther4EJB(String other4EJB) {
        this.other4EJB = other4EJB;
    }

    public String getOther5Interface() {
        return other5Interface;
    }

    public void setOther5Interface(String other5Interface) {
        this.other5Interface = other5Interface;
    }

    public String getOther5EJB() {
        return other5EJB;
    }

    public void setOther5EJB(String other5EJB) {
        this.other5EJB = other5EJB;
    }

    public String getOther6Interface() {
        return other6Interface;
    }

    public void setOther6Interface(String other6Interface) {
        this.other6Interface = other6Interface;
    }

    public String getOther6EJB() {
        return other6EJB;
    }

    public void setOther6EJB(String other6EJB) {
        this.other6EJB = other6EJB;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        if (!(object instanceof Panels)) {
            return false;
        }
        Panels other = (Panels) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.kb.Panels[ id=" + id + " ]";
    }

}
