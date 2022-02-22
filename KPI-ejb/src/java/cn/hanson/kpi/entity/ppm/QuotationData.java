/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */

package cn.hanson.kpi.entity.ppm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "quotation_data")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuotationData.findAll", query = "SELECT q FROM QuotationData q"),
    @NamedQuery(name = "QuotationData.findById", query = "SELECT q FROM QuotationData q WHERE q.id = :id"),
    @NamedQuery(name = "QuotationData.findByUID", query = "SELECT q FROM QuotationData q WHERE q.uid = :uid"),
    @NamedQuery(name = "QuotationData.findByStatus", query = "SELECT q FROM QuotationData q WHERE q.status = :status")})
public class QuotationData implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "company")
    private String company;
    @Size(max = 20)
    @Column(name = "form_id")
    private String formId;
    @Column(name = "form_date")
    @Temporal(TemporalType.DATE)
    private Date formDate;
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
    @Column(name = "salesman_name")
    private String salesmanName;
    @Size(max = 10)
    @Column(name = "currency")
    private String currency;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "exchange")
    private BigDecimal exchange;
    @Size(max = 10)
    @Column(name = "tax")
    private String tax;
    @Column(name = "tax_rate")
    private BigDecimal taxRate;
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "seq")
    private Integer seq;
    @Size(max = 45)
    @Column(name = "customer_product")
    private String customerProduct;
    @Size(max = 45)
    @Column(name = "customer_itemno")
    private String customerItemno;
    @Size(max = 100)
    @Column(name = "customer_item_desc")
    private String customerItemDesc;
    @Size(max = 100)
    @Column(name = "customer_item_spec")
    private String customerItemSpec;
    @Size(max = 45)
    @Column(name = "itemno")
    private String itemno;
    @Size(max = 100)
    @Column(name = "item_desc")
    private String itemDesc;
    @Size(max = 100)
    @Column(name = "item_spec")
    private String itemSpec;
    @Size(max = 45)
    @Column(name = "item_model")
    private String itemModel;
    @Column(name = "weight")
    private BigDecimal weight;
    @Size(max = 10)
    @Column(name = "unit")
    private String unit;
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @Column(name = "additional_price")
    private BigDecimal additionalPrice;
    @Column(name = "material_price")
    private BigDecimal materialPrice;
    @Column(name = "processing_price")
    private BigDecimal processingPrice;
    @Column(name = "labor_price")
    private BigDecimal laborPrice;
    @Column(name = "other_price")
    private BigDecimal otherPrice;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @Column(name = "qty")
    private BigDecimal qty;
    @Size(max = 10)
    @Column(name = "sales_unit")
    private String salesUnit;
    @Size(max = 100)
    @Column(name = "remark")
    private String remark;
    @Size(max = 45)
    @Column(name = "uid")
    private String uid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "status")
    private String status;
    @Size(max = 20)
    @Column(name = "creator")
    private String creator;
    @Column(name = "credate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date credate;
    @Size(max = 20)
    @Column(name = "optuser")
    private String optuser;
    @Column(name = "optdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date optdate;
    @Size(max = 20)
    @Column(name = "cfmuser")
    private String cfmuser;
    @Column(name = "cfmdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cfmdate;

    public QuotationData() {
    }

    public QuotationData(Integer id) {
        this.id = id;
    }

    public QuotationData(Integer id, String company, String status) {
        this.id = id;
        this.company = company;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Date getFormDate() {
        return formDate;
    }

    public void setFormDate(Date formDate) {
        this.formDate = formDate;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getExchange() {
        return exchange;
    }

    public void setExchange(BigDecimal exchange) {
        this.exchange = exchange;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
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

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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

    public BigDecimal getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(BigDecimal additionalPrice) {
        this.additionalPrice = additionalPrice;
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

    public BigDecimal getLaborPrice() {
        return laborPrice;
    }

    public void setLaborPrice(BigDecimal laborPrice) {
        this.laborPrice = laborPrice;
    }

    public BigDecimal getOtherPrice() {
        return otherPrice;
    }

    public void setOtherPrice(BigDecimal otherPrice) {
        this.otherPrice = otherPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getSalesUnit() {
        return salesUnit;
    }

    public void setSalesUnit(String salesUnit) {
        this.salesUnit = salesUnit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCredate() {
        return credate;
    }

    public void setCredate(Date credate) {
        this.credate = credate;
    }

    public String getOptuser() {
        return optuser;
    }

    public void setOptuser(String optuser) {
        this.optuser = optuser;
    }

    public Date getOptdate() {
        return optdate;
    }

    public void setOptdate(Date optdate) {
        this.optdate = optdate;
    }

    public String getCfmuser() {
        return cfmuser;
    }

    public void setCfmuser(String cfmuser) {
        this.cfmuser = cfmuser;
    }

    public Date getCfmdate() {
        return cfmdate;
    }

    public void setCfmdate(Date cfmdate) {
        this.cfmdate = cfmdate;
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
        QuotationData other = (QuotationData) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanson.kpi.entity.ppm.QuotationData[ id=" + id + " ]";
    }

}
