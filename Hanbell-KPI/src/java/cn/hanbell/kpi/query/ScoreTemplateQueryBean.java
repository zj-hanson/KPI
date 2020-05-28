/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.query;

import cn.hanbell.kpi.ejb.ScorecardBean;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.lazy.ScorecardModel;
import cn.hanbell.kpi.web.SuperQueryBean;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "scoreTemplateQueryBean")
@ViewScoped
public class ScoreTemplateQueryBean extends SuperQueryBean<Scorecard> {

    @EJB
    private ScorecardBean scorecardBean;

    public ScoreTemplateQueryBean() {
        super(Scorecard.class);
    }

    @Override
    public void init() {
        superEJB = scorecardBean;
        model = new ScorecardModel(scorecardBean, userManagedBean);
        model.getSortFields().put("deptno", "ASC");
        model.getFilterFields().put("template", true);
        super.init();
    }

    @Override
    public void query() {
        model.getFilterFields().clear();
        if (this.queryName != null && !"".equals(this.queryName)) {
            model.getFilterFields().put("name", this.queryName);
        }
        model.getFilterFields().put("template", true);

    }

    @Override
    public void reset() {
        super.reset();
        model.getFilterFields().put("template", true);
    }

}
