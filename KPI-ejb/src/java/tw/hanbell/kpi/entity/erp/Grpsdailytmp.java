/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tw.hanbell.kpi.entity.erp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C1749
 */
@Entity
@Table(name = "N_RPT_grpsdailytmp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grpsdailytmp.findAll", query = "SELECT g FROM Grpsdailytmp g")
    , @NamedQuery(name = "Grpsdailytmp.findByHangrp", query = "SELECT g FROM Grpsdailytmp g WHERE g.grpsdailytmpPK.hangrp = :hangrp")
    , @NamedQuery(name = "Grpsdailytmp.findByTrdate", query = "SELECT g FROM Grpsdailytmp g WHERE g.grpsdailytmpPK.trdate = :trdate")
    , @NamedQuery(name = "Grpsdailytmp.findBySeries", query = "SELECT g FROM Grpsdailytmp g WHERE g.grpsdailytmpPK.series = :series")
    , @NamedQuery(name = "Grpsdailytmp.findByType", query = "SELECT g FROM Grpsdailytmp g WHERE g.grpsdailytmpPK.type = :type")
    , @NamedQuery(name = "Grpsdailytmp.findByIntertraGrp", query = "SELECT g FROM Grpsdailytmp g WHERE g.grpsdailytmpPK.intertraGrp = :intertraGrp")
    , @NamedQuery(name = "Grpsdailytmp.findBySaleqty", query = "SELECT g FROM Grpsdailytmp g WHERE g.saleqty = :saleqty")
    , @NamedQuery(name = "Grpsdailytmp.findBySaleamtfs", query = "SELECT g FROM Grpsdailytmp g WHERE g.saleamtfs = :saleamtfs")
    , @NamedQuery(name = "Grpsdailytmp.findByOrderqty", query = "SELECT g FROM Grpsdailytmp g WHERE g.orderqty = :orderqty")
    , @NamedQuery(name = "Grpsdailytmp.findByOrderamtfs", query = "SELECT g FROM Grpsdailytmp g WHERE g.orderamtfs = :orderamtfs")
    , @NamedQuery(name = "Grpsdailytmp.findByCoin", query = "SELECT g FROM Grpsdailytmp g WHERE g.coin = :coin")
    , @NamedQuery(name = "Grpsdailytmp.findByCreatetime", query = "SELECT g FROM Grpsdailytmp g WHERE g.createtime = :createtime")
    , @NamedQuery(name = "Grpsdailytmp.findByArea", query = "SELECT g FROM Grpsdailytmp g WHERE g.area = :area")})
public class Grpsdailytmp implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "saleqty")
    private BigDecimal saleqty;
    @Basic(optional = false)
    @NotNull
    @Column(name = "saleamtfs")
    private BigDecimal saleamtfs;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "orderqty")
    private BigDecimal orderqty;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orderamtfs")
    private BigDecimal orderamtfs;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "coin")
    private String coin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "createtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @Size(max = 8)
    @Column(name = "area")
    private String area;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GrpsdailytmpPK grpsdailytmpPK;

    public Grpsdailytmp() {
        saleqty = BigDecimal.ZERO;
        saleamtfs = BigDecimal.ZERO;
        orderqty = BigDecimal.ZERO;
        orderamtfs = BigDecimal.ZERO;
        coin = "";
        createtime = null;
        area = "";
    }

    public Grpsdailytmp(GrpsdailytmpPK grpsdailytmpPK) {
        this.grpsdailytmpPK = grpsdailytmpPK;
    }

    public Grpsdailytmp(String hangrp, String trdate, String series, String type, String intertraGrp) {
        this.grpsdailytmpPK = new GrpsdailytmpPK(hangrp, trdate, series, type, intertraGrp);
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        saleqty = BigDecimal.ZERO;
        saleamtfs = BigDecimal.ZERO;
        orderqty = BigDecimal.ZERO;
        orderamtfs = BigDecimal.ZERO;
        coin = "RMB";
        createtime = timestamp;
        area = "";
    }

    public GrpsdailytmpPK getGrpsdailytmpPK() {
        return grpsdailytmpPK;
    }

    public void setGrpsdailytmpPK(GrpsdailytmpPK grpsdailytmpPK) {
        this.grpsdailytmpPK = grpsdailytmpPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (grpsdailytmpPK != null ? grpsdailytmpPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grpsdailytmp)) {
            return false;
        }
        Grpsdailytmp other = (Grpsdailytmp) object;
        if ((this.grpsdailytmpPK == null && other.grpsdailytmpPK != null) || (this.grpsdailytmpPK != null && !this.grpsdailytmpPK.equals(other.grpsdailytmpPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.erp.Grpsdailytmp[ grpsdailytmpPK=" + grpsdailytmpPK + " ]";
    }

    public BigDecimal getSaleqty() {
        return saleqty;
    }

    public void setSaleqty(BigDecimal saleqty) {
        this.saleqty = saleqty;
    }

    public BigDecimal getSaleamtfs() {
        return saleamtfs;
    }

    public void setSaleamtfs(BigDecimal saleamtfs) {
        this.saleamtfs = saleamtfs;
    }

    public BigDecimal getOrderqty() {
        return orderqty;
    }

    public void setOrderqty(BigDecimal orderqty) {
        this.orderqty = orderqty;
    }

    public BigDecimal getOrderamtfs() {
        return orderamtfs;
    }

    public void setOrderamtfs(BigDecimal orderamtfs) {
        this.orderamtfs = orderamtfs;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

}
