/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.RoleGrantModule;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "turnoverdaysBean")
@ViewScoped
public class TurnoverdaysBean extends FinancingFreeServiceReportBean {

    protected List<Indicator> indicators;

    public TurnoverdaysBean() {
        super();
    }

    @PostConstruct
    @Override
    public void construct() {
        fc = FacesContext.getCurrentInstance();
        ec = fc.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String id = request.getParameter("id");
        if (id == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        indicatorChart = indicatorChartBean.findById(Integer.valueOf(id));
        if (getIndicatorChart() == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        } else {
            for (RoleGrantModule m1 : userManagedBean.getRoleGrantDeptList()) {
                if (m1.getDeptno().equals(indicatorChart.getPid())) {
                    deny = false;
                }
            }
        }
        btndate = settlementDate().getTime();
        btnquery();
    }

    @Override
    public void btnquery() {
        firstList.clear();
        secondList.clear();
        thirdlyList.clear();
        List<Indicator> list = new ArrayList<>();
        m = getDate().get(Calendar.MONTH) + 1;
        y = getDate().get(Calendar.YEAR);
        boolean aa = true;
        if (getBtndate().after(settlementDate().getTime())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "日期选择不能超过系统结算日期！"));
            aa = false;
        }
        //通过备注分割指标第一个为应收 第二个为库存 第三个为应付
        String[] arr = indicatorChart.getRemark().split(",");
        //1、应收
        indicator = indicatorBean.findByFormidYearAndDeptno(arr[0], y, indicatorChart.getDeptno());
        if (indicator == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        if (indicator.isAssigned() && aa) {
            firstList = indicatorBean.findByPId(indicator.getId());
            //排序
            firstList.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() < o2.getSortid()) {
                    return -1;
                } else {
                    return 1;
                }
            });
            //2、库存 营业所有、服务合计、生产合计
            indicator = new Indicator();
            if (arr.length >= 2) {
                //营业所有 不要营业总产品
                indicator = indicatorBean.findByFormidYearAndDeptno(arr[1], y, indicatorChart.getDeptno());
                if (indicator != null) {
                    secondList = indicatorBean.findByPId(indicator.getId());
                    if (secondList != null) {
                        for (Indicator i : secondList) {
                            if (i.getCategory().contains("营业周转天数") && !i.getDescript().contains("合计")) {
                                i.getParent().setName("库存周转天数");
                                firstList.add(i);
                            }
                        }
                    }
                }
                //生产合计
                indicator = indicatorBean.findByFormidYearAndDeptno("生产周转天数合计", y, indicatorChart.getDeptno());
                indicator.getParent().setName("库存周转天数");
                firstList.add(indicator);
                //服务合计
                indicator = indicatorBean.findByFormidYearAndDeptno("服务周转天数合计", y, indicatorChart.getDeptno());
                indicator.getParent().setName("库存周转天数");
                firstList.add(indicator);
                //周转总合计
                indicator = indicatorBean.findByFormidYearAndDeptno("库存周转天数总计", y, indicatorChart.getDeptno());
                indicator.setParent(indicator);
                indicator.getParent().setName("库存周转天数");
                firstList.add(indicator);
            }
            //提出总产品指标
            firstList.forEach(a -> {
                if ("总产品".equals(a.getDescript())) {
                    list.add(a);
                }
            });
            //营业周期
            if (list.size() > 0) {
                sumIndicator = getSumValue(list);
                sumIndicator.setParent(null);
                sumIndicator.setDescript("营业周期");
                sumIndicator.setProduct("");
                sumIndicator.setDeptname("");
                sumIndicator.setUsername("");
                firstList.add(sumIndicator);
            }
            //3、应付
            indicator = new Indicator();
            if (arr.length >= 3) {
                indicator = indicatorBean.findByFormidYearAndDeptno(arr[2], y, indicatorChart.getDeptno());
                if (indicator != null) {
                    thirdlyList = indicatorBean.findByPId(indicator.getId());
                    //排序
                    thirdlyList.sort((Indicator o1, Indicator o2) -> {
                        if (o1.getSortid() < o2.getSortid()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    });
                    firstList.addAll(thirdlyList);
                    thirdlyList.forEach(a -> {
                        if (a.getDescript().contains("总")) {
                            indicatorSubtract(sumIndicator, a);
                            indicator.setParent(null);
                            indicator.setDescript("现金周期");
                            indicator.setDeptname("");
                            indicator.setUsername("");
                            indicator.setProduct("");
                            firstList.add(indicator);
                        }
                    });
                }
            }
            getRemarkOne(indicatorChart, y, m);
        }
    }

    protected void indicatorSubtract(Indicator first, Indicator second) {
        indicator.setActualIndicator(detailSubtract(first.getActualIndicator(), second.getActualIndicator()));
        indicator.setForecastIndicator(detailSubtract(first.getForecastIndicator(), second.getForecastIndicator()));
        indicator.setTargetIndicator(detailSubtract(first.getTargetIndicator(), second.getTargetIndicator()));
        indicator.setOther1Indicator(detailSubtract(first.getOther1Indicator(), second.getOther1Indicator()));
    }

    //IndicatorDaily两两相减
    protected IndicatorDetail detailSubtract(IndicatorDetail a, IndicatorDetail b) {
        IndicatorDetail detail = new IndicatorDetail();
        detail.setParent(indicator);
        detail.setType("A");
        try {
            detail.setN01(a.getN01().subtract(b.getN01()));
            detail.setN02(a.getN02().subtract(b.getN02()));
            detail.setN03(a.getN03().subtract(b.getN03()));
            detail.setN04(a.getN04().subtract(b.getN04()));
            detail.setN05(a.getN05().subtract(b.getN05()));
            detail.setN06(a.getN06().subtract(b.getN06()));
            detail.setN07(a.getN07().subtract(b.getN07()));
            detail.setN08(a.getN08().subtract(b.getN08()));
            detail.setN09(a.getN09().subtract(b.getN09()));
            detail.setN10(a.getN10().subtract(b.getN10()));
            detail.setN11(a.getN11().subtract(b.getN11()));
            detail.setN12(a.getN12().subtract(b.getN12()));
            detail.setNq1(a.getNq1().subtract(b.getNq1()));
            detail.setNq2(a.getNq2().subtract(b.getNq2()));
            detail.setNq3(a.getNq3().subtract(b.getNq3()));
            detail.setNq4(a.getNq4().subtract(b.getNq4()));
            detail.setNh1(a.getNh1().subtract(b.getNh1()));
            detail.setNh2(a.getNh2().subtract(b.getNh2()));
            detail.setNfy(a.getNfy().subtract(b.getNfy()));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return detail;
    }

    public Indicator getSumValue(List<Indicator> indicators) {
        if (indicators.isEmpty()) {
            return null;
        }
        Indicator entity = null;
        IndicatorDetail a, b, f, t, o1;
        IndicatorDetail sa, sb, sf, st, sp, so1;
        try {
            entity = (Indicator) BeanUtils.cloneBean(indicators.get(0));
            entity.setId(-1);
            sa = new IndicatorDetail();
            sa.setParent(entity);
            sa.setType("A");
            sb = new IndicatorDetail();
            sb.setParent(entity);
            sb.setType("B");
            sf = new IndicatorDetail();
            sf.setParent(entity);
            sf.setType("F");
            st = new IndicatorDetail();
            st.setParent(entity);
            st.setType("T");
            sp = new IndicatorDetail();
            sp.setParent(entity);
            sp.setType("P");
            so1 = new IndicatorDetail();
            so1.setParent(entity);
            so1.setType("O1");
            entity.setActualIndicator(sa);
            entity.setBenchmarkIndicator(sb);
            entity.setForecastIndicator(sf);
            entity.setTargetIndicator(st);
            entity.setPerformanceIndicator(sp);
            entity.setOther1Indicator(so1);
            for (int i = 0; i < indicators.size(); i++) {
                a = indicators.get(i).getActualIndicator();
                b = indicators.get(i).getBenchmarkIndicator();
                f = indicators.get(i).getForecastIndicator();
                t = indicators.get(i).getTargetIndicator();
                o1 = indicators.get(i).getOther1Indicator();
                indicatorBean.addValue(entity.getOther1Indicator(), o1, entity.getFormkind());
                indicatorBean.addValue(entity.getActualIndicator(), a, entity.getFormkind());
                indicatorBean.addValue(entity.getBenchmarkIndicator(), b, entity.getFormkind());
                indicatorBean.addValue(entity.getForecastIndicator(), f, entity.getFormkind());
                indicatorBean.addValue(entity.getTargetIndicator(), t, entity.getFormkind());
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            Logger.getLogger(IndicatorBean.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return entity;
    }

}
