/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.IndicatorChart;
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
public class IndicatorChartBean extends SuperEJBForKPI<IndicatorChart> {

    public IndicatorChartBean() {
        super(IndicatorChart.class);
    }

    public List<IndicatorChart> findByFormidAndDeptno(String formid, String deptno) {
        Query query = getEntityManager().createNamedQuery("IndicatorChart.findByFormidAndDeptno");
        query.setParameter("formid", formid);
        query.setParameter("deptno", deptno);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

}
