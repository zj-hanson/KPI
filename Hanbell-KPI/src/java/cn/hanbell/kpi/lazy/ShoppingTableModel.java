/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.lazy;

import cn.hanbell.eap.entity.SystemRole;
import cn.hanbell.kpi.entity.ShoppingTable;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;

/**
 *
 * @author C2082
 */
public class ShoppingTableModel extends BaseLazyModel<ShoppingTable> {

    public ShoppingTableModel(SuperEJB sessionBean) {
        this.superEJB = sessionBean;
    }
}
