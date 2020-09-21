/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
import java.math.BigDecimal;
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
 * @author Administrator
 */
@Entity
@Table(name = "invtrtrday")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invtrtrday.findAll", query = "SELECT i FROM Invtrtrday i"),
    @NamedQuery(name = "Invtrtrday.findById", query = "SELECT i FROM Invtrtrday i WHERE i.id = :id"),
    @NamedQuery(name = "Invtrtrday.findBySortid", query = "SELECT i FROM Invtrtrday i WHERE i.sortid = :sortid"),
    @NamedQuery(name = "Invtrtrday.findByCompany", query = "SELECT i FROM Invtrtrday i WHERE i.company = :company"),
    @NamedQuery(name = "Invtrtrday.findBySeq", query = "SELECT i FROM Invtrtrday i WHERE i.seq = :seq"),
    @NamedQuery(name = "Invtrtrday.findByMon", query = "SELECT i FROM Invtrtrday i WHERE i.mon = :mon"),
    @NamedQuery(name = "Invtrtrday.findByName", query = "SELECT i FROM Invtrtrday i WHERE i.name = :name"),
    @NamedQuery(name = "Invtrtrday.findByType", query = "SELECT i FROM Invtrtrday i WHERE i.type = :type"),
    @NamedQuery(name = "Invtrtrday.findByDeptno", query = "SELECT i FROM Invtrtrday i WHERE i.deptno = :deptno"),
    @NamedQuery(name = "Invtrtrday.findByDeptname", query = "SELECT i FROM Invtrtrday i WHERE i.deptname = :deptname"),
    @NamedQuery(name = "Invtrtrday.findByResponsible", query = "SELECT i FROM Invtrtrday i WHERE i.responsible = :responsible"),
    @NamedQuery(name = "Invtrtrday.findByCost", query = "SELECT i FROM Invtrtrday i WHERE i.cost = :cost"),
    @NamedQuery(name = "Invtrtrday.findByTuningday", query = "SELECT i FROM Invtrtrday i WHERE i.tuningday = :tuningday"),
    @NamedQuery(name = "Invtrtrday.findByInvamount", query = "SELECT i FROM Invtrtrday i WHERE i.invamount = :invamount"),
    @NamedQuery(name = "Invtrtrday.findByCompanyAndSeqAndMon", query = "SELECT i FROM Invtrtrday i WHERE i.company = :company AND i.seq = :seq AND i.mon = :mon")})
public class Invtrtrday extends SuperEntity{

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
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
    @NotNull()
    @Column(name = "mon")
    private int mon;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "type")
    private String type;
    @Size(max = 45)
    @Column(name = "deptno")
    private String deptno;
    @Size(max = 45)
    @Column(name = "deptname")
    private String deptname;
    @Size(max = 45)
    @Column(name = "responsible")
    private String responsible;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "tuningday")
    private BigDecimal tuningday;
    @Column(name = "invamount")
    private BigDecimal invamount;

    public Invtrtrday() {
    }

    public Invtrtrday(Integer id) {
        this.id = id;
    }

    public Invtrtrday(Integer id, int sortid, String company, int seq, int mon) {
        this.id = id;
        this.sortid = sortid;
        this.company = company;
        this.seq = seq;
        this.mon = mon;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getTuningday() {
        return tuningday;
    }

    public void setTuningday(BigDecimal tuningday) {
        this.tuningday = tuningday;
    }

    public BigDecimal getInvamount() {
        return invamount;
    }

    public void setInvamount(BigDecimal invamount) {
        this.invamount = invamount;
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
        if (!(object instanceof Invtrtrday)) {
            return false;
        }
        Invtrtrday other = (Invtrtrday) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.Invtrtrday[ id=" + id + " ]";
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

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

}
