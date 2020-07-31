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
 * @author C1749
 */
@ManagedBean(name = "scorecardQueryBean")
@ViewScoped
public class ScorecardQueryBean extends SuperQueryBean<Scorecard>{
    
    @EJB
    private ScorecardBean scorecardBean;
    
    public ScorecardQueryBean() {
        super(Scorecard.class);
    }

    @Override
    public void init() {
        superEJB = scorecardBean;
        model = new ScorecardModel(scorecardBean, userManagedBean);
        super.init(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void query() {
        if (this.model != null) {
            this.model.getFilterFields().clear();
            if (this.queryFormId != null && !"".equals(this.queryFormId)) {
                this.model.getFilterFields().put("id", this.queryFormId);
            }
            if (this.queryName != null && !"".equals(this.queryName)) {
                this.model.getFilterFields().put("name", this.queryName);
            }
        }
    }

}
