/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author C1879 此对象用于查询销售统计表数据，数据库无此表
 */
public class ClientTable implements Serializable {

    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //客户代号
    private String cusno;

    //客户名称
    private String cusna;

    //本年、月的销售数量
    private String nowshpqy1;

    //本年、月的销售金额
    private String nowshpamts;

    //本年、月的客户排名
    private String nowrank;

    //去年同期销售数量
    private String pastshpqy1;

    //去年同期销售金额
    private String pastshpamts;

    //去年同期客户排名
    private String pastrank;

    //差异值
    private String differencevalue;

    //成长率
    private String growthrate;
    //字体颜色
    private String style;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientTable)) {
            return false;
        }
        ClientTable other = (ClientTable) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.ClientTable[ id=" + getId() + " ]";
    }

    /**
     * @return the cusno
     */
    public String getCusno() {
        return cusno;
    }

    /**
     * @param cusno the cusno to set
     */
    public void setCusno(String cusno) {
        this.cusno = cusno;
    }

    /**
     * @return the cusna
     */
    public String getCusna() {
        return cusna;
    }

    /**
     * @param cusna the cusna to set
     */
    public void setCusna(String cusna) {
        this.cusna = cusna;
    }

    /**
     * @return the nowshpqy1
     */
    public String getNowshpqy1() {
        return nowshpqy1;
    }

    /**
     * @param nowshpqy1 the nowshpqy1 to set
     */
    public void setNowshpqy1(String nowshpqy1) {
        this.nowshpqy1 =nowshpqy1;
    }

    /**
     * @return the nowshpamts
     */
    public String getNowshpamts() {
        return nowshpamts;
    }

    /**
     * @param nowshpamts the nowshpamts to set
     */
    public void setNowshpamts(String nowshpamts) {
        this.nowshpamts =nowshpamts;
    }

    /**
     * @return the nowrank
     */
    public String getNowrank() {
        return nowrank;
    }

    /**
     * @param nowrank the nowrank to set
     */
    public void setNowrank(String nowrank) {
        this.nowrank = nowrank;
    }

    /**
     * @return the pastshpqy1
     */
    public String getPastshpqy1() {
        return pastshpqy1;
    }

    /**
     * @param pastshpqy1 the pastshpqy1 to set
     */
    public void setPastshpqy1(String pastshpqy1) {
        this.pastshpqy1 =pastshpqy1;
    }

    /**
     * @return the pastshpamts
     */
    public String getPastshpamts() {
        return pastshpamts;
    }

    /**
     * @param pastshpamts the pastshpamts to set
     */
    public void setPastshpamts(String pastshpamts) {
        this.pastshpamts =pastshpamts;
    }
    /**
     * @return the pastrank
     */
    public String getPastrank() {
        return pastrank;
    }

    /**
     * @param pastrank the pastrank to set
     */
    public void setPastrank(String pastrank) {
        this.pastrank = pastrank;
    }

    /**
     * @return the differencevalue
     */
    public String getDifferencevalue() {
        return differencevalue;
    }

    /**
     * @param differencevalue the differencevalue to set
     */
    public void setDifferencevalue(String differencevalue) {
        this.differencevalue = differencevalue;
    }

    /**
     * @return the growthrate
     */
    public String getGrowthrate() {
        return growthrate;
    }

    /**
     * @param growthrate the growthrate to set
     */
    public void setGrowthrate(String growthrate) {
        this.growthrate = growthrate;
    }

    /**
     * @return the style
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style the style to set
     */
    public void setStyle(String style) {
        this.style = style;
    }

    
}
