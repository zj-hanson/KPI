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
@Table(name = "role")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Role.getRowCount", query = "SELECT COUNT(s) FROM Role s"),
    @NamedQuery(name = "Role.findAll", query = "SELECT s FROM Role s"),
    @NamedQuery(name = "Role.findById", query = "SELECT s FROM Role s WHERE s.id = :id"),
    @NamedQuery(name = "Role.findByRoleno", query = "SELECT s FROM Role s WHERE s.roleno = :roleno"),
    @NamedQuery(name = "Role.findByRolename", query = "SELECT s FROM Role s WHERE s.rolename = :rolename"),
    @NamedQuery(name = "Role.findByStatus", query = "SELECT s FROM Role s WHERE s.status = :status")})
public class Role extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "roleno")
    private String roleno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "rolename")
    private String rolename;

    public Role() {
    }

    public String getRoleno() {
        return roleno;
    }

    public void setRoleno(String roleno) {
        this.roleno = roleno;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
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
        if (!(object instanceof Role)) {
            return false;
        }
        Role other = (Role) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.eap.entity.Role[ id=" + id + " ]";
    }

}
