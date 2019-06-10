/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.control.UserManagedBean;
import cn.hanbell.kpi.ejb.IndicatorAnalysisBean;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.ejb.IndicatorSummaryBean;
import cn.hanbell.kpi.ejb.InventoryBean;
import cn.hanbell.kpi.ejb.InventoryStatementBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorAnalysis;
import cn.hanbell.kpi.entity.IndicatorChart;
import cn.hanbell.kpi.entity.IndicatorSummary;
import cn.hanbell.kpi.entity.Inventory;
import cn.hanbell.kpi.entity.InventoryStatement;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
 * @author C1749 分别对应前台显示页面 ： 各库别物料库存状况表和事业部的（inventoryamountda）
 * 各库别之产品别库存金额表（inventoryamountdc）
 *
 */
@ManagedBean(name = "inventoryManagerBean")
@ViewScoped
public class InventoryManagerBean implements Serializable {

    @ManagedProperty(value = "#{userManagedBean}")
    protected UserManagedBean userManagedBean;

    @EJB
    protected IndicatorAnalysisBean indicatorAnalysisBean;
    @EJB
    protected IndicatorSummaryBean indicatorSummaryBean;
    @EJB
    protected IndicatorChartBean indicatorChartBean;
    @EJB
    protected IndicatorBean indicatorBean;
    protected Indicator indicator;
    @EJB
    protected InventoryBean inventoryBean;

    @EJB
    protected InventoryStatementBean inventoryStatementBean;

    private Integer year;
    private Integer month;
    private LinkedHashMap<String, String> map;
    private List<Inventory> inventoryList;
    private List<InventoryStatement> inventoryStatementsList;
    protected List<IndicatorAnalysis> analysisList;
    protected List<IndicatorSummary> summaryList;
    protected int analysisCount;
    protected int summaryCount;

    private String date1;
    private String date2;
    private String date3;

    private String type;
    private String genre;

    FacesContext fc;
    ExternalContext ec;
    protected IndicatorChart indicatorChart;

    private final DecimalFormat format;

    //下拉选项集合
    private Map<String, String> cities = new HashMap<String, String>();

    public InventoryManagerBean() {
        this.format = new DecimalFormat("#,###.##");

    }

    public int findyear() {
        Calendar c = Calendar.getInstance();
        c.setTime(userManagedBean.getBaseDate());
        return c.get(Calendar.YEAR);
    }

