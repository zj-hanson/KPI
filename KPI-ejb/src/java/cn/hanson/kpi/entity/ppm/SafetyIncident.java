/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author FredJie
 */
@Entity
@Table(name = "safety_incident")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SafetyIncident.findAll", query = "SELECT s FROM SafetyIncident s"),
    @NamedQuery(name = "SafetyIncident.findById", query = "SELECT s FROM SafetyIncident s WHERE s.id = :id"),
    @NamedQuery(name = "SafetyIncident.findByCompany", query = "SELECT s FROM SafetyIncident s WHERE s.company = :company"),
    @NamedQuery(name = "SafetyIncident.findByIncidentId", query = "SELECT s FROM SafetyIncident s WHERE s.incidentId = :incidentId"),
    @NamedQuery(name = "SafetyIncident.findByIncidentDate", query = "SELECT s FROM SafetyIncident s WHERE s.incidentDate = :incidentDate"),
    @NamedQuery(name = "SafetyIncident.findByIncidentTime", query = "SELECT s FROM SafetyIncident s WHERE s.incidentTime = :incidentTime"),
    @NamedQuery(name = "SafetyIncident.findByTitle", query = "SELECT s FROM SafetyIncident s WHERE s.title = :title"),
    @NamedQuery(name = "SafetyIncident.findByAddress", query = "SELECT s FROM SafetyIncident s WHERE s.address = :address"),
    @NamedQuery(name = "SafetyIncident.findByDeptId", query = "SELECT s FROM SafetyIncident s WHERE s.deptId = :deptId"),
    @NamedQuery(name = "SafetyIncident.findByDept", query = "SELECT s FROM SafetyIncident s WHERE s.dept = :dept"),
    @NamedQuery(name = "SafetyIncident.findByContent", query = "SELECT s FROM SafetyIncident s WHERE s.content = :content"),
    @NamedQuery(name = "SafetyIncident.findByDamage", query = "SELECT s FROM SafetyIncident s WHERE s.damage = :damage"),
    @NamedQuery(name = "SafetyIncident.findByDamageAmount", query = "SELECT s FROM SafetyIncident s WHERE s.damageAmount = :damageAmount"),
    @NamedQuery(name = "SafetyIncident.findByResponsibilityOrganization", query = "SELECT s FROM SafetyIncident s WHERE s.responsibilityOrganization = :responsibilityOrganization"),
    @NamedQuery(name = "SafetyIncident.findByResponsibilityDeptId", query = "SELECT s FROM SafetyIncident s WHERE s.responsibilityDeptId = :responsibilityDeptId"),
    @NamedQuery(name = "SafetyIncident.findByResponsibilityDept", query = "SELECT s FROM SafetyIncident s WHERE s.responsibilityDept = :responsibilityDept"),
    @NamedQuery(name = "SafetyIncident.findByResponsibilityEmployeeId", query = "SELECT s FROM SafetyIncident s WHERE s.responsibilityEmployeeId = :responsibilityEmployeeId"),
    @NamedQuery(name = "SafetyIncident.findByResponsibilityEmployee", query = "SELECT s FROM SafetyIncident s WHERE s.responsibilityEmployee = :responsibilityEmployee"),
    @NamedQuery(name = "SafetyIncident.findByPunishmentKind", query = "SELECT s FROM SafetyIncident s WHERE s.punishmentKind = :punishmentKind"),
    @NamedQuery(name = "SafetyIncident.findByPunishmentStandard", query = "SELECT s FROM SafetyIncident s WHERE s.punishmentStandard = :punishmentStandard"),
    @NamedQuery(name = "SafetyIncident.findByPunishmentAmount", query = "SELECT s FROM SafetyIncident s WHERE s.punishmentAmount = :punishmentAmount"),
    @NamedQuery(name = "SafetyIncident.findBySummary", query = "SELECT s FROM SafetyIncident s WHERE s.summary = :summary"),
    @NamedQuery(name = "SafetyIncident.findByMeasure", query = "SELECT s FROM SafetyIncident s WHERE s.measure = :measure"),
    @NamedQuery(name = "SafetyIncident.findByNotifyGroup", query = "SELECT s FROM SafetyIncident s WHERE s.notifyGroup = :notifyGroup"),
    @NamedQuery(name = "SafetyIncident.findByInjury", query = "SELECT s FROM SafetyIncident s WHERE s.injury = :injury"),
    @NamedQuery(name = "SafetyIncident.findByRemark", query = "SELECT s FROM SafetyIncident s WHERE s.remark = :remark"),
    @NamedQuery(name = "SafetyIncident.findByUid", query = "SELECT s FROM SafetyIncident s WHERE s.uid = :uid"),
    @NamedQuery(name = "SafetyIncident.findByStatus", query = "SELECT s FROM SafetyIncident s WHERE s.status = :status"),
    @NamedQuery(name = "SafetyIncident.findByCreator", query = "SELECT s FROM SafetyIncident s WHERE s.creator = :creator"),
    @NamedQuery(name = "SafetyIncident.findByCredate", query = "SELECT s FROM SafetyIncident s WHERE s.credate = :credate"),
    @NamedQuery(name = "SafetyIncident.findByOptuser", query = "SELECT s FROM SafetyIncident s WHERE s.optuser = :optuser"),
    @NamedQuery(name = "SafetyIncident.findByOptdate", query = "SELECT s FROM SafetyIncident s WHERE s.optdate = :optdate"),
    @NamedQuery(name = "SafetyIncident.findByCfmuser", query = "SELECT s FROM SafetyIncident s WHERE s.cfmuser = :cfmuser"),
    @NamedQuery(name = "SafetyIncident.findByCfmdate", query = "SELECT s FROM SafetyIncident s WHERE s.cfmdate = :cfmdate")})
