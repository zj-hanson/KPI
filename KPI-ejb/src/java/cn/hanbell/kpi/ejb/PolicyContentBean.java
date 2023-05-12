/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.PolicyContent;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class PolicyContentBean extends SuperEJBForKPI<PolicyContent> {

    public PolicyContentBean() {
        super(PolicyContent.class);
    }

    public String getColumn(String type, int i) {
        return type.toLowerCase() + String.format("%01d", i);
    }
}
