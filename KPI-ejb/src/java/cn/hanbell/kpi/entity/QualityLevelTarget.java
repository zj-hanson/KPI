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
@Table(name = "qualitylevel_target")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QualityLevelTarget.findAll", query = "SELECT q FROM QualityLevelTarget q"),
    @NamedQuery(name = "QualityLevelTarget.findById", query = "SELECT q FROM QualityLevelTarget q WHERE q.id = :id"),
    @NamedQuery(name = "QualityLevelTarget.findByName", query = "SELECT q FROM QualityLevelTarget q WHERE q.name = :name"),
    @NamedQuery(name = "QualityLevelTarget.findBySeq", query = "SELECT q FROM QualityLevelTarget q WHERE q.seq = :seq"),
    @NamedQuery(name = "QualityLevelTarget.findByIndicatorid", query = "SELECT q FROM QualityLevelTarget q WHERE q.indicatorid = :indicatorid"),
    @NamedQuery(name = "QualityLevelTarget.findByIndicatorname", query = "SELECT q FROM QualityLevelTarget q WHERE q.indicatorname = :indicatorname"),
    @NamedQuery(name = "QualityLevelTarget.findByOther", query = "SELECT q FROM QualityLevelTarget q WHERE q.other = :other"),
    @NamedQuery(name = "QualityLevelTarget.findByOtherlabel", query = "SELECT q FROM QualityLevelTarget q WHERE q.otherlabel = :otherlabel"),
    @NamedQuery(name = "QualityLevelTarget.findByTarget1", query = "SELECT q FROM QualityLevelTarget q WHERE q.target1 = :target1"),
    @NamedQuery(name = "QualityLevelTarget.findByTarget2", query = "SELECT q FROM QualityLevelTarget q WHERE q.target2 = :target2"),
    @NamedQuery(name = "QualityLevelTarget.findByRemark", query = "SELECT q FROM QualityLevelTarget q WHERE q.remark = :remark"),
    @NamedQuery(name = "QualityLevelTarget.findByStatus", query = "SELECT q FROM QualityLevelTarget q WHERE q.status = :status"),
    @NamedQuery(name = "QualityLevelTarget.findByCreator", query = "SELECT q FROM QualityLevelTarget q WHERE q.creator = :creator"),
    @NamedQuery(name = "QualityLevelTarget.findByCredate", query = "SELECT q FROM QualityLevelTarget q WHERE q.credate = :credate"),
    @NamedQuery(name = "QualityLevelTarget.findByOptuser", query = "SELECT q FROM QualityLevelTarget q WHERE q.optuser = :optuser"),
    @NamedQuery(name = "QualityLevelTarget.findByOptdate", query = "SELECT q FROM QualityLevelTarget q WHERE q.optdate = :optdate"),
    @NamedQuery(name = "QualityLevelTarget.findByCfmuser", query = "SELECT q FROM QualityLevelTarget q WHERE q.cfmuser = :cfmuser"),
    @NamedQuery(name = "QualityLevelTarget.findByCfmdate", query = "SELECT q FROM QualityLevelTarget q WHERE q.cfmdate = :cfmdate"),
    @NamedQuery(name = "QualityLevelTarget.findByIndicatorIdAndSeq", query = "SELECT q FROM QualityLevelTarget q WHERE q.indicatorid = :indicatorId AND q.seq = :seq")})
public class QualityLevelTarget extends SuperEntity{

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull()
    @Column(name = "seq")
    private int seq;
    @Basic(optional = false)
    @NotNull
    @Column(name = "indicatorid")
    private int indicatorid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "indicatorname")
    private String indicatorname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "other")
    private int other;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "otherlabel")
    private String otherlabel;
    @Size(max = 45)
    @Column(name = "remark")
    private String remark;
    @Column(name = "target1")
    private BigDecimal target1;
    @Column(name = "target2")
    private BigDecimal target2;

    public QualityLevelTarget() {
    }

    public QualityLevelTarget(Integer id) {
        this.id = id;
    }

    public QualityLevelTarget(Integer id, String name, int seq, int indicatorid, String indicatorname, int other, String otherlabel) {
        this.id = id;
        this.name = name;
        this.seq = seq;
        this.indicatorid = indicatorid;
        this.indicatorname = indicatorname;
        this.other = other;
        this.otherlabel = otherlabel;
    }

    public BigDecimal getTarget1() {
        return target1;
    }

    public void setTarget1(BigDecimal target1) {
        this.target1 = target1;
    }

    public BigDecimal getTarget2() {
        return target2;
    }

    public void setTarget2(BigDecimal target2) {
        this.target2 = target2;
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
        if (!(object instanceof QualityLevelTarget)) {
            return false;
        }
        QualityLevelTarget other = (QualityLevelTarget) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.QualityLevelTarget[ id=" + id + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getIndicatorid() {
        return indicatorid;
    }

    public void setIndicatorid(int indicatorid) {
        this.indicatorid = indicatorid;
    }

    public String getIndicatorname() {
        return indicatorname;
    }

    public void setIndicatorname(String indicatorname) {
        this.indicatorname = indicatorname;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }

    public String getOtherlabel() {
        return otherlabel;
    }

    public void setOtherlabel(String otherlabel) {
        this.otherlabel = otherlabel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
