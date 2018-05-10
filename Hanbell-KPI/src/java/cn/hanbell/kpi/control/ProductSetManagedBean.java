/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.entity.Category;
import java.util.Calendar;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "productSetManagedBean")
@SessionScoped
public class ProductSetManagedBean extends IndicatorSetManagedBean {

    /**
     * Creates a new instance of ProductIndicatorManagedBean
     */
    public ProductSetManagedBean() {
    }

    @Override
    public void create() {
        super.create();
        newEntity.setCompany(userManagedBean.getCompany());
        newEntity.setFormtype("N");
        newEntity.setFormkind("M");
        newEntity.setSeq(Calendar.getInstance().get(Calendar.YEAR));
        newEntity.setObjtype("P");
        newEntity.setLvl(0);
        newEntity.setValueMode("S");
        newEntity.setPerfCalc("AT");
    }

    @Override
    public void createDetail2() {
        super.createDetail2();
        newDetail2.setDeptno(currentEntity.getDeptno());
        newDetail2.setDeptname(currentEntity.getDeptname());
    }

    public void handleDialogReturnProductWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Category e = (Category) event.getObject();
            currentEntity.setProductId(e.getId());
            currentEntity.setProduct(e.getCategory());
            currentEntity.setSortid(e.getSortid());
        }
    }

    public void handleDialogReturnProductWhenNew(SelectEvent event) {
        if (event.getObject() != null && newEntity != null) {
            Category e = (Category) event.getObject();
            newEntity.setProductId(e.getId());
            newEntity.setProduct(e.getCategory());
            newEntity.setSortid(e.getSortid());
        }
    }

    public void handleDialogReturnProductWhenDetail2Edit(SelectEvent event) {
        if (currentDetail2 != null && event.getObject() != null) {
            Category e = (Category) event.getObject();
            currentDetail2.setProductId(e.getId());
            currentDetail2.setProduct(e.getCategory());
        }
    }

    @Override
    public void init() {
        super.init();
        model.getFilterFields().put("objtype =", "P");
    }

    @Override
    public void query() {
        super.query();
        model.getFilterFields().put("objtype =", "P");
    }

    @Override
    public void reset() {
        super.reset();
        model.getFilterFields().put("objtype =", "P");
    }

    public void updateActualValue() {
        if (queryDateBegin != null && currentEntity != null) {
            try {
                Calendar c = Calendar.getInstance();
                c.setTime(queryDateBegin);
                indicatorBean.updateActual(currentEntity.getId(), c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.getTime(), Calendar.MONTH);
                currentEntity = indicatorBean.findById(currentEntity.getId());
                indicatorBean.updatePerformance(currentEntity);
                indicatorBean.update(currentEntity);
                showInfoMsg("Info", "更新实际值成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.toString());
            }
        }
    }

}
