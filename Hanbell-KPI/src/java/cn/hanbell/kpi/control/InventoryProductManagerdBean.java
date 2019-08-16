/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.InventoryProductBean;
import cn.hanbell.kpi.entity.InventoryProduct;
import cn.hanbell.kpi.lazy.inventoryProductModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author C1749
 */
@ManagedBean(name = "inventoryProductManagerdBean")
@SessionScoped
public class InventoryProductManagerdBean extends SuperSingleBean<InventoryProduct> {

    @EJB
    protected InventoryProductBean inventoryProductBean;

    protected String queryYearmon;
    protected String queryWhdsc;
    protected String queryGenre;
    protected String queryItclscode;

    protected List<InventoryProduct> editInventoryProductList;

    protected final DecimalFormat doubleFormat;

    public InventoryProductManagerdBean() {
        super(InventoryProduct.class);
        this.doubleFormat = new DecimalFormat("###,###.##");
    }

    @Override
    protected boolean doBeforePersist() throws Exception {
        if (newEntity != null) {
            if (inventoryProductBean.queryInventoryProductIsExist(newEntity)) {
                showErrorMsg("Error", "添加失败,数据库已存在相同的数据，请在确认是否添加？？？");
                return false;
            }
        }
        return super.doBeforePersist(); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean doAfterPersist() throws Exception {
        super.doAfterPersist(); // To change body of generated methods, choose Tools | Templates.
        create();
        return true;
    }

    @Override
    public void init() {
        this.superEJB = inventoryProductBean;
        this.model = new inventoryProductModel(superEJB);
        queryYearmon = "";
        queryWhdsc = "";
        queryGenre = "0";
        queryItclscode = "";
        newEntity = new InventoryProduct();
        setEditInventoryProductList(new ArrayList<>());
        super.init(); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void query() {
        super.query(); // To change body of generated methods, choose Tools | Templates.
        if (model != null) {
            model.getFilterFields().clear();
            if (!"".equals(queryYearmon) && queryYearmon != null) {
                model.getFilterFields().put("yearmon", queryYearmon);
            }
            if (!"".equals(queryWhdsc) && queryWhdsc != null) {
                model.getFilterFields().put("whdsc", queryWhdsc);
            }
            if (!"".equals(queryGenre) && queryGenre != null) {
                model.getFilterFields().put("genre", queryGenre);
            }
            if (!"".equals(queryItclscode) && queryItclscode != null) {
                model.getFilterFields().put("itclscode", queryItclscode);
            }
        }
    }

    @Override
    public void reset() {
        super.reset(); // To change body of generated methods, choose Tools | Templates.
        queryYearmon = "";
        queryWhdsc = "";
        queryGenre = "";
        queryItclscode = "";
    }

    @Override
    public void update() {
        super.update(); // To change body of generated methods, choose Tools | Templates.
        unverify();
    }

    public void queryEdit() {
        if (currentEntity != null) {
            List<InventoryProduct> list = inventoryProductBean.getEditList(currentEntity);
            if (!list.isEmpty()) {
                setEditInventoryProductList(list);
            } else {
                showErrorMsg("Error", "当前数据异常，请重试！！！");
            }
        }
    }

    public void updateEdit() {
        // 取到当前修改的集合
        List<InventoryProduct> list = getEditInventoryProductList();
        if (!list.isEmpty()) {
            for (InventoryProduct ip : list) {
                ip.setAmamount(ip.getEditamount());
                ip.setEditamount(BigDecimal.ZERO);
                ip.setOptuser(getUserManagedBean().getCurrentUser().getUsername());
                ip.setOptdateToNow();
                inventoryProductBean.update(ip);
            }
            showErrorMsg("Error", "更新数据成功！！！");
        } else {
            showErrorMsg("Error", "更新数据失败，请重试！！！");
        }
    }

    @Override
    public void create() {
        super.create(); // To change body of generated methods, choose Tools | Templates.
        if (newEntity != null) {
            newEntity.setStatus("N");
            newEntity.setCreator(getUserManagedBean().getCurrentUser().getUsername());
            newEntity.setCredateToNow();
        }
        setCurrentEntity(newEntity);
    }

    @Override
    public void unverify() {
        if (null != getCurrentEntity()) {
            try {
                if (doBeforeUnverify()) {
                    newEntity.setStatus("N");// 简化查询条件,此处不再提供修改状态(M)
                    newEntity.setOptuser(getUserManagedBean().getCurrentUser().getUsername());
                    newEntity.setOptdateToNow();
                    superEJB.unverify(currentEntity);
                    doAfterUnverify();
                }
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(null, e.getMessage()));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "没有可更新数据!"));
        }
    }

    public String getQueryYearmon() {
        return queryYearmon;
    }

    public void setQueryYearmon(String queryYearmon) {
        this.queryYearmon = queryYearmon;
    }

    public String getQueryWhdsc() {
        return queryWhdsc;
    }

    public void setQueryWhdsc(String queryWhdsc) {
        this.queryWhdsc = queryWhdsc;
    }

    public String getQueryGenre() {
        return queryGenre;
    }

    public void setQueryGenre(String queryGenre) {
        this.queryGenre = queryGenre;
    }

    public String getQueryItclscode() {
        return queryItclscode;
    }

    public void setQueryItclscode(String queryItclscode) {
        this.queryItclscode = queryItclscode;
    }

    public InventoryProduct getInventoryProduct() {
        return newEntity;
    }

    public void setExchangEntity(InventoryProduct newEntity) {
        this.newEntity = newEntity;
    }

    public List<InventoryProduct> getEditInventoryProductList() {
        return editInventoryProductList;
    }

    public void setEditInventoryProductList(List<InventoryProduct> editInventoryProductList) {
        this.editInventoryProductList = editInventoryProductList;
    }

    public String format(BigDecimal b) {
        return doubleFormat.format(b);
    }

}
