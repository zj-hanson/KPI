/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
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
@Table(name = "indicatorchart")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicatorChart.findAll", query = "SELECT i FROM IndicatorChart i")
    ,
    @NamedQuery(name = "IndicatorChart.findById", query = "SELECT i FROM IndicatorChart i WHERE i.id = :id")
    ,
    @NamedQuery(name = "IndicatorChart.findByCompany", query = "SELECT i FROM IndicatorChart i WHERE i.company = :company")
    ,
    @NamedQuery(name = "IndicatorChart.findByFormid", query = "SELECT i FROM IndicatorChart i WHERE i.formid = :formid")
    ,
    @NamedQuery(name = "IndicatorChart.findByPId", query = "SELECT i FROM IndicatorChart i WHERE i.pid = :pid ORDER BY i.formid")
     ,
    @NamedQuery(name = "IndicatorChart.findByFormidAndDeptno", query = "SELECT i FROM IndicatorChart i WHERE i.formid = :formid AND i.deptno=:deptno")})
public class IndicatorChart extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "company")
    private String company;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "formid")
    private String formid;
    @Size(max = 45)
    @Column(name = "formtype")
    private String formtype;
    @Size(max = 10)
    @Column(name = "formkind")
    private String formkind;
    @Size(max = 200)
    @Column(name = "name")
    private String name;
    @Size(max = 400)
    @Column(name = "descript")
    private String descript;
    @Size(max = 2)
    @Column(name = "objtype")
    private String objtype;
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "api")
    private String api;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "pid")
    private String pid;
    @NotNull
    @Column(name = "sortid")
    private int sortid;
    @Size(max = 200)
    @Column(name = "remark")
    private String remark;

    public IndicatorChart() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    public String getFormtype() {
        return formtype;
    }

    public void setFormtype(String formtype) {
        this.formtype = formtype;
    }

    public String getFormkind() {
        return formkind;
    }

    public void setFormkind(String formkind) {
        this.formkind = formkind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getObjtype() {
        return objtype;
    }

    public void setObjtype(String objtype) {
        this.objtype = objtype;
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

    /**
     * @return the api
     */
    public String getApi() {
        return api;
    }

    /**
     * @param api the api to set
     */
    public void setApi(String api) {
        this.api = api;
    }

    /**
     * @return the pid
     */
    public String getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * @return the sortid
     */
    public int getSortid() {
        return sortid;
    }

    /**
     * @param sortid the sortid to set
     */
    public void setSortid(int sortid) {
        this.sortid = sortid;
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
        if (!(object instanceof IndicatorChart)) {
            return false;
        }
        IndicatorChart other = (IndicatorChart) object;
        if (this.id != null && other.id != null) {
            return this.id == other.id;
        }
        return Objects.equals(this.formid, other.formid);
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.IndicatorChart[ id=" + id + " ]";
    }

}
