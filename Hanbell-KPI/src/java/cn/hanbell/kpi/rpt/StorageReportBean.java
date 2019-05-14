/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.control.UserManagedBean;
import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.ejb.StorageBean;
import cn.hanbell.kpi.entity.IndicatorChart;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
@ManagedBean(name = "storageReportBean")
@ViewScoped
public class StorageReportBean  implements Serializable {

    @EJB
    protected IndicatorChartBean indicatorChartBean;

    @EJB
    protected StorageBean storageBean;

    protected List<String[]> unoccupiedlist;
    protected List<String[]> occupylist;

    @ManagedProperty(value = "#{userManagedBean}")
    protected UserManagedBean userManagedBean;

    FacesContext fc;
    ExternalContext ec;
    protected IndicatorChart indicatorChart;

    public StorageReportBean() {
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
        queryOccupy();
        queryUnoccupied();
    }

    public void queryOccupy() {
        setOccupylist(new ArrayList<>());
        List<String[]> aaList;
        aaList = storageBean.getStorageOccupyList();
        if (aaList != null && !aaList.isEmpty()) {
            setOccupylist(aaList);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "数据自动刷新失败，请手动刷新！"));
        }
    }

    public void queryUnoccupied() {
        setUnoccupiedlist(new ArrayList<>());
        List<String[]> aaList;
        aaList = storageBean.getStorageUnoccupiedList();
        if (aaList != null && !aaList.isEmpty()) {
            setUnoccupiedlist(aaList);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "数据自动刷新失败，请手动刷新！"));
        }
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
     * @return the unoccupiedlist
     */
    public List<String[]> getUnoccupiedlist() {
        return unoccupiedlist;
    }

    /**
     * @param unoccupiedlist the unoccupiedlist to set
     */
    public void setUnoccupiedlist(List<String[]> unoccupiedlist) {
        this.unoccupiedlist = unoccupiedlist;
    }

    /**
     * @return the occupylist
     */
    public List<String[]> getOccupylist() {
        return occupylist;
    }

    /**
     * @param occupylist the occupylist to set
     */
    public void setOccupylist(List<String[]> occupylist) {
        this.occupylist = occupylist;
    }

}
