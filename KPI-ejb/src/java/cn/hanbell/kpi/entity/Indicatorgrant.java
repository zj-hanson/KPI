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

/**
 *
 * @author C1879
 */
@Entity
@Table(name = "indicatorgrant")
@NamedQueries({
    @NamedQuery(name = "Indicatorgrant.findAll", query = "SELECT i FROM Indicatorgrant i")
    ,
    @NamedQuery(name = "Indicatorgrant.findByUserid", query = "SELECT i FROM Indicatorgrant i WHERE i.userid = :userid")
    ,
    @NamedQuery(name = "Indicatorgrant.findByUseridAndFormid", query = "SELECT i FROM Indicatorgrant i WHERE i.userid = :userid AND i.formid = :formid")
    ,
    @NamedQuery(name = "Indicatorgrant.findByUseridAndFormidNotId", query = "SELECT i FROM Indicatorgrant i WHERE i.userid = :userid AND i.formid = :formid AND i.id <> :id ")

})
public class Indicatorgrant extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "userid")
    private String userid;
    @Size(max = 20)
    @Column(name = "username")
    private String username;
    @Size(max = 50)
    @Column(name = "formid")
    private String formid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "forecast")
    private boolean forecast;
    @Basic(optional = false)
    @NotNull
    @Column(name = "benchmark")
    private boolean benchmark;
    @Basic(optional = false)
    @NotNull
    @Column(name = "target")
    private boolean target;
    @Basic(optional = false)
    @NotNull
    @Column(name = "actual")
    private boolean actual;
    @Basic(optional = false)
    @NotNull
    @Column(name = "performance")
    private boolean performance;
    @Basic(optional = false)
    @NotNull
    @Column(name = "other")
    private boolean other;
    @Size(max = 200)
    @Column(name = "remark1")
    private String remark1;
    @Size(max = 200)
    @Column(name = "remark2")
    private String remark2;

    public Indicatorgrant() {
    }

    public Indicatorgrant(Integer id) {
        this.id = id;
    }

    public Indicatorgrant(Integer id, String userid, boolean forecast, boolean benchmark, boolean target, boolean actual, boolean performance, boolean other, String status) {
        this.id = id;
        this.userid = userid;
        this.forecast = forecast;
        this.benchmark = benchmark;
        this.target = target;
        this.actual = actual;
        this.performance = performance;
        this.other = other;
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    public boolean getForecast() {
        return forecast;
    }

    public void setForecast(boolean forecast) {
        this.forecast = forecast;
    }

    public boolean getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(boolean benchmark) {
        this.benchmark = benchmark;
    }

    public boolean getTarget() {
        return target;
    }

    public void setTarget(boolean target) {
        this.target = target;
    }

    public boolean getActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

    public boolean getPerformance() {
        return performance;
    }

    public void setPerformance(boolean performance) {
        this.performance = performance;
    }

    public boolean getOther() {
        return other;
    }

    public void setOther(boolean other) {
        this.other = other;
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
        if (!(object instanceof Indicatorgrant)) {
            return false;
        }
        Indicatorgrant other = (Indicatorgrant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.Indicatorgrant[ id=" + id + " ]";
    }

}
