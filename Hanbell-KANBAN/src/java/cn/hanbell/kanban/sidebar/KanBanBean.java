/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kanban.sidebar;

import cn.hanbell.kpi.ejb.kb.PaneldataBean;
import cn.hanbell.kpi.ejb.kb.PanelsBean;
import cn.hanbell.kpi.entity.kb.Paneldata;
import cn.hanbell.kpi.entity.kb.Panels;
import java.io.Serializable;
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
@ManagedBean(name = "kanBanBean")
@ViewScoped
public class KanBanBean implements Serializable {

    @EJB
    protected PanelsBean panelsBean;

    @EJB
    protected PaneldataBean paneldataBean;

    protected Panels panels;
    protected List<Panels> panelses;
    protected List<Paneldata> paneldatas;

    //倒计时秒
    protected int second;
    protected int id;

    protected final DecimalFormat Format = new DecimalFormat("#,###");

    public KanBanBean() {
    }

    @PostConstruct
    public void construct() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request.getParameter("id") != null) {
            id = Integer.parseInt(request.getParameter("id"));
            panels = panelsBean.findById(id);
            //删除以往的数据
            paneldataBean.deleteYesterdayData(id, getYesterDayDate(new Date()).getTime());
            paneldatas = new ArrayList<>();
        }
    }

    public void queryPanelDataList() {
        setSecond(60);
        Calendar c = Calendar.getInstance();
        //相同时间删除数据
        List<Paneldata> list;
        paneldataBean.deleteTheSameData(id, c.getTime());
        //根据panels计算接口更新数据 1 代表更新paneldata数据
        panelsBean.updateValue(panels, c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.getTime(), 1);
        //查询更新数据
        paneldatas = paneldataBean.findByPidAndDate(id, c.getTime());
    }

    public void increment() {
        if (getSecond() == 0) {
            setSecond(60);
        }
        setSecond(getSecond() - 1);
    }

    public String getFormat(int ss) {
        return String.format("%02d", ss) + "";
    }

    public String getFormat(BigDecimal value) {
        return Format.format(value);
    }

    public Calendar getYesterDayDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, -1);
        return c;
    }

    /**
     * @return the second
     */
    public int getSecond() {
        return second;
    }

    /**
     * @param second the second to set
     */
    public void setSecond(int second) {
        this.second = second;
    }

    /**
     * @return the panels
     */
    public Panels getPanels() {
        return panels;
    }

    /**
     * @param panels the panels to set
     */
    public void setPanels(Panels panels) {
        this.panels = panels;
    }

    /**
     * @return the panelses
     */
    public List<Panels> getPanelses() {
        return panelses;
    }

    /**
     * @param panelses the panelses to set
     */
    public void setPanelses(List<Panels> panelses) {
        this.panelses = panelses;
    }

    /**
     * @return the paneldatas
     */
    public List<Paneldata> getPaneldatas() {
        return paneldatas;
    }

    /**
     * @param paneldatas the paneldatas to set
     */
    public void setPaneldatas(List<Paneldata> paneldatas) {
        this.paneldatas = paneldatas;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

}
