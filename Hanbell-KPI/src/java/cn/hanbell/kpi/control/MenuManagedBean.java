/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.ejb.SystemGrantModuleBean;
import cn.hanbell.eap.ejb.SystemGrantPrgBean;
import cn.hanbell.eap.ejb.SystemRoleDetailBean;
import cn.hanbell.eap.entity.SystemGrantModule;
import cn.hanbell.eap.entity.SystemGrantPrg;
import cn.hanbell.eap.entity.SystemRoleDetail;
import cn.hanbell.kpi.ejb.IndicatorDepartmentBean;
import cn.hanbell.kpi.ejb.RoleDetailBean;
import cn.hanbell.kpi.ejb.RoleGrantModuleBean;
import cn.hanbell.kpi.entity.IndicatorDepartment;
import cn.hanbell.kpi.entity.RoleDetail;
import cn.hanbell.kpi.entity.RoleGrantModule;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author kevindong
 */
@ManagedBean(name = "menuManagedBean")
@SessionScoped
public class MenuManagedBean implements Serializable {

    @EJB
    private IndicatorDepartmentBean indicatorDepartmentBean;

    @EJB
    private RoleDetailBean roleDetailBean;
    @EJB
    private RoleGrantModuleBean roleGrantModuleBean;

    @EJB
    private SystemRoleDetailBean systemRoleDetailBean;
    @EJB
    private SystemGrantModuleBean systemGrantModuleBean;
    @EJB
    private SystemGrantPrgBean systemGrantPrgBean;

    @ManagedProperty(value = "#{userManagedBean}")
    private UserManagedBean userManagedBean;

    private List<SystemGrantModule> userModuleGrantList;
    private List<SystemGrantModule> roleModuleGrantList;
    private List<SystemGrantModule> moduleGrantList;
    private List<SystemGrantPrg> userPrgGrantList;
    private List<SystemGrantPrg> rolePrgGrantList;
    private List<SystemGrantPrg> prgGrantList;
    private List<SystemRoleDetail> systemRoleList;

    private List<RoleGrantModule> roleGrantList;
    private List<RoleGrantModule> grantList;
    private List<RoleDetail> roleDetailList;

    private List<IndicatorDepartment> indicatorDepartmentList;

    private MenuModel model;

    public MenuManagedBean() {
    }

