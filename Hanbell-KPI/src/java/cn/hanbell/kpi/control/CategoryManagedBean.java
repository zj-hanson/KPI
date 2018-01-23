/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.CategoryBean;
import cn.hanbell.kpi.entity.Category;
import cn.hanbell.kpi.lazy.CategoryModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "categoryManagedBean")
@SessionScoped
public class CategoryManagedBean extends SuperSingleBean<Category> {

    @EJB
    private CategoryBean categoryBean;

    private TreeNode rootNode;
    private TreeNode selectedNode;
    private List<Category> categoryList;

    public CategoryManagedBean() {
        super(Category.class);
    }

    @Override
    public void create() {
        super.create();
        newEntity.setCompany(userManagedBean.getCompany());
    }

    @Override
    protected boolean doAfterDelete() throws Exception {
        initTree();
        return super.doAfterDelete();
    }

    @Override
    protected boolean doAfterPersist() throws Exception {
        initTree();
        return super.doAfterPersist();
    }

    @Override
    protected boolean doAfterUpdate() throws Exception {
        initTree();
        return super.doAfterUpdate();
    }

    @Override
    public void handleDialogReturnWhenNew(SelectEvent event) {
        if (event.getObject() != null && newEntity != null) {
            Category e = (Category) event.getObject();
            newEntity.setParent(e);
        }
    }

    @Override
    public void handleDialogReturnWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Category e = (Category) event.getObject();
            currentEntity.setParent(e);
        }
    }

    @Override
    public void init() {
        this.superEJB = categoryBean;
        this.model = new CategoryModel(categoryBean, userManagedBean);
        super.init();
        initTree();
    }

    private void initTree() {
        rootNode = new DefaultTreeNode(new Category("root", "Root"), null);
        categoryList = categoryBean.findRootByCompany(this.userManagedBean.getCompany());
        if (categoryList != null && !categoryList.isEmpty()) {
            categoryList.stream().forEach((e) -> {
                TreeNode n = new DefaultTreeNode(e, rootNode);
                //n.setExpanded(true);
                initTree(e, n);
            });
        }
    }

    private void initTree(Category category, TreeNode node) {
        List<Category> childList = categoryBean.findByPId(category.getId());
        if (childList != null && !childList.isEmpty()) {
            childList.stream().forEach((e) -> {
                TreeNode n = new DefaultTreeNode(e, node);
                n.setExpanded(true);
                initTree(e, n);
            });
        }
    }

    public void removeParentWhenEdit() {
        if (currentEntity != null) {
            currentEntity.setParent(null);
        }
    }

    public void removeParentWhenNew() {
        if (newEntity != null) {
            newEntity.setParent(null);
        }
    }

    /**
     * @return the rootNode
     */
    public TreeNode getRootNode() {
        return rootNode;
    }

    /**
     * @param rootNode the rootNode to set
     */
    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * @return the selectedNode
     */
    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    /**
     * @param selectedNode the selectedNode to set
     */
    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
        if (selectedNode != null) {
            currentEntity = (Category) selectedNode.getData();
            setToolBar();
        }
    }

}
