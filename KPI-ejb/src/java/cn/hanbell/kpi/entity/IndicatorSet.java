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
@Table(name = "indicatorset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicatorSet.findAll", query = "SELECT i FROM IndicatorSet i"),
    @NamedQuery(name = "IndicatorSet.findById", query = "SELECT i FROM IndicatorSet i WHERE i.id = :id"),
    @NamedQuery(name = "IndicatorSet.findByPId", query = "SELECT i FROM IndicatorSet i WHERE i.pid = :pid ORDER BY i.seq"),
    @NamedQuery(name = "IndicatorSet.findByFormid", query = "SELECT i FROM IndicatorSet i WHERE i.formid = :formid"),
    @NamedQuery(name = "IndicatorSet.findByRemark", query = "SELECT i FROM IndicatorSet i WHERE i.remark = :remark")})
public class IndicatorSet extends SuperDetailEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "formid")
    private String formid;
    @Size(max = 10)
    @Column(name = "deptno")
    private String deptno;
    @Size(max = 100)
    @Column(name = "remark")
    private String remark;

    public IndicatorSet() {
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    /**
     * @return the deptno
     */
    public String getDeptno() {
        return deptno;
    }

    /**
     * @param deptno the deptno to set
     */
    public void setDeptno(String deptno) {
        this.deptno = deptno;
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
        if (!(object instanceof IndicatorSet)) {
            return false;
        }
        IndicatorSet other = (IndicatorSet) object;
        if (this.id != null && other.id != null) {
            return this.id == other.id;
        }
        return Objects.equals(this.formid, other.formid);
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.IndicatorSet[ id=" + id + " ]";
    }

}
