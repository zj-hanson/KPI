/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
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
 * @author C1879
 */
@Entity
@Table(name = "exchangerate")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExchangeRate.findAll", query = "SELECT e FROM ExchangeRate e"),
    @NamedQuery(name = "ExchangeRate.findById", query = "SELECT e FROM ExchangeRate e WHERE e.id = :id"),
    @NamedQuery(name = "ExchangeRate.findByFacno", query = "SELECT e FROM ExchangeRate e WHERE e.facno = :facno"),
    @NamedQuery(name = "ExchangeRate.findByCoin", query = "SELECT e FROM ExchangeRate e WHERE e.coin = :coin"),
    @NamedQuery(name = "ExchangeRate.findByRateday", query = "SELECT e FROM ExchangeRate e WHERE e.rateday = :rateday"),
    @NamedQuery(name = "ExchangeRate.findByRate", query = "SELECT e FROM ExchangeRate e WHERE e.rate = :rate"),
    @NamedQuery(name = "ExchangeRate.findByCoinna", query = "SELECT e FROM ExchangeRate e WHERE e.coinna = :coinna"),
    @NamedQuery(name = "ExchangeRate.findByExcoin", query = "SELECT e FROM ExchangeRate e WHERE e.excoin = :excoin"),
    @NamedQuery(name = "ExchangeRate.findByExcoinna", query = "SELECT e FROM ExchangeRate e WHERE e.excoinna = :excoinna"),
    @NamedQuery(name = "ExchangeRate.findByExchangena", query = "SELECT e FROM ExchangeRate e WHERE e.exchangena = :exchangena"),
    @NamedQuery(name = "ExchangeRate.findByRpttype", query = "SELECT e FROM ExchangeRate e WHERE e.rpttype = :rpttype"),
    @NamedQuery(name = "ExchangeRate.findByQueryDateBegin", query = "SELECT e FROM ExchangeRate e WHERE e.rateday >= :ratedayBegin "),
    @NamedQuery(name = "ExchangeRate.findByQueryDateEnd", query = "SELECT e FROM ExchangeRate e WHERE e.rateday <= :ratedayEnd ")
})

public class ExchangeRate extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "facno")
    private String facno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "coin")
    private String coin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rateday")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rateday;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "rate")
    private BigDecimal rate;
    @Size(max = 20)
    @Column(name = "coinna")
    private String coinna;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "excoin")
    private String excoin;
    @Size(max = 20)
    @Column(name = "excoinna")
    private String excoinna;
    @Size(max = 40)
    @Column(name = "exchangena")
    private String exchangena;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "rpttype")
    private String rpttype;

    public ExchangeRate() {
    }

    public ExchangeRate(Integer id) {
        this.id = id;
    }

    public ExchangeRate(Integer id, String facno, String coin, Date rateday, BigDecimal rate, String excoin, String rpttype) {
        this.id = id;
        this.facno = facno;
        this.coin = coin;
        this.rateday = rateday;
        this.rate = rate;
        this.excoin = excoin;
        this.rpttype = rpttype;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public Date getRateday() {
        return rateday;
    }

    public void setRateday(Date rateday) {
        this.rateday = rateday;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getCoinna() {
        return coinna;
    }

    public void setCoinna(String coinna) {
        this.coinna = coinna;
    }

    public String getExcoin() {
        return excoin;
    }

    public void setExcoin(String excoin) {
        this.excoin = excoin;
    }

    public String getExcoinna() {
        return excoinna;
    }

    public void setExcoinna(String excoinna) {
        this.excoinna = excoinna;
    }

    public String getExchangena() {
        return exchangena;
    }

    public void setExchangena(String exchangena) {
        this.exchangena = exchangena;
    }

    public String getRpttype() {
        return rpttype;
    }

    public void setRpttype(String rpttype) {
        this.rpttype = rpttype;
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
        if (!(object instanceof ExchangeRate)) {
            return false;
        }
        ExchangeRate other = (ExchangeRate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.ExchangeRate[ id=" + id + " ]";
    }

}