    public int findmonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(userManagedBean.getBaseDate());
        return c.get(Calendar.MONTH) + 1;
    }

    public void getDeopt() {
        cities = new HashMap<String, String>();
        String deptno = indicatorChart.getDeptno();
        if (cities.isEmpty()) {
            switch (deptno) {
                case "1F000":
                    cities.put("物料库存状况表(冷媒)", "R");
                    cities.put("物料库存状况表(冷冻)", "L");
                    break;
                case "1G100":
                    cities.put("物料库存状况表(冷冻)", "AJ");
                    break;
                case "1G500":
                    cities.put("物料库存状况表(无油机组)", "AD");
                    break;
                case "5A":
                    cities.put("物料库存状况表(离心机)", "RT");
                    break;
                case "1Q000":
                    cities.put("物料库存状况表(机组)", "A");
                    break;
                case "1H000":
                    cities.put("物料库存状况表(真空)", "P");
                    break;
                case "1U000":
                    cities.put("物料库存状况表(涡旋)", "S");
                    break;
                default:
                    cities.put("物料库存状况表(冷媒)", "R");
                    cities.put("物料库存状况表(冷冻)", "L");
                    cities.put("物料库存状况表(机体)", "AJ");
                    cities.put("物料库存状况表(无油机组)", "AD");
                    cities.put("物料库存状况表(离心机)", "RT");
                    cities.put("物料库存状况表(机组)", "A");
                    cities.put("物料库存状况表(真空)", "P");
                    cities.put("物料库存状况表(涡旋)", "S");
                    break;
            }
        }

    }

    public void init() {
        getDeopt();
        setInventoryList(new ArrayList<>());
        setAnalysisCount(0);
        setSummaryCount(0);
        analysisList = new ArrayList<>();
        summaryList = new ArrayList<>();
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
        init();

    }

    public void resetQuery() {
        year = findyear();
        month = findmonth();
        type = "";
        genre = "";
        init();
    }

    public boolean addQueryModel() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int m = cal.get(Calendar.MONTH) + 1;
        int y = cal.get(Calendar.YEAR);
        if (getYear() == null || getYear() > y || getMonth() > 12 || getMonth() < 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "输入的年份或月份值有误请重新填写！"));
            return false;
        }
        return true;
    }

    //各库别之产品别库存金额数据集 DivisionQuery
    public void DCQuery() {
        init();
        try {
            if (addQueryModel()) {
                int m = getMonth();
                int y = getYear();
                List<Inventory> list;
                list = inventoryBean.getInventorysList(y, m);
                if (!list.isEmpty()) {
                    setInventoryList(list);
                    if (indicator != null) {
                        analysisList = indicatorAnalysisBean.findByPIdAndMonth(indicator.getId(), month);//指标分析
                        if (getAnalysisList() != null) {
                            setAnalysisCount(getAnalysisList().size());
                        }
                        summaryList = indicatorSummaryBean.findByPIdAndMonth(indicator.getId(), month);//指标说明
                        if (getSummaryList() != null) {
                            setSummaryCount(getSummaryList().size());
                        }
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "无法查询到该日期的数据，请重新查询！"));
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.toString()));
        }

    }

    // 各事业部和各产品目标的各库别物料库存状况表数据集
    public void DAQuery() {
        //初始化
        init();
        //获取页面显示的时间三个月的时间
        getDate();
        try {
            if (addQueryModel()) {
                int m = getMonth();
                int y = getYear();
                String typeValue = getType();
                String genreValue = getGenre();
                List<InventoryStatement> list;
                list = inventoryStatementBean.GetInventoryStatementList(typeValue, genreValue, y, m);
                if (!list.isEmpty()) {
                    setInventoryStatementsList(list);
                    if (indicator != null) {
                        analysisList = indicatorAnalysisBean.findByPIdAndMonth(indicator.getId(), month);//指标分析
                        if (getAnalysisList() != null) {
                            setAnalysisCount(getAnalysisList().size());
                        }
                        summaryList = indicatorSummaryBean.findByPIdAndMonth(indicator.getId(), month);//指标说明
                        if (getSummaryList() != null) {
                            setSummaryCount(getSummaryList().size());
                        }
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "无法查询到该日期的数据，请重新查询！"));
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.toString()));
        }

    }

    //获取日期当月、上月、上上月
    private void getDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        cal.set(getYear(), getMonth(), 1);
        cal.add(Calendar.DATE, -1);
        date1 = sdf.format(cal.getTime());
        cal.set(getYear(), getMonth() - 1, 1);
        cal.add(Calendar.DATE, -1);
        date2 = sdf.format(cal.getTime());
        cal.set(getYear(), getMonth() - 2, 1);
        cal.add(Calendar.DATE, -1);
        date3 = sdf.format(cal.getTime());
        cal.clear();
    }

    public String dfAmount(String a) {
        return format.format(Double.parseDouble(a));
    }

    public UserManagedBean getUserManagedBean() {
        return userManagedBean;
    }

    public void setUserManagedBean(UserManagedBean userManagedBean) {
        this.userManagedBean = userManagedBean;
    }

    public IndicatorAnalysisBean getIndicatorAnalysisBean() {
        return indicatorAnalysisBean;
    }

    public void setIndicatorAnalysisBean(IndicatorAnalysisBean indicatorAnalysisBean) {
        this.indicatorAnalysisBean = indicatorAnalysisBean;
    }

    public IndicatorSummaryBean getIndicatorSummaryBean() {
        return indicatorSummaryBean;
    }

    public void setIndicatorSummaryBean(IndicatorSummaryBean indicatorSummaryBean) {
        this.indicatorSummaryBean = indicatorSummaryBean;
    }

    public IndicatorChartBean getIndicatorChartBean() {
        return indicatorChartBean;
    }

    public void setIndicatorChartBean(IndicatorChartBean indicatorChartBean) {
        this.indicatorChartBean = indicatorChartBean;
    }

    public IndicatorBean getIndicatorBean() {
        return indicatorBean;
    }

    public void setIndicatorBean(IndicatorBean indicatorBean) {
        this.indicatorBean = indicatorBean;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public LinkedHashMap<String, String> getMap() {
        return map;
    }

    public void setMap(LinkedHashMap<String, String> map) {
        this.map = map;
    }

    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<Inventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

    public List<IndicatorAnalysis> getAnalysisList() {
        return analysisList;
    }

    public void setAnalysisList(List<IndicatorAnalysis> analysisList) {
        this.analysisList = analysisList;
    }

    public List<IndicatorSummary> getSummaryList() {
        return summaryList;
    }

    public void setSummaryList(List<IndicatorSummary> summaryList) {
        this.summaryList = summaryList;
    }

    public int getAnalysisCount() {
        return analysisCount;
    }

    public void setAnalysisCount(int analysisCount) {
        this.analysisCount = analysisCount;
    }

    public int getSummaryCount() {
        return summaryCount;
    }

    public void setSummaryCount(int summaryCount) {
        this.summaryCount = summaryCount;
    }

    public FacesContext getFc() {
        return fc;
    }

    public void setFc(FacesContext fc) {
        this.fc = fc;
    }

    public ExternalContext getEc() {
        return ec;
    }

    public void setEc(ExternalContext ec) {
        this.ec = ec;
    }

    public IndicatorChart getIndicatorChart() {
        return indicatorChart;
    }

    public void setIndicatorChart(IndicatorChart indicatorChart) {
        this.indicatorChart = indicatorChart;
    }

    public List<InventoryStatement> getInventoryStatementsList() {
        return inventoryStatementsList;
    }

    public void setInventoryStatementsList(List<InventoryStatement> inventoryStatementsList) {
        this.inventoryStatementsList = inventoryStatementsList;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getDate3() {
        return date3;
    }

    public void setDate3(String date3) {
        this.date3 = date3;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Map<String, String> getCities() {
        return cities;
    }

    public void setCities(Map<String, String> cities) {
        this.cities = cities;
    }

}
