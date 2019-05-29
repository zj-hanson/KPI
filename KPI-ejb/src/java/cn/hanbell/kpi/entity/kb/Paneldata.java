/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity.kb;

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

/**
 *
 * @author C1879
 */
@Entity
@Table(name = "paneldata")
@NamedQueries({
    @NamedQuery(name = "Paneldata.findAll", query = "SELECT p FROM Paneldata p")
})
public class Paneldata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pid")
    private int pid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pddate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "pdname")
    private String pdname;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value1")
    private BigDecimal value1;
    @Column(name = "value2")
    private BigDecimal value2;
    @Column(name = "value3")
    private BigDecimal value3;
    @Column(name = "value4")
    private BigDecimal value4;
    @Column(name = "value5")
    private BigDecimal value5;
    @Column(name = "value6")
    private BigDecimal value6;
    @Size(max = 40)
    @Column(name = "remark")
    private String remark;

    public Paneldata() {
        this.value1 = BigDecimal.ZERO;
        this.value2 = BigDecimal.ZERO;
        this.value3 = BigDecimal.ZERO;
        this.value4 = BigDecimal.ZERO;
        this.value5 = BigDecimal.ZERO;
        this.value6 = BigDecimal.ZERO;
    }

    public Paneldata(Integer id) {
        this.id = id;
    }

    public Paneldata(Integer id, int pid, Date pddate, String pdname) {
        this.id = id;
        this.pid = pid;
        this.pddate = pddate;
        this.pdname = pdname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public Date getPddate() {
        return pddate;
    }

    public void setPddate(Date pddate) {
        this.pddate = pddate;
    }

    public String getPdname() {
        return pdname;
    }

    public void setPdname(String pdname) {
        this.pdname = pdname;
    }

    public BigDecimal getValue1() {
        return value1;
    }

    public void setValue1(BigDecimal value1) {
        this.value1 = value1;
    }

    public BigDecimal getValue2() {
        return value2;
    }

    public void setValue2(BigDecimal value2) {
        this.value2 = value2;
    }

    public BigDecimal getValue3() {
        return value3;
    }

    public void setValue3(BigDecimal value3) {
        this.value3 = value3;
    }

    public BigDecimal getValue4() {
        return value4;
    }

    public void setValue4(BigDecimal value4) {
        this.value4 = value4;
    }

    public BigDecimal getValue5() {
        return value5;
    }

    public void setValue5(BigDecimal value5) {
        this.value5 = value5;
    }

    public BigDecimal getValue6() {
        return value6;
    }

    public void setValue6(BigDecimal value6) {
        this.value6 = value6;
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
        if (!(object instanceof Paneldata)) {
            return false;
        }
        Paneldata other = (Paneldata) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.kb.Paneldata[ id=" + id + " ]";
    }

}
