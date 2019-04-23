/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.crm.DSALP;
import cn.hanbell.kpi.ejb.crm.DSALPBean;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.control.UserManagedBean;
import cn.hanbell.kpi.entity.IndicatorChart;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "dsalpReportBean")
@ViewScoped
public class DsalpReportBean implements Serializable {

    @EJB
    protected DSALPBean dsalpBean;

    protected Date btnDate;
    protected String da;
    protected String type;
    protected String display;
    protected String status1;
    protected String status2;
    protected String userid;
    protected String username;
    protected String quantitySum;
    protected String amountSum;
    protected List<DSALP> dsalplist;
    protected List<DSALP> deletelist;
    protected LinkedHashMap<String, Object> map;

    @ManagedProperty(value = "#{userManagedBean}")
    protected UserManagedBean userManagedBean;

    FacesContext fc;
    ExternalContext ec;
    protected IndicatorChart indicatorChart;

    public DsalpReportBean() {
        map = new LinkedHashMap<>();
    }

    public Calendar getQueryDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(getBtnDate());
        return c;
    }

    public void reset() {
        initial();
    }

    public void init() {
        initial();
    }

    public void initial() {
        setQuantitySum("");
        setAmountSum("");
        setDisplay("none");
        setStatus1("");
        setStatus2("");
        setDsalplist(new ArrayList<>());
        setDeleteList(new ArrayList<>());
    }

    public void addValueForMap() {
        map.clear();
        map.put("status", "1");
        map.put("userid", getUserid());
        map.put("type", getType());
        map.put("da", getDa());
    }

    public void query() {
        if (userid == null || "".equals(userid)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "请填写业务员！"));
        } else {
            initial();
            try {
                addValueForMap();
                if ("Shipment".equals(getType())) {
                    setDsalplist(dsalpBean.getShipmentListSum(btnDate, getDa(), map));
                }
                if ("SalesOrder".equals(getType())) {
                    setDsalplist(dsalpBean.getSalesOrderListSum(btnDate, getDa(), map));
                }
                setDeleteList(dsalpBean.findByParams(getType(), getUserid(), getDa(), btnDate));
                if (dsalplist != null && !dsalplist.isEmpty()) {
                  
                    if (deletelist != null && !deletelist.isEmpty()) {
                        setStatus1(new SimpleDateFormat("yyyy年MM月").format(getQueryDate().getTime()) + getDa() + " (" + getType() + ") 业务员" + getUsername() + "数据已存在，如点击更新则会重新覆盖该时间数据！！！慎重");
                    } else {
                        setStatus2(new SimpleDateFormat("yyyy年MM月").format(getQueryDate().getTime()) + getDa() + " (" + getType() + ")业务员" + getUsername() + "为新数据，请及时更新");
                    }
                    Double quantity = 0.00;
                    Double amount = 0.00;
                    for (DSALP dsalp : getDsalplist()) {
                        quantity += dsalp.getDs011().doubleValue();
                        amount += dsalp.getDs012().doubleValue();
                    }
                    setQuantitySum(new DecimalFormat("#,###.##").format(quantity));
                    setAmountSum(new DecimalFormat("#,##0.00").format(amount));
                    setDisplay("block");
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "无法查询到该数据！"));
                }
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.toString()));
            }
        }
    }

    public void updateSales() {
        try {
            if (dsalpBean.addDsalpList(deletelist, getDsalplist())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Info", "数据更新成功！"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "数据更新异常！"));
            }
            initial();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.toString()));
        }
    }

    public void view() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 650);
        RequestContext.getCurrentInstance().openDialog("sysuserSelect", options, null);
    }

    public void handleDialogReturnUserWhenDetailNew(SelectEvent event) {
        if (event.getObject() != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            userid = user.getUserid();
            username = user.getUsername();
        }
    }

    /**
     * @return the btnDate
     */
    public Date getBtnDate() {
        return btnDate;
    }

    /**
     * @param btnDate the btnDate to set
     */
    public void setBtnDate(Date btnDate) {
        this.btnDate = btnDate;
    }

    /**
     * @return the da
     */
    public String getDa() {
        return da;
    }

    /**
     * @param da the da to set
     */
    public void setDa(String da) {
        this.da = da;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the display
     */
    public String getDisplay() {
        return display;
    }

    /**
     * @param display the display to set
     */
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
     * @return the status1
     */
    public String getStatus1() {
        return status1;
    }

    /**
     * @param status1 the status1 to set
     */
    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    /**
     * @return the status2
     */
    public String getStatus2() {
        return status2;
    }

    /**
     * @param status2 the status2 to set
     */
    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    /**
     * @return the userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the quantitySum
     */
    public String getQuantitySum() {
        return quantitySum;
    }

    /**
     * @param quantitySum the quantitySum to set
     */
    public void setQuantitySum(String quantitySum) {
        this.quantitySum = quantitySum;
    }

    /**
     * @return the amountSum
     */
    public String getAmountSum() {
        return amountSum;
    }

    /**
     * @param amountSum the amountSum to set
     */
    public void setAmountSum(String amountSum) {
        this.amountSum = amountSum;
    }

    /**
     * @return the dsalplist
     */
    public List<DSALP> getDsalplist() {
        return dsalplist;
    }

    
    
    /**
     * @param dsalplist the dsalplist to set
     */
    public void setDsalplist(List<DSALP> dsalplist) {
        this.dsalplist = dsalplist;
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
     * @return the deleteList
     */
    public List<DSALP> getDeleteList() {
        return deletelist;
    }

    /**
     * @param deleteList the deleteList to set
     */
    public void setDeleteList(List<DSALP> deleteList) {
        this.deletelist = deleteList;
    }

}
