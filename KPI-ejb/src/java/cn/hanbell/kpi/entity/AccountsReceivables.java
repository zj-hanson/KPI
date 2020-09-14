/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "accountsreceivables")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccountsReceivables.findAll", query = "SELECT a FROM AccountsReceivables a"),
    @NamedQuery(name = "AccountsReceivables.findById", query = "SELECT a FROM AccountsReceivables a WHERE a.id = :id"),
    @NamedQuery(name = "AccountsReceivables.findBySortid", query = "SELECT a FROM AccountsReceivables a WHERE a.sortid = :sortid"),
    @NamedQuery(name = "AccountsReceivables.findByCompany", query = "SELECT a FROM AccountsReceivables a WHERE a.company = :company"),
    @NamedQuery(name = "AccountsReceivables.findBySeq", query = "SELECT a FROM AccountsReceivables a WHERE a.seq = :seq"),
    @NamedQuery(name = "AccountsReceivables.findByMon", query = "SELECT a FROM AccountsReceivables a WHERE a.mon = :mon"),
    @NamedQuery(name = "AccountsReceivables.findByName", query = "SELECT a FROM AccountsReceivables a WHERE a.name = :name"),
    @NamedQuery(name = "AccountsReceivables.findByDeptno", query = "SELECT a FROM AccountsReceivables a WHERE a.deptno = :deptno"),
    @NamedQuery(name = "AccountsReceivables.findByDeptname", query = "SELECT a FROM AccountsReceivables a WHERE a.deptname = :deptname"),
    @NamedQuery(name = "AccountsReceivables.findByType", query = "SELECT a FROM AccountsReceivables a WHERE a.type = :type"),
    @NamedQuery(name = "AccountsReceivables.findBySaleTarget", query = "SELECT a FROM AccountsReceivables a WHERE a.saleTarget = :saleTarget"),
    @NamedQuery(name = "AccountsReceivables.findBySaleActual", query = "SELECT a FROM AccountsReceivables a WHERE a.saleActual = :saleActual"),
    @NamedQuery(name = "AccountsReceivables.findBySaleRatio", query = "SELECT a FROM AccountsReceivables a WHERE a.saleRatio = :saleRatio"),
    @NamedQuery(name = "AccountsReceivables.findBySaleCost", query = "SELECT a FROM AccountsReceivables a WHERE a.saleCost = :saleCost"),
    @NamedQuery(name = "AccountsReceivables.findByGincmrt", query = "SELECT a FROM AccountsReceivables a WHERE a.gincmrt = :gincmrt"),
    @NamedQuery(name = "AccountsReceivables.findByARTdayTarget", query = "SELECT a FROM AccountsReceivables a WHERE a.aRTdayTarget = :aRTdayTarget"),
    @NamedQuery(name = "AccountsReceivables.findByARTday", query = "SELECT a FROM AccountsReceivables a WHERE a.aRTday = :aRTday"),
    @NamedQuery(name = "AccountsReceivables.findByBeginAR", query = "SELECT a FROM AccountsReceivables a WHERE a.beginAR = :beginAR"),
    @NamedQuery(name = "AccountsReceivables.findByEndAR", query = "SELECT a FROM AccountsReceivables a WHERE a.endAR = :endAR"),
    @NamedQuery(name = "AccountsReceivables.findByOverdueAccount", query = "SELECT a FROM AccountsReceivables a WHERE a.overdueAccount = :overdueAccount"),
    @NamedQuery(name = "AccountsReceivables.findByOpeCass", query = "SELECT a FROM AccountsReceivables a WHERE a.opeCass = :opeCass"),
    @NamedQuery(name = "AccountsReceivables.findBySaleInTax", query = "SELECT a FROM AccountsReceivables a WHERE a.saleInTax = :saleInTax"),
    @NamedQuery(name = "AccountsReceivables.findByBillAR3", query = "SELECT a FROM AccountsReceivables a WHERE a.billAR3 = :billAR3"),
    @NamedQuery(name = "AccountsReceivables.findByBillAR6", query = "SELECT a FROM AccountsReceivables a WHERE a.billAR6 = :billAR6"),
    @NamedQuery(name = "AccountsReceivables.findByCashAR", query = "SELECT a FROM AccountsReceivables a WHERE a.cashAR = :cashAR"),
    @NamedQuery(name = "AccountsReceivables.findByBenchmarkARTday", query = "SELECT a FROM AccountsReceivables a WHERE a.benchmarkARTday = :benchmarkARTday"),
    @NamedQuery(name = "AccountsReceivables.findByBenchmarkOpeCass", query = "SELECT a FROM AccountsReceivables a WHERE a.benchmarkOpeCass = :benchmarkOpeCass"),
    @NamedQuery(name = "AccountsReceivables.findByStatus", query = "SELECT a FROM AccountsReceivables a WHERE a.status = :status"),
    @NamedQuery(name = "AccountsReceivables.findByCreator", query = "SELECT a FROM AccountsReceivables a WHERE a.creator = :creator"),
    @NamedQuery(name = "AccountsReceivables.findByCredate", query = "SELECT a FROM AccountsReceivables a WHERE a.credate = :credate"),
    @NamedQuery(name = "AccountsReceivables.findByOptuser", query = "SELECT a FROM AccountsReceivables a WHERE a.optuser = :optuser"),
    @NamedQuery(name = "AccountsReceivables.findByOptdate", query = "SELECT a FROM AccountsReceivables a WHERE a.optdate = :optdate"),
    @NamedQuery(name = "AccountsReceivables.findByCfmuser", query = "SELECT a FROM AccountsReceivables a WHERE a.cfmuser = :cfmuser"),
    @NamedQuery(name = "AccountsReceivables.findByCfmdate", query = "SELECT a FROM AccountsReceivables a WHERE a.cfmdate = :cfmdate"),
    @NamedQuery(name = "AccountsReceivables.findByCompanyAndSeqAndMon", query = "SELECT a FROM AccountsReceivables a WHERE a.company = :company AND a.seq = :seq AND a.mon = :mon")})
