/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.ScorecardDetail;
import cn.hanbell.kpi.entity.ScorecardExplanation;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class ScorecardDetailBean extends SuperEJBForKPI<ScorecardDetail> {

    @EJB
    private ScorecardExplanationBean scorecardExplanationBean;

    public ScorecardDetailBean() {
        super(ScorecardDetail.class);
    }

    @Override
    public void delete(ScorecardDetail entity) {
        super.delete(entity);
        scorecardExplanationBean.deleteByPId(entity.getId());
    }

    @Override
    public void persist(ScorecardDetail entity) {
        super.persist(entity);
        this.getEntityManager().flush();
        //自评
        ScorecardExplanation s = new ScorecardExplanation();
        s.setPid(entity.getId());
        s.setSeq(1);
        s.setType("S");
        scorecardExplanationBean.persist(s);
        scorecardExplanationBean.getEntityManager().flush();
        entity.setSelfScore(s);
        //部门
        ScorecardExplanation d = new ScorecardExplanation();
        d.setPid(entity.getId());
        d.setSeq(2);
        d.setType("D");
        scorecardExplanationBean.persist(d);
        scorecardExplanationBean.getEntityManager().flush();
        entity.setDeptScore(d);
        //老总
        ScorecardExplanation g = new ScorecardExplanation();
        g.setPid(entity.getId());
        g.setSeq(3);
        g.setType("G");
        scorecardExplanationBean.persist(g);
        scorecardExplanationBean.getEntityManager().flush();
        entity.setGeneralScore(g);
        //其他
        ScorecardExplanation o = new ScorecardExplanation();
        o.setPid(entity.getId());
        o.setSeq(4);
        o.setType("O");
        scorecardExplanationBean.persist(o);
        scorecardExplanationBean.getEntityManager().flush();
        entity.setOtherScore(o);
        //设置后保存
        update(entity);
    }

    @Override
    public ScorecardDetail update(ScorecardDetail entity) {
        scorecardExplanationBean.update(entity.getSelfScore());
        scorecardExplanationBean.update(entity.getDeptScore());
        scorecardExplanationBean.update(entity.getGeneralScore());
        scorecardExplanationBean.update(entity.getOtherScore());
        scorecardExplanationBean.getEntityManager().flush();
        return super.update(entity);
    }

}
