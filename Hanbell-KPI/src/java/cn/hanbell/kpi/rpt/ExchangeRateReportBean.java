/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.control.UserManagedBean;
import cn.hanbell.kpi.ejb.ExchangeRateBean;
import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.web.BscQueryTableManageBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "exchangeRateReportBean")
@ViewScoped
public class ExchangeRateReportBean extends BscQueryTableManageBean implements Serializable {

    @EJB
    protected IndicatorChartBean indicatorChartBean;
    @EJB
    protected ExchangeRateBean exchangeRateBean;

    //查询开始时间
    protected Date queryDateBegin;
    //查询结束时间
    protected Date queryDateEnd;
    //货币种类
    protected String queryCurrency;
    //div隐藏属性
    protected String displaySting;
    //提示
    protected String remind;
    //map为table显示内容
    protected LinkedHashMap<String, String> map;
    protected List<String[]> list;

    protected LineChartModel chartModel;

    public ExchangeRateReportBean() {
    }

    public Calendar getNowDateBegin() {
        Calendar c = Calendar.getInstance();
        c.setTime(getUserManagedBean().getBaseDate());
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c;
    }

    public Calendar getDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(getUserManagedBean().getBaseDate());
        return c;
    }

    public Calendar getNowDateEnd() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c;
    }

    @PostConstruct
    public void construct() {
        displaySting = "none";
        remind = "block";
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
        } else {
            for (RoleGrantModule m : userManagedBean.getRoleGrantDeptList()) {
                if (m.getDeptno().equals(indicatorChart.getPid())) {
                    deny = false;
                }
            }
        }
        queryDateBegin = getNowDateBegin().getTime(); //new SimpleDateFormat("yyyy/MM/dd").format(c.getTime());
        queryDateEnd = getNowDateEnd().getTime();

        map = new LinkedHashMap<>();
        list = new ArrayList<>();
        chartModel = new LineChartModel();
    }

    public void initial() {
        displaySting = "none";
        remind = "block";
        queryDateBegin = getNowDateBegin().getTime();
        queryDateEnd = getNowDateEnd().getTime();
        list = new ArrayList<>();
        map = new LinkedHashMap<>();
        queryCurrency = "0";
        chartModel.clear();
    }

    public void queryExchangeRate() {
        boolean aa = true;
        map = new LinkedHashMap<>();
        list.clear();
        chartModel.clear();
        displaySting = "none";
        remind = "block";
        if (hint()) {
            map = exchangeRateBean.getHashMap(queryCurrency, queryDateBegin, queryDateEnd);
            if (map != null) {
                list = exchangeRateBean.getlList(queryCurrency, queryDateBegin, queryDateEnd);
                ChartSeries t = new ChartSeries();
                t.setLabel(map.get("name"));
                for (int i = 0; i < list.size(); i++) {
                    String[] get = list.get(i);
                    t.set(get[0], Double.parseDouble(get[1]));
                }
                getChartModel().addSeries(t);
                getChartModel().setLegendPosition("e");
                //getChartModel().setTitle("");
                getChartModel().setShowPointLabels(true);
                getChartModel().setBreakOnNull(true);
                getChartModel().setSeriesColors("28B732");//自定义颜色
                Axis yAxis;
                getChartModel().getAxes().put(AxisType.X, new CategoryAxis("日期"));
                yAxis = getChartModel().getAxis(AxisType.Y);
                //yAxis.setLabel("");
                displaySting = "block";
                remind = "none";
                super.getRemarkOne(indicatorChart, getDate().get(Calendar.YEAR), getDate().get(Calendar.MONTH) + 1);
            } else {
                displaySting = "none";
                remind = "block";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "无法查询到该日期的数据，请重新查询！"));
            }
        }
    }

    private boolean hint() {
        boolean aa = true;
        if (queryDateBegin.after(queryDateEnd)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "查询开始日期不能大于结束日期！"));
            aa = false;
        }
        if (queryDateEnd.after(new Date())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "日期选择不能超过系统结算日期！"));
            aa = false;
        }
        if (queryCurrency.equals("0")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "请选择需要查询的币种！"));
            aa = false;
        }
        return aa;
    }

    /**
     * @return the queryDateBegin
     */
    public Date getQueryDateBegin() {
        return queryDateBegin;
    }

    /**
     * @return the queryDateEnd
     */
    public Date getQueryDateEnd() {
        return queryDateEnd;
    }

    /**
     * @return the queryCurrency
     */
    public String getQueryCurrency() {
        return queryCurrency;
    }

    /**
     * @param queryDateBegin the queryDateBegin to set
     */
    public void setQueryDateBegin(Date queryDateBegin) {
        this.queryDateBegin = queryDateBegin;
    }

    /**
     * @param queryDateEnd the queryDateEnd to set
     */
    public void setQueryDateEnd(Date queryDateEnd) {
        this.queryDateEnd = queryDateEnd;
    }

    /**
     * @param queryCurrency the queryCurrency to set
     */
    public void setQueryCurrency(String queryCurrency) {
        this.queryCurrency = queryCurrency;
    }

    /**
     * @return the displaySting
     */
    public String getDisplaySting() {
        return displaySting;
    }

    /**
     * @param displaySting the displaySting to set
     */
    public void setDisplaySting(String displaySting) {
        this.displaySting = displaySting;
    }

    /**
     * @return the map
     */
    public LinkedHashMap<String, String> getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(LinkedHashMap<String, String> map) {
        this.map = map;
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
     * @return the remind
     */
    public String getRemind() {
        return remind;
    }

    /**
     * @param remind the remind to set
     */
    public void setRemind(String remind) {
        this.remind = remind;
    }

    /**
     * @return the chartModel
     */
    public LineChartModel getChartModel() {
        return chartModel;
    }

    /**
     * @param chartModel the chartModel to set
     */
    public void setChartModel(LineChartModel chartModel) {
        this.chartModel = chartModel;
    }

}