public class AccountsReceivables extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Column(name = "sortid")
    private int sortid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "company")
    private String company;
    @Basic(optional = false)
    @NotNull
    @Column(name = "seq")
    private int seq;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mon")
    private int mon;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "deptno")
    private String deptno;
    @Size(max = 45)
    @Column(name = "deptname")
    private String deptname;
    @Size(max = 45)
    @Column(name = "type")
    private String type;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saleTarget")
    private BigDecimal saleTarget;
    @Column(name = "saleActual")
    private BigDecimal saleActual;
    @Column(name = "saleRatio")
    private BigDecimal saleRatio;
    @Column(name = "saleCost")
    private BigDecimal saleCost;
    @Column(name = "gincmrt")
    private BigDecimal gincmrt;
    @Column(name = "ARTdayTarget")
    private BigDecimal aRTdayTarget;
    @Column(name = "ARTday")
    private BigDecimal aRTday;
    @Column(name = "beginAR")
    private BigDecimal beginAR;
    @Column(name = "endAR")
    private BigDecimal endAR;
    @Column(name = "overdueAccount")
    private BigDecimal overdueAccount;
    @Column(name = "opeCass")
    private BigDecimal opeCass;
    @Column(name = "saleInTax")
    private BigDecimal saleInTax;
    @Column(name = "billAR3")
    private BigDecimal billAR3;
    @Column(name = "billAR6")
    private BigDecimal billAR6;
    @Column(name = "cashAR")
    private BigDecimal cashAR;
    @Column(name = "benchmarkARTday")
    private BigDecimal benchmarkARTday;
    @Column(name = "benchmarkOpeCass")
    private BigDecimal benchmarkOpeCass;

    public AccountsReceivables() {
    }

    public AccountsReceivables(Integer id) {
        this.id = id;
    }

    public AccountsReceivables(Integer id, int sortid, String company, int seq, int mon) {
        this.id = id;
        this.sortid = sortid;
        this.company = company;
        this.seq = seq;
        this.mon = mon;
    }

    public int getSortid() {
        return sortid;
    }

    public void setSortid(int sortid) {
        this.sortid = sortid;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getMon() {
        return mon;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getSaleTarget() {
        return saleTarget;
    }

    public void setSaleTarget(BigDecimal saleTarget) {
        this.saleTarget = saleTarget;
    }

    public BigDecimal getSaleActual() {
        return saleActual;
    }

    public void setSaleActual(BigDecimal saleActual) {
        this.saleActual = saleActual;
    }

    public BigDecimal getSaleRatio() {
        return saleRatio;
    }

    public void setSaleRatio(BigDecimal saleRatio) {
        this.saleRatio = saleRatio;
    }

    public BigDecimal getSaleCost() {
        return saleCost;
    }

    public void setSaleCost(BigDecimal saleCost) {
        this.saleCost = saleCost;
    }

    public BigDecimal getGincmrt() {
        return gincmrt;
    }

    public void setGincmrt(BigDecimal gincmrt) {
        this.gincmrt = gincmrt;
    }

    public BigDecimal getARTdayTarget() {
        return aRTdayTarget;
    }

    public void setARTdayTarget(BigDecimal aRTdayTarget) {
        this.aRTdayTarget = aRTdayTarget;
    }

    public BigDecimal getARTday() {
        return aRTday;
    }

    public void setARTday(BigDecimal aRTday) {
        this.aRTday = aRTday;
    }

    public BigDecimal getBeginAR() {
        return beginAR;
    }

    public void setBeginAR(BigDecimal beginAR) {
        this.beginAR = beginAR;
    }

    public BigDecimal getEndAR() {
        return endAR;
    }

    public void setEndAR(BigDecimal endAR) {
        this.endAR = endAR;
    }

    public BigDecimal getOverdueAccount() {
        return overdueAccount;
    }

    public void setOverdueAccount(BigDecimal overdueAccount) {
        this.overdueAccount = overdueAccount;
    }

    public BigDecimal getOpeCass() {
        return opeCass;
    }

    public void setOpeCass(BigDecimal opeCass) {
        this.opeCass = opeCass;
    }

    public BigDecimal getSaleInTax() {
        return saleInTax;
    }

    public void setSaleInTax(BigDecimal saleInTax) {
        this.saleInTax = saleInTax;
    }

    public BigDecimal getBillAR3() {
        return billAR3;
    }

    public void setBillAR3(BigDecimal billAR3) {
        this.billAR3 = billAR3;
    }

    public BigDecimal getBillAR6() {
        return billAR6;
    }

    public void setBillAR6(BigDecimal billAR6) {
        this.billAR6 = billAR6;
    }

    public BigDecimal getCashAR() {
        return cashAR;
    }

    public void setCashAR(BigDecimal cashAR) {
        this.cashAR = cashAR;
    }

    public BigDecimal getBenchmarkARTday() {
        return benchmarkARTday;
    }

    public void setBenchmarkARTday(BigDecimal benchmarkARTday) {
        this.benchmarkARTday = benchmarkARTday;
    }

    public BigDecimal getBenchmarkOpeCass() {
        return benchmarkOpeCass;
    }

    public void setBenchmarkOpeCass(BigDecimal benchmarkOpeCass) {
        this.benchmarkOpeCass = benchmarkOpeCass;
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
        if (!(object instanceof AccountsReceivables)) {
            return false;
        }
        AccountsReceivables other = (AccountsReceivables) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.AccountsReceivables[ id=" + id + " ]";
    }

}
