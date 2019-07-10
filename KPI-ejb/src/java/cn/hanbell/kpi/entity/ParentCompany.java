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
 * @author C1879
 */
@Entity
@Table(name = "parentcompany")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParentCompany.findAll", query = "SELECT p FROM ParentCompany p")
    , @NamedQuery(name = "ParentCompany.findById", query = "SELECT p FROM ParentCompany p WHERE p.id = :id")
    , @NamedQuery(name = "ParentCompany.findByCusno", query = "SELECT p FROM ParentCompany p WHERE p.cusno = :cusno ")
    , @NamedQuery(name = "ParentCompany.findByCusna", query = "SELECT p FROM ParentCompany p WHERE p.cusna = :cusna")
    , @NamedQuery(name = "ParentCompany.findByParentcusno", query = "SELECT p FROM ParentCompany p WHERE p.parentcusno = :parentcusno")
    , @NamedQuery(name = "ParentCompany.findByParentcusna", query = "SELECT p FROM ParentCompany p WHERE p.parentcusna = :parentcusna")
    , @NamedQuery(name = "ParentCompany.findByCusnoAndDeptno", query = "SELECT p FROM ParentCompany p WHERE p.cusno = :cusno AND p.deptno = :deptno ")
    , @NamedQuery(name = "ParentCompany.findByRemark2", query = "SELECT p FROM ParentCompany p WHERE p.remark2 = :remark2")
})
public class ParentCompany extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "cusno")
    private String cusno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "cusna")
    private String cusna;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "parentcusno")
    private String parentcusno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "parentcusna")
    private String parentcusna;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "deptno")
    private String deptno;
    @Size(max = 200)
    @Column(name = "remark1")
    private String remark1;
    @Size(max = 45)
    @Column(name = "remark2")
    private String remark2;

    public ParentCompany() {
    }

    public ParentCompany(Integer id) {
        this.id = id;
    }

    public ParentCompany(Integer id, String cusno, String cusna, String parentcusno, String parentcusna, String status) {
        this.id = id;
        this.cusno = cusno;
        this.cusna = cusna;
        this.parentcusno = parentcusno;
        this.parentcusna = parentcusna;
        this.status = status;
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
        if (!(object instanceof ParentCompany)) {
            return false;
        }
        ParentCompany other = (ParentCompany) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.ParentCompany[ id=" + id + " ]";
    }

    public String getCusno() {
        return cusno;
    }

    public void setCusno(String cusno) {
        this.cusno = cusno;
    }

    public String getCusna() {
        return cusna;
    }

    public void setCusna(String cusna) {
        this.cusna = cusna;
    }

    public String getParentcusno() {
        return parentcusno;
    }

    public void setParentcusno(String parentcusno) {
        this.parentcusno = parentcusno;
    }

    public String getParentcusna() {
        return parentcusna;
    }

    public void setParentcusna(String parentcusna) {
        this.parentcusna = parentcusna;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
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

}
