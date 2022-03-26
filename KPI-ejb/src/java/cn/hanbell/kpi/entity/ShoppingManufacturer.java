/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@Entity
@Table(name = "shoppingmanufacturer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ShoppingManufacturer.findAll", query = "SELECT s FROM ShoppingManufacturer s"),
    @NamedQuery(name = "ShoppingManufacturer.findById", query = "SELECT s FROM ShoppingManufacturer s WHERE s.id = :id"),
    @NamedQuery(name = "ShoppingManufacturer.findByVdrno", query = "SELECT s FROM ShoppingManufacturer s WHERE s.vdrno = :vdrno"),
    @NamedQuery(name = "ShoppingManufacturer.findByVdrna", query = "SELECT s FROM ShoppingManufacturer s WHERE s.vdrna = :vdrna"),
    @NamedQuery(name = "ShoppingManufacturer.findByFacno", query = "SELECT s FROM ShoppingManufacturer s WHERE s.facno = :facno"),
    @NamedQuery(name = "ShoppingManufacturer.findByFacnoAndMaterialTypeName", query = "SELECT s FROM ShoppingManufacturer s WHERE s.facno = :facno and s.materialTypeName in(:materialTypeName)"),
    @NamedQuery(name = "ShoppingManufacturer.findByUserno", query = "SELECT s FROM ShoppingManufacturer s WHERE s.userno = :userno"),
    @NamedQuery(name = "ShoppingManufacturer.findByUserna", query = "SELECT s FROM ShoppingManufacturer s WHERE s.userna = :userna"),
    @NamedQuery(name = "ShoppingManufacturer.findByStatus", query = "SELECT s FROM ShoppingManufacturer s WHERE s.status = :status"),
    @NamedQuery(name = "ShoppingManufacturer.findByCreator", query = "SELECT s FROM ShoppingManufacturer s WHERE s.creator = :creator"),
    @NamedQuery(name = "ShoppingManufacturer.findByCredate", query = "SELECT s FROM ShoppingManufacturer s WHERE s.credate = :credate"),
    @NamedQuery(name = "ShoppingManufacturer.findByOptuser", query = "SELECT s FROM ShoppingManufacturer s WHERE s.optuser = :optuser"),
    @NamedQuery(name = "ShoppingManufacturer.findByOptdate", query = "SELECT s FROM ShoppingManufacturer s WHERE s.optdate = :optdate"),
    @NamedQuery(name = "ShoppingManufacturer.findByCfmuser", query = "SELECT s FROM ShoppingManufacturer s WHERE s.cfmuser = :cfmuser"),
    @NamedQuery(name = "ShoppingManufacturer.findByCfmdate", query = "SELECT s FROM ShoppingManufacturer s WHERE s.cfmdate = :cfmdate"),
    @NamedQuery(name = "ShoppingManufacturer.findByVdrnoAndFacno", query = "SELECT s FROM ShoppingManufacturer s WHERE s.vdrno = :vdrno and s.facno = :facno")})

public class ShoppingManufacturer extends SuperEntity {

    private static final long serialVersionUID = 1L;

    @Size(max = 8)
    @Column(name = "vdrno")
    private String vdrno;
    @Size(max = 30)
    @Column(name = "vdrna")
    private String vdrna;
    @Size(max = 8)
    @Column(name = "facno")
    private String facno;
    @Size(max = 30)
    @Column(name = "materialTypeName")
    private String materialTypeName;
    @Size(max = 5)
    @Column(name = "userno")
    private String userno;
    @Size(max = 30)
    @Column(name = "userna")
    private String userna;

    public ShoppingManufacturer() {
    }

    public ShoppingManufacturer(Integer id) {
        this.id = id;
    }

    public String getVdrno() {
        return vdrno;
    }

    public void setVdrno(String vdrno) {
        this.vdrno = vdrno;
    }

    public String getVdrna() {
        return vdrna;
    }

    public void setVdrna(String vdrna) {
        this.vdrna = vdrna;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

    public String getMaterialTypeName() {
        return materialTypeName;
    }

    public void setMaterialTypeName(String materialTypeName) {
        this.materialTypeName = materialTypeName;
    }

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public String getUserna() {
        return userna;
    }

    public void setUserna(String userna) {
        this.userna = userna;
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
        if (!(object instanceof ShoppingManufacturer)) {
            return false;
        }
        ShoppingManufacturer other = (ShoppingManufacturer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.ShoppingManufacturer[ id=" + id + " ]";
    }

}
