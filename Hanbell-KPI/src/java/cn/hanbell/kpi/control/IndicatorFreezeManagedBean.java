/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.lazy.IndicatorModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "indicatorFreezeManagedBean")
@SessionScoped
public class IndicatorFreezeManagedBean extends SuperSingleBean<Indicator> {

    @EJB
    private IndicatorBean indicatorBean;

    protected String objtype;
    protected Date choiceFreezeDate;

    public IndicatorFreezeManagedBean() {
        super(Indicator.class);
    }

    @Override
    public void init() {
        this.superEJB = indicatorBean;
        this.model = new IndicatorModel(indicatorBean, userManagedBean);
        super.init();
    }

    @Override
    public void verify() {
        List<Indicator> indicatorsList;
        Calendar c = Calendar.getInstance();
        c.setTime(choiceFreezeDate);
        int seq = c.get(Calendar.YEAR);
        if (!"".equals(objtype)) {
            indicatorsList = indicatorBean.findBySeqObjtypeAndCompany(seq, objtype,userManagedBean.getCompany());
            if (!indicatorsList.isEmpty()) {
                for (Indicator i : indicatorsList) {
                    if ("V".equals(i.getStatus())) {
                        i.setFreezeDate(choiceFreezeDate);
                        indicatorBean.update(i); 
                    }
                }
                showInfoMsg("Info", "冻结日期成功");
            } else {
                showErrorMsg("Error", "冻结日期失败");
            }
        }
    }

    public String getObjtype() {
        return objtype;
    }

    public void setObjtype(String objtype) {
        this.objtype = objtype;
    }

    public Date getChoiceFreezeDate() {
        return choiceFreezeDate;
    }

    public void setChoiceFreezeDate(Date choiceFreezeDate) {
        this.choiceFreezeDate = choiceFreezeDate;
    }

}
