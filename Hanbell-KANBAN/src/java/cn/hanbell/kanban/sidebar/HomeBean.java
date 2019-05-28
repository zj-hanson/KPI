/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kanban.sidebar;

import cn.hanbell.kpi.entity.kb.Panels;
import java.util.Calendar;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "homeBean")
@ViewScoped
public class HomeBean extends KanBanBean {

    public HomeBean() {
    }

    @Override
    public void construct() {
        panelses = panelsBean.findAll();
        Calendar c = Calendar.getInstance();
        for (Panels panelse : panelses) {
            panelsBean.updateValue(panelse, c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.getTime(), 0);
        }
    }

}
