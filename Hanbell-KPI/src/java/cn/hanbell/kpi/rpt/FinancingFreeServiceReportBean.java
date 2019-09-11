/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.web.BscQueryTableManageBean;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "financingFreeServiceReportBean")
@ViewScoped
public class FinancingFreeServiceReportBean extends BscQueryTableManageBean implements Serializable {

    @EJB
    protected IndicatorChartBean indicatorChartBean;

    protected int y;
    protected int m;
    protected Date btndate;
    protected String facno;
    protected String[] title;
    protected List<String[]> list;

    protected final DecimalFormat decimalFormat;


    public FinancingFreeServiceReportBean() {
        this.decimalFormat = new DecimalFormat("#,###.##");
    }

    public Calendar settlementDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(getUserManagedBean().getBaseDate());
        return c;
    }

    public Calendar getDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(btndate);
        return c;
    }

    @PostConstruct
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
        }else {
            for (RoleGrantModule m1 : userManagedBean.getRoleGrantDeptList()) {
                if (m1.getDeptno().equals(indicatorChart.getPid())) {
                    deny = false;
                }
            }
        }
        btndate = settlementDate().getTime();
        btnquery();
    }

    public void btnreset() {;
        btndate = settlementDate().getTime();
        btnquery();
    }

    public void btnquery() {
        title = new String[9];
        list = new ArrayList<>();
        m = getDate().get(Calendar.MONTH) + 1;
        y = getDate().get(Calendar.YEAR);
        boolean aa = true;
        if (getBtndate().after(settlementDate().getTime())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "日期选择不能超过系统结算日期！"));
            aa = false;
        }
        indicator = indicatorBean.findByFormidYearAndDeptno(getIndicatorChart().getFormid(), y, getIndicatorChart().getDeptno());
        if (indicator == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        if (indicator.isAssigned() && aa) {
            List<Indicator> indicatorList = indicatorBean.findByPId(indicator.getId());
            title[0] = "项目";
            title[1] = "考核部门";
            title[2] = "目标全年";
            if (m >= 1 && m < 4) {
                title[3] = "Q1目标(1~3月)";
            } else if (m >= 4 && m < 7) {
                title[3] = "Q2目标(4~6月)";
            } else if (m >= 7 && m < 10) {
                title[3] = "Q3目标(7~9月)";
            } else {
                title[3] = "Q4目标(10~12月)";
            }
            title[4] = m == 1 ? (y - 1) + "年12月实绩" : (m - 1) + "月实绩";
            title[5] = m + "月实绩";
            title[6] = m == 1 ? y + "年累计（1月)" : y + "年累计（1-" + m + "月)";
            title[7] = m == 1 ? (y - 1) + "年累计（1月)" : (y - 1) + "年累计（1-" + m + "月)";
            title[8] = "累计与上年同期对比差异";
            String[] arr;
            String[] arrsum = new String[10];
            arrsum[0] = "合计";
            arrsum[1] = "";
            arrsum[2] = "0";
            arrsum[3] = "0";
            arrsum[4] = "0";
            arrsum[5] = "0";
            arrsum[6] = "0";
            arrsum[7] = "0";
            arrsum[8] = "0";
            String mon;
            Field f;
            //排序
            indicatorList.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() < o2.getSortid()) {
                    return -1;
                } else {
                    return 1;
                }
            });
            try {
                for (Indicator idt : indicatorList) {
                    if (!"5".equals(idt.getDeptno().substring(0, 1))) {
                        //换算率
                        indicatorBean.divideByRate(idt, 2);
                        arr = new String[10];
                        arr[0] = idt.getName().replace("免费服务", "");
                        arr[1] = idt.getDeptname();
                        arr[2] = idt.getTargetIndicator().getNfy().toString();
                        arrsum[2] = BigDecimal.valueOf(Double.parseDouble(arrsum[2])).add(idt.getTargetIndicator().getNfy()).toString();
                        if (m >= 1 && m < 4) {
                            arr[3] = idt.getTargetIndicator().getNq1().toString();
                        } else if (m >= 4 && m < 7) {
                            arr[3] = idt.getTargetIndicator().getNq2().toString();
                        } else if (m >= 7 && m < 10) {
                            arr[3] = idt.getTargetIndicator().getNq3().toString();
                        } else {
                            arr[3] = idt.getTargetIndicator().getNq4().toString();
                        }
                        arrsum[3] = BigDecimal.valueOf(Double.parseDouble(arrsum[3])).add(BigDecimal.valueOf(Double.parseDouble(arr[3]))).toString();
                        if (m == 1) {
                            arr[4] = idt.getBenchmarkIndicator().getN12().toString();
                        } else {
                            mon = indicatorBean.getIndicatorColumn("N", m - 1);
                            f = idt.getActualIndicator().getClass().getDeclaredField(mon);
                            f.setAccessible(true);
                            arr[4] = f.get(idt.getActualIndicator()).toString();
                        }
                        arrsum[4] = BigDecimal.valueOf(Double.parseDouble(arrsum[4])).add(BigDecimal.valueOf(Double.parseDouble(arr[4]))).toString();
                        mon = indicatorBean.getIndicatorColumn("N", m);
                        f = idt.getActualIndicator().getClass().getDeclaredField(mon);
                        f.setAccessible(true);
                        arr[5] = f.get(idt.getActualIndicator()).toString();
                        arrsum[5] = BigDecimal.valueOf(Double.parseDouble(arrsum[5])).add(BigDecimal.valueOf(Double.parseDouble(arr[5]))).toString();
                        BigDecimal sumA = indicatorBean.getAccumulatedValue(idt.getActualIndicator(), m);
                        arr[6] = sumA.toString();
                        arrsum[6] = BigDecimal.valueOf(Double.parseDouble(arrsum[6])).add(sumA).toString();
                        BigDecimal sumB = indicatorBean.getAccumulatedValue(idt.getBenchmarkIndicator(), m);
                        arr[7] = sumB.toString();
                        arrsum[7] = BigDecimal.valueOf(Double.parseDouble(arrsum[7])).add(sumB).toString();
                        arr[8] = sumA.subtract(sumB).toString();
                        arrsum[8] = BigDecimal.valueOf(Double.parseDouble(arrsum[8])).add(BigDecimal.valueOf(Double.parseDouble(arr[8]))).toString();
                        arr[9] = Double.parseDouble(arr[8]) > 0 ? "red" : "black";
                        list.add(arr);
                    }
                }
                arrsum[9] = Double.parseDouble(arrsum[8]) > 0 ? "red" : "black";
                list.add(arrsum);
            } catch (Exception e) {
                System.out.println("cn.hanbell.kpi.rpt.FreeServiceChartReportBean.init()" + e.toString());
            }
        }
    }

    public String format(BigDecimal value) {
        return decimalFormat.format(value); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the btndate
     */
    public Date getBtndate() {
        return btndate;
    }

    /**
     * @param btndate the btndate to set
     */
    public void setBtndate(Date btndate) {
        this.btndate = btndate;
    }

    /**
     * @return the title
     */
    public String[] getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String[] title) {
        this.title = title;
    }

    /**
     * @return the list
     */
    public List<String[]> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<String[]> list) {
        this.list = list;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

}
