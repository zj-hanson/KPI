/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kanban.sidebar;

import java.util.LinkedHashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "storageInformationBean")
@ViewScoped
public class StorageInformationBean extends AutomaticWarehouseBean {

    public StorageInformationBean() {
    }

    @Override
    public void construct() {
        map = new LinkedHashMap<>();
        map1 = new LinkedHashMap<>();
        map2 = new LinkedHashMap<>();
        super.query();
    }

}
