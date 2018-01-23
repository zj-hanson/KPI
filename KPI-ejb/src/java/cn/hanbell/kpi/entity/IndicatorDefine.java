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
 * @author C0160
 */
@Entity
@Table(name = "indicatordefine")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicatorDefine.findAll", query = "SELECT i FROM IndicatorDefine i"),
    @NamedQuery(name = "IndicatorDefine.findById", query = "SELECT i FROM IndicatorDefine i WHERE i.id = :id"),
    @NamedQuery(name = "IndicatorDefine.findByCompany", query = "SELECT i FROM IndicatorDefine i WHERE i.company = :company"),
    @NamedQuery(name = "IndicatorDefine.findByFormtype", query = "SELECT i FROM IndicatorDefine i WHERE i.formtype = :formtype"),
    @NamedQuery(name = "IndicatorDefine.findByFormkind", query = "SELECT i FROM IndicatorDefine i WHERE i.formkind = :formkind"),
    @NamedQuery(name = "IndicatorDefine.findByDeptno", query = "SELECT i FROM IndicatorDefine i WHERE i.deptno = :deptno"),
    @NamedQuery(name = "IndicatorDefine.findByDeptname", query = "SELECT i FROM IndicatorDefine i WHERE i.deptname = :deptname"),
    @NamedQuery(name = "IndicatorDefine.findByRemark", query = "SELECT i FROM IndicatorDefine i WHERE i.remark = :remark"),
    @NamedQuery(name = "IndicatorDefine.findByStatus", query = "SELECT i FROM IndicatorDefine i WHERE i.status = :status")})
public class IndicatorDefine extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "company")
    private String company;
    @Size(max = 20)
    @Column(name = "formid")
    private String formid;
    @Size(max = 10)
    @Column(name = "formtype")
    private String formtype;
    @Size(max = 10)
    @Column(name = "formkind")
    private String formkind;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "name")
    private String name;
    @Size(max = 400)
    @Column(name = "descript")
    private String descript;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "objtype")
    private String objtype;
    @Size(max = 10)
    @Column(name = "deptno")
    private String deptno;
    @Size(max = 45)
    @Column(name = "deptname")
    private String deptname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sortid")
    private int sortid;
    @Basic(optional = false)
    @NotNull
    @Size(max = 10)
    @Column(name = "valuemode")
    private String valueMode;
    @Basic(optional = false)
    @NotNull
    @Size(max = 10)
    @Column(name = "perfcalc")
    private String perfCalc;
    @Size(max = 10)
    @Column(name = "symbol")
    private String symbol;
    @Size(max = 10)
    @Column(name = "unit")
    private String unit;
    @Column(name = "rate")
    private BigDecimal rate;
    @Column(name = "minNum")
    private BigDecimal minNum;
    @Column(name = "maxNum")
    private BigDecimal maxNum;
    @Column(name = "limited")
    private boolean limited;
    @Size(max = 45)
    @Column(name = "api")
    private String api;
    @Size(max = 200)
    @Column(name = "remark")
    private String remark;

    public IndicatorDefine() {
        this.symbol = "";
        this.rate = BigDecimal.ONE;
        this.minNum = BigDecimal.ZERO;
        this.maxNum = BigDecimal.ZERO;
        this.limited = false;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return the formid
     */
    public String getFormid() {
        return formid;
    }

    /**
     * @param formid the formid to set
     */
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

    /**
     * @return the objtype
     */
    public String getObjtype() {
        return objtype;
    }

    /**
     * @param objtype the objtype to set
     */
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

    /**
     * @return the valueMode
     */
    public String getValueMode() {
        return valueMode;
    }

    /**
     * @param valueMode the valueMode to set
     */
    public void setValueMode(String valueMode) {
        this.valueMode = valueMode;
    }

    /**
     * @return the perfCalc
     */
    public String getPerfCalc() {
        return perfCalc;
    }

    /**
     * @param perfCalc the perfCalc to set
     */
    public void setPerfCalc(String perfCalc) {
        this.perfCalc = perfCalc;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @return the rate
     */
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * @return the limited
     */
    public boolean isLimited() {
        return limited;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * @param limited the limited to set
     */
    public void setLimited(boolean limited) {
        this.limited = limited;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the minNum
     */
    public BigDecimal getMinNum() {
        return minNum;
    }

    /**
     * @param minNum the minNum to set
     */
    public void setMinNum(BigDecimal minNum) {
        this.minNum = minNum;
    }

    /**
     * @return the maxNum
     */
    public BigDecimal getMaxNum() {
        return maxNum;
    }

    /**
     * @param maxNum the maxNum to set
     */
    public void setMaxNum(BigDecimal maxNum) {
        this.maxNum = maxNum;
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
        if (!(object instanceof IndicatorDefine)) {
            return false;
        }
        IndicatorDefine other = (IndicatorDefine) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.IndicatorDefine[ id=" + id + " ]";
    }

}
