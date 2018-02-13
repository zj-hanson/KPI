/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.query;

import cn.hanbell.kpi.ejb.CategoryBean;
import cn.hanbell.kpi.entity.Category;
import cn.hanbell.kpi.lazy.CategoryModel;
import cn.hanbell.kpi.web.SuperQueryBean;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "categoryQueryBean")
@ViewScoped
public class CategoryQueryBean extends SuperQueryBean<Category> {

    @EJB
    private CategoryBean categoryBean;

    protected Integer querySortid;

    public CategoryQueryBean() {
        super(Category.class);
    }

    @Override
    public void init() {
        superEJB = categoryBean;
        model = new CategoryModel(categoryBean, userManagedBean);
        model.getSortFields().put("sortid", "ASC");
        super.init();
    }

    @Override
    public void query() {
        if (querySortid != null) {
            model.getFilterFields().put("sortid", querySortid);
        }
        if (queryName != null && !"".equals(queryName)) {
            model.getFilterFields().put("category", queryName);
        }
    }

    /**
     * @return the querySortid
     */
    public Integer getQuerySortid() {
        return querySortid;
    }

    /**
     * @param querySortid the querySortid to set
     */
    public void setQuerySortid(Integer querySortid) {
        this.querySortid = querySortid;
    }

}
