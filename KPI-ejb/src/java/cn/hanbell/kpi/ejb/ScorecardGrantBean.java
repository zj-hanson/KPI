/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.ScorecardGrant;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class ScorecardGrantBean extends SuperEJBForKPI<ScorecardGrant> {

    public ScorecardGrantBean() {
        super(ScorecardGrant.class);
    }

    @Override
    public ScorecardGrant findById(int value) {
        return super.findById(value); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ScorecardGrant findByCompanyAndScorecardidAndContentidAndSeq(String company, int scorecardid, int contentid, int seq) {
        Query query = getEntityManager().createNamedQuery("ScorecardGrant.findByCompanyAndScorecardidAndContentidAndSeq");
        query.setParameter("company", company);
        query.setParameter("scorecardid", scorecardid);
        query.setParameter("contentid", contentid);
        query.setParameter("seq", seq);
        try {
            Object o = query.getSingleResult();
            return (ScorecardGrant) o;
        } catch (Exception ex) {
            return null;
        }
    }

}
