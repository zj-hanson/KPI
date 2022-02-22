/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
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

    public List<IndicatorDepartment> findByCompanyDeptnoTypeAndYear(String company, String deptno, String objtype,
        int y) {
        Query query = getEntityManager().createNamedQuery("IndicatorDepartment.findByCompanyDeptnoTypeAndYear");
        query.setParameter("company", company);
        query.setParameter("deptno", deptno);
        query.setParameter("objtype", objtype);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<IndicatorDepartment> findByDeptnoAndType(String deptno, String objtype) {
        Query query = getEntityManager().createNamedQuery("IndicatorDepartment.findByDeptnoAndType");
        query.setParameter("deptno", deptno);
        query.setParameter("objtype", objtype);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<IndicatorDepartment> findByDeptnoTypeAndYear(String deptno, String objtype, int y) {
        Query query = getEntityManager().createNamedQuery("IndicatorDepartment.findByDeptnoTypeAndYear");
        query.setParameter("deptno", deptno);
        query.setParameter("objtype", objtype);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

}
