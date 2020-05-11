/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperDetailEntity;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
@Table(name = "indicatordetail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicatorDetail.findAll", query = "SELECT i FROM IndicatorDetail i"),
    @NamedQuery(name = "IndicatorDetail.findById", query = "SELECT i FROM IndicatorDetail i WHERE i.id = :id"),
    @NamedQuery(name = "IndicatorDetail.findByPId", query = "SELECT i FROM IndicatorDetail i WHERE i.pid = :pid")})
public class IndicatorDetail extends SuperDetailEntity {

    @JoinColumn(name = "pid", referencedColumnName = "id", updatable = false, insertable = false)
    @ManyToOne(optional = false)
    private Indicator parent;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "type")
    private String type;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
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
    @Column(name = "nq1")
    private BigDecimal nq1;
    @Column(name = "nq2")
    private BigDecimal nq2;
    @Column(name = "nq3")
    private BigDecimal nq3;
    @Column(name = "nq4")
    private BigDecimal nq4;
    @Column(name = "nh1")
    private BigDecimal nh1;
    @Column(name = "nh2")
    private BigDecimal nh2;
    @Column(name = "nfy")
    private BigDecimal nfy;

    public IndicatorDetail() {
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
        this.nq1 = BigDecimal.ZERO;
        this.nq2 = BigDecimal.ZERO;
        this.nq3 = BigDecimal.ZERO;
        this.nq4 = BigDecimal.ZERO;
        this.nh1 = BigDecimal.ZERO;
        this.nh2 = BigDecimal.ZERO;
        this.nfy = BigDecimal.ZERO;
    }

    /**
     * @return the parent
     */
    public Indicator getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Indicator parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getN01() {
        return n01;
    }

    public void setN01(BigDecimal n01) {
        this.n01 = n01;
        this.calcMonth();
    }

    public BigDecimal getN02() {
        return n02;
    }

    public void setN02(BigDecimal n02) {
        this.n02 = n02;
        this.calcMonth();
    }

    public BigDecimal getN03() {
        return n03;
    }

    public void setN03(BigDecimal n03) {
        this.n03 = n03;
        this.calcMonth();
    }

    public BigDecimal getN04() {
        return n04;
    }

    public void setN04(BigDecimal n04) {
        this.n04 = n04;
        this.calcMonth();
    }

    public BigDecimal getN05() {
        return n05;
    }

    public void setN05(BigDecimal n05) {
        this.n05 = n05;
        this.calcMonth();
    }

    public BigDecimal getN06() {
        return n06;
    }

    public void setN06(BigDecimal n06) {
        this.n06 = n06;
        this.calcMonth();
    }

    public BigDecimal getN07() {
        return n07;
    }

    public void setN07(BigDecimal n07) {
        this.n07 = n07;
        this.calcMonth();
    }

    public BigDecimal getN08() {
        return n08;
    }

    public void setN08(BigDecimal n08) {
        this.n08 = n08;
        this.calcMonth();
    }

    public BigDecimal getN09() {
        return n09;
    }

    public void setN09(BigDecimal n09) {
        this.n09 = n09;
        this.calcMonth();
    }

    public BigDecimal getN10() {
        return n10;
    }

    public void setN10(BigDecimal n10) {
        this.n10 = n10;
        this.calcMonth();
    }

    public BigDecimal getN11() {
        return n11;
    }

    public void setN11(BigDecimal n11) {
        this.n11 = n11;
        this.calcMonth();
    }

    public BigDecimal getN12() {
        return n12;
    }

    public void setN12(BigDecimal n12) {
        this.n12 = n12;
        this.calcMonth();
    }

    public BigDecimal getNq1() {
        return nq1;
    }

    public void setNq1(BigDecimal nq1) {
        this.nq1 = nq1;
        this.calcQuarter();
    }

    public BigDecimal getNq2() {
        return nq2;
    }

    public void setNq2(BigDecimal nq2) {
        this.nq2 = nq2;
        this.calcQuarter();
    }

    public BigDecimal getNq3() {
        return nq3;
    }

    public void setNq3(BigDecimal nq3) {
        this.nq3 = nq3;
        this.calcQuarter();
    }

    public BigDecimal getNq4() {
        return nq4;
    }

    public void setNq4(BigDecimal nq4) {
        this.nq4 = nq4;
        this.calcQuarter();
    }

    public BigDecimal getNh1() {
        return nh1;
    }

    public void setNh1(BigDecimal nh1) {
        this.nh1 = nh1;
        this.calcHalfYear();
    }

    public BigDecimal getNh2() {
        return nh2;
    }

    public void setNh2(BigDecimal nh2) {
        this.nh2 = nh2;
        this.calcHalfYear();
    }

    public BigDecimal getNfy() {
        return nfy;
    }

    public void setNfy(BigDecimal nfy) {
        this.nfy = nfy;
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
        if (!(object instanceof IndicatorDetail)) {
            return false;
        }
        IndicatorDetail other = (IndicatorDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.IndicatorDetail[ id=" + id + " ]";
    }

    public void calcMonth() {
        if (!this.type.equals("P") && this.getParent().getValueMode().equals("S")) {
            this.nq1 = this.n01.add(this.n02).add(this.n03);
            this.nq2 = this.n04.add(this.n05).add(this.n06);
            this.nq3 = this.n07.add(this.n08).add(this.n09);
            this.nq4 = this.n10.add(this.n11).add(this.n12);
            calcQuarter();
        }
        if (!this.type.equals("P") && this.getParent().getValueMode().equals("A")) {
            this.nq1 = this.n01.add(this.n02).add(this.n03).divide(BigDecimal.valueOf(3d), 2, RoundingMode.HALF_UP);
            this.nq2 = this.n04.add(this.n05).add(this.n06).divide(BigDecimal.valueOf(3d), 2, RoundingMode.HALF_UP);
            this.nq3 = this.n07.add(this.n08).add(this.n09).divide(BigDecimal.valueOf(3d), 2, RoundingMode.HALF_UP);
            this.nq4 = this.n10.add(this.n11).add(this.n12).divide(BigDecimal.valueOf(3d), 2, RoundingMode.HALF_UP);
            calcQuarter();
        }
    }

    public void calcQuarter() {
        if (!this.type.equals("P") && this.getParent().getValueMode().equals("S")) {
            this.nh1 = this.nq1.add(this.nq2);
            this.nh2 = this.nq3.add(this.nq4);
            calcHalfYear();
        }
        if (!this.type.equals("P") && this.getParent().getValueMode().equals("A")) {
            if (this.nq2.compareTo(BigDecimal.ZERO) > 0) {
                this.nh1 = this.nq1.add(this.nq2).divide(BigDecimal.valueOf(2d), 2, RoundingMode.HALF_UP);
            } else {
                this.nh1 = this.nq1;
            }
            if (this.nq4.compareTo(BigDecimal.ZERO) > 0) {
                this.nh2 = this.nq3.add(this.nq4).divide(BigDecimal.valueOf(2d), 2, RoundingMode.HALF_UP);
            } else {
                this.nh2 = this.nq3;
            }
            calcHalfYear();
        }
    }

    public void calcHalfYear() {
        if (!this.type.equals("P") && this.getParent().getValueMode().equals("S")) {
            this.nfy = this.nh1.add(this.nh2);
        }
        if (!this.type.equals("P") && this.getParent().getValueMode().equals("A")) {
            if (this.nh2.compareTo(BigDecimal.ZERO) > 0) {
                this.nfy = this.nh1.add(this.nh2).divide(BigDecimal.valueOf(2d), 2, RoundingMode.HALF_UP);
            } else {
                this.nfy = this.nh1;
            }
        }
    }

    //增加合计值
    public BigDecimal nsy() {
        BigDecimal a = BigDecimal.ZERO;
        a = this.n01.add(this.n02).add(this.n03).add(this.n04).add(this.n05).add(this.n06).add(this.n07).add(this.n08).add(this.n09).add(this.n10).add(this.n11).add(this.n12);
        return a;
    }

    public void initialize() {
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
        this.nq1 = BigDecimal.ZERO;
        this.nq2 = BigDecimal.ZERO;
        this.nq3 = BigDecimal.ZERO;
        this.nq4 = BigDecimal.ZERO;
        this.nh1 = BigDecimal.ZERO;
        this.nh2 = BigDecimal.ZERO;
        this.nfy = BigDecimal.ZERO;
    }

}
