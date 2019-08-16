/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C1749
 */
@Entity
@Table(name = "inventorydepartment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InventoryDepartment.findAll", query = "SELECT i FROM InventoryDepartment i")
    ,
    @NamedQuery(name = "InventoryDepartment.findByFacno", query = "SELECT i FROM InventoryDepartment i WHERE i.inventoryDepartmentPK.facno = :facno")
    ,
    @NamedQuery(name = "InventoryDepartment.findByProno", query = "SELECT i FROM InventoryDepartment i WHERE i.inventoryDepartmentPK.prono = :prono")
    ,
    @NamedQuery(name = "InventoryDepartment.findByCreyear", query = "SELECT i FROM InventoryDepartment i WHERE i.inventoryDepartmentPK.creyear = :creyear")
    ,
    @NamedQuery(name = "InventoryDepartment.findByWareh", query = "SELECT i FROM InventoryDepartment i WHERE i.inventoryDepartmentPK.wareh = :wareh")
    ,
    @NamedQuery(name = "InventoryDepartment.findByWhdsc", query = "SELECT i FROM InventoryDepartment i WHERE i.inventoryDepartmentPK.whdsc = :whdsc")
    ,
    @NamedQuery(name = "InventoryDepartment.findByCategories", query = "SELECT i FROM InventoryDepartment i WHERE i.inventoryDepartmentPK.categories = :categories")
    ,
    @NamedQuery(name = "InventoryDepartment.findByGenre", query = "SELECT i FROM InventoryDepartment i WHERE i.inventoryDepartmentPK.genre = :genre")
    ,
    @NamedQuery(name = "InventoryDepartment.findByN01", query = "SELECT i FROM InventoryDepartment i WHERE i.n01 = :n01")
    ,
    @NamedQuery(name = "InventoryDepartment.findByN02", query = "SELECT i FROM InventoryDepartment i WHERE i.n02 = :n02")
    ,
    @NamedQuery(name = "InventoryDepartment.findByN03", query = "SELECT i FROM InventoryDepartment i WHERE i.n03 = :n03")
    ,
    @NamedQuery(name = "InventoryDepartment.findByN04", query = "SELECT i FROM InventoryDepartment i WHERE i.n04 = :n04")
    ,
    @NamedQuery(name = "InventoryDepartment.findByN05", query = "SELECT i FROM InventoryDepartment i WHERE i.n05 = :n05")
    ,
    @NamedQuery(name = "InventoryDepartment.findByN06", query = "SELECT i FROM InventoryDepartment i WHERE i.n06 = :n06")
    ,
    @NamedQuery(name = "InventoryDepartment.findByN07", query = "SELECT i FROM InventoryDepartment i WHERE i.n07 = :n07")
    ,
    @NamedQuery(name = "InventoryDepartment.findByN08", query = "SELECT i FROM InventoryDepartment i WHERE i.n08 = :n08")
    ,
    @NamedQuery(name = "InventoryDepartment.findByN09", query = "SELECT i FROM InventoryDepartment i WHERE i.n09 = :n09")
    ,
    @NamedQuery(name = "InventoryDepartment.findByN10", query = "SELECT i FROM InventoryDepartment i WHERE i.n10 = :n10")
    ,
    @NamedQuery(name = "InventoryDepartment.findByN11", query = "SELECT i FROM InventoryDepartment i WHERE i.n11 = :n11")
    ,
    @NamedQuery(name = "InventoryDepartment.findByN12", query = "SELECT i FROM InventoryDepartment i WHERE i.n12 = :n12")
    ,
    @NamedQuery(name = "InventoryDepartment.findByDifference", query = "SELECT i FROM InventoryDepartment i WHERE i.difference = :difference")
    ,
    @NamedQuery(name = "InventoryDepartment.findByProportion", query = "SELECT i FROM InventoryDepartment i WHERE i.proportion = :proportion")
    ,
    @NamedQuery(name = "InventoryDepartment.findByPK", query = "SELECT i FROM InventoryDepartment i WHERE i.inventoryDepartmentPK.facno = :facno and i.inventoryDepartmentPK.prono = :prono and i.inventoryDepartmentPK.creyear = :creyear and "
            + "i.inventoryDepartmentPK.wareh = :wareh and i.inventoryDepartmentPK.whdsc = :whdsc and i.inventoryDepartmentPK.categories = :categories and i.inventoryDepartmentPK.genre = :genre")})
