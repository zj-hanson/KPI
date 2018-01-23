/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDepartment;
import cn.hanbell.kpi.entity.IndicatorDetail;
import com.lightshell.comm.SuperEJB;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
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
public class IndicatorBean extends SuperEJBForKPI<Indicator> {

    @EJB
    private IndicatorDepartmentBean indicatorDepartmentBean;

    @EJB
    private IndicatorDetailBean indicatorDetailBean;

    public IndicatorBean() {
        super(Indicator.class);
    }

    @Override
    public void delete(Indicator entity, HashMap<SuperEJB, List<?>> detailDeleted) {
        super.delete(entity, detailDeleted);
        indicatorDetailBean.deleteByPId(entity.getId());
    }

    public void deleteByFormidYearAndDeptno(String formid, int y, String value) {
        Indicator i = findByFormidYearAndDeptno(formid, y, value);
        if (i != null) {
            indicatorDetailBean.deleteByPId(i.getId());
            indicatorDepartmentBean.deleteByPId(i.getId());
            delete(i);
        }
    }

    public void deleteByPIdAndYear(int pid, int y) {
        Indicator i = findByPIdAndYear(pid, y);
        if (i != null) {
            indicatorDetailBean.deleteByPId(i.getId());
            indicatorDepartmentBean.deleteByPId(i.getId());
            delete(i);
        }
    }

    public Indicator findByFormidYearAndDeptno(String formid, int y, String value) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByFormidSeqAndDeptno");
        query.setParameter("formid", formid);
        query.setParameter("seq", y);
        query.setParameter("deptno", value);
        try {
            Object o = query.getSingleResult();
            return (Indicator) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public Indicator findByPIdAndYear(int pid, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByPIdAndSeq");
        query.setParameter("pid", pid);
        query.setParameter("seq", y);
        try {
            Object o = query.getSingleResult();
            return (Indicator) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findRootByCompany(String company, String objtype) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByCompany");
        query.setParameter("company", company);
        query.setParameter("objtype", objtype);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void persist(Indicator entity) {
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
    public Indicator update(Indicator entity) {
        indicatorDetailBean.update(entity.getTargetIndicator());
        indicatorDetailBean.update(entity.getBenchmarkIndicator());
        indicatorDetailBean.update(entity.getForecastIndicator());
        indicatorDetailBean.update(entity.getActualIndicator());
        indicatorDetailBean.update(entity.getPerformanceIndicator());
        return super.update(entity);
    }

    public void updatePerformance(Indicator entity) {
        if (entity != null) {
            switch (entity.getPerfCalc()) {
                case "AT":
                    if (entity.getTargetIndicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN01(entity.getActualIndicator().getN01().divide(entity.getTargetIndicator().getN01(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN02(entity.getActualIndicator().getN02().divide(entity.getTargetIndicator().getN02(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN03(entity.getActualIndicator().getN03().divide(entity.getTargetIndicator().getN03(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN04(entity.getActualIndicator().getN04().divide(entity.getTargetIndicator().getN04(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN05(entity.getActualIndicator().getN05().divide(entity.getTargetIndicator().getN05(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN06(entity.getActualIndicator().getN06().divide(entity.getTargetIndicator().getN06(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN07(entity.getActualIndicator().getN07().divide(entity.getTargetIndicator().getN07(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN08(entity.getActualIndicator().getN08().divide(entity.getTargetIndicator().getN08(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN09(entity.getActualIndicator().getN09().divide(entity.getTargetIndicator().getN09(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN10(entity.getActualIndicator().getN10().divide(entity.getTargetIndicator().getN10(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN11(entity.getActualIndicator().getN11().divide(entity.getTargetIndicator().getN11(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN12(entity.getActualIndicator().getN12().divide(entity.getTargetIndicator().getN12(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getNq1().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setNq1(entity.getActualIndicator().getNq1().divide(entity.getTargetIndicator().getNq1(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getNq2().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setNq2(entity.getActualIndicator().getNq2().divide(entity.getTargetIndicator().getNq2(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getNq3().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setNq3(entity.getActualIndicator().getNq3().divide(entity.getTargetIndicator().getNq3(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getNq4().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setNq4(entity.getActualIndicator().getNq4().divide(entity.getTargetIndicator().getNq4(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getNh1().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setNh1(entity.getActualIndicator().getNh1().divide(entity.getTargetIndicator().getNh1(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getNh2().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setNh2(entity.getActualIndicator().getNh2().divide(entity.getTargetIndicator().getNh2(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getTargetIndicator().getNfy().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setNfy(entity.getActualIndicator().getNfy().divide(entity.getTargetIndicator().getNfy(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    break;
                case "TA":
                    if (entity.getActualIndicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN01(entity.getTargetIndicator().getN01().divide(entity.getActualIndicator().getN01(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN02(entity.getTargetIndicator().getN02().divide(entity.getActualIndicator().getN02(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN03(entity.getTargetIndicator().getN03().divide(entity.getActualIndicator().getN03(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN04(entity.getTargetIndicator().getN04().divide(entity.getActualIndicator().getN04(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN05(entity.getTargetIndicator().getN05().divide(entity.getActualIndicator().getN05(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN06(entity.getTargetIndicator().getN06().divide(entity.getActualIndicator().getN06(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN07(entity.getTargetIndicator().getN07().divide(entity.getActualIndicator().getN07(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN08(entity.getTargetIndicator().getN08().divide(entity.getActualIndicator().getN08(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN09(entity.getTargetIndicator().getN09().divide(entity.getActualIndicator().getN09(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN10(entity.getTargetIndicator().getN10().divide(entity.getActualIndicator().getN10(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN11(entity.getTargetIndicator().getN11().divide(entity.getActualIndicator().getN11(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setN12(entity.getTargetIndicator().getN12().divide(entity.getActualIndicator().getN12(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getNq1().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setNq1(entity.getTargetIndicator().getNq1().divide(entity.getActualIndicator().getNq1(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getNq2().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setNq2(entity.getTargetIndicator().getNq2().divide(entity.getActualIndicator().getNq2(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getNq3().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setNq3(entity.getTargetIndicator().getNq3().divide(entity.getActualIndicator().getNq3(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getNq4().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setNq4(entity.getTargetIndicator().getNq4().divide(entity.getActualIndicator().getNq4(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getNh1().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setNh1(entity.getTargetIndicator().getNh1().divide(entity.getActualIndicator().getNh1(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getNh2().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setNh2(entity.getTargetIndicator().getNh2().divide(entity.getActualIndicator().getNh2(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    if (entity.getActualIndicator().getNfy().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator().setNfy(entity.getTargetIndicator().getNfy().divide(entity.getActualIndicator().getNfy(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                    }
                    break;
            }
        }
    }

}