    @PostConstruct
    public void init() {

        boolean flag;
        moduleGrantList = new ArrayList<>();
        prgGrantList = new ArrayList<>();
        //KPI授權列表
        grantList = new ArrayList<>();

        model = new DefaultMenuModel();

        if (getUserManagedBean() != null) {

            DefaultSubMenu kpimenu;
            DefaultSubMenu appmenu;
            DefaultSubMenu submenu;
            DefaultMenuItem menuitem;

            menuitem = new DefaultMenuItem("Home");
            menuitem.setId("menu_home");
            menuitem.setOutcome("home");
            menuitem.setIcon("dashboard");

            model.addElement(menuitem);

            appmenu = new DefaultSubMenu("应用");
            appmenu.setIcon("menu");
            //将用户权限和角色权限合并后产生菜单,用户权限优先角色权限
            moduleGrantList.clear();
            userModuleGrantList = systemGrantModuleBean.findBySystemNameAndUserId("KPI", userManagedBean.getCurrentUser().getId());
            userModuleGrantList.forEach((m) -> {
                moduleGrantList.add(m);
            });
            prgGrantList.clear();
            userPrgGrantList = systemGrantPrgBean.findBySystemNameAndUserId("KPI", userManagedBean.getCurrentUser().getId());
            userPrgGrantList.forEach((p) -> {
                prgGrantList.add(p);
            });
            systemRoleList = systemRoleDetailBean.findByUserId(userManagedBean.getCurrentUser().getId());
            for (SystemRoleDetail r : systemRoleList) {
                roleModuleGrantList = systemGrantModuleBean.findBySystemNameAndRoleId("KPI", r.getPid());
                if (moduleGrantList.isEmpty()) {
                    moduleGrantList.addAll(roleModuleGrantList);
                } else {
                    for (SystemGrantModule m : roleModuleGrantList) {
                        flag = true;
                        for (SystemGrantModule e : moduleGrantList) {
                            if (e.getSystemModule().getId().compareTo(m.getSystemModule().getId()) == 0) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            moduleGrantList.add(m);
                        }
                    }
                }
                rolePrgGrantList = systemGrantPrgBean.findBySystemNameAndRoleId("KPI", r.getPid());
                if (prgGrantList.isEmpty()) {
                    prgGrantList.addAll(rolePrgGrantList);
                } else {
                    for (SystemGrantPrg p : rolePrgGrantList) {
                        flag = true;
                        for (SystemGrantPrg e : prgGrantList) {
                            if (e.getSysprg().getId().compareTo(p.getSysprg().getId()) == 0) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            prgGrantList.add(p);
                        }
                    }
                }
            }
            moduleGrantList.sort((SystemGrantModule o1, SystemGrantModule o2) -> {
                if (o1.getSystemModule().getSortid() < o2.getSystemModule().getSortid()) {
                    return -1;
                } else {
                    return 1;
                }
            });
            prgGrantList.sort((SystemGrantPrg o1, SystemGrantPrg o2) -> {
                if (o1.getSysprg().getSortid() < o2.getSysprg().getSortid()) {
                    return -1;
                } else {
                    return 1;
                }
            });
            userManagedBean.setSystemGrantPrgList(prgGrantList);
            for (SystemGrantModule m : moduleGrantList) {
                submenu = new DefaultSubMenu(m.getSystemModule().getName());
                submenu.setIcon("menu");
                for (SystemGrantPrg p : prgGrantList) {
                    if (p.getPid() == m.getSystemModule().getId()) {
                        menuitem = new DefaultMenuItem(p.getSysprg().getName());
                        menuitem.setIcon("menu");
                        menuitem.setOutcome(p.getSysprg().getApi());
                        submenu.addElement(menuitem);
                    }
                }
                appmenu.addElement(submenu);
            }
            model.addElement(appmenu);

            kpimenu = new DefaultSubMenu("KPI");
            kpimenu.setIcon("menu");
            roleDetailList = roleDetailBean.findByUserId(userManagedBean.getCurrentUser().getUserid());
            for (RoleDetail r : roleDetailList) {
                roleGrantList = roleGrantModuleBean.findByRoleId(r.getPid());
                if (grantList.isEmpty()) {
                    grantList.addAll(roleGrantList);
                } else {
                    for (RoleGrantModule m : roleGrantList) {
                        flag = true;
                        for (RoleGrantModule e : grantList) {
                            if (e.getDeptno().equals(m.getDeptno())) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            grantList.add(m);
                        }
                    }
                }
            }//得到授權查看的部門列表
            grantList.sort((RoleGrantModule o1, RoleGrantModule o2) -> {
                if (o1.getDeptno().compareTo(o2.getDeptno()) < 1) {
                    return -1;
                } else {
                    return 1;
                }
            });
            for (RoleGrantModule r : grantList) {
                submenu = new DefaultSubMenu(r.getDept());
                submenu.setIcon("menu");
                indicatorDepartmentList = indicatorDepartmentBean.findByDeptno(r.getDeptno());
                if (indicatorDepartmentList != null && !indicatorDepartmentList.isEmpty()) {
                    for (IndicatorDepartment i : indicatorDepartmentList) {
                        menuitem = new DefaultMenuItem(String.valueOf(i.getParent().getSeq()) + i.getParent().getName());
                        menuitem.setIcon("menu");
                        menuitem.setOutcome(i.getParent().getApi());
                        menuitem.setParam("id", i.getParent().getId());
                        submenu.addElement(menuitem);
                    }
                }
                kpimenu.addElement(submenu);
            }
            model.addElement(kpimenu);

        }
    }

    @PreDestroy
    public void destory() {

    }

    /**
     * @return the userManagedBean
     */
    public UserManagedBean getUserManagedBean() {
        return userManagedBean;
    }

    /**
     * @param userManagedBean the userManagedBean to set
     */
    public void setUserManagedBean(UserManagedBean userManagedBean) {
        this.userManagedBean = userManagedBean;
    }

    /**
     * @return the model
     */
    public MenuModel getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(MenuModel model) {
        this.model = model;
    }

}
