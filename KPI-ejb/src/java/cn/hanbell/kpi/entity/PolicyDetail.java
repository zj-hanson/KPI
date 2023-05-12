/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperDetailEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@Entity
@Table(name = "policydetail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PolicyDetail.findAll", query = "SELECT p FROM PolicyDetail p"),
    @NamedQuery(name = "PolicyDetail.findById", query = "SELECT p FROM PolicyDetail p WHERE p.id = :id"),
    @NamedQuery(name = "PolicyDetail.findByPId", query = "SELECT p FROM PolicyDetail p WHERE p.pid = :pid order by p.seq"),
    @NamedQuery(name = "PolicyDetail.findBySeq", query = "SELECT p FROM PolicyDetail p WHERE p.seq = :seq"),
    @NamedQuery(name = "PolicyDetail.findByName", query = "SELECT p FROM PolicyDetail p WHERE p.name = :name"),
    @NamedQuery(name = "PolicyDetail.findByPerspective", query = "SELECT p FROM PolicyDetail p WHERE p.perspective = :perspective"),
    @NamedQuery(name = "PolicyDetail.findByObjective", query = "SELECT p FROM PolicyDetail p WHERE p.objective = :objective"),
    @NamedQuery(name = "PolicyDetail.findByType", query = "SELECT p FROM PolicyDetail p WHERE p.type = :type"),
    @NamedQuery(name = "PolicyDetail.findByBq1", query = "SELECT p FROM PolicyDetail p WHERE p.bq1 = :bq1"),
    @NamedQuery(name = "PolicyDetail.findByBq2", query = "SELECT p FROM PolicyDetail p WHERE p.bq2 = :bq2"),
    @NamedQuery(name = "PolicyDetail.findByBq3", query = "SELECT p FROM PolicyDetail p WHERE p.bq3 = :bq3"),
    @NamedQuery(name = "PolicyDetail.findByBq4", query = "SELECT p FROM PolicyDetail p WHERE p.bq4 = :bq4"),
    @NamedQuery(name = "PolicyDetail.findByBhy", query = "SELECT p FROM PolicyDetail p WHERE p.bhy = :bhy"),
    @NamedQuery(name = "PolicyDetail.findByBfy", query = "SELECT p FROM PolicyDetail p WHERE p.bfy = :bfy"),
    @NamedQuery(name = "PolicyDetail.findByTq1", query = "SELECT p FROM PolicyDetail p WHERE p.tq1 = :tq1"),
    @NamedQuery(name = "PolicyDetail.findByTq2", query = "SELECT p FROM PolicyDetail p WHERE p.tq2 = :tq2"),
    @NamedQuery(name = "PolicyDetail.findByTq3", query = "SELECT p FROM PolicyDetail p WHERE p.tq3 = :tq3"),
    @NamedQuery(name = "PolicyDetail.findByTq4", query = "SELECT p FROM PolicyDetail p WHERE p.tq4 = :tq4"),
    @NamedQuery(name = "PolicyDetail.findByThy", query = "SELECT p FROM PolicyDetail p WHERE p.thy = :thy"),
    @NamedQuery(name = "PolicyDetail.findByTfy", query = "SELECT p FROM PolicyDetail p WHERE p.tfy = :tfy"),
    @NamedQuery(name = "PolicyDetail.findByAq1", query = "SELECT p FROM PolicyDetail p WHERE p.aq1 = :aq1"),
    @NamedQuery(name = "PolicyDetail.findByAq2", query = "SELECT p FROM PolicyDetail p WHERE p.aq2 = :aq2"),
    @NamedQuery(name = "PolicyDetail.findByAq3", query = "SELECT p FROM PolicyDetail p WHERE p.aq3 = :aq3"),
    @NamedQuery(name = "PolicyDetail.findByAq4", query = "SELECT p FROM PolicyDetail p WHERE p.aq4 = :aq4"),
    @NamedQuery(name = "PolicyDetail.findByAhy", query = "SELECT p FROM PolicyDetail p WHERE p.ahy = :ahy"),
    @NamedQuery(name = "PolicyDetail.findByAfy", query = "SELECT p FROM PolicyDetail p WHERE p.afy = :afy"),
    @NamedQuery(name = "PolicyDetail.findByPq1", query = "SELECT p FROM PolicyDetail p WHERE p.pq1 = :pq1"),
    @NamedQuery(name = "PolicyDetail.findByPq2", query = "SELECT p FROM PolicyDetail p WHERE p.pq2 = :pq2"),
    @NamedQuery(name = "PolicyDetail.findByPq3", query = "SELECT p FROM PolicyDetail p WHERE p.pq3 = :pq3"),
    @NamedQuery(name = "PolicyDetail.findByPq4", query = "SELECT p FROM PolicyDetail p WHERE p.pq4 = :pq4"),
    @NamedQuery(name = "PolicyDetail.findByPhy", query = "SELECT p FROM PolicyDetail p WHERE p.phy = :phy"),
    @NamedQuery(name = "PolicyDetail.findByPfy", query = "SELECT p FROM PolicyDetail p WHERE p.pfy = :pfy"),
    @NamedQuery(name = "PolicyDetail.findByUnit", query = "SELECT p FROM PolicyDetail p WHERE p.unit = :unit"),
    @NamedQuery(name = "PolicyDetail.findByCalculationtype", query = "SELECT p FROM PolicyDetail p WHERE p.calculationtype = :calculationtype"),
    @NamedQuery(name = "PolicyDetail.findByPerformancecalculation", query = "SELECT p FROM PolicyDetail p WHERE p.performancecalculation = :performancecalculation"),
    @NamedQuery(name = "PolicyDetail.findByFromkpi", query = "SELECT p FROM PolicyDetail p WHERE p.fromkpi = :fromkpi"),
    @NamedQuery(name = "PolicyDetail.findByFromplm", query = "SELECT p FROM PolicyDetail p WHERE p.fromplm = :fromplm"),
    @NamedQuery(name = "PolicyDetail.findByQ1action", query = "SELECT p FROM PolicyDetail p WHERE p.q1action = :q1action"),
    @NamedQuery(name = "PolicyDetail.findByQ1reason1", query = "SELECT p FROM PolicyDetail p WHERE p.q1reason1 = :q1reason1"),
    @NamedQuery(name = "PolicyDetail.findByQ1countermeasure1", query = "SELECT p FROM PolicyDetail p WHERE p.q1countermeasure1 = :q1countermeasure1"),
    @NamedQuery(name = "PolicyDetail.findByQ1reason2", query = "SELECT p FROM PolicyDetail p WHERE p.q1reason2 = :q1reason2"),
    @NamedQuery(name = "PolicyDetail.findByQ1countermeasure2", query = "SELECT p FROM PolicyDetail p WHERE p.q1countermeasure2 = :q1countermeasure2"),
    @NamedQuery(name = "PolicyDetail.findByQ2action", query = "SELECT p FROM PolicyDetail p WHERE p.q2action = :q2action"),
    @NamedQuery(name = "PolicyDetail.findByQ2reason1", query = "SELECT p FROM PolicyDetail p WHERE p.q2reason1 = :q2reason1"),
    @NamedQuery(name = "PolicyDetail.findByQ2countermeasure1", query = "SELECT p FROM PolicyDetail p WHERE p.q2countermeasure1 = :q2countermeasure1"),
    @NamedQuery(name = "PolicyDetail.findByQ2reason2", query = "SELECT p FROM PolicyDetail p WHERE p.q2reason2 = :q2reason2"),
    @NamedQuery(name = "PolicyDetail.findByQ2countermeasure2", query = "SELECT p FROM PolicyDetail p WHERE p.q2countermeasure2 = :q2countermeasure2"),
    @NamedQuery(name = "PolicyDetail.findByQ3action", query = "SELECT p FROM PolicyDetail p WHERE p.q3action = :q3action"),
    @NamedQuery(name = "PolicyDetail.findByQ3reason1", query = "SELECT p FROM PolicyDetail p WHERE p.q3reason1 = :q3reason1"),
    @NamedQuery(name = "PolicyDetail.findByQ3countermeasure1", query = "SELECT p FROM PolicyDetail p WHERE p.q3countermeasure1 = :q3countermeasure1"),
    @NamedQuery(name = "PolicyDetail.findByQ3reason2", query = "SELECT p FROM PolicyDetail p WHERE p.q3reason2 = :q3reason2"),
    @NamedQuery(name = "PolicyDetail.findByQ3countermeasure2", query = "SELECT p FROM PolicyDetail p WHERE p.q3countermeasure2 = :q3countermeasure2"),
    @NamedQuery(name = "PolicyDetail.findByQ4action", query = "SELECT p FROM PolicyDetail p WHERE p.q4action = :q4action"),
    @NamedQuery(name = "PolicyDetail.findByQ4reason1", query = "SELECT p FROM PolicyDetail p WHERE p.q4reason1 = :q4reason1"),
    @NamedQuery(name = "PolicyDetail.findByQ4countermeasure1", query = "SELECT p FROM PolicyDetail p WHERE p.q4countermeasure1 = :q4countermeasure1"),
    @NamedQuery(name = "PolicyDetail.findByQ4reason2", query = "SELECT p FROM PolicyDetail p WHERE p.q4reason2 = :q4reason2"),
    @NamedQuery(name = "PolicyDetail.findByQ4countermeasure2", query = "SELECT p FROM PolicyDetail p WHERE p.q4countermeasure2 = :q4countermeasure2"),
    @NamedQuery(name = "PolicyDetail.findByHyaction", query = "SELECT p FROM PolicyDetail p WHERE p.hyaction = :hyaction"),
    @NamedQuery(name = "PolicyDetail.findByHyreason1", query = "SELECT p FROM PolicyDetail p WHERE p.hyreason1 = :hyreason1"),
    @NamedQuery(name = "PolicyDetail.findByHycountermeasure1", query = "SELECT p FROM PolicyDetail p WHERE p.hycountermeasure1 = :hycountermeasure1"),
    @NamedQuery(name = "PolicyDetail.findByHyreason2", query = "SELECT p FROM PolicyDetail p WHERE p.hyreason2 = :hyreason2"),
    @NamedQuery(name = "PolicyDetail.findByHycountermeasure2", query = "SELECT p FROM PolicyDetail p WHERE p.hycountermeasure2 = :hycountermeasure2"),
    @NamedQuery(name = "PolicyDetail.findByFyaction", query = "SELECT p FROM PolicyDetail p WHERE p.fyaction = :fyaction"),
    @NamedQuery(name = "PolicyDetail.findByFyreason1", query = "SELECT p FROM PolicyDetail p WHERE p.fyreason1 = :fyreason1"),
    @NamedQuery(name = "PolicyDetail.findByFycountermeasure1", query = "SELECT p FROM PolicyDetail p WHERE p.fycountermeasure1 = :fycountermeasure1"),
    @NamedQuery(name = "PolicyDetail.findByFyreason2", query = "SELECT p FROM PolicyDetail p WHERE p.fyreason2 = :fyreason2"),
    @NamedQuery(name = "PolicyDetail.findByFycountermeasure2", query = "SELECT p FROM PolicyDetail p WHERE p.fycountermeasure2 = :fycountermeasure2")})
