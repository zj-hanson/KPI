/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.IndicatorAssignment;
import cn.hanbell.kpi.entity.IndicatorDepartment;
import cn.hanbell.kpi.entity.IndicatorDetail;
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
public class IndicatorAssignmentBean extends SuperEJBForKPI<IndicatorAssignment> {

    @EJB
    private IndicatorDepartmentBean indicatorDepartmentBean;

    @EJB
    private IndicatorDetailBean indicatorDetailBean;

    public IndicatorAssignmentBean() {
        super(IndicatorAssignment.class);
    }

    @Override
    public void delete(IndicatorAssignment entity) {
        super.delete(entity);
        if (entity.isAssigned()) {
            deleteByPIdAndYear(entity.getId(), entity.getSeq());
        } else {
            indicatorDetailBean.deleteByPId(entity.getId());
            indicatorDepartmentBean.deleteByPId(entity.getId());
        }
    }

    public void deleteByPIdAndYear(int pid, int y) {
        List<IndicatorAssignment> list = findByPIdAndYear(pid, y);
        if (list != null && !list.isEmpty()) {
            for (IndicatorAssignment i : list) {
                delete(i);
            }
        }
        indicatorDetailBean.deleteByPId(pid);
        indicatorDepartmentBean.deleteByPId(pid);
    }

    public List<IndicatorAssignment> findByPIdAndYear(int pid, int y) {
        Query query = getEntityManager().createNamedQuery("IndicatorAssignment.findByPIdAndSeq");
        query.setParameter("pid", pid);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void persist(IndicatorAssignment entity) {
        IndicatorAssignment e = entity;
        super.persist(entity);
        this.getEntityManager().flush();
        //目标
        IndicatorDetail t = new IndicatorDetail();
        t.setPid(entity.getId());
        t.setSeq(1);
        t.setType("T");
        indicatorDetailBean.persist(t);
        indicatorDetailBean.getEntityManager().flush();
        entity.setTargetIndicator(t);
        //基准
        IndicatorDetail b = new IndicatorDetail();
        b.setPid(entity.getId());
        b.setSeq(2);
        b.setType("B");
        indicatorDetailBean.persist(b);
        indicatorDetailBean.getEntityManager().flush();
        entity.setBenchmarkIndicator(b);
        //预期
        IndicatorDetail f = new IndicatorDetail();
        f.setPid(entity.getId());
        f.setSeq(3);
        f.setType("F");
        indicatorDetailBean.persist(f);
        indicatorDetailBean.getEntityManager().flush();
        entity.setForecastIndicator(f);
        //实际
        IndicatorDetail a = new IndicatorDetail();
        a.setPid(entity.getId());
        a.setSeq(4);
        a.setType("A");
        indicatorDetailBean.persist(a);
        indicatorDetailBean.getEntityManager().flush();
        entity.setActualIndicator(a);
        //绩效
        IndicatorDetail p = new IndicatorDetail();
        p.setPid(entity.getId());
        p.setSeq(5);
        p.setType("P");
        indicatorDetailBean.persist(p);
        indicatorDetailBean.getEntityManager().flush();
        entity.setPerformanceIndicator(p);
        //设置指标明细后保存
        update(entity);
        //设置指标关联部门
        IndicatorDepartment dept = new IndicatorDepartment();
        dept.setPid(entity.getId());
        dept.setSeq(entity.getSeq());
        dept.setDeptno(entity.getDeptno());
        dept.setDeptname(entity.getDeptname());
        indicatorDepartmentBean.persist(dept);
    }

    @Override
    public IndicatorAssignment update(IndicatorAssignment entity) {
        indicatorDetailBean.update(entity.getTargetIndicator());
        indicatorDetailBean.update(entity.getBenchmarkIndicator());
        indicatorDetailBean.update(entity.getForecastIndicator());
        indicatorDetailBean.update(entity.getActualIndicator());
        indicatorDetailBean.update(entity.getPerformanceIndicator());
        return super.update(entity);
    }

}
