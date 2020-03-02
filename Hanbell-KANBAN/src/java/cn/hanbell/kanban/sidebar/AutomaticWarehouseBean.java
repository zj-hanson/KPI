/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kanban.sidebar;

import cn.hanbell.kpi.ejb.kb.WarehouseBean;
import java.util.LinkedHashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "automaticWarehouseBean")
@ViewScoped
public class AutomaticWarehouseBean extends KanBanBean {

    @EJB
    protected WarehouseBean warehouseBean;

    protected int number;
    protected String name;
    protected LinkedHashMap<String, String[]> map;
    protected LinkedHashMap<String, List> map1;
    protected LinkedHashMap<String, List> map2;
    protected int index;

    public AutomaticWarehouseBean() {
        map = new LinkedHashMap<>();
        map1 = new LinkedHashMap<>();
        map2 = new LinkedHashMap<>();
        index = 1;
    }

    @Override
    public void construct() {
        super.construct();
        this.query();
    }

    public void query(int i, String name) {
        this.name = name;
        this.number = i;
        map1.clear();
        map2.clear();
        map1 = warehouseBean.getMap(getNumber());
        map2 = warehouseBean.getMap(getNumber() + 1);
    }

    public void query() {
        map.clear();
        second = 300;
        map = warehouseBean.getTableMap();
        query(1, "1号线");
    }

    /**
     * 刷新右边面板，每75秒1-4号信息轮换
     */
    public void flushRight() {
        if (second <= 225 && second > 150) {
            query(3, "2号线");
        }
        if (second <= 150 && second > 75) {
            query(5, "3号线");
        }
        if (second <= 75 && second > 0) {
            query(7, "4号线");
        }
    }

    //优化看板自动刷新
    public void refresh() {
        index++;
        switch (index) {
            case 1:
                query(1, "1号线");
                break;
            case 2:
                query(3, "2号线");
                break;
            case 3:
                query(5, "3号线");
                break;
            case 4:
                query(7, "4号线");
                index = 0;
                break;
        }

    }

    @Override
    public void increment() {
        if (second == 0) {
            second = 300;
        }
        second--;
    }

    public String setColor(String value) {
        switch (value) {
            case "N":
                return "#f0f0f0";
            case "E":
                return "#0904C0";
            case "L":
                return "#66ff66";
            case "S":
                return "#ffcccc";
            case "I":
                return "#FFFF00";
            case "O":
                return "#009933";
            case "C":
                return "#7B107C";
            case "P":
                return "#a9a954";
            case "X":
                return "#FF0000";
            default:
                return "#f0f0f0";
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the map
     */
    public LinkedHashMap<String, String[]> getMap() {
        return map;
    }

    /**
     * @return the map1
     */
    public LinkedHashMap<String, List> getMap1() {
        return map1;
    }

    /**
     * @return the map2
     */
    public LinkedHashMap<String, List> getMap2() {
        return map2;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

}
