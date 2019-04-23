package cn.hanbell.kpi.entity.crm;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author C1879
 */
@Entity
@Table(name = "DSALP")
@NamedQueries({
    @NamedQuery(name = "DSALP.findAll", query = "SELECT d FROM DSALP d")
    ,
    @NamedQuery(name = "DSALP.findByParams", query = "SELECT d FROM DSALP d WHERE d.ds003= :type AND d.ds005= :userid AND d.ds015= :DA AND d.ds006 >= :datebegin AND d.ds006<= :dateend ")
})
public class DSALP implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "DS001")
    private BigDecimal ds001;
    @Size(max = 20)
    @Column(name = "COMPANY")
    private String company;
    @Size(max = 10)
    @Column(name = "CREATOR")
    private String creator;
    @Size(max = 10)
    @Column(name = "USR_GROUP")
    private String usrGroup;
    @Size(max = 8)
    @Column(name = "CREATE_DATE")
    private String createDate;
    @Size(max = 10)
    @Column(name = "MODIFIER")
    private String modifier;
    @Size(max = 8)
    @Column(name = "MODI_DATE")
    private String modiDate;
    @Column(name = "FLAG")
    private Short flag;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation

    @Size(max = 3)
    @Column(name = "DS002")
    private String ds002;
    @Size(max = 20)
    @Column(name = "DS003")
    private String ds003;
    @Size(max = 8)
    @Column(name = "DS004")
    private String ds004;
    @Size(max = 8)
    @Column(name = "DS005")
    private String ds005;
    @Size(max = 8)
    @Column(name = "DS006")
    private String ds006;
    @Size(max = 20)
    @Column(name = "DS007")
    private String ds007;
    @Size(max = 8)
    @Column(name = "DS008")
    private String ds008;
    @Size(max = 30)
    @Column(name = "DS009")
    private String ds009;
    @Size(max = 8)
    @Column(name = "DS010")
    private String ds010;
    @Column(name = "DS011")
    private BigDecimal ds011;
    @Column(name = "DS012")
    private BigDecimal ds012;
    @Column(name = "DS013")
    private BigDecimal ds013;
    @Column(name = "DS014")
    private BigDecimal ds014;
    @Size(max = 8)
    @Column(name = "DS015")
    private String ds015;
    @Size(max = 8)
    @Column(name = "DS016")
    private String ds016;
    @Size(max = 8)
    @Column(name = "DS017")
    private String ds017;
    @Size(max = 8)
    @Column(name = "DS018")
    private String ds018;
    @Size(max = 30)
    @Column(name = "DS019")
    private String ds019;
    @Size(max = 8)
    @Column(name = "DS020")
    private String ds020;
    @Size(max = 255)
    @Column(name = "DS021")
    private String ds021;
    @Size(max = 255)
    @Column(name = "DS022")
    private String ds022;
    @Size(max = 255)
    @Column(name = "DS023")
    private String ds023;

    public DSALP() {
    }

    public DSALP(BigDecimal ds001) {
        this.ds001 = ds001;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUsrGroup() {
        return usrGroup;
    }

    public void setUsrGroup(String usrGroup) {
        this.usrGroup = usrGroup;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate;
    }

    public Short getFlag() {
        return flag;
    }

    public void setFlag(Short flag) {
        this.flag = flag;
    }

    public BigDecimal getDs001() {
        return ds001;
    }

    public void setDs001(BigDecimal ds001) {
        this.ds001 = ds001;
    }

    public String getDs002() {
        return ds002;
    }

    public void setDs002(String ds002) {
        this.ds002 = ds002;
    }

    public String getDs003() {
        return ds003;
    }

    public void setDs003(String ds003) {
        this.ds003 = ds003;
    }

    public String getDs004() {
        return ds004;
    }

    public void setDs004(String ds004) {
        this.ds004 = ds004;
    }

    public String getDs005() {
        return ds005;
    }

    public void setDs005(String ds005) {
        this.ds005 = ds005;
    }

    public String getDs006() {
        return ds006;
    }

    public void setDs006(String ds006) {
        this.ds006 = ds006;
    }

    public String getDs007() {
        return ds007;
    }

    public void setDs007(String ds007) {
        this.ds007 = ds007;
    }

    public String getDs008() {
        return ds008;
    }

    public void setDs008(String ds008) {
        this.ds008 = ds008;
    }

    public String getDs009() {
        return ds009;
    }

    public void setDs009(String ds009) {
        this.ds009 = ds009;
    }

    public String getDs010() {
        return ds010;
    }

    public void setDs010(String ds010) {
        this.ds010 = ds010;
    }

    public BigDecimal getDs011() {
        return ds011;
    }

    public void setDs011(BigDecimal ds011) {
        this.ds011 = ds011;
    }

    public BigDecimal getDs012() {
        return ds012;
    }

    public void setDs012(BigDecimal ds012) {
        this.ds012 = ds012;
    }

    public BigDecimal getDs013() {
        return ds013;
    }

    public void setDs013(BigDecimal ds013) {
        this.ds013 = ds013;
    }

    public BigDecimal getDs014() {
        return ds014;
    }

    public void setDs014(BigDecimal ds014) {
        this.ds014 = ds014;
    }

    public String getDs015() {
        return ds015;
    }

    public void setDs015(String ds015) {
        this.ds015 = ds015;
    }

    public String getDs016() {
        return ds016;
    }

    public void setDs016(String ds016) {
        this.ds016 = ds016;
    }

    public String getDs017() {
        return ds017;
    }

    public void setDs017(String ds017) {
        this.ds017 = ds017;
    }

    public String getDs018() {
        return ds018;
    }

    public void setDs018(String ds018) {
        this.ds018 = ds018;
    }

    public String getDs019() {
        return ds019;
    }

    public void setDs019(String ds019) {
        this.ds019 = ds019;
    }

    public String getDs020() {
        return ds020;
    }

    public void setDs020(String ds020) {
        this.ds020 = ds020;
    }

    public String getDs021() {
        return ds021;
    }

    public void setDs021(String ds021) {
        this.ds021 = ds021;
    }

    public String getDs022() {
        return ds022;
    }

    public void setDs022(String ds022) {
        this.ds022 = ds022;
    }

    public String getDs023() {
        return ds023;
    }

    public void setDs023(String ds023) {
        this.ds023 = ds023;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ds001 != null ? ds001.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DSALP)) {
            return false;
        }
        DSALP other = (DSALP) object;
        if ((this.ds001 == null && other.ds001 != null) || (this.ds001 != null && !this.ds001.equals(other.ds001))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.crm.entity.DSALP[ ds001=" + ds001 + " ]";
    }

}
