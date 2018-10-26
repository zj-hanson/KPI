/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.lazy;

import cn.hanbell.kpi.entity.ExchangeRate;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;

/**
 *
 * @author C1879
 */
public class ExchangeRateModel extends BaseLazyModel<ExchangeRate> {

    public ExchangeRateModel(SuperEJB superEJB) {
        this.superEJB = superEJB;
    }

}
