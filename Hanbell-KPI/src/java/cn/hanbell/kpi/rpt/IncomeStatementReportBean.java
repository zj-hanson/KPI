/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.control.UserManagedBean;
import cn.hanbell.kpi.ejb.IncomeStatementBean;
import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.entity.IndicatorChart;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "incomeStatementReportBean")
@ViewScoped
public class IncomeStatementReportBean implements Serializable {

    @EJB
    protected IncomeStatementBean incomeStatementBean;

    @EJB
    protected IndicatorChartBean indicatorChartBean;

    protected boolean checkbox;
    protected Date btndate;
    protected LinkedHashMap<String, String[]> map;
    protected LinkedHashMap<String, String> statusMap;

    @ManagedProperty(value = "#{userManagedBean}")
    protected UserManagedBean userManagedBean;

    FacesContext fc;
    ExternalContext ec;
    protected IndicatorChart indicatorChart;

    public IncomeStatementReportBean() {
    }

    public Calendar settlementDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(getUserManagedBean().getBaseDate());
        return c;
    }

    public Calendar getdate() {
        Calendar c = Calendar.getInstance();
        c.setTime(getBtndate());
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
        if (indicatorChart == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        map = new LinkedHashMap<>();
        statusMap = new LinkedHashMap<>();
        checkbox = true;
        statusMap.put("title", checkbox ? "(当月)" : "(年度累计)");
        statusMap.put("displaydiv1", "block");
        statusMap.put("displaydiv2", "none");
        setBtndate(settlementDate().getTime());
    }

    public void btnreset() {;
        checkbox = true;
        statusMap.put("title", checkbox ? "(当月)" : "(年度累计)");
        statusMap.put("displaydiv1", "block");
        statusMap.put("displaydiv2", "none");
        setBtndate(settlementDate().getTime());
    }

    public void btnquery() {
        map = new LinkedHashMap<>();
        statusMap.put("displaydiv1", "block");
        statusMap.put("displaydiv2", "none");
        statusMap.put("title", checkbox ? "(当月)" : "(年度累计)");
        boolean aa = true;
        if (getBtndate().after(settlementDate().getTime())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "日期选择不能超过系统结算日期！"));
            aa = false;
        }
        if (aa) {
            if (checkbox) {
                map = incomeStatementBean.monthMap(btndate);

            } else {
                map = incomeStatementBean.yearMap(btndate);
            }
            if (map != null && !map.isEmpty()) {
                statusMap.put("displaydiv1", "none");
                statusMap.put("displaydiv2", "block");
                statusMap.put("th1title", checkbox ? getdate().get(Calendar.YEAR) + "年" + (getdate().get(Calendar.MONTH) + 1) + "月"
                        : getdate().get(Calendar.YEAR) + "年1～" + (getdate().get(Calendar.MONTH) + 1) + "月");
                statusMap.put("th2title", checkbox ? (getdate().get(Calendar.YEAR) - 1) + "年" + (getdate().get(Calendar.MONTH) + 1) + "月"
                        : (getdate().get(Calendar.YEAR) - 1) + "年1～" + (getdate().get(Calendar.MONTH) + 1) + "月");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "无法查询到该日期的数据，请重新查询！"));
            }

        }

    }

    /**
     * @return the checkbox
     */
    public boolean isCheckbox() {
        return checkbox;
    }

    /**
     * @param checkbox the checkbox to set
     */
    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    /**
     * @return the map
     */
    public LinkedHashMap<String, String[]> getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(LinkedHashMap<String, String[]> map) {
        this.map = map;
    }

    /**
     * @return the statusMap
     */
    public LinkedHashMap<String, String> getStatusMap() {
        return statusMap;
    }

    /**
     * @param statusMap the statusMap to set
     */
    public void setStatusMap(LinkedHashMap<String, String> statusMap) {
        this.statusMap = statusMap;
    }

    /**
     * @return the userManagedBean
     */
    public UserManagedBean getUserManagedBean() {
        return userManagedBean;
    }

    /**
     * @param userManagedBean the userManagedBean to set
     */
    public void setUserManagedBean(UserManagedBean userManagedBean) {
        this.userManagedBean = userManagedBean;
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

}
