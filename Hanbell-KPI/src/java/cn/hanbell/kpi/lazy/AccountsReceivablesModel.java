/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.lazy;


import cn.hanbell.kpi.entity.AccountsReceivables;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;

/**
 *
 * @author C1749
 */
public class AccountsReceivablesModel extends BaseLazyModel<AccountsReceivables> {

    public AccountsReceivablesModel(SuperEJB superEJB) {
        this.superEJB = superEJB;
    }
}
