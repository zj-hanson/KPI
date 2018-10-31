/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.control.UserManagedBean;
import cn.hanbell.kpi.ejb.BalanceSheetBean;
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
@ManagedBean(name = "balanceSheetReportBean")
@ViewScoped
public class BalanceSheetReportBean implements Serializable {

    @EJB
    protected BalanceSheetBean balanceSheetBean;

    @EJB
    protected IndicatorChartBean indicatorChartBean;

    protected Date btndate;
    protected LinkedHashMap<String, String[]> zcmap;
    protected LinkedHashMap<String, String[]> fzmap;
    protected LinkedHashMap<String, String> statusMap;

    @ManagedProperty(value = "#{userManagedBean}")
    protected UserManagedBean userManagedBean;

    FacesContext fc;
    ExternalContext ec;
    protected IndicatorChart indicatorChart;

    public BalanceSheetReportBean() {
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
        zcmap = new LinkedHashMap<>();
        fzmap = new LinkedHashMap<>();
        statusMap = new LinkedHashMap<>();
        statusMap.put("displaydiv1", "block");
        statusMap.put("displaydiv2", "none");
        setBtndate(settlementDate().getTime());
    }

    public void btnreset() {;
        statusMap.put("displaydiv1", "block");
        statusMap.put("displaydiv2", "none");
        setBtndate(settlementDate().getTime());
    }

    public void btnquery() {
        zcmap = new LinkedHashMap<>();
        fzmap = new LinkedHashMap<>();
        statusMap.put("displaydiv1", "block");
        statusMap.put("displaydiv2", "none");
        boolean aa = true;
        if (getBtndate().after(settlementDate().getTime())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "日期选择不能超过系统结算日期！"));
            aa = false;
        }
        if (aa) {
            zcmap = balanceSheetBean.assetMap(btndate);
            fzmap = balanceSheetBean.liabilitiesMap(btndate);
            if ((zcmap != null && !zcmap.isEmpty()) || (fzmap != null && !fzmap.isEmpty())) {
                statusMap.put("displaydiv1", "none");
                statusMap.put("displaydiv2", "block");
                statusMap.put("th1title", getdate().get(Calendar.YEAR) + "年" + (getdate().get(Calendar.MONTH) + 1) + "月");
                statusMap.put("th2title", (getdate().get(Calendar.YEAR) - 1) + "");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "无法查询到该日期的数据，请重新查询！"));
            }
        }
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
     * @return the zcmap
     */
    public LinkedHashMap<String, String[]> getZcmap() {
        return zcmap;
    }

    /**
     * @param zcmap the zcmap to set
     */
    public void setZcmap(LinkedHashMap<String, String[]> zcmap) {
        this.zcmap = zcmap;
    }

    /**
     * @return the fzmap
     */
    public LinkedHashMap<String, String[]> getFzmap() {
        return fzmap;
    }

    /**
     * @param fzmap the fzmap to set
     */
    public void setFzmap(LinkedHashMap<String, String[]> fzmap) {
        this.fzmap = fzmap;
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

}