public class SafetyIncident implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "incident_id")
    private String incidentId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "incident_date")
    @Temporal(TemporalType.DATE)
    private Date incidentDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "incident_time")
    @Temporal(TemporalType.TIME)
    private Date incidentTime;
    @Size(max = 45)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "address")
    private String address;
    @Size(max = 20)
    @Column(name = "dept_id")
    private String deptId;
    @Size(max = 45)
    @Column(name = "dept")
    private String dept;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "content")
    private String content;
    @Size(max = 200)
    @Column(name = "damage")
    private String damage;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "damage_amount")
    private BigDecimal damageAmount;
    @Size(max = 45)
    @Column(name = "responsibility_organization")
    private String responsibilityOrganization;
    @Size(max = 10)
    @Column(name = "responsibility_dept_id")
    private String responsibilityDeptId;
    @Size(max = 20)
    @Column(name = "responsibility_dept")
    private String responsibilityDept;
    @Size(max = 10)
    @Column(name = "responsibility_employee_id")
    private String responsibilityEmployeeId;
    @Size(max = 20)
    @Column(name = "responsibility_employee")
    private String responsibilityEmployee;
    @Size(max = 45)
    @Column(name = "punishment_kind")
    private String punishmentKind;
    @Size(max = 45)
    @Column(name = "punishment_standard")
    private String punishmentStandard;
    @Column(name = "punishment_amount")
    private BigDecimal punishmentAmount;
    @Size(max = 200)
    @Column(name = "summary")
    private String summary;
    @Size(max = 200)
    @Column(name = "measure")
    private String measure;
    @Column(name = "notify_group")
    private Short notifyGroup;
    @Column(name = "injury")
    private Short injury;
    @Size(max = 100)
    @Column(name = "remark")
    private String remark;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
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

    public SafetyIncident() {
    }

    public SafetyIncident(Integer id) {
        this.id = id;
    }

    public SafetyIncident(Integer id, String company, String incidentId, Date incidentDate, Date incidentTime, String address, String content, String uid, String status) {
        this.id = id;
        this.company = company;
        this.incidentId = incidentId;
        this.incidentDate = incidentDate;
        this.incidentTime = incidentTime;
        this.address = address;
        this.content = content;
        this.uid = uid;
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

    public String getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(String incidentId) {
        this.incidentId = incidentId;
    }

    public Date getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(Date incidentDate) {
        this.incidentDate = incidentDate;
    }

    public Date getIncidentTime() {
        return incidentTime;
    }

    public void setIncidentTime(Date incidentTime) {
        this.incidentTime = incidentTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public BigDecimal getDamageAmount() {
        return damageAmount;
    }

    public void setDamageAmount(BigDecimal damageAmount) {
        this.damageAmount = damageAmount;
    }

    public String getResponsibilityOrganization() {
        return responsibilityOrganization;
    }

    public void setResponsibilityOrganization(String responsibilityOrganization) {
        this.responsibilityOrganization = responsibilityOrganization;
    }

    public String getResponsibilityDeptId() {
        return responsibilityDeptId;
    }

    public void setResponsibilityDeptId(String responsibilityDeptId) {
        this.responsibilityDeptId = responsibilityDeptId;
    }

    public String getResponsibilityDept() {
        return responsibilityDept;
    }

    public void setResponsibilityDept(String responsibilityDept) {
        this.responsibilityDept = responsibilityDept;
    }

    public String getResponsibilityEmployeeId() {
        return responsibilityEmployeeId;
    }

    public void setResponsibilityEmployeeId(String responsibilityEmployeeId) {
        this.responsibilityEmployeeId = responsibilityEmployeeId;
    }

    public String getResponsibilityEmployee() {
        return responsibilityEmployee;
    }

    public void setResponsibilityEmployee(String responsibilityEmployee) {
        this.responsibilityEmployee = responsibilityEmployee;
    }

    public String getPunishmentKind() {
        return punishmentKind;
    }

    public void setPunishmentKind(String punishmentKind) {
        this.punishmentKind = punishmentKind;
    }

    public String getPunishmentStandard() {
        return punishmentStandard;
    }

    public void setPunishmentStandard(String punishmentStandard) {
        this.punishmentStandard = punishmentStandard;
    }

    public BigDecimal getPunishmentAmount() {
        return punishmentAmount;
    }

    public void setPunishmentAmount(BigDecimal punishmentAmount) {
        this.punishmentAmount = punishmentAmount;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Short getNotifyGroup() {
        return notifyGroup;
    }

    public void setNotifyGroup(Short notifyGroup) {
        this.notifyGroup = notifyGroup;
    }

    public Short getInjury() {
        return injury;
    }

    public void setInjury(Short injury) {
        this.injury = injury;
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
        if (!(object instanceof SafetyIncident)) {
            return false;
        }
        SafetyIncident other = (SafetyIncident) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanson.kpi.entity.ppm.SafetyIncident[ id=" + id + " ]";
    }
    
}
