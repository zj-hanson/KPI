/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.ejb.ScorecardBean;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.entity.ScorecardAuditor;
import cn.hanbell.kpi.entity.ScorecardDetail;
import com.lightshell.comm.SuperSingleReportBean;
import java.util.List;

/**
 *
 * @author C1749
 */
public class ScorecardReport extends SuperSingleReportBean<ScorecardBean, Scorecard> {

    public ScorecardReport() {
    }

    @Override
    public Scorecard getEntity(int i) throws Exception {
        return superEJB.findById(i);
    }

    public List<ScorecardDetail> getDetail(int pid) {
        return this.superEJB.getDetail(pid);
    }

    public List<ScorecardAuditor> getAuditorDetail(int pid,int quater) {
        return this.superEJB.getAuditorDetail1(pid,quater);
    }
}
