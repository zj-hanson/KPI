/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import com.lightshell.comm.SuperSingleReportBean;

/**
 *
 * @author C0160
 */
public class IndicatorReport extends SuperSingleReportBean<IndicatorBean, Indicator> {

    public IndicatorReport() {

    }

    @Override
    public Indicator getEntity(int i) throws Exception {
        return superEJB.findById(i);
    }

}
