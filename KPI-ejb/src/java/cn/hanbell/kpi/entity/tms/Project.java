/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity.tms;

import com.lightshell.comm.SuperEntity;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author C1749
 */
@Entity
@Table(name = "Project")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Project.findAll", query = "SELECT p.projectSeq,p.projectName,p.pMUserId,p.pMUser FROM Project p"),
    @NamedQuery(name = "Project.findByProjectSeq", query = "SELECT p.ph FROM Project p WHERE p.projectSeq = :projectSeq")})
public class Project extends SuperEntity{

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Id")
    private String id;
    @Column(name = "ProjectSeq")
    private Integer projectSeq;
    @Size(max = 20)
    @Column(name = "ProjectNo")
    private String projectNo;
    @Size(max = 64)
    @Column(name = "ProjectName")
    private String projectName;
    @Size(max = 128)
    @Column(name = "ProjectKey")
    private String projectKey;
    @Column(name = "StartDateSch")
    @Temporal(TemporalType.DATE)
    private Date startDateSch;
    @Column(name = "EndDateSch")
    @Temporal(TemporalType.DATE)
    private Date endDateSch;
    @Size(max = 32)
    @Column(name = "State")
    private String state;
    @Size(max = 20)
    @Column(name = "PMUserId")
    private String pMUserId;
    @Size(max = 20)
    @Column(name = "PMUser")
    private String pMUser;
    @Column(name = "OverdueQty")
    private Integer overdueQty;
    @Column(name = "WaitReply")
    private Integer waitReply;
    @Size(max = 20)
    @Column(name = "FactoryName")
    private String factoryName;
    @Size(max = 40)
    @Column(name = "ProductType")
    private String productType;
    @Column(name = "Notice_PreDay")
    private Integer noticePreDay;
    @Column(name = "Notice_FreqDay")
    private Integer noticeFreqDay;
    @Column(name = "UpdateTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Size(max = 20)
    @Column(name = "ModifyUserId")
    private String modifyUserId;
    @Size(max = 20)
    @Column(name = "ModifyUser")
    private String modifyUser;
    @Column(name = "ModifyTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    @Column(name = "Ph")
    private Integer ph;
    @Column(name = "Ph01")
    private Integer ph01;
    @Column(name = "Ph02")
    private Integer ph02;
    @Column(name = "Ph03")
    private Integer ph03;
    @Column(name = "Ph04")
    private Integer ph04;
    @Column(name = "Ph05")
    private Integer ph05;
    @Column(name = "Ph06")
    private Integer ph06;
    @Column(name = "Ph07")
    private Integer ph07;
    @Column(name = "Ph08")
    private Integer ph08;
    @Column(name = "DR")
    private Integer dr;
    @Column(name = "DR01")
    private Integer dr01;
    @Column(name = "DR02")
    private Integer dr02;
    @Column(name = "DR03")
    private Integer dr03;
    @Column(name = "DR04")
    private Integer dr04;
    @Column(name = "DR05")
    private Integer dr05;
    @Column(name = "DR06")
    private Integer dr06;

    public Project() {
    }

//    public Project(String id) {
//        this.id = id;
//    }
//
//    public String getId() {
//        return id;
//    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getProjectSeq() {
        return projectSeq;
    }

    public void setProjectSeq(Integer projectSeq) {
        this.projectSeq = projectSeq;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public Date getStartDateSch() {
        return startDateSch;
    }

    public void setStartDateSch(Date startDateSch) {
        this.startDateSch = startDateSch;
    }

    public Date getEndDateSch() {
        return endDateSch;
    }

    public void setEndDateSch(Date endDateSch) {
        this.endDateSch = endDateSch;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPMUserId() {
        return pMUserId;
    }

    public void setPMUserId(String pMUserId) {
        this.pMUserId = pMUserId;
    }

    public String getPMUser() {
        return pMUser;
    }

    public void setPMUser(String pMUser) {
        this.pMUser = pMUser;
    }

    public Integer getOverdueQty() {
        return overdueQty;
    }

    public void setOverdueQty(Integer overdueQty) {
        this.overdueQty = overdueQty;
    }

    public Integer getWaitReply() {
        return waitReply;
    }

    public void setWaitReply(Integer waitReply) {
        this.waitReply = waitReply;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getNoticePreDay() {
        return noticePreDay;
    }

    public void setNoticePreDay(Integer noticePreDay) {
        this.noticePreDay = noticePreDay;
    }

    public Integer getNoticeFreqDay() {
        return noticeFreqDay;
    }

    public void setNoticeFreqDay(Integer noticeFreqDay) {
        this.noticeFreqDay = noticeFreqDay;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getPh() {
        return ph;
    }

    public void setPh(Integer ph) {
        this.ph = ph;
    }

    public Integer getPh01() {
        return ph01;
    }

    public void setPh01(Integer ph01) {
        this.ph01 = ph01;
    }

    public Integer getPh02() {
        return ph02;
    }

    public void setPh02(Integer ph02) {
        this.ph02 = ph02;
    }

    public Integer getPh03() {
        return ph03;
    }

    public void setPh03(Integer ph03) {
        this.ph03 = ph03;
    }

    public Integer getPh04() {
        return ph04;
    }

    public void setPh04(Integer ph04) {
        this.ph04 = ph04;
    }

    public Integer getPh05() {
        return ph05;
    }

    public void setPh05(Integer ph05) {
        this.ph05 = ph05;
    }

    public Integer getPh06() {
        return ph06;
    }

    public void setPh06(Integer ph06) {
        this.ph06 = ph06;
    }

    public Integer getPh07() {
        return ph07;
    }

    public void setPh07(Integer ph07) {
        this.ph07 = ph07;
    }

    public Integer getPh08() {
        return ph08;
    }

    public void setPh08(Integer ph08) {
        this.ph08 = ph08;
    }

    public Integer getDr() {
        return dr;
    }

    public void setDr(Integer dr) {
        this.dr = dr;
    }

    public Integer getDr01() {
        return dr01;
    }

    public void setDr01(Integer dr01) {
        this.dr01 = dr01;
    }

    public Integer getDr02() {
        return dr02;
    }

    public void setDr02(Integer dr02) {
        this.dr02 = dr02;
    }

    public Integer getDr03() {
        return dr03;
    }

    public void setDr03(Integer dr03) {
        this.dr03 = dr03;
    }

    public Integer getDr04() {
        return dr04;
    }

    public void setDr04(Integer dr04) {
        this.dr04 = dr04;
    }

    public Integer getDr05() {
        return dr05;
    }

    public void setDr05(Integer dr05) {
        this.dr05 = dr05;
    }

    public Integer getDr06() {
        return dr06;
    }

    public void setDr06(Integer dr06) {
        this.dr06 = dr06;
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
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.tms.Project[ id=" + id + " ]";
    }
    
}
