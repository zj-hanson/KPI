/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "scorecardgrant")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ScorecardGrant.findAll", query = "SELECT s FROM ScorecardGrant s"),
    @NamedQuery(name = "ScorecardGrant.findById", query = "SELECT s FROM ScorecardGrant s WHERE s.id = :id"),
    @NamedQuery(name = "ScorecardGrant.findByDeptno", query = "SELECT s FROM ScorecardGrant s WHERE s.deptno = :deptno"),
    @NamedQuery(name = "ScorecardGrant.findByDeptname", query = "SELECT s FROM ScorecardGrant s WHERE s.deptname = :deptname"),
    @NamedQuery(name = "ScorecardGrant.findByCompany", query = "SELECT s FROM ScorecardGrant s WHERE s.company = :company"),
    @NamedQuery(name = "ScorecardGrant.findByScorecardid", query = "SELECT s FROM ScorecardGrant s WHERE s.scorecardid = :scorecardid"),
    @NamedQuery(name = "ScorecardGrant.findByScorecardname", query = "SELECT s FROM ScorecardGrant s WHERE s.scorecardname = :scorecardname"),
    @NamedQuery(name = "ScorecardGrant.findBySeq", query = "SELECT s FROM ScorecardGrant s WHERE s.seq = :seq"),
    @NamedQuery(name = "ScorecardGrant.findByContentid", query = "SELECT s FROM ScorecardGrant s WHERE s.contentid = :contentid"),
    @NamedQuery(name = "ScorecardGrant.findByContentname", query = "SELECT s FROM ScorecardGrant s WHERE s.contentname = :contentname"),
    @NamedQuery(name = "ScorecardGrant.findByBenchmark", query = "SELECT s FROM ScorecardGrant s WHERE s.benchmark = :benchmark"),
    @NamedQuery(name = "ScorecardGrant.findByTarget", query = "SELECT s FROM ScorecardGrant s WHERE s.target = :target"),
    @NamedQuery(name = "ScorecardGrant.findByActual", query = "SELECT s FROM ScorecardGrant s WHERE s.actual = :actual"),
    @NamedQuery(name = "ScorecardGrant.findByPerformance", query = "SELECT s FROM ScorecardGrant s WHERE s.performance = :performance"),
    @NamedQuery(name = "ScorecardGrant.findByDeptscore", query = "SELECT s FROM ScorecardGrant s WHERE s.deptscore = :deptscore"),
    @NamedQuery(name = "ScorecardGrant.findByGeneralscore", query = "SELECT s FROM ScorecardGrant s WHERE s.generalscore = :generalscore"),
    @NamedQuery(name = "ScorecardGrant.findByRemark", query = "SELECT s FROM ScorecardGrant s WHERE s.remark = :remark"),
    @NamedQuery(name = "ScorecardGrant.findByStatus", query = "SELECT s FROM ScorecardGrant s WHERE s.status = :status"),
    @NamedQuery(name = "ScorecardGrant.findByCreator", query = "SELECT s FROM ScorecardGrant s WHERE s.creator = :creator"),
    @NamedQuery(name = "ScorecardGrant.findByCredate", query = "SELECT s FROM ScorecardGrant s WHERE s.credate = :credate"),
    @NamedQuery(name = "ScorecardGrant.findByOptuser", query = "SELECT s FROM ScorecardGrant s WHERE s.optuser = :optuser"),
    @NamedQuery(name = "ScorecardGrant.findByOptdate", query = "SELECT s FROM ScorecardGrant s WHERE s.optdate = :optdate"),
    @NamedQuery(name = "ScorecardGrant.findByCfmuser", query = "SELECT s FROM ScorecardGrant s WHERE s.cfmuser = :cfmuser"),
    @NamedQuery(name = "ScorecardGrant.findByCfmdate", query = "SELECT s FROM ScorecardGrant s WHERE s.cfmdate = :cfmdate"),
    @NamedQuery(name = "ScorecardGrant.findByCompanyAndScorecardidAndContentidAndSeq", query = "SELECT s FROM ScorecardGrant s WHERE s.company = :company AND s.scorecardid = :scorecardid AND s.contentid = :contentid AND s.seq = :seq")})
public class ScorecardGrant extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "deptno")
    private String deptno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "deptname")
    private String deptname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "company")
    private String company;
    @Column(name = "scorecardid")
    private Integer scorecardid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "scorecardname")
    private String scorecardname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "seq")
    private int seq;
    @Basic(optional = false)
    @NotNull
    @Column(name = "contentid")
    private int contentid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "contentname")
    private String contentname;
    @Column(name = "benchmark")
    private Boolean benchmark;
    @Column(name = "target")
    private Boolean target;
    @Column(name = "actual")
    private Boolean actual;
    @Column(name = "performance")
    private Boolean performance;
    @Column(name = "deptscore")
    private Boolean deptscore;
    @Column(name = "generalscore")
    private Boolean generalscore;
    @Size(max = 100)
    @Column(name = "remark")
    private String remark;

    public ScorecardGrant() {
    }

    public ScorecardGrant(Integer id) {
        this.id = id;
    }

    public ScorecardGrant(Integer id, String deptno, String deptname, String company, String scorecardname, int seq, int contentid, String contentname) {
        this.id = id;
        this.deptno = deptno;
        this.deptname = deptname;
        this.company = company;
        this.scorecardname = scorecardname;
        this.seq = seq;
        this.contentid = contentid;
        this.contentname = contentname;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getScorecardid() {
        return scorecardid;
    }

    public void setScorecardid(Integer scorecardid) {
        this.scorecardid = scorecardid;
    }

    public String getScorecardname() {
        return scorecardname;
    }

    public void setScorecardname(String scorecardname) {
        this.scorecardname = scorecardname;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getContentid() {
        return contentid;
    }

    public void setContentid(int contentid) {
        this.contentid = contentid;
    }

    public String getContentname() {
        return contentname;
    }

    public void setContentname(String contentname) {
        this.contentname = contentname;
    }

    public Boolean getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(Boolean benchmark) {
        this.benchmark = benchmark;
    }

    public Boolean getTarget() {
        return target;
    }

    public void setTarget(Boolean target) {
        this.target = target;
    }

    public Boolean getActual() {
        return actual;
    }

    public void setActual(Boolean actual) {
        this.actual = actual;
    }

    public Boolean getPerformance() {
        return performance;
    }

    public void setPerformance(Boolean performance) {
        this.performance = performance;
    }

    public Boolean getDeptscore() {
        return deptscore;
    }

    public void setDeptscore(Boolean deptscore) {
        this.deptscore = deptscore;
    }

    public Boolean getGeneralscore() {
        return generalscore;
    }

    public void setGeneralscore(Boolean generalscore) {
        this.generalscore = generalscore;
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
        if (!(object instanceof ScorecardGrant)) {
            return false;
        }
        ScorecardGrant other = (ScorecardGrant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.ScorecardGrant[ id=" + id + " ]";
    }

}
