/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.IndicatorDepartment;
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
public class IndicatorDepartmentBean extends SuperEJBForKPI<IndicatorDepartment> {

    public IndicatorDepartmentBean() {
        super(IndicatorDepartment.class);
    }

    public void deleteByPId(int pid) {
        List<IndicatorDepartment> details = findByPId(pid);
        if (details != null && !details.isEmpty()) {
            delete(details);
        }
    }

    public List<IndicatorDepartment> findByDeptno(String deptno) {
        Query query = getEntityManager().createNamedQuery("IndicatorDepartment.findByDeptno");
        query.setParameter("deptno", deptno);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

}
