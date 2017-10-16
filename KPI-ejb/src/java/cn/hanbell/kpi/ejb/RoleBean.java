/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.entity.Role;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class RoleBean extends SuperEJBForKPI<Role> {

    public RoleBean() {
        super(Role.class);
    }

}
