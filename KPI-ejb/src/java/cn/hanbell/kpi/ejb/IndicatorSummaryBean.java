/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.IndicatorSummary;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class IndicatorSummaryBean extends SuperEJBForKPI<IndicatorSummary> {

    public IndicatorSummaryBean() {
        super(IndicatorSummary.class);
    }

    public List<IndicatorSummary> findByPIdAndMonth(int pid, int m) {
        Query query = getEntityManager().createNamedQuery("IndicatorSummary.findByPIdAndMonth");
        query.setParameter("pid", pid);
        query.setParameter("m", m);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

}
