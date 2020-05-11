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
 * @author C1879
 */
@Entity
@Table(name = "indicatordaily")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicatorDaily.findAll", query = "SELECT i FROM IndicatorDaily i"),
    @NamedQuery(name = "IndicatorDaily.findById", query = "SELECT i FROM IndicatorDaily i WHERE i.id = :id"),
    @NamedQuery(name = "IndicatorDaily.findByPidDateAndType", query = "SELECT i FROM IndicatorDaily i WHERE i.pid = :pid AND i.seq = :seq AND i.mth = :mth and i.type = :type "),
    @NamedQuery(name = "IndicatorDaily.findByPidAndSeq", query = "SELECT i FROM IndicatorDaily i WHERE i.pid = :pid AND i.seq = :seq ")
})
public class IndicatorDaily extends SuperDetailEntity {

    @JoinColumn(name = "pid", referencedColumnName = "id", updatable = false, insertable = false)
    @ManyToOne(optional = false)
    protected IndicatorDetail parent;

    @Basic(optional = false)
    @NotNull
    @Column(name = "mth")
    private int mth;
    @Size(max = 10)
    @Column(name = "type")
    private String type;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "d01")
    private BigDecimal d01;
    @Column(name = "d02")
    private BigDecimal d02;
    @Column(name = "d03")
    private BigDecimal d03;
    @Column(name = "d04")
    private BigDecimal d04;
    @Column(name = "d05")
    private BigDecimal d05;
    @Column(name = "d06")
    private BigDecimal d06;
    @Column(name = "d07")
    private BigDecimal d07;
    @Column(name = "d08")
    private BigDecimal d08;
    @Column(name = "d09")
    private BigDecimal d09;
    @Column(name = "d10")
    private BigDecimal d10;
    @Column(name = "d11")
    private BigDecimal d11;
    @Column(name = "d12")
    private BigDecimal d12;
    @Column(name = "d13")
    private BigDecimal d13;
    @Column(name = "d14")
    private BigDecimal d14;
    @Column(name = "d15")
    private BigDecimal d15;
    @Column(name = "d16")
    private BigDecimal d16;
    @Column(name = "d17")
    private BigDecimal d17;
    @Column(name = "d18")
    private BigDecimal d18;
    @Column(name = "d19")
    private BigDecimal d19;
    @Column(name = "d20")
    private BigDecimal d20;
    @Column(name = "d21")
    private BigDecimal d21;
    @Column(name = "d22")
    private BigDecimal d22;
    @Column(name = "d23")
    private BigDecimal d23;
    @Column(name = "d24")
    private BigDecimal d24;
    @Column(name = "d25")
    private BigDecimal d25;
    @Column(name = "d26")
    private BigDecimal d26;
    @Column(name = "d27")
    private BigDecimal d27;
    @Column(name = "d28")
    private BigDecimal d28;
    @Column(name = "d29")
    private BigDecimal d29;
    @Column(name = "d30")
    private BigDecimal d30;
    @Column(name = "d31")
    private BigDecimal d31;
    @Column(name = "total")
    private BigDecimal total;

    public IndicatorDaily() {
        this.d01 = BigDecimal.ZERO;
        this.d02 = BigDecimal.ZERO;
        this.d03 = BigDecimal.ZERO;
        this.d04 = BigDecimal.ZERO;
        this.d05 = BigDecimal.ZERO;
        this.d06 = BigDecimal.ZERO;
        this.d07 = BigDecimal.ZERO;
        this.d08 = BigDecimal.ZERO;
        this.d09 = BigDecimal.ZERO;
        this.d10 = BigDecimal.ZERO;
        this.d11 = BigDecimal.ZERO;
        this.d12 = BigDecimal.ZERO;
        this.d13 = BigDecimal.ZERO;
        this.d14 = BigDecimal.ZERO;
        this.d15 = BigDecimal.ZERO;
        this.d16 = BigDecimal.ZERO;
        this.d17 = BigDecimal.ZERO;
        this.d18 = BigDecimal.ZERO;
        this.d19 = BigDecimal.ZERO;
        this.d20 = BigDecimal.ZERO;
        this.d21 = BigDecimal.ZERO;
        this.d22 = BigDecimal.ZERO;
        this.d23 = BigDecimal.ZERO;
        this.d24 = BigDecimal.ZERO;
        this.d25 = BigDecimal.ZERO;
        this.d26 = BigDecimal.ZERO;
        this.d27 = BigDecimal.ZERO;
        this.d28 = BigDecimal.ZERO;
        this.d29 = BigDecimal.ZERO;
        this.d30 = BigDecimal.ZERO;
        this.d31 = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
    }

    /**
     * @return the parent
     */
    public IndicatorDetail getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(IndicatorDetail parent) {
        this.parent = parent;
    }

    public int getMth() {
        return mth;
    }

    public void setMth(int mth) {
        this.mth = mth;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getD01() {
        return d01;
    }

    public void setD01(BigDecimal d01) {
        this.d01 = d01;
        calcHalfYear();
    }

    public BigDecimal getD02() {
        return d02;
    }

    public void setD02(BigDecimal d02) {
        this.d02 = d02;
        calcHalfYear();
    }

    public BigDecimal getD03() {
        return d03;
    }

    public void setD03(BigDecimal d03) {
        this.d03 = d03;
        calcHalfYear();
    }

    public BigDecimal getD04() {
        return d04;
    }

    public void setD04(BigDecimal d04) {
        this.d04 = d04;
        calcHalfYear();
    }

    public BigDecimal getD05() {
        return d05;
    }

    public void setD05(BigDecimal d05) {
        this.d05 = d05;
        calcHalfYear();
    }

    public BigDecimal getD06() {
        return d06;
    }

    public void setD06(BigDecimal d06) {
        this.d06 = d06;
        calcHalfYear();
    }

    public BigDecimal getD07() {
        return d07;
    }

    public void setD07(BigDecimal d07) {
        this.d07 = d07;
        calcHalfYear();
    }

    public BigDecimal getD08() {
        return d08;
    }

    public void setD08(BigDecimal d08) {
        this.d08 = d08;
        calcHalfYear();
    }

    public BigDecimal getD09() {
        return d09;
    }

    public void setD09(BigDecimal d09) {
        this.d09 = d09;
        calcHalfYear();
    }

    public BigDecimal getD10() {
        return d10;
    }

    public void setD10(BigDecimal d10) {
        this.d10 = d10;
        calcHalfYear();
    }

    public BigDecimal getD11() {
        return d11;
    }

    public void setD11(BigDecimal d11) {
        this.d11 = d11;
        calcHalfYear();
    }

    public BigDecimal getD12() {
        return d12;
    }

    public void setD12(BigDecimal d12) {
        this.d12 = d12;
        calcHalfYear();
    }

    public BigDecimal getD13() {
        return d13;
    }

    public void setD13(BigDecimal d13) {
        this.d13 = d13;
        calcHalfYear();
    }

    public BigDecimal getD14() {
        return d14;
    }

    public void setD14(BigDecimal d14) {
        this.d14 = d14;
        calcHalfYear();
    }

    public BigDecimal getD15() {
        return d15;
    }

    public void setD15(BigDecimal d15) {
        this.d15 = d15;
        calcHalfYear();
    }

    public BigDecimal getD16() {
        return d16;
    }

    public void setD16(BigDecimal d16) {
        this.d16 = d16;
        calcHalfYear();
    }

    public BigDecimal getD17() {
        return d17;
    }

    public void setD17(BigDecimal d17) {
        this.d17 = d17;
        calcHalfYear();
    }

    public BigDecimal getD18() {
        return d18;
    }

    public void setD18(BigDecimal d18) {
        this.d18 = d18;
        calcHalfYear();
    }

    public BigDecimal getD19() {
        return d19;
    }

    public void setD19(BigDecimal d19) {
        this.d19 = d19;
        calcHalfYear();
    }

    public BigDecimal getD20() {
        return d20;
    }

    public void setD20(BigDecimal d20) {
        this.d20 = d20;
        calcHalfYear();
    }

    public BigDecimal getD21() {
        return d21;
    }

    public void setD21(BigDecimal d21) {
        this.d21 = d21;
        calcHalfYear();
    }

    public BigDecimal getD22() {
        return d22;
    }

    public void setD22(BigDecimal d22) {
        this.d22 = d22;
        calcHalfYear();
    }

    public BigDecimal getD23() {
        return d23;
    }

    public void setD23(BigDecimal d23) {
        this.d23 = d23;
        calcHalfYear();
    }

    public BigDecimal getD24() {
        return d24;
    }

    public void setD24(BigDecimal d24) {
        this.d24 = d24;
        calcHalfYear();
    }

    public BigDecimal getD25() {
        return d25;
    }

    public void setD25(BigDecimal d25) {
        this.d25 = d25;
        calcHalfYear();
    }

    public BigDecimal getD26() {
        return d26;
    }

    public void setD26(BigDecimal d26) {
        this.d26 = d26;
        calcHalfYear();
    }

    public BigDecimal getD27() {
        return d27;
    }

    public void setD27(BigDecimal d27) {
        this.d27 = d27;
        calcHalfYear();
    }

    public BigDecimal getD28() {
        return d28;
    }

    public void setD28(BigDecimal d28) {
        this.d28 = d28;
        calcHalfYear();
    }

    public BigDecimal getD29() {
        return d29;
    }

    public void setD29(BigDecimal d29) {
        this.d29 = d29;
        calcHalfYear();
    }

    public BigDecimal getD30() {
        return d30;
    }

    public void setD30(BigDecimal d30) {
        this.d30 = d30;
        calcHalfYear();
    }

    public BigDecimal getD31() {
        return d31;
    }

    public void setD31(BigDecimal d31) {
        this.d31 = d31;
        calcHalfYear();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public void calcHalfYear() {
        if (!"P".equals(this.getType())) {
            this.total = addAllDay();
        }

    }

    public BigDecimal avgTotal(int year) {
        return this.total.divide(BigDecimal.valueOf(days(year, this.mth)), 2, RoundingMode.HALF_UP);
    }

    public int days(int year, int m) {
        int days = 0;
        if (m != 2) {
            switch (m) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    days = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    days = 30;

            }
        } else {
            // 闰年
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                days = 29;
            } else {
                days = 28;
            }

        }
        return days;
    }

    public BigDecimal addAllDay() {
        return this.d01.add(this.d02).add(this.d03).add(this.d04).add(this.d05).add(this.d06).add(this.d07).add(this.d08)
                .add(this.d09).add(this.d10).add(this.d11).add(this.d12).add(this.d13).add(this.d14).add(this.d15).add(this.d16)
                .add(this.d17).add(this.d18).add(this.d19).add(this.d20).add(this.d21).add(this.d22).add(this.d23).add(this.d24)
                .add(this.d25).add(this.d26).add(this.d27).add(this.d28).add(this.d29).add(this.d30).add(this.d31);
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IndicatorDaily)) {
            return false;
        }
        IndicatorDaily other = (IndicatorDaily) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public void clearDate() {
        this.d01 = BigDecimal.ZERO;
        this.d02 = BigDecimal.ZERO;
        this.d03 = BigDecimal.ZERO;
        this.d04 = BigDecimal.ZERO;
        this.d05 = BigDecimal.ZERO;
        this.d06 = BigDecimal.ZERO;
        this.d07 = BigDecimal.ZERO;
        this.d08 = BigDecimal.ZERO;
        this.d09 = BigDecimal.ZERO;
        this.d10 = BigDecimal.ZERO;
        this.d11 = BigDecimal.ZERO;
        this.d12 = BigDecimal.ZERO;
        this.d13 = BigDecimal.ZERO;
        this.d14 = BigDecimal.ZERO;
        this.d15 = BigDecimal.ZERO;
        this.d16 = BigDecimal.ZERO;
        this.d17 = BigDecimal.ZERO;
        this.d18 = BigDecimal.ZERO;
        this.d19 = BigDecimal.ZERO;
        this.d20 = BigDecimal.ZERO;
        this.d21 = BigDecimal.ZERO;
        this.d22 = BigDecimal.ZERO;
        this.d23 = BigDecimal.ZERO;
        this.d24 = BigDecimal.ZERO;
        this.d25 = BigDecimal.ZERO;
        this.d26 = BigDecimal.ZERO;
        this.d27 = BigDecimal.ZERO;
        this.d28 = BigDecimal.ZERO;
        this.d29 = BigDecimal.ZERO;
        this.d30 = BigDecimal.ZERO;
        this.d31 = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.IndicatorDaily[ id=" + id + " ]";
    }

}
