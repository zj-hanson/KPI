/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperDetailEntity;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C0160
 */
@Entity
@Table(name = "indicatordepartment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicatorDepartment.findAll", query = "SELECT i FROM IndicatorDepartment i"),
    @NamedQuery(name = "IndicatorDepartment.findById", query = "SELECT i FROM IndicatorDepartment i WHERE i.id = :id"),
    @NamedQuery(name = "IndicatorDepartment.findByPId", query = "SELECT i FROM IndicatorDepartment i WHERE i.pid = :pid"),
    @NamedQuery(name = "IndicatorDepartment.findByPIdAndSeq", query = "SELECT i FROM IndicatorDepartment i WHERE i.pid= :pid AND i.seq = :seq"),
    @NamedQuery(name = "IndicatorDepartment.findByDeptnoAndType", query = "SELECT i FROM IndicatorDepartment i WHERE i.deptno = :deptno AND i.parent.objtype = :objtype ORDER by i.parent.seq DESC,i.parent.sortid"),
    @NamedQuery(name = "IndicatorDepartment.findByDeptnoTypeAndYear", query = "SELECT i FROM IndicatorDepartment i WHERE i.deptno = :deptno AND i.parent.objtype = :objtype AND i.parent.seq = :seq ORDER by i.parent.sortid"),
    @NamedQuery(name = "IndicatorDepartment.findByDeptname", query = "SELECT i FROM IndicatorDepartment i WHERE i.deptname = :deptname"),
    @NamedQuery(name = "IndicatorDepartment.findByUserid", query = "SELECT i FROM IndicatorDepartment i WHERE i.userid = :userid"),
    @NamedQuery(name = "IndicatorDepartment.findByUsername", query = "SELECT i FROM IndicatorDepartment i WHERE i.username = :username")})
public class IndicatorDepartment extends SuperDetailEntity {

    @JoinColumn(name = "pid", referencedColumnName = "id", updatable = false, insertable = false)
    @ManyToOne(optional = false)
    private Indicator parent;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "deptno")
    private String deptno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "deptname")
    private String deptname;
    @Size(max = 20)
    @Column(name = "userid")
    private String userid;
    @Size(max = 20)
    @Column(name = "username")
    private String username;

    public IndicatorDepartment() {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IndicatorDepartment)) {
            return false;
        }
        IndicatorDepartment other = (IndicatorDepartment) object;
        if (this.id != null && other.id != null) {
            return Objects.equals(this.id, other.id);
        }
        if (Objects.equals(this.pid, other.pid)) {
            return (Objects.equals(this.seq, other.seq) && Objects.equals(this.deptno, other.deptno));
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.IndicatorDepartment[ id=" + id + " ]";
    }

    /**
     * @return the parent
     */
    public Indicator getParent() {
        return parent;
    }

}
