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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;
import org.apache.commons.beanutils.BeanUtils;
import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.entity.IndicatorAssignment;
import cn.hanbell.kpi.entity.IndicatorSet;
import java.util.ArrayList;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class IndicatorBean extends SuperEJBForKPI<Indicator> {

    @EJB
    private IndicatorAssignmentBean indicatorAssignmentBean;

    @EJB
    private IndicatorDepartmentBean indicatorDepartmentBean;

    @EJB
    private IndicatorDetailBean indicatorDetailBean;

    @EJB
    private IndicatorSetBean indicatorSetBean;

    protected Actual actualInterface;

    public IndicatorBean() {
        super(Indicator.class);
    }

    public void addValue(IndicatorDetail a, IndicatorDetail b, String formKind) {
        //先算汇总字段再算每月字段,A和S类型会重算汇总
        switch (formKind) {
            case "M":
                a.setNfy(a.getNfy().add(b.getNfy()));
                a.setNh2(a.getNh2().add(b.getNh2()));
                a.setNh1(a.getNh1().add(b.getNh1()));
                a.setNq4(a.getNq4().add(b.getNq4()));
                a.setNq3(a.getNq3().add(b.getNq3()));
                a.setNq2(a.getNq2().add(b.getNq2()));
                a.setNq1(a.getNq1().add(b.getNq1()));
                a.setN01(a.getN01().add(b.getN01()));
                a.setN02(a.getN02().add(b.getN02()));
                a.setN03(a.getN03().add(b.getN03()));
                a.setN04(a.getN04().add(b.getN04()));
                a.setN05(a.getN05().add(b.getN05()));
                a.setN06(a.getN06().add(b.getN06()));
                a.setN07(a.getN07().add(b.getN07()));
                a.setN08(a.getN08().add(b.getN08()));
                a.setN09(a.getN09().add(b.getN09()));
                a.setN10(a.getN10().add(b.getN10()));
                a.setN11(a.getN11().add(b.getN11()));
                a.setN12(a.getN12().add(b.getN12()));
                break;
            case "Q":
                a.setNfy(a.getNfy().add(b.getNfy()));
                a.setNh2(a.getNh2().add(b.getNh2()));
                a.setNh1(a.getNh1().add(b.getNh1()));
                a.setNq4(a.getNq4().add(b.getNq4()));
                a.setNq3(a.getNq3().add(b.getNq3()));
                a.setNq2(a.getNq2().add(b.getNq2()));
                a.setNq1(a.getNq1().add(b.getNq1()));
                break;
            case "H":
                a.setNfy(a.getNfy().add(b.getNfy()));
                a.setNh2(a.getNh2().add(b.getNh2()));
                a.setNh1(a.getNh1().add(b.getNh1()));
                break;
            case "Y":
                a.setNfy(a.getNfy().add(b.getNfy()));
        }
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
        List<Indicator> data = findByPIdAndYear(pid, y);
        if (data != null) {
            for (Indicator i : data) {
                indicatorDetailBean.deleteByPId(i.getId());
                indicatorDepartmentBean.deleteByPId(i.getId());
                delete(i);
            }
        }
    }

    public void divideByRate(Indicator i, int scale) {
        divideByRate(i.getActualIndicator(), i.getRate(), scale);
        divideByRate(i.getBenchmarkIndicator(), i.getRate(), scale);
        divideByRate(i.getForecastIndicator(), i.getRate(), scale);
        divideByRate(i.getTargetIndicator(), i.getRate(), scale);
    }

    public void divideByRate(IndicatorDetail id, BigDecimal rate, int scale) {
        //先算汇总字段再算每月字段,A和S类型会重算汇总
        id.setNfy(id.getNfy().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNh2(id.getNh2().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNh1(id.getNh1().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNq4(id.getNq4().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNq3(id.getNq3().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNq2(id.getNq2().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNq1(id.getNq1().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN01(id.getN01().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN02(id.getN02().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN03(id.getN03().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN04(id.getN04().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN05(id.getN05().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN06(id.getN06().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN07(id.getN07().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN08(id.getN08().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN09(id.getN09().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN10(id.getN10().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN11(id.getN11().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN12(id.getN12().divide(rate, scale, RoundingMode.HALF_UP));
    }

    public BigDecimal getAccumulatedGrowth(IndicatorDetail a, IndicatorDetail b, int m) {
        return getAccumulatedGrowth(a, b, m, 2);
    }

    public BigDecimal getAccumulatedGrowth(IndicatorDetail a, IndicatorDetail b, int m, int scale) {
        BigDecimal na, nb;
        na = getAccumulatedValue(a, m);
        nb = getAccumulatedValue(b, m);
        //计算
        if (nb.compareTo(BigDecimal.ZERO) != 0) {
            return na.divide(nb, scale, RoundingMode.HALF_UP).subtract(BigDecimal.ONE).multiply(BigDecimal.valueOf(100d));
        } else {
            return BigDecimal.valueOf(na.compareTo(nb)).multiply(BigDecimal.valueOf(100d));
        }
    }

    public BigDecimal getAccumulatedPerformance(IndicatorDetail a, IndicatorDetail b, int m) {
        return getAccumulatedPerformance(a, b, m, 2);
    }

    public BigDecimal getAccumulatedPerformance(IndicatorDetail a, IndicatorDetail b, int m, int scale) {
        BigDecimal na, nb;
        na = getAccumulatedValue(a, m);
        nb = getAccumulatedValue(b, m);
        //计算
        if (nb.compareTo(BigDecimal.ZERO) != 0) {
            return na.divide(nb, scale, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d));
        } else {
            return BigDecimal.valueOf(na.compareTo(nb)).multiply(BigDecimal.valueOf(100d));
        }
    }

    public BigDecimal getAccumulatedValue(IndicatorDetail entity, int m) {
        String mon;
        BigDecimal total = BigDecimal.ZERO;
        Field f;
        for (int i = 1; i <= m; i++) {
            try {
                mon = this.getIndicatorColumn("N", i);
                f = entity.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                total = total.add(BigDecimal.valueOf(Double.valueOf(f.get(entity).toString())));
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(IndicatorBean.class.getName()).log(Level.SEVERE, null, ex);
                total = BigDecimal.ZERO;
            }
        }
        return total;
    }

    public BigDecimal getGrowth(IndicatorDetail a, IndicatorDetail b, int m) {
        return getGrowth(a, b, m, 2);
    }

    public BigDecimal getGrowth(IndicatorDetail a, IndicatorDetail b, int m, int scale) {
        String mon;
        BigDecimal na, nb;
        Field f;
        try {
            mon = this.getIndicatorColumn("N", m);
            //实际值分子
            f = a.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            na = BigDecimal.valueOf(Double.valueOf(f.get(a).toString()));
            //比较值分母
            f = b.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            nb = BigDecimal.valueOf(Double.valueOf(f.get(b).toString()));
            //计算
            if (nb.compareTo(BigDecimal.ZERO) != 0) {
                return na.divide(nb, scale, RoundingMode.HALF_UP).subtract(BigDecimal.ONE).multiply(BigDecimal.valueOf(100d));
            } else {
                return BigDecimal.valueOf(na.compareTo(nb)).multiply(BigDecimal.valueOf(100d));
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(IndicatorBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    public String getIndicatorColumn(String formtype, int m) {
        if (formtype.equals("N")) {
            return "n" + String.format("%02d", m);
        } else {
            return "";
        }
    }

    public String getIndicatorColumn(String formtype, String c) {
        if (formtype.equals("N")) {
            return "n" + c;
        } else {
            return "";
        }
    }

    public Indicator getSumValue(List<Indicator> indicators) {
        if (indicators.isEmpty()) {
            return null;
        }
        Indicator entity = null;
        IndicatorDetail a, b, f, t;
        try {
            entity = (Indicator) BeanUtils.cloneBean(indicators.get(0));
            entity.setId(-1);
            entity.setName("合计");
            for (int i = 1; i < indicators.size(); i++) {
                a = indicators.get(i).getActualIndicator();
                b = indicators.get(i).getBenchmarkIndicator();
                f = indicators.get(i).getForecastIndicator();
                t = indicators.get(i).getTargetIndicator();
                addValue(entity.getActualIndicator(), a, entity.getFormkind());
                addValue(entity.getBenchmarkIndicator(), b, entity.getFormkind());
                addValue(entity.getForecastIndicator(), f, entity.getFormkind());
                addValue(entity.getTargetIndicator(), t, entity.getFormkind());
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            Logger.getLogger(IndicatorBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entity;
    }

    public List<Indicator> findByCategoryAndYear(String c, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByCategoryAndSeq");
        query.setParameter("category", c);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findByDeptnoObjtypeAndYear(String d, String t, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByDeptnoObjtypeAndYear");
        query.setParameter("deptno", d);
        query.setParameter("objtype", t);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findByFormidAndYear(String formid, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByFormidAndSeq");
        query.setParameter("formid", formid);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
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

    public Indicator findByIdAndYear(int id, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByIdAndSeq");
        query.setParameter("id", id);
        query.setParameter("seq", y);
        try {
            Object o = query.getSingleResult();
            return (Indicator) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findByObjtypeAndYear(String type, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByObjtypeAndSeq");
        query.setParameter("objtype", type);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findByPIdAndYear(int pid, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByPIdAndSeq");
        query.setParameter("pid", pid);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findRootByCompany(String company, String objtype, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findRootByCompany");
        query.setParameter("company", company);
        query.setParameter("objtype", objtype);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findRootByAssigned(String company, String objtype, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findRootByAssigned");
        query.setParameter("company", company);
        query.setParameter("objtype", objtype);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public String percentFormat(BigDecimal value) {
        return percentFormat(value, 2);
    }

    public String percentFormat(BigDecimal value, int scale) {
        return String.format("%s%%", value.setScale(scale, RoundingMode.HALF_UP).toString());
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

    public void updateActual(Indicator entity) {
        List<Indicator> indicators = new ArrayList<>();
        if (entity.isAssigned()) {
            List<IndicatorAssignment> assList = indicatorAssignmentBean.findByPId(entity.getId());
            for (IndicatorAssignment ia : assList) {
                Indicator i = findByFormidYearAndDeptno(ia.getFormid(), entity.getSeq(), ia.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        } else if (entity.getActualInterface() == null) {
            List<IndicatorSet> setList = indicatorSetBean.findByPId(entity.getId());
            for (IndicatorSet is : setList) {
                Indicator i = findByFormidYearAndDeptno(is.getFormid(), entity.getSeq(), is.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        }
        indicatorAssignmentBean.getEntityManager().clear();
        indicatorSetBean.getEntityManager().clear();
        getEntityManager().clear();
        if (!indicators.isEmpty()) {
            Indicator si = getSumValue(indicators);
            switch (entity.getFormkind()) {
                case "M":
                    entity.getActualIndicator().setNfy(si.getActualIndicator().getNfy());
                    entity.getActualIndicator().setNh2(si.getActualIndicator().getNh2());
                    entity.getActualIndicator().setNh1(si.getActualIndicator().getNh1());
                    entity.getActualIndicator().setNq1(si.getActualIndicator().getNq1());
                    entity.getActualIndicator().setNq2(si.getActualIndicator().getNq2());
                    entity.getActualIndicator().setNq3(si.getActualIndicator().getNq3());
                    entity.getActualIndicator().setNq4(si.getActualIndicator().getNq4());
                    entity.getActualIndicator().setN01(si.getActualIndicator().getN01());
                    entity.getActualIndicator().setN02(si.getActualIndicator().getN02());
                    entity.getActualIndicator().setN03(si.getActualIndicator().getN03());
                    entity.getActualIndicator().setN04(si.getActualIndicator().getN04());
                    entity.getActualIndicator().setN05(si.getActualIndicator().getN05());
                    entity.getActualIndicator().setN06(si.getActualIndicator().getN06());
                    entity.getActualIndicator().setN07(si.getActualIndicator().getN07());
                    entity.getActualIndicator().setN08(si.getActualIndicator().getN08());
                    entity.getActualIndicator().setN09(si.getActualIndicator().getN09());
                    entity.getActualIndicator().setN10(si.getActualIndicator().getN10());
                    entity.getActualIndicator().setN11(si.getActualIndicator().getN11());
                    entity.getActualIndicator().setN12(si.getActualIndicator().getN12());
                    break;
                case "Q":
                    entity.getActualIndicator().setNfy(si.getActualIndicator().getNfy());
                    entity.getActualIndicator().setNh2(si.getActualIndicator().getNh2());
                    entity.getActualIndicator().setNh1(si.getActualIndicator().getNh1());
                    entity.getActualIndicator().setNq1(si.getActualIndicator().getNq1());
                    entity.getActualIndicator().setNq2(si.getActualIndicator().getNq2());
                    entity.getActualIndicator().setNq3(si.getActualIndicator().getNq3());
                    entity.getActualIndicator().setNq4(si.getActualIndicator().getNq4());
                    break;
            }
        }
    }

    public void updateActual(Indicator entity, Object prop) {
        List<Indicator> indicators = new ArrayList<>();
        if (entity.isAssigned()) {
            List<IndicatorAssignment> assList = indicatorAssignmentBean.findByPId(entity.getId());
            for (IndicatorAssignment ia : assList) {
                Indicator i = findByFormidYearAndDeptno(ia.getFormid(), entity.getSeq(), ia.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        } else if (entity.getActualInterface() == null) {
            List<IndicatorSet> setList = indicatorSetBean.findByPId(entity.getId());
            for (IndicatorSet is : setList) {
                Indicator i = findByFormidYearAndDeptno(is.getFormid(), entity.getSeq(), is.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        }
        indicatorAssignmentBean.getEntityManager().clear();
        indicatorSetBean.getEntityManager().clear();
        getEntityManager().clear();
        if (!indicators.isEmpty()) {
            Indicator si = getSumValue(indicators);
            String col = "";
            Field f;
            Method setMethod;
            try {
                switch (entity.getFormtype() + entity.getFormkind()) {
                    case "NM":
                        col = this.getIndicatorColumn(entity.getFormtype(), Integer.valueOf(prop.toString()));
                        break;
                    case "NQ":
                        col = this.getIndicatorColumn(entity.getFormtype(), prop.toString());
                        break;
                }
                if (col != null && !"".equals(col)) {
                    f = entity.getActualIndicator().getClass().getDeclaredField(col);
                    f.setAccessible(true);
                    setMethod = entity.getActualIndicator().getClass().getDeclaredMethod("set" + col.substring(0, 1).toUpperCase() + col.substring(1), BigDecimal.class);
                    setMethod.invoke(entity.getActualIndicator(), BigDecimal.valueOf(Double.valueOf(f.get(entity.getActualIndicator()).toString())));
                }
            } catch (NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
                Logger.getLogger(IndicatorBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Indicator updateActual(int id, int y, int m, Date d, int type) {
        Indicator entity = findById(id);
        if ((entity != null) && (entity.getSeq() == y) && (entity.getActualInterface() != null)) {
            IndicatorDetail a = entity.getActualIndicator();
            try {
                actualInterface = (Actual) Class.forName(entity.getActualInterface()).newInstance();
                actualInterface.setEJB(entity.getActualEJB());
                BigDecimal na = actualInterface.getValue(y, m, d, type, actualInterface.getQueryParams());
                Method setMethod = a.getClass().getDeclaredMethod("set" + this.getIndicatorColumn("N", m).toUpperCase(), BigDecimal.class);
                setMethod.invoke(a, na);
                indicatorDetailBean.update(a);
            } catch (Exception ex) {
                Logger.getLogger(IndicatorBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return entity;
    }

    public void updateBenchmark(Indicator entity) {
        List<Indicator> indicators = new ArrayList<>();
        if (entity.isAssigned()) {
            List<IndicatorAssignment> assList = indicatorAssignmentBean.findByPId(entity.getId());
            for (IndicatorAssignment ia : assList) {
                Indicator i = findByFormidYearAndDeptno(ia.getFormid(), entity.getSeq(), ia.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        } else if (entity.getActualInterface() == null) {
            List<IndicatorSet> setList = indicatorSetBean.findByPId(entity.getId());
            for (IndicatorSet is : setList) {
                Indicator i = findByFormidYearAndDeptno(is.getFormid(), entity.getSeq(), is.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        }
        indicatorAssignmentBean.getEntityManager().clear();
        indicatorSetBean.getEntityManager().clear();
        getEntityManager().clear();
        if (!indicators.isEmpty()) {
            Indicator si = getSumValue(indicators);
            switch (entity.getFormkind()) {
                case "M":
                    entity.getBenchmarkIndicator().setNfy(si.getBenchmarkIndicator().getNfy());
                    entity.getBenchmarkIndicator().setNh2(si.getBenchmarkIndicator().getNh2());
                    entity.getBenchmarkIndicator().setNh1(si.getBenchmarkIndicator().getNh1());
                    entity.getBenchmarkIndicator().setNq1(si.getBenchmarkIndicator().getNq1());
                    entity.getBenchmarkIndicator().setNq2(si.getBenchmarkIndicator().getNq2());
                    entity.getBenchmarkIndicator().setNq3(si.getBenchmarkIndicator().getNq3());
                    entity.getBenchmarkIndicator().setNq4(si.getBenchmarkIndicator().getNq4());
                    entity.getBenchmarkIndicator().setN01(si.getBenchmarkIndicator().getN01());
                    entity.getBenchmarkIndicator().setN02(si.getBenchmarkIndicator().getN02());
                    entity.getBenchmarkIndicator().setN03(si.getBenchmarkIndicator().getN03());
                    entity.getBenchmarkIndicator().setN04(si.getBenchmarkIndicator().getN04());
                    entity.getBenchmarkIndicator().setN05(si.getBenchmarkIndicator().getN05());
                    entity.getBenchmarkIndicator().setN06(si.getBenchmarkIndicator().getN06());
                    entity.getBenchmarkIndicator().setN07(si.getBenchmarkIndicator().getN07());
                    entity.getBenchmarkIndicator().setN08(si.getBenchmarkIndicator().getN08());
                    entity.getBenchmarkIndicator().setN09(si.getBenchmarkIndicator().getN09());
                    entity.getBenchmarkIndicator().setN10(si.getBenchmarkIndicator().getN10());
                    entity.getBenchmarkIndicator().setN11(si.getBenchmarkIndicator().getN11());
                    entity.getBenchmarkIndicator().setN12(si.getBenchmarkIndicator().getN12());
                    break;
                case "Q":
                    entity.getBenchmarkIndicator().setNfy(si.getBenchmarkIndicator().getNfy());
                    entity.getBenchmarkIndicator().setNh2(si.getBenchmarkIndicator().getNh2());
                    entity.getBenchmarkIndicator().setNh1(si.getBenchmarkIndicator().getNh1());
                    entity.getBenchmarkIndicator().setNq1(si.getBenchmarkIndicator().getNq1());
                    entity.getBenchmarkIndicator().setNq2(si.getBenchmarkIndicator().getNq2());
                    entity.getBenchmarkIndicator().setNq3(si.getBenchmarkIndicator().getNq3());
                    entity.getBenchmarkIndicator().setNq4(si.getBenchmarkIndicator().getNq4());
                    break;
            }
        }
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

    public void updateTarget(Indicator entity) {
        List<Indicator> indicators = new ArrayList<>();
        if (entity.isAssigned()) {
            List<IndicatorAssignment> assList = indicatorAssignmentBean.findByPId(entity.getId());
            for (IndicatorAssignment ia : assList) {
                Indicator i = findByFormidYearAndDeptno(ia.getFormid(), entity.getSeq(), ia.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        } else if (entity.getActualInterface() == null) {
            List<IndicatorSet> setList = indicatorSetBean.findByPId(entity.getId());
            for (IndicatorSet is : setList) {
                Indicator i = findByFormidYearAndDeptno(is.getFormid(), entity.getSeq(), is.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        }
        indicatorAssignmentBean.getEntityManager().clear();
        indicatorSetBean.getEntityManager().clear();
        getEntityManager().clear();
        if (!indicators.isEmpty()) {
            Indicator si = getSumValue(indicators);
            switch (entity.getFormkind()) {
                case "M":
                    entity.getTargetIndicator().setNfy(si.getTargetIndicator().getNfy());
                    entity.getTargetIndicator().setNh2(si.getTargetIndicator().getNh2());
                    entity.getTargetIndicator().setNh1(si.getTargetIndicator().getNh1());
                    entity.getTargetIndicator().setNq1(si.getTargetIndicator().getNq1());
                    entity.getTargetIndicator().setNq2(si.getTargetIndicator().getNq2());
                    entity.getTargetIndicator().setNq3(si.getTargetIndicator().getNq3());
                    entity.getTargetIndicator().setNq4(si.getTargetIndicator().getNq4());
                    entity.getTargetIndicator().setN01(si.getTargetIndicator().getN01());
                    entity.getTargetIndicator().setN02(si.getTargetIndicator().getN02());
                    entity.getTargetIndicator().setN03(si.getTargetIndicator().getN03());
                    entity.getTargetIndicator().setN04(si.getTargetIndicator().getN04());
                    entity.getTargetIndicator().setN05(si.getTargetIndicator().getN05());
                    entity.getTargetIndicator().setN06(si.getTargetIndicator().getN06());
                    entity.getTargetIndicator().setN07(si.getTargetIndicator().getN07());
                    entity.getTargetIndicator().setN08(si.getTargetIndicator().getN08());
                    entity.getTargetIndicator().setN09(si.getTargetIndicator().getN09());
                    entity.getTargetIndicator().setN10(si.getTargetIndicator().getN10());
                    entity.getTargetIndicator().setN11(si.getTargetIndicator().getN11());
                    entity.getTargetIndicator().setN12(si.getTargetIndicator().getN12());
                    break;
                case "Q":
                    entity.getTargetIndicator().setNfy(si.getTargetIndicator().getNfy());
                    entity.getTargetIndicator().setNh2(si.getTargetIndicator().getNh2());
                    entity.getTargetIndicator().setNh1(si.getTargetIndicator().getNh1());
                    entity.getTargetIndicator().setNq1(si.getTargetIndicator().getNq1());
                    entity.getTargetIndicator().setNq2(si.getTargetIndicator().getNq2());
                    entity.getTargetIndicator().setNq3(si.getTargetIndicator().getNq3());
                    entity.getTargetIndicator().setNq4(si.getTargetIndicator().getNq4());
                    break;
            }
        }
    }

}
