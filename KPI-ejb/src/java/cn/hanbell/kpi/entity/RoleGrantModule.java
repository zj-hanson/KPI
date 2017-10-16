/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
import java.util.Objects;
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
@Table(name = "rolegrantmodule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RoleGrantModule.getRowCount", query = "SELECT COUNT(s) FROM RoleGrantModule s"),
    @NamedQuery(name = "RoleGrantModule.findAll", query = "SELECT s FROM RoleGrantModule s"),
    @NamedQuery(name = "RoleGrantModule.findById", query = "SELECT s FROM RoleGrantModule s WHERE s.id = :id"),
    @NamedQuery(name = "RoleGrantModule.findByKind", query = "SELECT s FROM RoleGrantModule s WHERE s.kind = :kind"),
    @NamedQuery(name = "RoleGrantModule.findByRoleId", query = "SELECT s FROM RoleGrantModule s WHERE s.systemRole.id = :roleid ORDER BY s.deptno "),
    @NamedQuery(name = "RoleGrantModule.findByStatus", query = "SELECT s FROM RoleGrantModule s WHERE s.status = :status")})
public class RoleGrantModule extends SuperEntity {

    @JoinColumn(name = "roleid", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Role systemRole;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "deptno")
    private String deptno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "dept")
    private String dept;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "kind")
    private String kind;

    public RoleGrantModule() {
        kind = "U";
    }

    /**
     * @return the deptno
     */
    public String getDeptno() {
        return deptno;
    }

    /**
     * @return the dept
     */
    public String getDept() {
        return dept;
    }

    /**
     * @param deptno the deptno to set
     */
    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    /**
     * @param dept the dept to set
     */
    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
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
        if (!(object instanceof RoleGrantModule)) {
            return false;
        }
        RoleGrantModule other = (RoleGrantModule) object;
        if (this.id != null && other.id != null) {
            return Objects.equals(this.id, other.id);
        }
        if ((!this.kind.equals(other.kind)) || !Objects.equals(this.systemRole, other.systemRole)) {
            return false;
        }
        if ((this.kind.equals(other.kind)) && (Objects.equals(this.systemRole, other.systemRole)) && (Objects.equals(this.deptno, other.deptno))) {
            return true;
        }
        if ((this.kind.equals(other.kind)) && (Objects.equals(this.systemRole, other.systemRole)) && (!Objects.equals(this.deptno, other.deptno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.eap.entity.RoleGrantModule[ id=" + id + " ]";
    }

    /**
     * @return the systemRole
     */
    public Role getSystemRole() {
        return systemRole;
    }

    /**
     * @param systemRole the systemRole to set
     */
    public void setSystemRole(Role systemRole) {
        this.systemRole = systemRole;
    }

}
