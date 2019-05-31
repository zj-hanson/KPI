/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author C1749
 * 改实体类只作为各库别物料库存状况表的对象使用
 */
public class InventoryStatement implements Serializable{
    //库名
    private String wareh;
    //三月前库存金额
    private BigDecimal amount1;
    //二月前库存金额
    private BigDecimal amount2;
    //一月前库存金额
    private BigDecimal amount3;
    //差异
    private BigDecimal difference;
    //占比
    private BigDecimal proportion;
    
    public InventoryStatement(){
        
    }

    public String getWareh() {
        return wareh;
    }

    public void setWareh(String wareh) {
        this.wareh = wareh;
    }

    public BigDecimal getAmount1() {
        return amount1;
    }

    public void setAmount1(BigDecimal amount1) {
        this.amount1 = amount1;
    }

    public BigDecimal getAmount2() {
        return amount2;
    }

    public void setAmount2(BigDecimal amount2) {
        this.amount2 = amount2;
    }

    public BigDecimal getAmount3() {
        return amount3;
    }

    public void setAmount3(BigDecimal amount3) {
        this.amount3 = amount3;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public BigDecimal getProportion() {
        return proportion;
    }

    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
    }
    
}
