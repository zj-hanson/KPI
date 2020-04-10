/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C1879
 */
@Entity
@Table(name = "datarecord")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataRecord.findAll", query = "SELECT d FROM DataRecord d"),
    @NamedQuery(name = "DataRecord.findDataRecords", query = "SELECT d FROM DataRecord d WHERE d.facno = :facno AND d.dataRecordAssisted.type = :type AND d.dataRecordAssisted.whethershow = :whethershow  AND d.yea = :yea AND d.mon = :mon  ORDER BY d.dataRecordAssisted.showno ASC "),
    @NamedQuery(name = "DataRecord.findByYeaAndMon", query = "SELECT d FROM DataRecord d WHERE d.facno = :facno AND d.type = :type AND d.yea = :yea AND d.mon = :mon ")
})
public class DataRecord extends SuperEntity {

    @JoinColumns({
        @JoinColumn(name = "facno", referencedColumnName = "facno", updatable = false, insertable = false),
        @JoinColumn(name = "type", referencedColumnName = "type", updatable = false, insertable = false),
        @JoinColumn(name = "itemname", referencedColumnName = "itemname", updatable = false, insertable = false)
    })
    @OneToOne(optional = true)
    private DataRecordAssisted dataRecordAssisted;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "facno")
    private String facno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Column(name = "itemno")
    private int itemno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "itemname")
    private String itemname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "yea")
    private int yea;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mon")
    private int mon;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "amt")
    private BigDecimal amt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "adjamt")
    private BigDecimal adjamt;
    @Size(max = 45)
    @Column(name = "remark1")
    private String remark1;
    @Size(max = 45)
    @Column(name = "remark2")
    private String remark2;

    public DataRecord() {
    }

    public DataRecord(Integer id) {
        this.id = id;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getItemno() {
        return itemno;
    }

    public void setItemno(int itemno) {
        this.itemno = itemno;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public int getYea() {
        return yea;
    }

    public void setYea(int yea) {
        this.yea = yea;
    }

    public int getMon() {
        return mon;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public BigDecimal getAdjamt() {
        return adjamt;
    }

    public void setAdjamt(BigDecimal adjamt) {
        this.adjamt = adjamt;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    /**
     * @return the dataRecordAssisted
     */
    public DataRecordAssisted getDataRecordAssisted() {
        return dataRecordAssisted;
    }

    /**
     * @param dataRecordAssisted the dataRecordAssisted to set
     */
    public void setDataRecordAssisted(DataRecordAssisted dataRecordAssisted) {
        this.dataRecordAssisted = dataRecordAssisted;
    }

}