public class InventoryDepartment implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InventoryDepartmentPK inventoryDepartmentPK;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Column(name = "n01")
    private BigDecimal n01;
    @Column(name = "n02")
    private BigDecimal n02;
    @Column(name = "n03")
    private BigDecimal n03;
    @Column(name = "n04")
    private BigDecimal n04;
    @Column(name = "n05")
    private BigDecimal n05;
    @Column(name = "n06")
    private BigDecimal n06;
    @Column(name = "n07")
    private BigDecimal n07;
    @Column(name = "n08")
    private BigDecimal n08;
    @Column(name = "n09")
    private BigDecimal n09;
    @Column(name = "n10")
    private BigDecimal n10;
    @Column(name = "n11")
    private BigDecimal n11;
    @Column(name = "n12")
    private BigDecimal n12;
    @Column(name = "difference")
    private BigDecimal difference;
    @Column(name = "proportion")
    private BigDecimal proportion;

    public InventoryDepartment() {
        
    }

    public InventoryDepartment(InventoryDepartmentPK inventoryDepartmentPK) {
        this.inventoryDepartmentPK = inventoryDepartmentPK;
    }

    public InventoryDepartment(String facno, String prono, String creyear, String wareh, String whdsc, String categories,
            String genre) {
        this.inventoryDepartmentPK = new InventoryDepartmentPK(facno, prono, creyear, wareh, whdsc, categories, genre);
        this.n01 = BigDecimal.ZERO;
        this.n02 = BigDecimal.ZERO;
        this.n03 = BigDecimal.ZERO;
        this.n04 = BigDecimal.ZERO;
        this.n05 = BigDecimal.ZERO;
        this.n06 = BigDecimal.ZERO;
        this.n07 = BigDecimal.ZERO;
        this.n08 = BigDecimal.ZERO;
        this.n09 = BigDecimal.ZERO;
        this.n10 = BigDecimal.ZERO;
        this.n11 = BigDecimal.ZERO;
        this.n12 = BigDecimal.ZERO;
    }

    public InventoryDepartmentPK getInventoryDepartmentPK() {
        return inventoryDepartmentPK;
    }

    public void setInventoryDepartmentPK(InventoryDepartmentPK inventoryDepartmentPK) {
        this.inventoryDepartmentPK = inventoryDepartmentPK;
    }

    public BigDecimal getN01() {
        return n01;
    }

    public void setN01(BigDecimal n01) {
        this.n01 = n01;
    }

    public BigDecimal getN02() {
        return n02;
    }

    public void setN02(BigDecimal n02) {
        this.n02 = n02;
    }

    public BigDecimal getN03() {
        return n03;
    }

    public void setN03(BigDecimal n03) {
        this.n03 = n03;
    }

    public BigDecimal getN04() {
        return n04;
    }

    public void setN04(BigDecimal n04) {
        this.n04 = n04;
    }

    public BigDecimal getN05() {
        return n05;
    }

    public void setN05(BigDecimal n05) {
        this.n05 = n05;
    }

    public BigDecimal getN06() {
        return n06;
    }

    public void setN06(BigDecimal n06) {
        this.n06 = n06;
    }

    public BigDecimal getN07() {
        return n07;
    }

    public void setN07(BigDecimal n07) {
        this.n07 = n07;
    }

    public BigDecimal getN08() {
        return n08;
    }

    public void setN08(BigDecimal n08) {
        this.n08 = n08;
    }

    public BigDecimal getN09() {
        return n09;
    }

    public void setN09(BigDecimal n09) {
        this.n09 = n09;
    }

    public BigDecimal getN10() {
        return n10;
    }

    public void setN10(BigDecimal n10) {
        this.n10 = n10;
    }

    public BigDecimal getN11() {
        return n11;
    }

    public void setN11(BigDecimal n11) {
        this.n11 = n11;
    }

    public BigDecimal getN12() {
        return n12;
    }

    public void setN12(BigDecimal n12) {
        this.n12 = n12;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public BigDecimal getProportion() {
        return proportion;
    }

    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (inventoryDepartmentPK != null ? inventoryDepartmentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InventoryDepartment)) {
            return false;
        }
        InventoryDepartment other = (InventoryDepartment) object;
        if ((this.inventoryDepartmentPK == null && other.inventoryDepartmentPK != null)
                || (this.inventoryDepartmentPK != null && !this.inventoryDepartmentPK.equals(other.inventoryDepartmentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.InventoryDepartment[ inventoryDepartmentPK=" + inventoryDepartmentPK + " ]";
    }

}
