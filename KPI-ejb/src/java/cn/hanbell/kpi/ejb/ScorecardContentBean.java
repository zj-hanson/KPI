/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.ScorecardContent;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class ScorecardContentBean extends SuperEJBForKPI<ScorecardContent> {

    @EJB
    private ScorecardExplanationBean scorecardExplanationBean;

    public ScorecardContentBean() {
        super(ScorecardContent.class);
    }

    @Override
    public ScorecardContent update(ScorecardContent entity) {
        scorecardExplanationBean.update(entity.getSelfScore());
        scorecardExplanationBean.update(entity.getDeptScore());
        scorecardExplanationBean.update(entity.getGeneralScore());
        scorecardExplanationBean.update(entity.getOtherScore());
        scorecardExplanationBean.getEntityManager().flush();
        return super.update(entity);
    }

}
