/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperDetailEntity;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
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
@Table(name = "indicatoranalysis")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicatorAnalysis.findAll", query = "SELECT i FROM IndicatorAnalysis i")
    , @NamedQuery(name = "IndicatorAnalysis.findById", query = "SELECT i FROM IndicatorAnalysis i WHERE i.id = :id")
    , @NamedQuery(name = "IndicatorAnalysis.findByPId", query = "SELECT i FROM IndicatorAnalysis i WHERE i.pid = :pid ORDER BY i.m,i.seq")
    , @NamedQuery(name = "IndicatorAnalysis.findByPIdAndMonth", query = "SELECT i FROM IndicatorAnalysis i WHERE i.pid = :pid AND i.m = :m ORDER BY i.seq")})
public class IndicatorAnalysis extends SuperDetailEntity {

    @Basic(optional = false)
    @NotNull
    @Column(name = "m")
    private int m;
    @Size(max = 400)
    @Column(name = "title")
    private String title;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "issue")
    private String issue;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "factor")
    private String factor;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "countermeasure")
    private String countermeasure;

    public IndicatorAnalysis() {
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public String getCountermeasure() {
        return countermeasure;
    }

    public void setCountermeasure(String countermeasure) {
        this.countermeasure = countermeasure;
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
        if (!(object instanceof IndicatorAnalysis)) {
            return false;
        }
        IndicatorAnalysis other = (IndicatorAnalysis) object;
        if ((this.id != null) && (other.id != null)) {
            return Objects.equals(this.id, other.id);
        }
        return (this.seq == other.seq && this.m == other.m);
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.IndicatorAnalysis[ id=" + id + " ]";
    }

}
