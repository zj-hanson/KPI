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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "category")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Category.getRowCount", query = "SELECT COUNT(p) FROM Category p"),
    @NamedQuery(name = "Category.findAll", query = "SELECT p FROM Category p"),
    @NamedQuery(name = "Category.findById", query = "SELECT p FROM Category p WHERE p.id = :id"),
    @NamedQuery(name = "Category.findByCategory", query = "SELECT p FROM Category p WHERE p.category = :category"),
    @NamedQuery(name = "Category.findByPId", query = "SELECT p FROM Category p WHERE p.parent.id = :pid ORDER BY p.sortid"),
    @NamedQuery(name = "Category.findRootByCompany", query = "SELECT p FROM Category p WHERE  p.company = :company AND p.parent IS NULL ORDER BY p.sortid ASC"),
    @NamedQuery(name = "Category.findByStatus", query = "SELECT p FROM Category p WHERE p.status = :status")})
public class Category extends SuperEntity {

    @JoinColumn(name = "pid", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Category parent;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "company")
    private String company;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "category")
    private String category;
    @Column(name = "sortid")
    private int sortid;
    @Size(max = 200)
    @Column(name = "remark")
    private String remark;

    public Category() {
        sortid = 0;
    }

    public Category(String company, String category) {
        this.company = company;
        this.category = category;
    }

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.Category[ id=" + id + " ]";
    }

    /**
     * @return the parent
     */
    public Category getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Category parent) {
        this.parent = parent;
    }

}
