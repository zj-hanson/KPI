/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kanban.sidebar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "storageBean")
@ViewScoped
public class StorageBean extends KanBanBean {

    public StorageBean() {
    }

    @Override
    public void construct() {
        super.construct();
        this.queryPanelDataList();
    }

    @Override
    public void queryPanelDataList() {
        super.queryPanelDataList();
    }

}
