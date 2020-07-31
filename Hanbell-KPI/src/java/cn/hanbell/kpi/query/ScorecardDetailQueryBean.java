/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.query;

import cn.hanbell.kpi.ejb.ScorecardDetailBean;
import cn.hanbell.kpi.entity.ScorecardDetail;
import cn.hanbell.kpi.lazy.ScorecardContentModel;
import cn.hanbell.kpi.web.SuperQueryBean;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author C1749
 */
@ManagedBean(name = "scorecardDetailBean")
@ViewScoped
public class ScorecardDetailQueryBean extends SuperQueryBean<ScorecardDetail> {

    @EJB
    private ScorecardDetailBean scorecardDetailBean;

    protected int parampid;

    public ScorecardDetailQueryBean() {
        super(ScorecardDetail.class);
    }

    @Override
    public void init() {
        superEJB = scorecardDetailBean;
        model = new ScorecardContentModel(scorecardDetailBean, userManagedBean);
        params = ec.getRequestParameterValuesMap();
        if (params != null) {
            if (params.containsKey("pid")) {
                parampid = Integer.parseInt(params.get("pid")[0]);
            }
        }
        if (parampid > 0) {
            model.getFilterFields().put("pid", parampid);
        }
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
                this.model.getFilterFields().put("content", this.queryName);
            }
        }
    }

    public int getParampid() {
        return parampid;
    }

    public void setParampid(int parampid) {
        this.parampid = parampid;
    }

}
