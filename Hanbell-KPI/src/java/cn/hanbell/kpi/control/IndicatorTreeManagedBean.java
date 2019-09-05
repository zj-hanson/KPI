/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.entity.Indicator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "indicatorTreeManagedBean")
@SessionScoped
public class IndicatorTreeManagedBean extends IndicatorSetManagedBean {

    private TreeNode rootNode;
    private TreeNode selectedNode;
    private List<Indicator> rootIndicator;

    public IndicatorTreeManagedBean() {

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
    public void init() {
        super.init();
        initTree();
    }

    private void initTree() {
        setRootNode(new DefaultTreeNode());
        //rootNode.setExpanded(true);
        rootIndicator = indicatorBean.findRootByCompany(userManagedBean.getCompany(), "D", queryYear);
        if (rootIndicator != null && !rootIndicator.isEmpty()) {
            for (Indicator e : rootIndicator) {
                TreeNode n = new DefaultTreeNode(e, getRootNode());
                //n.setExpanded(true);
                initTree(e, n);
            }
        }
    }

    private void initTree(Indicator indicator, TreeNode node) {
        List<Indicator> list = indicatorBean.findByPId(indicator.getId());
        if (list != null && !list.isEmpty()) {
            for (Indicator e : list) {
                TreeNode n = new DefaultTreeNode(e, node);
                n.setExpanded(true);
                initTree(e, n);
            }
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
            currentEntity = (Indicator) selectedNode.getData();
            detailList = detailEJB.findByPId(currentEntity.getId());
            detailList2 = detailEJB2.findByPId(currentEntity.getId());
            detailList3 = detailEJB3.findByPId(currentEntity.getId());
            setToolBar();
        }
    }

    /**
     * @return the queryDeptno
     */
    public String getQueryDeptno() {
        return queryDeptno;
    }

    /**
     * @param queryDeptno the queryDeptno to set
     */
    public void setQueryDeptno(String queryDeptno) {
        this.queryDeptno = queryDeptno;
    }

    /**
     * @return the queryDeptname
     */
    public String getQueryDeptname() {
        return queryDeptname;
    }

    /**
     * @param queryDeptname the queryDeptname to set
     */
    public void setQueryDeptname(String queryDeptname) {
        this.queryDeptname = queryDeptname;
    }

}
