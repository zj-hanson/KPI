/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.ParentCompany;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class ParentCompanyBean extends SuperEJBForKPI<ParentCompany> {

    public ParentCompanyBean() {
        super(ParentCompany.class);
    }

    public ParentCompany findByCusno(String cusno) {
        Query query = getEntityManager().createNamedQuery("ParentCompany.findByCusno");
        query.setParameter("cusno", cusno);
        try {
            Object o = query.getSingleResult();
            return (ParentCompany) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public Boolean queryCusnoIsExist(String cusno, String deptno) {
        Query query = getEntityManager().createNamedQuery("ParentCompany.findByCusnoAndDeptno");
        query.setParameter("cusno", cusno);
        query.setParameter("deptno", deptno);
        Object o;
        try {
            o = query.getSingleResult();
        } catch (Exception ex) {
            return false;
        }
        if (o != null) {
            return true;
        } else {
            return false;
        }
    }
}
