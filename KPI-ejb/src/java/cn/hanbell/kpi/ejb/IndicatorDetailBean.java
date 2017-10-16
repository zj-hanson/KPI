/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class IndicatorDetailBean extends SuperEJBForKPI<IndicatorDetail> {

    public IndicatorDetailBean() {
        super(IndicatorDetail.class);
    }

    public void deleteByPId(int pid) {
        List<IndicatorDetail> details = findByPId(pid);
        if (details != null && !details.isEmpty()) {
            delete(details);
        }
    }

}
