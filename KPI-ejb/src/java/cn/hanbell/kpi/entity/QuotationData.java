/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
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
 * @author C0160
 */
@Entity
@Table(name = "quotationdata")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "QuotationData.findAll", query = "SELECT q FROM QuotationData q"),
    @NamedQuery(name = "QuotationData.findById", query = "SELECT q FROM QuotationData q WHERE q.id = :id"),
    @NamedQuery(name = "QuotationData.findByFormid", query = "SELECT q FROM QuotationData q WHERE q.formid = :formid"),
    @NamedQuery(name = "QuotationData.findByCustomerno",
        query = "SELECT q FROM QuotationData q WHERE q.customerno = :customerno"),
    @NamedQuery(name = "QuotationData.findByCustomer",
        query = "SELECT q FROM QuotationData q WHERE q.customer = :customer"),
    @NamedQuery(name = "QuotationData.findBySalesman",
        query = "SELECT q FROM QuotationData q WHERE q.salesman = :salesman"),
    @NamedQuery(name = "QuotationData.findBySalesmanName",
        query = "SELECT q FROM QuotationData q WHERE q.salesmanName = :salesmanName"),
    @NamedQuery(name = "QuotationData.findByStartDate",
        query = "SELECT q FROM QuotationData q WHERE q.startDate = :startDate"),
    @NamedQuery(name = "QuotationData.findByEndDate",
        query = "SELECT q FROM QuotationData q WHERE q.endDate = :endDate"),
    @NamedQuery(name = "QuotationData.findByCustomerProduct",
        query = "SELECT q FROM QuotationData q WHERE q.customerProduct = :customerProduct"),
    @NamedQuery(name = "QuotationData.findByCustomerItemno",
        query = "SELECT q FROM QuotationData q WHERE q.customerItemno = :customerItemno"),
    @NamedQuery(name = "QuotationData.findByCustomerItemDesc",
        query = "SELECT q FROM QuotationData q WHERE q.customerItemDesc = :customerItemDesc"),
    @NamedQuery(name = "QuotationData.findByCustomerItemSpec",
        query = "SELECT q FROM QuotationData q WHERE q.customerItemSpec = :customerItemSpec"),
    @NamedQuery(name = "QuotationData.findByItemno", query = "SELECT q FROM QuotationData q WHERE q.itemno = :itemno"),
    @NamedQuery(name = "QuotationData.findByItemDesc",
        query = "SELECT q FROM QuotationData q WHERE q.itemDesc = :itemDesc"),
    @NamedQuery(name = "QuotationData.findByItemSpec",
        query = "SELECT q FROM QuotationData q WHERE q.itemSpec = :itemSpec"),
    @NamedQuery(name = "QuotationData.findByItemModel",
        query = "SELECT q FROM QuotationData q WHERE q.itemModel = :itemModel"),
    @NamedQuery(name = "QuotationData.findByStatus", query = "SELECT q FROM QuotationData q WHERE q.status = :status")})
public class QuotationData extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "company")
    private String company;
    @Size(max = 20)
    @Column(name = "formid")
    private String formid;
    @Column(name = "formdate")
    @Temporal(TemporalType.DATE)
    private Date formdate;
    @Size(max = 20)
    @Column(name = "customerno")
    private String customerno;
    @Size(max = 45)
    @Column(name = "customer")
    private String customer;
    @Size(max = 20)
    @Column(name = "salesman")
    private String salesman;
    @Size(max = 45)
    @Column(name = "salesmanName")
    private String salesmanName;
    @Column(name = "startDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "seq")
    private Integer seq;
    @Size(max = 45)
    @Column(name = "customerProduct")
    private String customerProduct;
    @Size(max = 45)
    @Column(name = "customerItemno")
    private String customerItemno;
    @Size(max = 100)
    @Column(name = "customerItemDesc")
    private String customerItemDesc;
    @Size(max = 100)
    @Column(name = "customerItemSpec")
    private String customerItemSpec;
    @Size(max = 45)
    @Column(name = "itemno")
    private String itemno;
    @Size(max = 100)
    @Column(name = "itemDesc")
    private String itemDesc;
    @Size(max = 100)
    @Column(name = "itemSpec")
    private String itemSpec;
    @Size(max = 45)
    @Column(name = "itemModel")
    private String itemModel;
    @Size(max = 10)
    @Column(name = "unit")
    private String unit;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce
    // field validation
    @Column(name = "unitPrice")
    private BigDecimal unitPrice;
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "materialPrice")
    private BigDecimal materialPrice;
    @Column(name = "processingPrice")
    private BigDecimal processingPrice;

    public QuotationData() {
    }

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company
     *                    the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    public Date getFormdate() {
        return formdate;
    }

    public void setFormdate(Date formdate) {
        this.formdate = formdate;
    }

    public String getCustomerno() {
        return customerno;
    }

    public void setCustomerno(String customerno) {
        this.customerno = customerno;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getCustomerProduct() {
        return customerProduct;
    }

    public void setCustomerProduct(String customerProduct) {
        this.customerProduct = customerProduct;
    }

    public String getCustomerItemno() {
        return customerItemno;
    }

    public void setCustomerItemno(String customerItemno) {
        this.customerItemno = customerItemno;
    }

    public String getCustomerItemDesc() {
        return customerItemDesc;
    }

    public void setCustomerItemDesc(String customerItemDesc) {
        this.customerItemDesc = customerItemDesc;
    }

    public String getCustomerItemSpec() {
        return customerItemSpec;
    }

    public void setCustomerItemSpec(String customerItemSpec) {
        this.customerItemSpec = customerItemSpec;
    }

    public String getItemno() {
        return itemno;
    }

    public void setItemno(String itemno) {
        this.itemno = itemno;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemSpec() {
        return itemSpec;
    }

    public void setItemSpec(String itemSpec) {
        this.itemSpec = itemSpec;
    }

    public String getItemModel() {
        return itemModel;
    }

    public void setItemModel(String itemModel) {
        this.itemModel = itemModel;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(BigDecimal materialPrice) {
        this.materialPrice = materialPrice;
    }

    public BigDecimal getProcessingPrice() {
        return processingPrice;
    }

    public void setProcessingPrice(BigDecimal processingPrice) {
        this.processingPrice = processingPrice;
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
        if (!(object instanceof QuotationData)) {
            return false;
        }
        QuotationData other = (QuotationData)object;
        if (this.itemno != null && other.itemno != null) {
            return this.itemno.equals(other.itemno);
        }

        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.QuotationData[ id=" + id + " ]";
    }

}
