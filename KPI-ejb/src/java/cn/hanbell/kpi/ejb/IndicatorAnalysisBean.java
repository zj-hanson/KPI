/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.IndicatorAnalysis;
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
public class IndicatorAnalysisBean extends SuperEJBForKPI<IndicatorAnalysis> {

    public IndicatorAnalysisBean() {
        super(IndicatorAnalysis.class);
    }

    public List<IndicatorAnalysis> findByPIdAndMonth(int pid, int m) {
        Query query = getEntityManager().createNamedQuery("IndicatorAnalysis.findByPIdAndMonth");
        query.setParameter("pid", pid);
        query.setParameter("m", m);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

}
