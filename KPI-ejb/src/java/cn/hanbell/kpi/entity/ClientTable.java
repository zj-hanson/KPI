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
 * @author C1879
 */
@Entity
@Table(name = "clienttable")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClientTable.findAll", query = "SELECT c FROM ClientTable c")
    , @NamedQuery(name = "ClientTable.findById", query = "SELECT c FROM ClientTable c WHERE c.id = :id")
    , @NamedQuery(name = "ClientTable.findByFacno", query = "SELECT c FROM ClientTable c WHERE c.facno = :facno")
    , @NamedQuery(name = "ClientTable.findByCusno", query = "SELECT c FROM ClientTable c WHERE c.cusno = :cusno")
    , @NamedQuery(name = "ClientTable.findByCusna", query = "SELECT c FROM ClientTable c WHERE c.cusna = :cusna")
    , @NamedQuery(name = "ClientTable.findByShipmentsyear", query = "SELECT c FROM ClientTable c WHERE c.shipmentsyear = :shipmentsyear")
    , @NamedQuery(name = "ClientTable.findByShipmentmonth", query = "SELECT c FROM ClientTable c WHERE c.shipmentmonth = :shipmentmonth")
    , @NamedQuery(name = "ClientTable.findByDeptno", query = "SELECT c FROM ClientTable c WHERE c.deptno = :deptno")
    , @NamedQuery(name = "ClientTable.findByQuantity", query = "SELECT c FROM ClientTable c WHERE c.quantity = :quantity")
    , @NamedQuery(name = "ClientTable.findByAmount", query = "SELECT c FROM ClientTable c WHERE c.amount = :amount")
    , @NamedQuery(name = "ClientTable.findByNcodeDA", query = "SELECT c FROM ClientTable c WHERE c.ncodeDA = :ncodeDA")
    , @NamedQuery(name = "ClientTable.findByNcodeDC", query = "SELECT c FROM ClientTable c WHERE c.ncodeDC = :ncodeDC")
    , @NamedQuery(name = "ClientTable.findByNcodeCD", query = "SELECT c FROM ClientTable c WHERE c.ncodeCD = :ncodeCD")
    , @NamedQuery(name = "ClientTable.findByRemark1", query = "SELECT c FROM ClientTable c WHERE c.remark1 = :remark1")
    , @NamedQuery(name = "ClientTable.findByRemark2", query = "SELECT c FROM ClientTable c WHERE c.remark2 = :remark2")
    , @NamedQuery(name = "ClientTable.findByRemark3", query = "SELECT c FROM ClientTable c WHERE c.remark3 = :remark3")
    , @NamedQuery(name = "ClientTable.findByRemark4", query = "SELECT c FROM ClientTable c WHERE c.remark4 = :remark4")
})
public class ClientTable extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "facno")
    private String facno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "cusno")
    private String cusno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "cusna")
    private String cusna;
    @Basic(optional = false)
    @NotNull
    @Column(name = "shipmentsyear")
    private int shipmentsyear;
    @Basic(optional = false)
    @NotNull
    @Column(name = "shipmentmonth")
    private int shipmentmonth;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "deptno")
    private String deptno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount")
    private BigDecimal amount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "n_code_DA")
    private String ncodeDA;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "n_code_DC")
    private String ncodeDC;
    @Size(max = 10)
    @Column(name = "n_code_CD")
    private String ncodeCD;
    @Size(max = 45)
    @Column(name = "remark1")
    private String remark1;
    @Size(max = 45)
    @Column(name = "remark2")
    private String remark2;
    @Size(max = 45)
    @Column(name = "remark3")
    private String remark3;
    @Size(max = 45)
    @Column(name = "remark4")
    private String remark4;

    public ClientTable() {
    }

    public ClientTable(Integer id) {
        this.id = id;
    }

    public ClientTable(Integer id, String facno, String cusno, String cusna, int shipmentsyear, int shipmentmonth, String deptno, int quantity, BigDecimal amount, String ncodeDA, String ncodeDC, String status) {
        this.id = id;
        this.facno = facno;
        this.cusno = cusno;
        this.cusna = cusna;
        this.shipmentsyear = shipmentsyear;
        this.shipmentmonth = shipmentmonth;
        this.deptno = deptno;
        this.quantity = quantity;
        this.amount = amount;
        this.ncodeDA = ncodeDA;
        this.ncodeDC = ncodeDC;
        this.status = status;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
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

    public int getShipmentsyear() {
        return shipmentsyear;
    }

    public void setShipmentsyear(int shipmentsyear) {
        this.shipmentsyear = shipmentsyear;
    }

    public int getShipmentmonth() {
        return shipmentmonth;
    }

    public void setShipmentmonth(int shipmentmonth) {
        this.shipmentmonth = shipmentmonth;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNcodeDA() {
        return ncodeDA;
    }

    public void setNcodeDA(String ncodeDA) {
        this.ncodeDA = ncodeDA;
    }

    public String getNcodeDC() {
        return ncodeDC;
    }

    public void setNcodeDC(String ncodeDC) {
        this.ncodeDC = ncodeDC;
    }

    public String getNcodeCD() {
        return ncodeCD;
    }

    public void setNcodeCD(String ncodeCD) {
        this.ncodeCD = ncodeCD;
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

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public String getRemark4() {
        return remark4;
    }

    public void setRemark4(String remark4) {
        this.remark4 = remark4;
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
        if (!(object instanceof ClientTable)) {
            return false;
        }
        ClientTable other = (ClientTable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.ClientTable[ id=" + id + " ]";
    }
    
}