public class PolicyDetail extends SuperDetailEntity {

    @JoinColumn(name = "pid", referencedColumnName = "id", updatable = false, insertable = false)
    @ManyToOne(optional = true)
    private Policy parent;

    private static final long serialVersionUID = 1L;
    @Size(max = 200)
    @Column(name = "seqname")
    private String seqname;
    @Size(max = 50)
    @Column(name = "name")
    private String name;
    @Size(max = 50)
    @Column(name = "perspective")
    private String perspective;
    @Size(max = 50)
    @Column(name = "objective")
    private String objective;
    @Size(max = 50)
    @Column(name = "type")
    private String type;
    @Size(max = 200)
    @Column(name = "bq1")
    private String bq1;
    @Size(max = 200)
    @Column(name = "bq2")
    private String bq2;
    @Size(max = 200)
    @Column(name = "bq3")
    private String bq3;
    @Size(max = 200)
    @Column(name = "bq4")
    private String bq4;
    @Size(max = 200)
    @Column(name = "bhy")
    private String bhy;
    @Size(max = 200)
    @Column(name = "bfy")
    private String bfy;
    @Size(max = 200)
    @Column(name = "tq1")
    private String tq1;
    @Size(max = 200)
    @Column(name = "tq2")
    private String tq2;
    @Size(max = 200)
    @Column(name = "tq3")
    private String tq3;
    @Size(max = 200)
    @Column(name = "tq4")
    private String tq4;
    @Size(max = 200)
    @Column(name = "thy")
    private String thy;
    @Size(max = 200)
    @Column(name = "tfy")
    private String tfy;
    @Size(max = 200)
    @Column(name = "aq1")
    private String aq1;
    @Size(max = 200)
    @Column(name = "aq2")
    private String aq2;
    @Size(max = 200)
    @Column(name = "aq3")
    private String aq3;
    @Size(max = 200)
    @Column(name = "aq4")
    private String aq4;
    @Size(max = 200)
    @Column(name = "ahy")
    private String ahy;
    @Size(max = 200)
    @Column(name = "afy")
    private String afy;
    @Size(max = 200)
    @Column(name = "pq1")
    private String pq1;
    @Size(max = 200)
    @Column(name = "pq2")
    private String pq2;
    @Size(max = 200)
    @Column(name = "pq3")
    private String pq3;
    @Size(max = 200)
    @Column(name = "pq4")
    private String pq4;
    @Size(max = 200)
    @Column(name = "phy")
    private String phy;
    @Size(max = 200)
    @Column(name = "pfy")
    private String pfy;
    @Size(max = 50)
    @Column(name = "unit")
    private String unit;
    @Size(max = 50)
    @Column(name = "calculationtype")
    private String calculationtype;
    @Size(max = 50)
    @Column(name = "performancecalculation")
    private String performancecalculation;
    @Size(max = 50)
    @Column(name = "fromkpi")
    private String fromkpi;
    @Size(max = 20)
    @Column(name = "fromkpiname")
    private String fromkpiname;
    @Column(name = "indicatorrate")
    private BigDecimal indicatorrate;
    @Size(max = 50)
    @Column(name = "fromplm")
    private String fromplm;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q1action")
    private String q1action;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q1reason1")
    private String q1reason1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q1countermeasure1")
    private String q1countermeasure1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q1reason2")
    private String q1reason2;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q1countermeasure2")
    private String q1countermeasure2;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q2action")
    private String q2action;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q2reason1")
    private String q2reason1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q2countermeasure1")
    private String q2countermeasure1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q2reason2")
    private String q2reason2;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q2countermeasure2")
    private String q2countermeasure2;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q3action")
    private String q3action;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q3reason1")
    private String q3reason1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q3countermeasure1")
    private String q3countermeasure1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q3reason2")
    private String q3reason2;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q3countermeasure2")
    private String q3countermeasure2;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q4action")
    private String q4action;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q4reason1")
    private String q4reason1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q4countermeasure1")
    private String q4countermeasure1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q4reason2")
    private String q4reason2;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q4countermeasure2")
    private String q4countermeasure2;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "hyaction")
    private String hyaction;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "hyreason1")
    private String hyreason1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "hycountermeasure1")
    private String hycountermeasure1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "hyreason2")
    private String hyreason2;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "hycountermeasure2")
    private String hycountermeasure2;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "fyaction")
    private String fyaction;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "fyreason1")
    private String fyreason1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "fycountermeasure1")
    private String fycountermeasure1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "fyreason2")
    private String fyreason2;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "fycountermeasure2")
    private String fycountermeasure2;

    public PolicyDetail() {
    }

    public PolicyDetail(Integer id) {
        this.id = id;
    }

    public Policy getParent() {
        return parent;
    }

    public void setParent(Policy parent) {
        this.parent = parent;
    }

    public PolicyDetail(Integer id, int seq) {
        this.id = id;
        this.seq = seq;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public String getSeqname() {
        return seqname;
    }

    public void setSeqname(String seqname) {
        this.seqname = seqname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerspective() {
        return perspective;
    }

    public void setPerspective(String perspective) {
        this.perspective = perspective;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBq1() {
        return bq1;
    }

    public void setBq1(String bq1) {
        this.bq1 = bq1;
    }

    public String getBq2() {
        return bq2;
    }

    public void setBq2(String bq2) {
        this.bq2 = bq2;
    }

    public String getBq3() {
        return bq3;
    }

    public void setBq3(String bq3) {
        this.bq3 = bq3;
    }

    public String getBq4() {
        return bq4;
    }

    public void setBq4(String bq4) {
        this.bq4 = bq4;
    }

    public String getBhy() {
        return bhy;
    }

    public void setBhy(String bhy) {
        this.bhy = bhy;
    }

    public String getBfy() {
        return bfy;
    }

    public void setBfy(String bfy) {
        this.bfy = bfy;
    }

    public String getTq1() {
        return tq1;
    }

    public void setTq1(String tq1) {
        this.tq1 = tq1;
    }

    public String getTq2() {
        return tq2;
    }

    public void setTq2(String tq2) {
        this.tq2 = tq2;
    }

    public String getTq3() {
        return tq3;
    }

    public void setTq3(String tq3) {
        this.tq3 = tq3;
    }

    public String getTq4() {
        return tq4;
    }

    public void setTq4(String tq4) {
        this.tq4 = tq4;
    }

    public String getThy() {
        return thy;
    }

    public void setThy(String thy) {
        this.thy = thy;
    }

    public String getTfy() {
        return tfy;
    }

    public void setTfy(String tfy) {
        this.tfy = tfy;
    }

    public String getAq1() {
        return aq1;
    }

    public void setAq1(String aq1) {
        this.aq1 = aq1;
    }

    public String getAq2() {
        return aq2;
    }

    public void setAq2(String aq2) {
        this.aq2 = aq2;
    }

    public String getAq3() {
        return aq3;
    }

    public void setAq3(String aq3) {
        this.aq3 = aq3;
    }

    public String getAq4() {
        return aq4;
    }

    public void setAq4(String aq4) {
        this.aq4 = aq4;
    }

    public String getAhy() {
        return ahy;
    }

    public void setAhy(String ahy) {
        this.ahy = ahy;
    }

    public String getAfy() {
        return afy;
    }

    public void setAfy(String afy) {
        this.afy = afy;
    }

    public String getPq1() {
        return pq1;
    }

    public void setPq1(String pq1) {
        this.pq1 = pq1;
    }

    public String getPq2() {
        return pq2;
    }

    public void setPq2(String pq2) {
        this.pq2 = pq2;
    }

    public String getPq3() {
        return pq3;
    }

    public void setPq3(String pq3) {
        this.pq3 = pq3;
    }

    public String getPq4() {
        return pq4;
    }

    public void setPq4(String pq4) {
        this.pq4 = pq4;
    }

    public String getPhy() {
        return phy;
    }

    public void setPhy(String phy) {
        this.phy = phy;
    }

    public String getPfy() {
        return pfy;
    }

    public void setPfy(String pfy) {
        this.pfy = pfy;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCalculationtype() {
        return calculationtype;
    }

    public void setCalculationtype(String calculationtype) {
        this.calculationtype = calculationtype;
    }

    public String getPerformancecalculation() {
        return performancecalculation;
    }

    public void setPerformancecalculation(String performancecalculation) {
        this.performancecalculation = performancecalculation;
    }

    public String getFromkpi() {
        return fromkpi;
    }

    public void setFromkpi(String fromkpi) {
        this.fromkpi = fromkpi;
    }

    public String getFromkpiname() {
        return fromkpiname;
    }

    public void setFromkpiname(String fromkpiname) {
        this.fromkpiname = fromkpiname;
    }

    public BigDecimal getIndicatorrate() {
        return indicatorrate;
    }

    public void setIndicatorrate(BigDecimal indicatorrate) {
        this.indicatorrate = indicatorrate;
    }

    public String getFromplm() {
        return fromplm;
    }

    public void setFromplm(String fromplm) {
        this.fromplm = fromplm;
    }

    public String getQ1action() {
        return q1action;
    }

    public void setQ1action(String q1action) {
        this.q1action = q1action;
    }

    public String getQ1reason1() {
        return q1reason1;
    }

    public void setQ1reason1(String q1reason1) {
        this.q1reason1 = q1reason1;
    }

    public String getQ1countermeasure1() {
        return q1countermeasure1;
    }

    public void setQ1countermeasure1(String q1countermeasure1) {
        this.q1countermeasure1 = q1countermeasure1;
    }

    public String getQ1reason2() {
        return q1reason2;
    }

    public void setQ1reason2(String q1reason2) {
        this.q1reason2 = q1reason2;
    }

    public String getQ1countermeasure2() {
        return q1countermeasure2;
    }

    public void setQ1countermeasure2(String q1countermeasure2) {
        this.q1countermeasure2 = q1countermeasure2;
    }

    public String getQ2action() {
        return q2action;
    }

    public void setQ2action(String q2action) {
        this.q2action = q2action;
    }

    public String getQ2reason1() {
        return q2reason1;
    }

    public void setQ2reason1(String q2reason1) {
        this.q2reason1 = q2reason1;
    }

    public String getQ2countermeasure1() {
        return q2countermeasure1;
    }

    public void setQ2countermeasure1(String q2countermeasure1) {
        this.q2countermeasure1 = q2countermeasure1;
    }

    public String getQ2reason2() {
        return q2reason2;
    }

    public void setQ2reason2(String q2reason2) {
        this.q2reason2 = q2reason2;
    }

    public String getQ2countermeasure2() {
        return q2countermeasure2;
    }

    public void setQ2countermeasure2(String q2countermeasure2) {
        this.q2countermeasure2 = q2countermeasure2;
    }

    public String getQ3action() {
        return q3action;
    }

    public void setQ3action(String q3action) {
        this.q3action = q3action;
    }

    public String getQ3reason1() {
        return q3reason1;
    }

    public void setQ3reason1(String q3reason1) {
        this.q3reason1 = q3reason1;
    }

    public String getQ3countermeasure1() {
        return q3countermeasure1;
    }

    public void setQ3countermeasure1(String q3countermeasure1) {
        this.q3countermeasure1 = q3countermeasure1;
    }

    public String getQ3reason2() {
        return q3reason2;
    }

    public void setQ3reason2(String q3reason2) {
        this.q3reason2 = q3reason2;
    }

    public String getQ3countermeasure2() {
        return q3countermeasure2;
    }

    public void setQ3countermeasure2(String q3countermeasure2) {
        this.q3countermeasure2 = q3countermeasure2;
    }

    public String getQ4action() {
        return q4action;
    }

    public void setQ4action(String q4action) {
        this.q4action = q4action;
    }

    public String getQ4reason1() {
        return q4reason1;
    }

    public void setQ4reason1(String q4reason1) {
        this.q4reason1 = q4reason1;
    }

    public String getQ4countermeasure1() {
        return q4countermeasure1;
    }

    public void setQ4countermeasure1(String q4countermeasure1) {
        this.q4countermeasure1 = q4countermeasure1;
    }

    public String getQ4reason2() {
        return q4reason2;
    }

    public void setQ4reason2(String q4reason2) {
        this.q4reason2 = q4reason2;
    }

    public String getQ4countermeasure2() {
        return q4countermeasure2;
    }

    public void setQ4countermeasure2(String q4countermeasure2) {
        this.q4countermeasure2 = q4countermeasure2;
    }

    public String getHyaction() {
        return hyaction;
    }

    public void setHyaction(String hyaction) {
        this.hyaction = hyaction;
    }

    public String getHyreason1() {
        return hyreason1;
    }

    public void setHyreason1(String hyreason1) {
        this.hyreason1 = hyreason1;
    }

    public String getHycountermeasure1() {
        return hycountermeasure1;
    }

    public void setHycountermeasure1(String hycountermeasure1) {
        this.hycountermeasure1 = hycountermeasure1;
    }

    public String getHyreason2() {
        return hyreason2;
    }

    public void setHyreason2(String hyreason2) {
        this.hyreason2 = hyreason2;
    }

    public String getHycountermeasure2() {
        return hycountermeasure2;
    }

    public void setHycountermeasure2(String hycountermeasure2) {
        this.hycountermeasure2 = hycountermeasure2;
    }

    public String getFyaction() {
        return fyaction;
    }

    public void setFyaction(String fyaction) {
        this.fyaction = fyaction;
    }

    public String getFyreason1() {
        return fyreason1;
    }

    public void setFyreason1(String fyreason1) {
        this.fyreason1 = fyreason1;
    }

    public String getFycountermeasure1() {
        return fycountermeasure1;
    }

    public void setFycountermeasure1(String fycountermeasure1) {
        this.fycountermeasure1 = fycountermeasure1;
    }

    public String getFyreason2() {
        return fyreason2;
    }

    public void setFyreason2(String fyreason2) {
        this.fyreason2 = fyreason2;
    }

    public String getFycountermeasure2() {
        return fycountermeasure2;
    }

    public void setFycountermeasure2(String fycountermeasure2) {
        this.fycountermeasure2 = fycountermeasure2;
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
        if (!(object instanceof PolicyDetail)) {
            return false;
        }
        PolicyDetail other = (PolicyDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.PolicyDetail[ id=" + id + " ]";
    }

}
