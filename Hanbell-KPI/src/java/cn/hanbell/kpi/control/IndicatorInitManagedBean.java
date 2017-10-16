/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.entity.Department;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDefine;
import java.util.Calendar;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "indicatorInitManagedBean")
@SessionScoped
public class IndicatorInitManagedBean extends IndicatorDefineManagedBean {

    private int queryYear;

    public IndicatorInitManagedBean() {
    }

    public void handleDialogReturnDeptForQuery(SelectEvent event) {
        if (event.getObject() != null) {
            Department d = (Department) event.getObject();
            queryDeptno = d.getDeptno();
            queryDeptname = d.getDept();
        }
    }

    @Override
    public void init() {
        super.init();
        model.getFilterFields().put("status", "V");
        model.getSortFields().put("sortid", "ASC");
        queryYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    public void verify() {
        if (entityList == null || entityList.isEmpty()) {
            showWarnMsg("Warn", "没有可初始化明细");
            return;
        }
        if (queryYear == 0) {
            showWarnMsg("Warn", "请输入初始化年度");
            return;
        }
        Indicator e;
        try {
            for (IndicatorDefine id : entityList) {
                e = new Indicator();
                e.setCompany(id.getCompany());
                e.setFormid(id.getFormid());
                e.setFormtype(id.getFormtype());
                e.setFormkind(id.getFormkind());
                e.setName(id.getName());
                e.setDescript(id.getDescript());
                e.setPid(id.getId());
                e.setSeq(queryYear);
                if (queryDeptno != null && queryDeptname != null && !"".equals(queryDeptno) && !"".equals(queryDeptname)) {
                    e.setDeptno(queryDeptno);
                    e.setDeptname(queryDeptname);
                    indicatorBean.deleteByFormidYearAndDeptno(id.getFormid(), queryYear, queryDeptno);
                } else {
                    e.setDeptno(id.getDeptno());
                    e.setDeptname(id.getDeptname());
                    indicatorBean.deleteByFormidYearAndDeptno(id.getFormid(), queryYear, id.getDeptno());
                }
                e.setSortid(id.getSortid());
                e.setLvl(0);
                e.setValuemode(id.getValuemode());
                e.setSymbol(id.getSymbol());
                e.setUnit(id.getUnit());
                e.setRate(id.getRate());
                e.setLimited(id.isLimited());
                e.setApi(id.getApi());
                e.setStatusToNew();
                e.setCreator(userManagedBean.getCurrentUser().getUsername());
                e.setCredateToNow();
                indicatorBean.persist(e);
            }
            entityList.clear();
            showInfoMsg("Info", "初始化成功");
        } catch (Exception ex) {
            showErrorMsg("Error", ex.getMessage());
        }
    }

    /**
     * @return the queryYear
     */
    public int getQueryYear() {
        return queryYear;
    }

    /**
     * @param queryYear the queryYear to set
     */
    public void setQueryYear(int queryYear) {
        this.queryYear = queryYear;
    }

}
