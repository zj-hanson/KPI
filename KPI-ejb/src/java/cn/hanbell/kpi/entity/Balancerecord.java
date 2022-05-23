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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@Entity
@Table(name = "balancerecord")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Balancerecord.findAll", query = "SELECT b FROM Balancerecord b"),
    @NamedQuery(name = "Balancerecord.findById", query = "SELECT b FROM Balancerecord b WHERE b.id = :id"),
    @NamedQuery(name = "Balancerecord.findByFacno", query = "SELECT b FROM Balancerecord b WHERE b.facno = :facno"),
    @NamedQuery(name = "Balancerecord.findByType", query = "SELECT b FROM Balancerecord b WHERE b.type = :type"),
    @NamedQuery(name = "Balancerecord.findByItemno", query = "SELECT b FROM Balancerecord b WHERE b.itemno = :itemno"),
    @NamedQuery(name = "Balancerecord.findByItemname", query = "SELECT b FROM Balancerecord b WHERE b.itemname = :itemname"),
    @NamedQuery(name = "Balancerecord.findByYear", query = "SELECT b FROM Balancerecord b WHERE b.year = :year"),
    @NamedQuery(name = "Balancerecord.findByMon", query = "SELECT b FROM Balancerecord b WHERE b.mon = :mon"),
    @NamedQuery(name = "Balancerecord.findByYearmon", query = "SELECT b FROM Balancerecord b WHERE b.yearmon = :yearmon"),
    @NamedQuery(name = "Balancerecord.findByMonthmon", query = "SELECT b FROM Balancerecord b WHERE b.monthmon = :monthmon"),
    @NamedQuery(name = "Balancerecord.findByDifference", query = "SELECT b FROM Balancerecord b WHERE b.difference = :difference"),
    @NamedQuery(name = "Balancerecord.findByScale", query = "SELECT b FROM Balancerecord b WHERE b.scale = :scale"),
    @NamedQuery(name = "Balancerecord.findByStatus", query = "SELECT b FROM Balancerecord b WHERE b.status = :status"),
    @NamedQuery(name = "Balancerecord.findByCreator", query = "SELECT b FROM Balancerecord b WHERE b.creator = :creator"),
    @NamedQuery(name = "Balancerecord.findByCredate", query = "SELECT b FROM Balancerecord b WHERE b.credate = :credate"),
    @NamedQuery(name = "Balancerecord.findByOptuser", query = "SELECT b FROM Balancerecord b WHERE b.optuser = :optuser"),
    @NamedQuery(name = "Balancerecord.findByOptdate", query = "SELECT b FROM Balancerecord b WHERE b.optdate = :optdate"),
    @NamedQuery(name = "Balancerecord.findByCfmuser", query = "SELECT b FROM Balancerecord b WHERE b.cfmuser = :cfmuser"),
    @NamedQuery(name = "Balancerecord.findByCfmdate", query = "SELECT b FROM Balancerecord b WHERE b.cfmdate = :cfmdate"),
    @NamedQuery(name = "Balancerecord.findByYearAndMon", query = "SELECT b FROM Balancerecord b WHERE  b.year = :year and b.mon = :mon")})
public class Balancerecord extends SuperEntity {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "facno")
    private String facno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Column(name = "itemno")
    private int itemno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "itemname")
    private String itemname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "year")
    private int year;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mon")
    private int mon;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "yearmon")
    private BigDecimal yearmon;
    @Basic(optional = false)
    @NotNull
    @Column(name = "monthmon")
    private BigDecimal monthmon;
    @Basic(optional = false)
    @NotNull
    @Column(name = "difference")
    private BigDecimal difference;
    @Column(name = "scale")
    private BigDecimal scale;

    public Balancerecord() {
    }

    public Balancerecord(Integer id) {
        this.id = id;
    }

    public Balancerecord(Integer id, String facno, String type, int itemno, String itemname, int year, int mon, BigDecimal yearmon, BigDecimal monthmon, BigDecimal difference) {
        this.id = id;
        this.facno = facno;
        this.type = type;
        this.itemno = itemno;
        this.itemname = itemname;
        this.year = year;
        this.mon = mon;
        this.yearmon = yearmon;
        this.monthmon = monthmon;
        this.difference = difference;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getItemno() {
        return itemno;
    }

    public void setItemno(int itemno) {
        this.itemno = itemno;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMon() {
        return mon;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public BigDecimal getYearmon() {
        return yearmon;
    }

    public void setYearmon(BigDecimal yearmon) {
        this.yearmon = yearmon;
    }

    public BigDecimal getMonthmon() {
        return monthmon;
    }

    public void setMonthmon(BigDecimal monthmon) {
        this.monthmon = monthmon;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
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
        if (!(object instanceof Balancerecord)) {
            return false;
        }
        Balancerecord other = (Balancerecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.Balancerecord[ id=" + id + " ]";
    }
    
}
